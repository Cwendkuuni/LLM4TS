#!/bin/bash


export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
#export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64


WORK_DIR=$PWD
CURRENT_DIR=$WORK_DIR

# Définir le chemin de base pour les sources et les fichiers de test
# Chemin de base pour les fichiers de test et les sources
#BASE_DIR="Defects4J-FV-GOOD-ORGANIZATION/Defects4J"
#BASE_DIR="Defects4J-FV-GOOD-ORGANIZATION-NEW/Defects4J"
#BASE_DIR="AVEC_PACKAGE_GLOBAL_AVEC_TOUT/Defects4J-FV-GOOD-ORGANIZATION-NEW/Defects4J"

#BASE_DIR="CHATGPT_FOR_NEW_DATASET-V3-CLASSE"
BASE_DIR="AVEC-PACKAGE_GLOBAL"

JAR_PATH="JARS/dependencies"



#SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/SOURCES"
SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SOURCES-FILES"

STRATEGIES="COT TOT TOT2 FEWSHOT ZEROSHOT"

# Chemins vers JUnit et Hamcrest (à ajuster selon votre environnement)
JUNIT_PATH=$CURRENT_DIR/JUNIT-HAMCREST/junit-4.13.2.jar
HAMCREST_CORE_PATH=$CURRENT_DIR/JUNIT-HAMCREST/hamcrest-core-1.3.jar
SL4J_PATH="$CURRENT_DIR/slf4j-api-1.6.4.jar"
ODC_JAR_PATH="$CURRENT_DIR/$JAR_PATH/odc-server-4.2.3-SNAPSHOT-executable.jar"
CONDUCTOR_JAR_PATH="$CURRENT_DIR/$JAR_PATH/conductor-server-boot.jar"

JACOCO_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacococli.jar"
JACOCO_AGENT_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacocoagent.jar"

# Fichier pour enregistrer les chemins des classes compilées avec succès
SUCCESS_FILE="compiled_successfully-SETP1-V3.txt"
# Fichier CSV pour les statistiques
STATS_FILE="compilation_stats-SETP1-V3.csv"

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
    #javac -cp ".:${project_sources}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}" "$file"
    #javac -J--add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED -J--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED -J--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED -J--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED -J--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED -J--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED -J--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED -cp ".:${project_sources}:${JUNIT_PATH}:${SL4J_PATH}:${ODC_JAR_PATH}:${CONDUCTOR_JAR_PATH}:${JAR_PATH}/*:${HAMCREST_CORE_PATH}" "$file"
    #javac -cp ".:${project_sources}:${JUNIT_PATH}:${SL4J_PATH}:${ODC_JAR_PATH}:${CONDUCTOR_JAR_PATH}:${JAR_PATH}/*:${HAMCREST_CORE_PATH}" "$file"
    javac -cp ".:${project_sources}:${JUNIT_PATH}:${SL4J_PATH}:${JAR_PATH}/*:${HAMCREST_CORE_PATH}" "$file"


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

STATS_FILE="compilation_stats-SETP1-V3.csv"
SUMMARY_FILE="compilation_summary-SETP1-V3.csv"

# Initialisation du fichier récapitulatif
echo "Strategy,Total Success,Total Failed" > "$SUMMARY_FILE"

# Lire les données depuis STATS_FILE et calculer le récapitulatif
awk -F',' 'NR>1 {success[$1]+=$4; failed[$1]+=$5} END {for (i in success) print i","success[i]","failed[i]}' "$STATS_FILE" >> "$SUMMARY_FILE"

echo "Récapitulatif de compilation écrit dans $SUMMARY_FILE"





##!/bin/bash

# export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# WORK_DIR=$PWD
# CURRENT_DIR=$WORK_DIR

# # Définir le chemin de base pour les sources et les fichiers de test
# # Chemin de base pour les fichiers de test et les sources
# #BASE_DIR="Defects4J-FV-GOOD-ORGANIZATION/Defects4J"
# BASE_DIR="AVEC_PACKAGE_PLUS_ADDED-GLOBAL/Defects4J-FV-GOOD-ORGANIZATION/Defects4J"

# SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/SOURCES"
# STRATEGIES="COT TOT FEWSHOT ZEROSHOT"

# # Chemins vers JUnit et Hamcrest (à ajuster selon votre environnement)
# JUNIT_PATH=$CURRENT_DIR/JUNIT-HAMCREST/junit-4.13.2.jar
# HAMCREST_CORE_PATH=$CURRENT_DIR/JUNIT-HAMCREST/hamcrest-core-1.3.jar

# SL4J_PATH="$CURRENT_DIR/slf4j-api-1.6.4.jar"
# JACOCO_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacococli.jar"
# JACOCO_AGENT_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacocoagent.jar"

# # Fichier pour enregistrer les chemins des classes compilées avec succès
# SUCCESS_FILE="compiled_successfully.txt"
# # Fichier CSV pour les statistiques
# STATS_FILE="compilation_stats.csv"

# # Initialiser ou vider les fichiers
# > "$SUCCESS_FILE"
# echo "Strategy,Project,BugID,Success,Failed" > "$STATS_FILE"


# declare -A total_successes

# compile_java_files() {
#     local strategy="$1"
#     local project_name="$2"
#     local bug_id="$3"
#     local file="$4"
#     local project_sources="${SOURCES_DIR}/${project_name}/${bug_id}"
#     local success_count=0
#     local fail_count=0

#     echo "Compiling: $file"
#     if javac -cp ".:${project_sources}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}" "$file"; then
#         echo "Compilation succeeded for $file"
#         success_count=1
#         echo "$file" >> "$SUCCESS_FILE"
#     else
#         echo "Compilation failed for $file"
#         fail_count=1
#     fi

#     # Ajout des succès et échecs au fichier CSV
#     echo "${strategy},${project_name},${bug_id},${success_count},${fail_count}" >> "$STATS_FILE"
#     # Mise à jour du total de succès par stratégie
#     ((total_successes[$strategy]+=$success_count))
# }

# for strategy in $STRATEGIES; do
#     for project_bug_dir in "${BASE_DIR}/${strategy}"/*/*; do
#         if [ -d "$project_bug_dir" ]; then
#             project_name=$(basename $(dirname "$project_bug_dir"))
#             bug_id=$(basename "$project_bug_dir")
#             find "$project_bug_dir" -type f -name "*.java" -print0 | while IFS= read -r -d $'\0' file; do
#                 compile_java_files "$strategy" "$project_name" "$bug_id" "$file"
#             done
#         fi
#     done
# done

# # Écriture des totaux par stratégie dans le fichier CSV
# echo "Strategy,Total Success" >> "$STATS_FILE"
# for strategy in "${!total_successes[@]}"; do
#     echo "${strategy},${total_successes[$strategy]}" >> "$STATS_FILE"
# done

# echo "Compilation process completed. Stats written to $STATS_FILE"
















