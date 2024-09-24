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

$datasets = @("Defects4J", "SF110")
$output_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT"

# S'assurer que le répertoire de sortie existe
if (!(Test-Path -Path $output_dir)) {
    New-Item -ItemType Directory -Path $output_dir
}

foreach ($dataset in $datasets) {
    if ($dataset -eq "Defects4J") {
        $csvFile = "$output_dir\EvoSuite-$dataset-input.csv"
        "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $csvFile

        $compiledDir = "I:\DEZIP-OK\Defects4J-COMPLET\Defects4J\SORTIES-RESULTATS\EVOSUITE"
                        #I:\DEZIP-OK\Defects4J-COMPLET\Defects4J\SORTIES-RESULTATS\EVOSUITE
        $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT3\DEFECTS4J\COMPILED-CLASSES\SOURCES"  # Modifie en fonction de la vraie structure

    } elseif ($dataset -eq "SF110") {
        $csvFile = "$output_dir\EvoSuite-$dataset-input.csv"
        "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $csvFile

        $compiledDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SORTIES-RESULTATS\EVOSUITE"
                        #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SORTIES-RESULTATS\EVOSUITE
        $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\EVOSUITE\SF110-COMPLET\SF110\SOURCES"
    }

    if (Test-Path $compiledDir) {
        $files = Get-LongPathFile -folderPath $compiledDir -filter "*_ESTest.java"
        if ($files) {
            foreach ($file in $files) {
                $testFile = $file.FullName
                $className = $file.BaseName -replace '_ESTest$', ''  # Retirer le suffixe _ESTest

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
