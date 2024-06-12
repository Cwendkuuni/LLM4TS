import csv
import os
from bs4 import BeautifulSoup

def extraire_donnees_couverture(nom_fichier_html):
    with open(nom_fichier_html, 'r', encoding='utf-8') as fichier:
        contenu = fichier.read()
        soup = BeautifulSoup(contenu, 'html.parser')
        
        tfoot = soup.find('tfoot')
        if not tfoot:
            return 'n/a', 'n/a', 'n/a', 'n/a'
        
        td_elements = tfoot.find_all('td')
        if len(td_elements) < 11:
            return 'n/a', 'n/a', 'n/a', 'n/a'
        
        pourcentage_instructions = td_elements[2].text.strip()
        pourcentage_branches = td_elements[4].text.strip()
        
        try:
            lignes_manquantes = int(td_elements[7].text.strip())
            lignes_total = int(td_elements[8].text.strip())
            pourcentage_lignes = f"{((lignes_total - lignes_manquantes) / lignes_total) * 100:.2f}%"
            
            methodes_manquantes = int(td_elements[9].text.strip())
            methodes_total = int(td_elements[10].text.strip())
            pourcentage_methodes = f"{((methodes_total - methodes_manquantes) / methodes_total) * 100:.2f}%"
        except ValueError:
            return 'n/a', 'n/a', 'n/a', 'n/a'
        
        return pourcentage_instructions, pourcentage_branches, pourcentage_lignes, pourcentage_methodes

racine = 'SORTIES-RESULTATS'
strategie = 'EVOSUITE'
output_dir = "Jacoco-Coverage-Stats-output"
os.makedirs(output_dir, exist_ok=True)

for projet in os.listdir(os.path.join(racine, strategie)):
    chemin_projet = os.path.join(racine, strategie, projet)
    if os.path.isdir(chemin_projet):
        fichier_csv = os.path.join(output_dir, f'{strategie}-{projet}.csv')
        donnees_a_ecrire = []
        
        for bug_id in os.listdir(chemin_projet):
            chemin_bug_id = os.path.join(chemin_projet, bug_id, 'EXEC', bug_id)
            if os.path.isdir(chemin_bug_id):
                for classe_fqdn in os.listdir(chemin_bug_id):
                    chemin_index_html = os.path.join(chemin_bug_id, classe_fqdn, 'index.html')
                    
                    if os.path.exists(chemin_index_html):
                        donnees_couverture = extraire_donnees_couverture(chemin_index_html)
                        classe_nom_simple = classe_fqdn.split('.')[-1]  # Extraction du nom simple de la classe
                        donnees_a_ecrire.append((f"{projet}-{bug_id}", classe_nom_simple, *donnees_couverture))
        
        # Correction appliquée ici pour le tri des données
        donnees_a_ecrire.sort(key=lambda x: (x[0].split('-')[0], int(x[0].split('-')[1])))
        
        with open(fichier_csv, 'w', newline='', encoding='utf-8') as fichier:
            writer = csv.writer(fichier)
            writer.writerow(['Projects-Bug-Id', 'Class-name', 'Instruction-Coverage', 'Branch-Coverage', 'Line-Coverage', 'Method-Coverage'])
            for ligne in donnees_a_ecrire:
                writer.writerow(ligne)

print("Extraction et écriture ordonnées terminées pour EVOSUITE.")
