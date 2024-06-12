import csv
import os

# Structure pour stocker les données fusionnées
donnees_fusionnees = {}

# Liste des stratégies pour définir l'ordre des colonnes dans le fichier fusionné
strategies = ['COT', 'TOT', 'TOT2', 'FEWSHOT', 'ZEROSHOT']
#strategies = ['TOT2']

# Lire les fichiers CSV et remplir 'donnees_fusionnees'
for strategie in strategies:
    for projet in os.listdir('.'):  # Adaptez ce chemin au besoin
        if projet.startswith(strategie):
            with open(projet, 'r', encoding='utf-8') as fichier:
                lecteur = csv.DictReader(fichier)
                for ligne in lecteur:
                    bug_id = ligne['Projects-Bug-Id']
                    if bug_id not in donnees_fusionnees:
                        donnees_fusionnees[bug_id] = {s: {'Instruction-Coverage': 'n/a', 'Branch-Coverage': 'n/a', 'Line-Coverage': 'n/a', 'Method-Coverage': 'n/a'} for s in strategies}
                    donnees_fusionnees[bug_id][strategie] = ligne

# Fonction pour extraire le projet et le numéro de bug pour le tri
def cle_tri(bug_id):
    projet, numero = bug_id.rsplit('-', 1)
    return (projet, int(numero))

# Écrire le fichier CSV fusionné avec les entrées triées
with open('fusion.csv', 'w', newline='', encoding='utf-8') as fichier:
    en_tetes = ['Projects-Bug-Id'] + [f'{s}-{m}' for s in strategies for m in ['Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage']]
    ecrivain = csv.DictWriter(fichier, fieldnames=en_tetes)
    ecrivain.writeheader()
    for bug_id in sorted(donnees_fusionnees.keys(), key=cle_tri):
        data = donnees_fusionnees[bug_id]
        ligne_a_ecrire = {'Projects-Bug-Id': bug_id}
        for strategie in strategies:
            for mesure in ['Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage']:
                ligne_a_ecrire[f'{strategie}-{mesure}'] = data[strategie].get(mesure, 'n/a')
        ecrivain.writerow(ligne_a_ecrire)

print("Fusion terminée avec tri.")

