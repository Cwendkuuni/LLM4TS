#!/bin/bash


# Dossier racine pour "SOURCES" et "SANS_PACKAGE"
ROOT_DIR="$PWD"

# Dossier contenant les sources originales pour les projets Defects4J
#SOURCES_DIR="$ROOT_DIR/SOURCES"
SOURCES_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/SOURCES"

# Dossier contenant les classes sans l'instruction "package" pour Defects4J
#SANS_PACKAGE_DIR="$ROOT_DIR/SANS_PACKAGE/Defects4J-FV-GOOD-ORGANIZATION/Defects4J"
SANS_PACKAGE_DIR="$ROOT_DIR/SANS_PACKAGE/DEFECTS4J"

# Fichier pour enregistrer les fichiers source non trouvés
NOT_FOUND_FILE="$ROOT_DIR/files_not_found.txt"

# S'assurer que le fichier de rapport est vide au début du script
> "$NOT_FOUND_FILE"

# Stratégies
STRATEGIES=("COT" "TOT" "TOT2" "FEWSHOT" "ZEROSHOT")

# Parcours des stratégies
for strategy in "${STRATEGIES[@]}"; do
  # Parcours des projets et bug-ids
  for project_bug_dir in "$SANS_PACKAGE_DIR/$strategy"/*/*; do
    project_name=$(basename "$(dirname "$project_bug_dir")")
    bug_id=$(basename "$project_bug_dir")
    
    # Parcours des classes de test pour chaque itération de test généré
    find "$project_bug_dir" -type f -name "*Test.java" | while read -r test_class_file; do
      class_base_name=$(basename "$test_class_file" "Test.java")
      
      # Trouve le fichier source correspondant dans l'arborescence de SOURCES, tenant compte du nom du projet et du bug-id
      source_file_path=$(find "$SOURCES_DIR/$project_name/$bug_id" -type f -name "${class_base_name}.java" | head -n 1)
      
      if [ ! -z "$source_file_path" ] && [ -f "$source_file_path" ]; then
        # Extrait la ligne contenant l'instruction "package"
        package_line=$(grep "^package" "$source_file_path")
        
        # Ajoute la ligne "package" au début du fichier de classe de test
        if [ ! -z "$package_line" ]; then
          temp_file=$(mktemp)
          echo "$package_line" > "$temp_file"
          cat "$test_class_file" >> "$temp_file"
          mv "$temp_file" "$test_class_file"
          echo "Ajouté 'package' à $test_class_file depuis $source_file_path"
        fi
      else
        echo "Fichier source non trouvé pour $test_class_file basé sur ${class_base_name}.java dans $project_name/$bug_id" >> "$NOT_FOUND_FILE"
      fi
    done
  done
done

echo "Modification des classes de test terminée. Vérifiez $NOT_FOUND_FILE pour les fichiers source non trouvés."
