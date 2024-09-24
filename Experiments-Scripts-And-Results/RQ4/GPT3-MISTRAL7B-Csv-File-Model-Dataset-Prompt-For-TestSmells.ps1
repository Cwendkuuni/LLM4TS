# Fonction pour utiliser l'API .NET et gérer les chemins longs
function Get-LongPathFile {
    param([string]$folderPath, [string]$filter)

    $directoryInfo = New-Object -TypeName System.IO.DirectoryInfo -ArgumentList $folderPath
    if ($directoryInfo.Exists) {
        return $directoryInfo.GetFiles($filter, [System.IO.SearchOption]::AllDirectories)
    } else {
        Write-Host "Directory not found: $folderPath"
        return $null
    }
}

$models = @("GPT3", "Mistral7B")
$datasets = @("Defects4J", "SF110", "NEWDATASET")
$prompts = @("ZEROSHOT", "COT", "FEWSHOT", "TOT", "TOT2")
$output_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT"

# S'assurer que le répertoire de sortie existe
if (!(Test-Path -Path $output_dir)) {
    New-Item -ItemType Directory -Path $output_dir
}

foreach ($model in $models) {
    foreach ($dataset in $datasets) {
        foreach ($prompt in $prompts) {
            $compiledDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\$model\$dataset\COMPILED-CLASSES\$dataset\$prompt"
                            #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT3\NEWDATASET\COMPILED-CLASSES\NEWDATASET\FEWSHOT
                            #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT3\DEFECTS4J\COMPILED-CLASSES\DEFECTS4J\ZEROSHOT
                            #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT3\SF110\COMPILED-CLASSES\SF110\COT

                            #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\MISTRAL7B\DEFECTS4J\COMPILED-CLASSES\DEFECTS4J\COT
                            #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\MISTRAL7B\NEWDATASET\COMPILED-CLASSES\NEWDATASET\FEWSHOT
                            #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\MISTRAL7B\SF110\COMPILED-CLASSES\SF110\COT
            $csvFile = "$output_dir\$model-$dataset-$prompt-input.csv"
            "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $csvFile

            if (Test-Path $compiledDir) {
                $files = Get-LongPathFile -folderPath $compiledDir -filter "*.java"
                if ($files) {
                    foreach ($file in $files) {
                        $testFile = $file.FullName
                        $className = $file.BaseName -replace 'Test$', ''  # Enlever 'Test'

                        # Définir le répertoire sources basé sur le dataset
                        if ($dataset -eq "Defects4J") {
                            $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT3\DEFECTS4J\COMPILED-CLASSES\SOURCES"  # Modifie en fonction de ta structure réelle
                                          #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT3\DEFECTS4J\COMPILED-CLASSES\SOURCES
                                          

                        } elseif ($dataset -eq "SF110") {
                            $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SOURCES"
                                          #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SOURCES
                                         

                        } elseif ($dataset -eq "NEWDATASET") {
                            $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\NEW-DATASET\SOURCE"
                                          #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\NEW-DATASET\SOURCE

                        }

                        if (Test-Path -Path $sourceDir) {
                            $productionFile = Get-LongPathFile -folderPath $sourceDir -filter "$className.java" | Select-Object -First 1
                            if ($productionFile) {
                                "$dataset,$testFile,$($productionFile.FullName)" | Add-Content -Path $csvFile
                            } else {
                                Write-Host "Production file not found for: $className"
                            }
                        } else {
                            Write-Host "Source directory not found: $sourceDir"
                        }
                    }
                }
            } else {
                Write-Host "Compiled directory not found: $compiledDir"
            }
        }
    }
}
