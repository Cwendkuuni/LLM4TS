#!/bin/bash

# Chemin vers le fichier JAR de Checkstyle
CHECKSTYLE_JAR="checkstyle-10.14.2-all.jar"
# Dossier racine contenant les dossiers de stratégies avec fichiers Java
#RACINE_FOLDER='/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/AVEC_PACKAGE_GLOBAL_AVEC_TOUT/Defects4J-FV-GOOD-ORGANIZATION-NEW/Defects4J'
RACINE_FOLDER='/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests'


# Dossier de sortie principal pour les rapports XML
OUTPUT_FOLDER="reports_strategies_Defects4J"
# Fichier de configuration Checkstyle pour Google Java Style
GOOGLE_CONFIG="google_checks.xml"
# Fichier de configuration Checkstyle pour Sun Code Convention
SUN_CONFIG="sun_checks.xml"

# Créer le dossier de sortie principal s'il n'existe pas
mkdir -p "$OUTPUT_FOLDER"

# Itération sur chaque stratégie/dossier dans le dossier racine
for STRATEGIE in "$RACINE_FOLDER"/*; do
    if [ -d "$STRATEGIE" ]; then
        # Nom du dossier de stratégie, utilisé pour créer un sous-dossier dans le dossier de sortie
        STRATEGIE_NAME=$(basename "$STRATEGIE")
        STRATEGIE_OUTPUT_FOLDER="$OUTPUT_FOLDER/$STRATEGIE_NAME"
        # Créer le dossier de sortie pour la stratégie courante s'il n'existe pas
        mkdir -p "$STRATEGIE_OUTPUT_FOLDER"
        
        # Itération sur tous les fichiers Java dans le dossier de stratégie
        find "$STRATEGIE" -name "*.java" | while read file; do
            filename=$(basename -- "$file")
            base="${filename%.*}"
            
            # Construire le chemin de sortie basé sur le nom de la stratégie et le nom du fichier
            OUTPUT_GOOGLE="$STRATEGIE_OUTPUT_FOLDER/${base}_google.xml"
            OUTPUT_SUN="$STRATEGIE_OUTPUT_FOLDER/${base}_sun.xml"
            
            # Génère les rapports XML pour chaque configuration
            /usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar "$CHECKSTYLE_JAR" -c "$GOOGLE_CONFIG" -f xml -o "$OUTPUT_GOOGLE" "$file"
            /usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar "$CHECKSTYLE_JAR" -c "$SUN_CONFIG" -f xml -o "$OUTPUT_SUN" "$file"
        done
    fi
done












# #!/bin/bash

# # Chemin vers le fichier JAR de Checkstyle
# CHECKSTYLE_JAR="checkstyle-10.14.2-all.jar"
# # Dossier contenant les fichiers Java à analyser
# SOURCE_FOLDER="source"
# # Dossier de sortie pour les rapports XML
# OUTPUT_FOLDER="rapports"
# # Fichier de configuration Checkstyle pour Google Java Style
# GOOGLE_CONFIG="google_checks.xml"
# # Fichier de configuration Checkstyle pour Sun Code Convention
# SUN_CONFIG="sun_checks.xml"

# mkdir -p "$OUTPUT_FOLDER"

# # Itération sur tous les fichiers Java dans le dossier source
# find "$SOURCE_FOLDER" -name "*.java" | while read file; do
#     filename=$(basename -- "$file")
#     base="${filename%.*}"
    
#     # Génère les rapports XML pour chaque configuration
#     /usr/lib/jvm/java-17-openjdk-amd64/bin/java  -jar "$CHECKSTYLE_JAR" -c "$GOOGLE_CONFIG" -f xml -o "$OUTPUT_FOLDER/${base}_google.xml" "$file"
#     /usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar "$CHECKSTYLE_JAR" -c "$SUN_CONFIG" -f xml -o "$OUTPUT_FOLDER/${base}_sun.xml" "$file"
# done
