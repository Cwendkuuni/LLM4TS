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

$models = @("GPT4", "Mixtral8x7b")
$output_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT"
$prompts = @("ZEROSHOT", "COT", "FEWSHOT", "TOT", "TOT2")

# S'assurer que le répertoire de sortie existe
if (!(Test-Path -Path $output_dir)) {
    New-Item -ItemType Directory -Path $output_dir
}

foreach ($model in $models) {
    foreach ($prompt in $prompts) {
        $compiledDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\$model\NEWDATASET\COMPILED-CLASSES\NEWDATASET\$prompt"
                        #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\GPT4\NEWDATASET\COMPILED-CLASSES\NEWDATASET
                        #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\MIXTRAL8x7B\NEWDATASET\COMPILED-CLASSES\NEWDATASET
        $csvFile = "$output_dir\$model-NEWDATASET-$prompt-input.csv"
        "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $csvFile

        if (Test-Path $compiledDir) {
            $files = Get-LongPathFile -folderPath $compiledDir -filter "*.java"
            if ($files) {
                foreach ($file in $files) {
                    $testFile = $file.FullName
                    $className = $file.BaseName -replace 'Test$', ''  # Enlever 'Test'

                    $sourceDir = "E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\NEW-DATASET\SOURCE"
                                  #E:\FROM-EVOSUITE-LLMS-PROJECT\DEZIP-OK\NEW-DATASET\SOURCE
                    if (Test-Path -Path $sourceDir) {
                        $productionFile = Get-LongPathFile -folderPath $sourceDir -filter "$className.java" | Select-Object -First 1
                        if ($productionFile) {
                            "NEWDATASET,$testFile,$($productionFile.FullName)" | Add-Content -Path $csvFile
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
