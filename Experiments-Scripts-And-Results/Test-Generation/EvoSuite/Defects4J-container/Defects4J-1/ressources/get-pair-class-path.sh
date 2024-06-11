#!/bin/bash

# Chemin du dossier racine à parcourir
root_dir="FILTERED"

# Fichier de sortie pour les informations extraites
output_file="project_info.txt"

# Vérifier si le fichier de sortie existe déjà et le supprimer pour éviter les doublons
if [ -f "$output_file" ]; then
    rm "$output_file"
fi

# Parcourir le dossier FILTERED et ses sous-dossiers
find "$root_dir" -type f -name "*.java" | while read file; do
    # Extraire les composants du chemin
    # Exemple de chemin: FILTERED/Cli/1/org/apache/commons/cli/CommandLine.java
    path_components=($(echo $file | tr "/" "\n"))
    
    # Nom du projet
    project_name=${path_components[1]}
    
    # Bug ID
    bug_id=${path_components[2]}
    
    # Chemin vers la classe (exclut le nom du fichier .java)
    class_path=$(echo "${path_components[@]:3:${#path_components[@]}-4}" | tr " " "/")
    
    # Nom complet de la classe (remplace les '/' par '.' et enlève l'extension .java)
    full_class_name=$(echo "${class_path}" | tr "/" ".").$(basename "$file" ".java")
    
    # Chemin vers la classe avec le format demandé "Cli/1/org/apache/commons/cli/"
    formatted_class_path="$project_name/$bug_id/$class_path/"
    
    # Écrire les informations dans le fichier de sortie
    echo "\"$project_name $bug_id $full_class_name $formatted_class_path\"" >> "$output_file"
done

echo "Les informations ont été écrites dans $output_file"

