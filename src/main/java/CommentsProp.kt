import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.nio.file.Files
import java.util.stream.Collectors

class CommentsProp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                println("You have to give one parameter")
                return
            }
            val configFile = args[0]
            val outputFile = "properties.json"
            val resultList: MutableList<Result> = mutableListOf()

            val folderName = File(configFile).readLines()


            val files = Files.walk(File(folderName[0]).toPath())
                .filter { Files.isRegularFile(it) }
                .filter { it.fileName.toString().endsWith(".java") }
                .collect(Collectors.toList())
            val mapFile = emptyMap<String, Int>().toMutableMap()
            val regex = " // [1-9]*[A-Za-z]*[0-9]*".toRegex()
            for (file in files) {
                var contorSingleLines = 0
                val f = file.toFile()
                f.forEachLine {
                    if (regex.containsMatchIn(it)) {
                        contorSingleLines++
                    }
                }
                if (contorSingleLines != 0) {
                    mapFile[file.toString()] = contorSingleLines
                    val fileNameMod =
                        file.toAbsolutePath().toString().replace("D:\\Lore\\Faculta\\An4\\Sem2\\CES\\cod\\", " ")
                    val result = Result(fileNameMod.replace("\\", "/"), contorSingleLines)
                    resultList.add(result)
                }
            }
            jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValue(File(outputFile), resultList)

        }

        data class Result(
            val file: String,
            val value: Number = 1
        )
    }

}
