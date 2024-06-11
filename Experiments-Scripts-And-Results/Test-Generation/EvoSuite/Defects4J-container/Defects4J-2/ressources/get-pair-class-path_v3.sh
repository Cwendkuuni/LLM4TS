#!/bin/bash

# Chemin du dossier racine à parcourir
root_dir="FILTERED"

# Fichier de sortie pour les informations extraites
output_file="project_info_3.txt"

# Vérifier si le fichier de sortie existe déjà et le supprimer pour éviter les doublons
if [ -f "$output_file" ]; then
    rm "$output_file"
fi

# Parcourir le dossier FILTERED et ses sous-dossiers
find "$root_dir" -type f -name "*.java" | while read file; do
    # Extraire les composants du chemin
    path_components=($(echo $file | tr "/" "\n"))
    
    # Nom du projet
    project_name=${path_components[1]}
    
    # Bug ID
    bug_id=${path_components[2]}
    
    # Chemin vers la classe (exclut le nom du fichier .java)
    class_path=$(echo "${path_components[@]:3:${#path_components[@]}-4}" | tr " " "/")
    
    # Nom complet de la classe (remplace les '/' par '.' et enlève l'extension .java)
    full_class_name=$(echo "${class_path}" | tr "/" ".").$(basename "$file" ".java")
    
    # Chemin vers la classe avec le format demandé "org/apache/commons/cli/"
    formatted_class_path="$class_path/"
    
    # Extraire le chemin avec un niveau de détail supplémentaire "org/apache/commons/"
    # Pour cela, nous retirons le dernier segment du chemin de la classe
    IFS='/' read -r -a path_array <<< "$class_path"
    unset path_array[-1] # Retire le dernier élément du tableau
    detailed_class_path=$(IFS=/; echo "${path_array[*]}")
    detailed_class_path="$detailed_class_path/"

    # Écrire les informations dans le fichier de sortie
    # Format: nom_projet bug-id classe chemin_vers_la_classe chemin_détail
    echo "\"$project_name $bug_id $full_class_name $formatted_class_path $detailed_class_path\"" >> "$output_file"
done

echo "Les informations ont été écrites dans $output_file"

