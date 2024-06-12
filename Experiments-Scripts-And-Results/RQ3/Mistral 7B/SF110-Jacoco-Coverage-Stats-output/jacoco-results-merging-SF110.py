import csv
import os
from collections import defaultdict

# Étape 1: Collecter les données
donnees = defaultdict(lambda: defaultdict(lambda: {'COT': 'n/a', 'TOT': 'n/a', 'TOT2': 'n/a', 'FEWSHOT': 'n/a', 'ZEROSHOT': 'n/a'}))
strategies = ['COT', 'TOT', 'TOT2', 'FEWSHOT', 'ZEROSHOT']

#donnees = defaultdict(lambda: defaultdict(lambda: {'TOT2': 'n/a'}))
#strategies = ['TOT2']

for strategie in strategies:
    for fichier_csv in os.listdir('.'):
        if fichier_csv.startswith(strategie):
            with open(fichier_csv, mode='r', encoding='utf-8') as fichier:
                lecteur = csv.DictReader(fichier)
                for ligne in lecteur:
                    projet_classe = (ligne['Projects'], ligne['Class-name'])
                    donnees[projet_classe][strategie] = {
                        'Instruction-Coverage': ligne['Instruction-Coverage'],
                        'Branch-Coverage': ligne['Branch-Coverage'],
                        'Line-Coverage': ligne['Line-Coverage'],
                        'Method-Coverage': ligne['Method-Coverage']
                    }

# Étape 2 & 3: Écrire le fichier CSV fusionné avec tri
with open('fusion_complete.csv', mode='w', newline='', encoding='utf-8') as fichier_fusion:
    en_tetes = ['Projects', 'Class-name'] + [f'{s}-{m}' for s in strategies for m in ['Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage']]
    ecrivain = csv.DictWriter(fichier_fusion, fieldnames=en_tetes)
    ecrivain.writeheader()

    # Trier les entrées par nom de projet puis par nom de classe
    for projet_classe in sorted(donnees, key=lambda x: (x[0], x[1])):
        couvertures = donnees[projet_classe]
        ligne = {'Projects': projet_classe[0], 'Class-name': projet_classe[1]}
        for strategie, mesures in couvertures.items():
            for mesure, valeur in mesures.items():
                ligne[f'{strategie}-{mesure}'] = valeur
        ecrivain.writerow(ligne)

print("Fusion complète avec tri terminée.")

