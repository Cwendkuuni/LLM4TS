$input_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT"
$output_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT\SYNTHESIS"

# Fonction pour fusionner les fichiers CSV par modèle-dataset
function Merge-CSVs {
    param([string]$model, [string]$dataset)

    $outputFile = "$output_dir\$model-$dataset-input.csv"
    # Créer l'en-tête dans le fichier de synthèse
    "appName,pathToTestFile,pathToProductionFile" | Out-File -FilePath $outputFile

    foreach ($prompt in $prompts) {
        $inputFile = "$input_dir\$model-$dataset-$prompt-input.csv"  # Utiliser le bon chemin pour les fichiers d'entrée
        if (Test-Path $inputFile) {
            $fileSize = (Get-Item $inputFile).Length
            if ($fileSize -gt 0) {
                # Ajouter le contenu du fichier au fichier de synthèse (sans réécrire l'en-tête)
                Get-Content $inputFile | Select-Object -Skip 1 | Add-Content -Path $outputFile
                Write-Host "Merged $inputFile into $outputFile"
            } else {
                Write-Host "File is empty: $inputFile"
            }
        } else {
            Write-Host "File not found: $inputFile"
        }
    }

    Write-Host "Merged files for $model-$dataset into $outputFile"
}

# Fusionner les fichiers CSV par modèle et dataset
foreach ($model in $models) {
    foreach ($dataset in $datasets) {
        Merge-CSVs -model $model -dataset $dataset
    }
}
