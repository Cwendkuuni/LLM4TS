import csv
import os
import xml.etree.ElementTree as ET

# Chemin vers le dossier racine contenant les dossiers de stratégies
dossier_racine = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V4/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL'
# Fichier CSV de sortie
csv_fichier = 'resultats_checkstyle_par_strategie_V4.csv'

# Entêtes pour le fichier CSV
entetes = ['Strategie', 'Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']

# Fonction pour analyser les fichiers XML et écrire les détails
def analyser_xml_et_ecrire_details(writer, chemin_complet, nom_classe, strategie):
    tree = ET.parse(chemin_complet)
    root = tree.getroot()

    for error in root.findall('.//error'):
        details = {
            'line': error.get('line'),
            'column': error.get('column'),
            'severity': error.get('severity'),
            'message': error.get('message'),
            'source': error.get('source'),
            'type': error.get('source').split('.')[-1],
        }
        writer.writerow([strategie, nom_classe] + list(details.values()))

def parcourir_dossiers_et_analyser(dossier_racine, writer):
    for strategie in os.listdir(dossier_racine):
        chemin_strategie = os.path.join(dossier_racine, strategie)
        if os.path.isdir(chemin_strategie):
            for root, _, files in os.walk(chemin_strategie):
                for file in files:
                    if file.endswith('.xml'):
                        analyser_xml_et_ecrire_details(writer, os.path.join(root, file), file.split('.')[0], strategie)

