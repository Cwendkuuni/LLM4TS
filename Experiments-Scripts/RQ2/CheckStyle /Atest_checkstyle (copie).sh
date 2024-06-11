#!/bin/bash

# Chemin vers le fichier JAR de Checkstyle
CHECKSTYLE_JAR="checkstyle-10.14.2-all.jar"
# Dossier contenant les fichiers Java à analyser
SOURCE_FOLDER="source"
# Dossier de sortie pour les rapports XML
OUTPUT_FOLDER="rapports"
# Fichier de configuration Checkstyle pour Google Java Style
GOOGLE_CONFIG="google_checks.xml"
# Fichier de configuration Checkstyle pour Sun Code Convention
SUN_CONFIG="sun_checks.xml"

mkdir -p "$OUTPUT_FOLDER"

# Itération sur tous les fichiers Java dans le dossier source
find "$SOURCE_FOLDER" -name "*.java" | while read file; do
    filename=$(basename -- "$file")
    base="${filename%.*}"
    
    # Génère les rapports XML pour chaque configuration
    /usr/lib/jvm/java-17-openjdk-amd64/bin/java  -jar "$CHECKSTYLE_JAR" -c "$GOOGLE_CONFIG" -f xml -o "$OUTPUT_FOLDER/${base}_google.xml" "$file"
    /usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar "$CHECKSTYLE_JAR" -c "$SUN_CONFIG" -f xml -o "$OUTPUT_FOLDER/${base}_sun.xml" "$file"
done
