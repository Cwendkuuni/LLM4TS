#!/bin/bash


export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

WORK_DIR=$PWD
CURRENT_DIR=$WORK_DIR

# Définir le chemin de base pour les sources et les fichiers de test
# Chemin de base pour les fichiers de test et les sources
#BASE_DIR="Defects4J-FV-GOOD-ORGANIZATION/Defects4J"
#BASE_DIR="Defects4J-FV-GOOD-ORGANIZATION-NEW/Defects4J"
#BASE_DIR="AVEC_PACKAGE_GLOBAL_AVEC_TOUT/Defects4J-FV-GOOD-ORGANIZATION-NEW/Defects4J"

BASE_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/TOT2-Defects4J/Defects4J"


#SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/SOURCES"

SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/SOURCES"



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
echo "Strategy,Project,BugID,Success,Failed" > "$STATS_FILE"

# Fonction pour compiler les fichiers Java et mettre à jour les statistiques
compile_java_files() {
    local strategy="$1"
    local project_name="$2"
    local bug_id="$3"
    local file="$4"
    local project_sources="${SOURCES_DIR}/${project_name}/${bug_id}"
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
    echo "${strategy},${project_name},${bug_id},${success_count},${fail_count}" >> "$STATS_FILE"
}

# Parcourir chaque stratégie
for strategy in $STRATEGIES; do
    # Parcourir chaque projet et bug ID sous la stratégie actuelle
    for project_bug_dir in "${BASE_DIR}/${strategy}"/*/*; do
        if [ -d "$project_bug_dir" ]; then
            project_name=$(basename $(dirname "$project_bug_dir"))
            bug_id=$(basename "$project_bug_dir")
            
            # Compiler tous les fichiers Java dans ce dossier
            find "$project_bug_dir" -type f -name "*.java" -print0 | while IFS= read -r -d $'\0' file; do
                compile_java_files "$strategy" "$project_name" "$bug_id" "$file"
            done
        fi
    done
done

echo "Compilation process completed. Stats written to $STATS_FILE"


########################################################################

STATS_FILE="compilation_stats.csv"
SUMMARY_FILE="compilation_summary.csv"

# Initialisation du fichier récapitulatif
echo "Strategy,Total Success,Total Failed" > "$SUMMARY_FILE"

# Lire les données depuis STATS_FILE et calculer le récapitulatif
awk -F',' 'NR>1 {success[$1]+=$4; failed[$1]+=$5} END {for (i in success) print i","success[i]","failed[i]}' "$STATS_FILE" >> "$SUMMARY_FILE"

echo "Récapitulatif de compilation écrit dans $SUMMARY_FILE"




