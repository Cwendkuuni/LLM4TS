# Répertoire où se trouvent les fichiers CSV
$csv_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT\SYNTHESIS"
$jar_path = "E:\FROM-EVOSUITE-LLMS-PROJECT\jar\TestSmellDetector.jar"  # Chemin complet vers le .jar
$error_log_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\TEST-SMELLS-ERRORS"
$error_log = "$error_log_dir\error_log.txt"  # Fichier pour enregistrer les erreurs

# S'assurer que le répertoire de log existe
if (!(Test-Path -Path $error_log_dir)) {
    New-Item -ItemType Directory -Path $error_log_dir
}

# S'assurer que le fichier journal existe ou le créer
if (!(Test-Path -Path $error_log)) {
    New-Item -ItemType File -Path $error_log
}

# Fonction pour lancer la détection des test smells
function Run-SmellDetection {
    param([string]$csv_file)

    try {
        # Exécuter la commande Java pour le fichier CSV
        java -jar $jar_path $csv_file
        Write-Host "Test smell detection completed for $csv_file"
    } catch {
        # Enregistrer l'erreur dans le fichier journal
        "$((Get-Date).ToString()): Error processing file $csv_file - $($_.Exception.Message)" | Add-Content -Path $error_log
        Write-Host "Error during smell detection for $csv_file. Logged the error."
    }
}

# Parcourir tous les fichiers CSV dans le répertoire SYNTHESIS et traiter chaque fichier
Get-ChildItem -Path $csv_dir -Filter "*.csv" | ForEach-Object {
    $csv_file = $_.FullName
    Run-SmellDetection -csv_file $csv_file
}
