# Répertoire où se trouvent les fichiers CSV
$csv_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\CSV-OUTPUT\SYNTHESIS"
$jar_path = "E:\FROM-EVOSUITE-LLMS-PROJECT\jar\TestSmellDetector.jar"
$error_log_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\TEST-SMELLS-ERRORS"
$error_log = "$error_log_dir\error_log.txt"
$temp_csv_dir = "E:\FROM-EVOSUITE-LLMS-PROJECT\TEMP-CSV"  # Dossier temporaire pour stocker les CSV individuels

# S'assurer que les répertoires de log et temp existent
if (!(Test-Path -Path $error_log_dir)) { New-Item -ItemType Directory -Path $error_log_dir }
if (!(Test-Path -Path $temp_csv_dir)) { New-Item -ItemType Directory -Path $temp_csv_dir }

# Fonction pour traiter chaque ligne du fichier CSV individuellement
function Process-Line {
    param([string]$line, [string]$header, [string]$csv_file, [int]$lineNumber)

    try {
        # Créer un fichier CSV temporaire pour cette ligne avec encodage UTF-8
        $temp_csv = "$temp_csv_dir\temp_line.csv"
        "$header" | Out-File -FilePath $temp_csv -Encoding UTF8
        "$line" | Add-Content -Path $temp_csv -Encoding UTF8

        # Log avant l'exécution
        Write-Host "Processing line ${lineNumber}: $line"

        # Exécuter la commande Java pour cette ligne dans un fichier CSV temporaire sans paramètre de sortie
        java -jar $jar_path $temp_csv
        Write-Host "Processed line ${lineNumber} in $csv_file"
    } catch {
        # Enregistrer l'erreur spécifique à la ligne dans le fichier journal
        "$((Get-Date).ToString()): Error processing line ${lineNumber} in file $csv_file - $($_.Exception.Message)" | Add-Content -Path $error_log
        Write-Host "Error during smell detection for line ${lineNumber} in $csv_file. Logged."
    }
}

# Fonction pour traiter chaque fichier CSV ligne par ligne
function Process-CSV {
    param([string]$csv_file)

    $lines = Get-Content $csv_file
    $header = $lines[0]
    $content = $lines | Select-Object -Skip 1
    $lineNumber = 1

    foreach ($line in $content) {
        Process-Line -line $line -header $header -csv_file $csv_file -lineNumber $lineNumber
        $lineNumber++
    }
}

# Parcourir tous les fichiers CSV dans le répertoire SYNTHESIS et traiter chaque ligne
Get-ChildItem -Path $csv_dir -Filter "*.csv" | ForEach-Object {
    $csv_file = $_.FullName
    Process-CSV -csv_file $csv_file
}