def ecrire_csv_complet(dossier_racine, csv_fichier):
    with open(csv_fichier, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(entetes)  # Écrire l'entête du fichier CSV
        parcourir_dossiers_et_analyser(dossier_racine, writer)

ecrire_csv_complet(dossier_racine, csv_fichier)



# import csv
# import os
# import xml.etree.ElementTree as ET

# # Chemin vers le dossier contenant les rapports XML
# rapports_dossier = 'rapports'
# # Fichier CSV de sortie
# csv_fichier = 'resultats_checkstyle.csv'

# # Entêtes pour le fichier CSV
# entetes = ['Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']

# # Dictionnaires pour les synthèses
# synthese_types = {}
# synthese_classes = {}

# def analyser_xml_et_ecrire_details(writer, chemin_complet, nom_classe):
#     tree = ET.parse(chemin_complet)
#     root = tree.getroot()
#     problemes_par_classe = 0

#     for error in root.findall('.//error'):
#         line = error.get('line')
#         column = error.get('column')
#         severity = error.get('severity')
#         message = error.get('message')
#         source = error.get('source')
#         type_probleme = source.split('.')[-1]

#         # Écrire les détails de l'erreur dans le CSV
#         writer.writerow([nom_classe, line, column, severity, message, source, type_probleme])

#         # Mise à jour des comptages pour les synthèses
#         synthese_types[type_probleme] = synthese_types.get(type_probleme, 0) + 1
#         problemes_par_classe += 1

#     return problemes_par_classe

# def ecrire_csv_complet(rapports_dossier, csv_fichier):
#     with open(csv_fichier, 'w', newline='', encoding='utf-8') as csvfile:
#         writer = csv.writer(csvfile)
#         writer.writerow(entetes)  # Écrire l'entête du fichier CSV

#         for filename in os.listdir(rapports_dossier):
#             if filename.endswith('.xml'):
#                 chemin_complet = os.path.join(rapports_dossier, filename)
#                 nom_classe = os.path.basename(filename).split('.')[0]
#                 problemes_par_classe = analyser_xml_et_ecrire_details(writer, chemin_complet, nom_classe)
#                 synthese_classes[nom_classe] = problemes_par_classe

#         # Écrire la synthèse par Type d'Erreur
#         writer.writerow(['', '', '', '', '', 'Synthèse par Type d\'Erreur', ''])
#         for type_erreur, total in synthese_types.items():
#             writer.writerow(['', '', '', '', '', type_erreur, total])
        
#         # Écrire la synthèse par Classe
#         writer.writerow(['', '', '', '', '', 'Synthèse par Classe', ''])
#         for classe, total in synthese_classes.items():
#             writer.writerow([classe, '', '', '', '', 'Total Erreurs', total])

# ecrire_csv_complet(rapports_dossier, csv_fichier)





# import csv
# import os
# import xml.etree.ElementTree as ET

# # Chemin vers le dossier contenant les rapports XML
# rapports_dossier = 'rapports'
# # Fichier CSV de sortie
# csv_fichier = 'resultats_checkstyle.csv'

# # Entêtes pour le fichier CSV
# entetes = ['Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']

# # Dictionnaires pour les synthèses
# synthese_types = {}
# synthese_classes = {}

# def analyser_xml(chemin_complet):
#     tree = ET.parse(chemin_complet)
#     root = tree.getroot()
#     problemes_par_classe = 0

#     for error in root.findall('.//error'):
#         type_probleme = error.get('source').split('.')[-1]
#         # Compter les occurrences de chaque type d'erreur
#         synthese_types[type_probleme] = synthese_types.get(type_probleme, 0) + 1
#         problemes_par_classe += 1
    
#     return problemes_par_classe

# def ecrire_csv_et_syntheses(rapports_dossier, csv_fichier):
#     with open(csv_fichier, 'w', newline='', encoding='utf-8') as csvfile:
#         writer = csv.writer(csvfile)
#         writer.writerow(entetes)  # Écrire l'entête du fichier CSV

#         for filename in os.listdir(rapports_dossier):
#             if filename.endswith('.xml'):
#                 chemin_complet = os.path.join(rapports_dossier, filename)
#                 nom_classe = os.path.basename(filename).split('.')[0]
#                 problemes_par_classe = analyser_xml(chemin_complet)
#                 synthese_classes[nom_classe] = problemes_par_classe

#         # Écrire la synthèse par Type d'Erreur
#         writer.writerow(['', '', '', '', '', 'Synthèse par Type d\'Erreur', ''])
#         for type_erreur, total in synthese_types.items():
#             writer.writerow(['', '', '', '', '', type_erreur, total])
        
#         # Écrire la synthèse par Classe
#         writer.writerow(['', '', '', '', '', 'Synthèse par Classe', ''])
#         for classe, total in synthese_classes.items():
#             writer.writerow([classe, '', '', '', '', 'Total Erreurs', total])

# ecrire_csv_et_syntheses(rapports_dossier, csv_fichier)




# import csv
# import os
# import xml.etree.ElementTree as ET

# # Chemin vers le dossier contenant les rapports XML
# rapports_dossier = 'rapports'
# # Fichier CSV de sortie
# csv_fichier = 'resultats_checkstyle.csv'

# # Entêtes pour le fichier CSV
# entetes = ['Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']

# def analyser_et_ecrire_csv(rapports_dossier, csv_fichier):
#     with open(csv_fichier, 'w', newline='', encoding='utf-8') as csvfile:
#         writer = csv.writer(csvfile)
#         writer.writerow(entetes)  # Écrire l'entête du fichier CSV

#         # Itération sur tous les fichiers XML du dossier
#         for filename in os.listdir(rapports_dossier):
#             if filename.endswith('.xml'):
#                 chemin_complet = os.path.join(rapports_dossier, filename)
#                 tree = ET.parse(chemin_complet)
#                 root = tree.getroot()

#                 # Initialisation du compteur pour les types d'erreurs
#                 problemes_types = {}

#                 for error in root.findall('.//error'):
#                     line = error.get('line')
#                     column = error.get('column')
#                     severity = error.get('severity')
#                     message = error.get('message')
#                     source = error.get('source')
#                     type_probleme = source.split('.')[-1]
                    
#                     # Compter les types d'erreurs
#                     problemes_types[type_probleme] = problemes_types.get(type_probleme, 0) + 1
                    
#                     # Écrire les détails de l'erreur dans le CSV
#                     nom_classe = os.path.basename(filename).split('.')[0]
#                     writer.writerow([nom_classe, line, column, severity, message, source, type_probleme])

#                 # Après avoir listé toutes les erreurs pour une classe, écrire une ligne de synthèse
#                 total_problemes = sum(problemes_types.values())
#                 synthese = [f"{k}: {v}" for k, v in problemes_types.items()]
#                 writer.writerow([nom_classe, 'Summary', '', '', f'Total Issues: {total_problemes}', '', '; '.join(synthese)])

# analyser_et_ecrire_csv(rapports_dossier, csv_fichier)
