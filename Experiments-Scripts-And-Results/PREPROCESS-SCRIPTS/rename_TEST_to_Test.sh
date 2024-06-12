#!/bin/bash

# Chemin du dossier racine contenant les sous-dossiers et fichiers Java
#RACINE="$PWD/Defects4J-FV-GOOD-ORGANIZATION/Defects4J/"
RACINE="$PWD/MISTRAL7B_FOR_DEFECTS4J_DATASET/"

find "$RACINE" -type f -name '*TEST*.java' | while read -r fichier; do
  # Détermine le nouveau nom du fichier en remplaçant 'TEST' par 'Test'
  nom_fichier=$(basename "$fichier")
  nouveau_nom_fichier=$(echo "$nom_fichier" | sed 's/TEST/Test/')
  
  # Construit le chemin complet du nouveau fichier sans changer son dossier
  nouveau_chemin=$(dirname "$fichier")/"$nouveau_nom_fichier"
  
  # Renomme le fichier
  mv "$fichier" "$nouveau_chemin"
  
  echo "Renommé: $fichier -> $nouveau_chemin"
done

echo "Renommage terminé."

