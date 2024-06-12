#!/bin/bash


export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

WORK_DIR=$PWD
CURRENT_DIR=$WORK_DIR

# Définir le chemin de base pour les sources et les fichiers de test
#BASE_DIR="SF110-FV-GOOD-ORGANIZATION/SF110"
#BASE_DIR="AVEC_PACKAGE_PLUS_ADDED-GLOBAL/SF110-FV-GOOD-ORGANIZATION/SF110"
#BASE_DIR="SF110-FV-GOOD-ORGANIZATION-NEW/SF110"
#BASE_DIR="AVEC_PACKAGE_GLOBAL_AVEC_TOUT/SF110-FV-GOOD-ORGANIZATION-NEW/SF110"
#BASE_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/TOT2-SF110-NEW/SF110"

BASE_DIR="SF110"


#SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/SOURCES"

SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/SOURCES"

STRATEGIES="COT TOT TOT2 FEWSHOT ZEROSHOT"

# Chemins vers JUnit et Hamcrest (à ajuster selon votre environnement)
JUNIT_PATH=$CURRENT_DIR/JUNIT-HAMCREST/junit-4.13.2.jar
HAMCREST_CORE_PATH=$CURRENT_DIR/JUNIT-HAMCREST/hamcrest-core-1.3.jar

SL4J_PATH="$CURRENT_DIR/slf4j-api-1.6.4.jar"
JACOCO_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacococli.jar"
JACOCO_AGENT_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacocoagent.jar"


# Fichier pour enregistrer les chemins des classes compilées avec succès
SUCCESS_FILE="compiled_successfully.txt"
# Fichier CSV pour les statistiques
STATS_FILE="compilation_stats.csv"

# Initialiser ou vider les fichiers
> "$SUCCESS_FILE"
echo "Strategy,Project,Success,Failed" > "$STATS_FILE"

# Fonction pour compiler les fichiers Java et mettre à jour les statistiques
compile_java_files() {
    local strategy="$1"
    local project_name="$2"
    local file="$3"
    local success_count=0
    local fail_count=0

    echo "Compiling: $file"
    javac -cp ".:${project_sources}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}" "$file"
    if [ $? -ne 0 ]; then
        echo "Compilation failed for $file"
        let fail_count++
    else
        echo "Compilation succeeded for $file"
        let success_count++
        echo "$file" >> "$SUCCESS_FILE"
    fi

    # Mise à jour du fichier CSV avec les statistiques
    echo "${strategy},${project_name},${success_count},${fail_count}" >> "$STATS_FILE"
}

# Parcourir chaque stratégie
for strategy in $STRATEGIES; do
    # Parcourir chaque projet sous la stratégie actuelle
    for project_dir in "${BASE_DIR}/${strategy}"/*; do
        if [ -d "$project_dir" ]; then
            project_name=$(basename "$project_dir")
            project_sources="${SOURCES_DIR}/${project_name}"
            
            # Trouver tous les dossiers de test pour ce projet
            find "$project_dir" -type d -name "*TEST" | while read test_dir; do
                # Compiler tous les fichiers Java dans ce dossier de test
                find "$test_dir" -type f -name "*.java" -print0 | while IFS= read -r -d $'\0' file; do
                    compile_java_files "$strategy" "$project_name" "$file"
                done
            done
        fi
    done
done

echo "Compilation process completed. Stats written to $STATS_FILE"





#################################################################################################################################
#################################################################################################################################
#!/bin/bash

STATS_FILE="compilation_stats.csv"
SUMMARY_FILE="compilation_summary.csv"

# Initialisation du fichier récapitulatif
echo "Strategy,Total Success,Total Failed" > "$SUMMARY_FILE"

# Lire les données depuis STATS_FILE et calculer le récapitulatif par stratégie
awk -F',' 'NR>1 {success[$1]+=$3; failed[$1]+=$4} END {for (i in success) print i","success[i]","failed[i]}' "$STATS_FILE" >> "$SUMMARY_FILE"

echo "Récapitulatif de compilation écrit dans $SUMMARY_FILE"
