#!/bin/bash

# Chemin du répertoire à partir duquel la recherche doit commencer
# START_DIR="Defects4J-FV-GOOD-ORGANIZATION/Defects4J/"
START_DIR="MISTRAL7B_FOR_DEFECTS4J_DATASET"


# Dossiers de destination
AVEC_PACKAGE_DIR="AVEC_PACKAGE"
SANS_PACKAGE_DIR="SANS_PACKAGE"

# Initialisation des compteurs de stratégies
#declare -A stats=( ["COT"]=0 ["FEWSHOT"]=0 ["TOT"]=0 ["ZEROSHOT"]=0 )
declare -A stats=( ["COT"]=0 ["FEWSHOT"]=0 ["TOT"]=0 ["TOT2"]=0 ["ZEROSHOT"]=0 )

# Crée les dossiers de destination s'ils n'existent pas
mkdir -p "$AVEC_PACKAGE_DIR" "$SANS_PACKAGE_DIR"

# Fonction pour extraire la stratégie à partir du chemin du fichier
extract_strategy() {
  #echo "$1" | grep -oE '(COT|FEWSHOT|TOT|ZEROSHOT)'
  echo "$1" | grep -oE '(COT|FEWSHOT|TOT|TOT2|ZEROSHOT)'
}

# Utilisation de la redirection de processus au lieu d'un pipeline
while IFS= read -r file; do
  strategy=$(extract_strategy "$file")
  
  if grep -qm1 "^package" "$file"; then
    # Incrémente le compteur de la stratégie
    if [[ ! -z "$strategy" ]]; then
      ((stats[$strategy]++))
    fi
    
    # Copie le fichier dans AVEC_PACKAGE en respectant l'arborescence
    dest="$AVEC_PACKAGE_DIR/${file#$START_DIR/}"
    mkdir -p "$(dirname "$dest")"
    cp "$file" "$dest"
  else
    # Copie le fichier dans SANS_PACKAGE en respectant l'arborescence
    dest="$SANS_PACKAGE_DIR/${file#$START_DIR/}"
    mkdir -p "$(dirname "$dest")"
    cp "$file" "$dest"
  fi
done < <(find "$START_DIR" -type f -name "*.java")

# Enregistrement des statistiques dans un fichier CSV
STATS_FILE="defects4j-stats-with-package.csv"
echo "Stratégie,Nombre" > "$STATS_FILE"
for strategy in "${!stats[@]}"; do
  echo "$strategy,${stats[$strategy]}" >> "$STATS_FILE"
done

echo "Terminé. Les statistiques sont enregistrées dans $STATS_FILE."





# #!/bin/bash

# # Chemin du répertoire à partir duquel la recherche doit commencer
# START_DIR="SF110-FV-GOOD-ORGANIZATION/SF110/"

# # Dossiers de destination
# AVEC_PACKAGE_DIR="AVEC_PACKAGE"
# SANS_PACKAGE_DIR="SANS_PACKAGE"

# # Initialisation des compteurs de stratégies
# declare -A stats=( ["COT"]=0 ["FEWSHOT"]=0 ["TOT"]=0 ["ZEROSHOT"]=0 )

# # Crée les dossiers de destination s'ils n'existent pas
# mkdir -p "$AVEC_PACKAGE_DIR" "$SANS_PACKAGE_DIR"

# # Fonction pour extraire la stratégie à partir du chemin du fichier
# extract_strategy() {
#   echo "$1" | grep -oE '(COT|FEWSHOT|TOT|ZEROSHOT)'
# }

# # Parcours des fichiers Java
# find "$START_DIR" -type f -name "*.java" | while read -r file; do
#   # Extrait la stratégie du fichier courant
#   strategy=$(extract_strategy "$file")
  
#   if grep -qm1 "^package" "$file"; then
#     # Incrémente le compteur de la stratégie
#     ((stats[$strategy]++))
    
#     # Copie le fichier dans AVEC_PACKAGE en respectant l'arborescence
#     dest="$AVEC_PACKAGE_DIR/${file#$START_DIR/}"
#     mkdir -p "$(dirname "$dest")"
#     cp "$file" "$dest"
#   else
#     # Copie le fichier dans SANS_PACKAGE en respectant l'arborescence
#     dest="$SANS_PACKAGE_DIR/${file#$START_DIR/}"
#     mkdir -p "$(dirname "$dest")"
#     cp "$file" "$dest"
#   fi
# done

# # Enregistrement des statistiques dans un fichier CSV
# STATS_FILE="stats.csv"
# echo "Stratégie,Nombre" > "$STATS_FILE"
# for strategy in "${!stats[@]}"; do
#   echo "$strategy,${stats[$strategy]}" >> "$STATS_FILE"
# done

# echo "Terminé. Les statistiques sont enregistrées dans $STATS_FILE."
