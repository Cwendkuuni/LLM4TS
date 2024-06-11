#!/bin/bash

# Définition du tableau des identifiants connus
liste_ids=(101 202 303 404)

# Fonction pour vérifier si un élément est dans le tableau
est_dans_la_liste() {
    local e match="$1"
    shift
    for e; do [[ "$e" == "$match" ]] && return 0; done
    return 1
}

# Action pour les éléments qui sont dans le tableau
action_pour_elements_dans_tableau() {
    echo "L'élément $1 est dans le tableau."
    # Action spécifique pour les éléments dans le tableau
    cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/target/classes/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
}

# Action pour les éléments qui ne sont pas dans le tableau
action_pour_elements_hors_tableau() {
    echo "L'élément $1 n'est pas dans le tableau."
    # Action spécifique pour les éléments hors du tableau
    cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/build/classes/main/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
}

# Liste d'éléments à vérifier
elements_a_verifier=(100 202 303 405)

# Vérification pour chaque élément
for element in "${elements_a_verifier[@]}"; do
    if est_dans_la_liste "$element" "${liste_ids[@]}"; then
        action_pour_elements_dans_tableau "$element"
    else
        action_pour_elements_hors_tableau "$element"
    fi
done

