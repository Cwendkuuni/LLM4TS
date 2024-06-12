import csv
import os
from bs4 import BeautifulSoup

# def extraire_donnees_couverture(nom_fichier_html):
#     with open(nom_fichier_html, 'r', encoding='utf-8') as fichier:
#         contenu = fichier.read()
#         soup = BeautifulSoup(contenu, 'html.parser')
        
#         # Extrait le pourcentage de couverture des instructions
#         pourcentage_instructions = soup.find('tfoot').find_all('td')[2].text.strip()
        
#         # Extrait le pourcentage de couverture des branches
#         pourcentage_branches = soup.find('tfoot').find_all('td')[4].text.strip()
        
#         # Extrait le nombre de lignes manquées et le total directement
#         lignes_manquantes = int(soup.find('tfoot').find_all('td')[7].text.strip())
#         lignes_total = int(soup.find('tfoot').find_all('td')[8].text.strip())
#         # Calcul du pourcentage de couverture des lignes
#         pourcentage_lignes = ((lignes_total - lignes_manquantes) / lignes_total) * 100 if lignes_total > 0 else 0
        
#         # Extrait le nombre de méthodes manquées et le total directement
#         methodes_manquantes = int(soup.find('tfoot').find_all('td')[9].text.strip())
#         methodes_total = int(soup.find('tfoot').find_all('td')[10].text.strip())
#         # Calcul du pourcentage de couverture des méthodes
#         pourcentage_methodes = ((methodes_total - methodes_manquantes) / methodes_total) * 100 if methodes_total > 0 else 0
        
        
#         return (pourcentage_instructions, pourcentage_branches, f"{pourcentage_lignes:.2f}%", f"{pourcentage_methodes:.2f}%")



def extraire_donnees_couverture(nom_fichier_html):
    with open(nom_fichier_html, 'r', encoding='utf-8') as fichier:
        contenu = fichier.read()
        soup = BeautifulSoup(contenu, 'html.parser')
        
        tfoot = soup.find('tfoot')
        if tfoot is None:
            return ('n/a', 'n/a', 'n/a', 'n/a')  # Retourne 'n/a' pour toutes les valeurs si <tfoot> est absent
        
        td_elements = tfoot.find_all('td')
        if len(td_elements) < 11:  # Vérifie si le nombre d'éléments <td> est suffisant
            return ('n/a', 'n/a', 'n/a', 'n/a')  # Retourne 'n/a' pour toutes les valeurs si pas assez de <td>
        
        pourcentage_instructions = td_elements[2].text.strip()
        pourcentage_branches = td_elements[4].text.strip()
        
        try:
            lignes_manquantes = int(td_elements[7].text.strip())
            lignes_total = int(td_elements[8].text.strip())
            pourcentage_lignes = ((lignes_total - lignes_manquantes) / lignes_total) * 100 if lignes_total > 0 else 0
            
            methodes_manquantes = int(td_elements[9].text.strip())
            methodes_total = int(td_elements[10].text.strip())
            pourcentage_methodes = ((methodes_total - methodes_manquantes) / methodes_total) * 100 if methodes_total > 0 else 0
        except ValueError:  # Gère le cas où la conversion en int échoue
            return ('n/a', 'n/a', 'n/a', 'n/a')
        
        return (pourcentage_instructions, pourcentage_branches, f"{pourcentage_lignes:.2f}%", f"{pourcentage_methodes:.2f}%")


racine = 'SORTIES-RESULTATS'
for strategie in os.listdir(racine):
    chemin_strategie = os.path.join(racine, strategie)
    if os.path.isdir(chemin_strategie):
        for projet in os.listdir(chemin_strategie):
            chemin_projet = os.path.join(chemin_strategie, projet)
            if os.path.isdir(chemin_projet):
                fichier_csv = f'Jacoco-Coverage-Stats-output/{strategie}-{projet}.csv'
                donnees_a_ecrire = []  # Liste pour collecter les données
                
                for dossier_bug in os.listdir(chemin_projet):
                    chemin_dossier_bug = os.path.join(chemin_projet, dossier_bug, 'EXEC', strategie, projet, dossier_bug.split('-')[-1], 'index.html')
                    if os.path.exists(chemin_dossier_bug):
                        donnees_couverture = extraire_donnees_couverture(chemin_dossier_bug)
                        bug_id = int(dossier_bug.split('-')[-1])  # Extrait le numéro du bug pour le tri
                        donnees_a_ecrire.append((f"{projet}-{bug_id}", *donnees_couverture))
                
                # Trier les données par 'Projects-Bug-Id' (le numéro du bug)
                donnees_a_ecrire.sort(key=lambda x: int(x[0].split('-')[-1]))
                
                # Écrire les données triées dans le fichier CSV
                with open(fichier_csv, 'w', newline='', encoding='utf-8') as fichier:
                    writer = csv.writer(fichier)
                    writer.writerow(['Projects-Bug-Id', 'Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage'])
                    for ligne in donnees_a_ecrire:
                        writer.writerow(ligne)

print("Extraction et écriture ordonnées terminées.")






# ##############################################"
# racine = 'SORTIES-RESULTATS'
# for strategie in os.listdir(racine):
#     chemin_strategie = os.path.join(racine, strategie)
#     if os.path.isdir(chemin_strategie):
#         for projet in os.listdir(chemin_strategie):
#             chemin_projet = os.path.join(chemin_strategie, projet)
#             if os.path.isdir(chemin_projet):
#                 fichier_csv = f'Jacoco-Coverage-Stats-output/{strategie}-{projet}.csv'
#                 with open(fichier_csv, 'w', newline='', encoding='utf-8') as fichier:
#                     writer = csv.writer(fichier)
#                     writer.writerow(['Projects-Bug-Id', 'Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage'])
#                     for dossier_bug in os.listdir(chemin_projet):
#                         chemin_dossier_bug = os.path.join(chemin_projet, dossier_bug, 'EXEC', strategie, projet, dossier_bug.split('-')[-1], 'index.html')
#                         if os.path.exists(chemin_dossier_bug):
#                             donnees_couverture = extraire_donnees_couverture(chemin_dossier_bug)
#                             writer.writerow([f"{projet}-{dossier_bug.split('-')[-1]}", *donnees_couverture])





# print("Extraction terminée.")






