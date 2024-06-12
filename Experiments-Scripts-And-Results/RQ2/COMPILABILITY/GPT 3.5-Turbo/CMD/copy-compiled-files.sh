#!/bin/bash

# Fichier contenant les chemins des fichiers source
#SOURCE_FILE="compiled_successfully.txt"
SOURCE_FILE="compiled_successfully-SETP1-V3.txt"

# Répertoire de destination pour copier les fichiers
DEST_DIR="COMPILED-CLASSES"

# Lire chaque ligne du fichier source
while IFS= read -r line; do
    # Construire le chemin de destination en préfixant avec le répertoire de destination
    dest_path="${DEST_DIR}/${line}"

    # Créer le répertoire de destination si nécessaire
    mkdir -p "$(dirname "$dest_path")"

    # Copier le fichier .java vers le répertoire de destination
    cp "$line" "$(dirname "$dest_path")"

    # Vérifier si le fichier .class correspondant existe et le copier si c'est le cas
    class_file="${line%.java}.class"
    if [ -f "$class_file" ]; then
        cp "$class_file" "$(dirname "$dest_path")"
    fi
done < "$SOURCE_FILE"

echo "Les fichiers ont été copiés avec succès."

