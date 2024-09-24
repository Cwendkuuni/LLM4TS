# Fonction pour utiliser l'API .NET et gérer les chemins longs
function Get-LongPathFile {
    param([string]$folderPath, [string]$filter)

    # Utiliser l'API .NET pour contourner la limite des chemins longs
    $directoryInfo = New-Object -TypeName System.IO.DirectoryInfo -ArgumentList $folderPath
    if ($directoryInfo.Exists) {
        return $directoryInfo.GetFiles($filter, [System.IO.SearchOption]::AllDirectories)
    } else {
        Write-Host "Directory not found: $folderPath"
        return $null
    }
}

$models = @("Mistral7b", "Mixtral8x7b", "GPT3", "GPT4", "EvoSuite")
$datasets = @("Defects4J", "SF110", "NEWDATASET")
$prompts = @("ZEROSHOT", "COT", "FEWSHOT", "TOT", "TOT2")
$output_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT"

# S'assurer que le répertoire de sortie existe
if (!(Test-Path -Path $output_dir)) {
    New-Item -ItemType Directory -Path $output_dir
}

foreach ($model in $models) {
    foreach ($dataset in $datasets) {
        # Gestion des cas EvoSuite (pas de prompt)
        if ($model -eq "EvoSuite" -and ($dataset -eq "Defects4J" -or $dataset -eq "SF110")) {
            $csvFile = "$output_dir\$model-$dataset-input.csv"
            "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $csvFile

            if ($dataset -eq "Defects4J") {
                $compiledDir = "I:\DEZIP-OK\Defects4J-COMPLET\Defects4J\SORTIES-RESULTATS\EVOSUITE"
                # Corrige ici le chemin vers le répertoire des sources selon ta structure réelle
                $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\Defects4J\SOURCES"  # Modifie selon ton vrai chemin
            } elseif ($dataset -eq "SF110") {
                $compiledDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SORTIES-RESULTATS\EVOSUITE"
                $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SOURCES"
            }

            if (Test-Path $compiledDir) {
                $files = Get-LongPathFile -folderPath $compiledDir -filter "*_ESTest.java"
                if ($files) {
                    foreach ($file in $files) {
                        $testFile = $file.FullName
                        $className = $file.BaseName -replace '_ESTest$', ''  # Retirer le suffixe _ESTest

                        # Rechercher le fichier source correspondant
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
                Write-Host "Directory not found: $compiledDir"
            }
        # Gestion des autres modèles avec prompts
        } else {
            foreach ($prompt in $prompts) {
                $compiledDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\$model\$dataset\COMPILED-CLASSES\$dataset\$prompt"
                $csvFile = "$output_dir\$model-$dataset-$prompt-input.csv"
                "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $csvFile

                if (Test-Path $compiledDir) {
                    $files = Get-LongPathFile -folderPath $compiledDir -filter "*.java"
                    if ($files) {
                        foreach ($file in $files) {
                            $testFile = $file.FullName
                            $className = $file.BaseName -replace 'Test$', ''  # Enlever 'Test'

                            # Corriger ici les chemins sources
                            if ($dataset -eq "Defects4J") {
                                $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\Defects4J\SOURCES"  # Corrige pour ton chemin réel
                            } elseif ($dataset -eq "SF110") {
                                $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SOURCES"
                            } elseif ($dataset -eq "NEWDATASET") {
                                $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\NEW-DATASET\SOURCE"
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
                    Write-Host "Directory not found: $compiledDir"
                }
            }
        }
    }
}
