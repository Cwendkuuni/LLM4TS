import csv
import os
from bs4 import BeautifulSoup

# def extraire_donnees_couverture(nom_fichier_html):
#     with open(nom_fichier_html, 'r', encoding='utf-8') as fichier:
#         soup = BeautifulSoup(fichier, 'html.parser')
        
#         pourcentage_instructions = soup.find('tfoot').find_all('td')[2].text.strip()
#         pourcentage_branches = soup.find('tfoot').find_all('td')[4].text.strip()
        
#         lignes_manquantes = int(soup.find('tfoot').find_all('td')[7].text.strip())
#         lignes_total = int(soup.find('tfoot').find_all('td')[8].text.strip())
#         pourcentage_lignes = ((lignes_total - lignes_manquantes) / lignes_total) * 100 if lignes_total > 0 else 0
        
#         methodes_manquantes = int(soup.find('tfoot').find_all('td')[9].text.strip())
#         methodes_total = int(soup.find('tfoot').find_all('td')[10].text.strip())
#         pourcentage_methodes = ((methodes_total - methodes_manquantes) / methodes_total) * 100 if methodes_total > 0 else 0
        
#         return pourcentage_instructions, pourcentage_branches, f"{pourcentage_lignes:.2f}%", f"{pourcentage_methodes:.2f}%"



def extraire_donnees_couverture(nom_fichier_html):
    with open(nom_fichier_html, 'r', encoding='utf-8') as fichier:
        soup = BeautifulSoup(fichier, 'html.parser')
        
        tfoot = soup.find('tfoot')
        if not tfoot:  # Vérifie si <tfoot> est trouvé
            return 'n/a', 'n/a', 'n/a', 'n/a'  # Retourne 'n/a' pour toutes les valeurs si non trouvé
        
        td_elements = tfoot.find_all('td')
        # Assurez-vous qu'il y a suffisamment de <td> éléments avant d'essayer d'accéder aux indices
        if len(td_elements) < 11:
            return 'n/a', 'n/a', 'n/a', 'n/a'  # Retourne 'n/a' pour toutes les valeurs si pas assez de <td>

        try:
            pourcentage_instructions = td_elements[2].text.strip()
            pourcentage_branches = td_elements[4].text.strip()
            lignes_manquantes = int(td_elements[7].text.strip())
            lignes_total = int(td_elements[8].text.strip())
            pourcentage_lignes = ((lignes_total - lignes_manquantes) / lignes_total) * 100 if lignes_total > 0 else 0
            methodes_manquantes = int(td_elements[9].text.strip())
            methodes_total = int(td_elements[10].text.strip())
            pourcentage_methodes = ((methodes_total - methodes_manquantes) / methodes_total) * 100 if methodes_total > 0 else 0
        except (ValueError, IndexError):  # Gère les erreurs de conversion et les accès aux indices
            return 'n/a', 'n/a', 'n/a', 'n/a'
        
        return pourcentage_instructions, pourcentage_branches, f"{pourcentage_lignes:.2f}%", f"{pourcentage_methodes:.2f}%"


Jacoco_output_dir="Jacoco-Coverage-Stats-output"
# Créer le dossier s'il n'existe pas
os.makedirs(Jacoco_output_dir, exist_ok=True)

racine = 'SF110/SORTIES-RESULTATS'
strategie = 'EVOSUITE'

for projet in os.listdir(os.path.join(racine, strategie)):
    chemin_projet = os.path.join(racine, strategie, projet)
    #fichier_csv = f'{strategie}-{projet}.csv'
    fichier_csv = f'Jacoco-Coverage-Stats-output/{strategie}-{projet}.csv'
    
    with open(fichier_csv, 'w', newline='', encoding='utf-8') as fichier:
        writer = csv.writer(fichier)
        writer.writerow(['Projects', 'Class-name', 'Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage'])
        
        for classe_dossier in os.listdir(chemin_projet):
            classe_nom_simple = classe_dossier.split('.')[-1]  # Extrait le nom simple de la classe
            chemin_index_html = os.path.join(chemin_projet, classe_dossier, 'EXEC', classe_dossier, 'index.html')
            
            if os.path.exists(chemin_index_html):
                donnees_couverture = extraire_donnees_couverture(chemin_index_html)
                writer.writerow([projet, classe_nom_simple, *donnees_couverture])

# for strategie in strategies:
#     for projet in os.listdir(os.path.join(racine, strategie)):
#         chemin_projet = os.path.join(racine, strategie, projet)
#         fichier_csv = f'Jacoco-Coverage-Stats-output/{strategie}-{projet}.csv'
        
#         with open(fichier_csv, 'w', newline='', encoding='utf-8') as fichier:
#             writer = csv.writer(fichier)
#             writer.writerow(['Projects', 'Class-name', 'Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage'])
            
#             for dossier_resultat in os.listdir(chemin_projet):
#                 if dossier_resultat.startswith('result-Class-'):
#                     nom_classe = dossier_resultat.split('-', 2)[-1]
#                     chemin_index_html = os.path.join(chemin_projet, dossier_resultat, 'EXEC', strategie, projet, nom_classe, 'index.html')
                    
#                     if os.path.exists(chemin_index_html):
#                         donnees_couverture = extraire_donnees_couverture(chemin_index_html)
#                         writer.writerow([projet, nom_classe, *donnees_couverture])

print("Extraction terminée.")
