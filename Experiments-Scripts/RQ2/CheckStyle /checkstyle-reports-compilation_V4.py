
import csv
import os
import collections
import xml.etree.ElementTree as ET

# Chemin vers le dossier racine contenant les dossiers de stratégies
dossier_racine = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/CHECKSTYLE/reports_strategies_V4'
# Fichiers CSV de sortie
csv_details_fichier = 'reports_strategies_V4/details_erreurs_par_standard.csv'
csv_synthese_fichier = 'reports_strategies_V4/synthese_statistique_par_standard.csv'

# Entêtes pour les fichiers CSV
entetes_details = ['Strategie', 'Standard', 'Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']
entetes_synthese = ['Strategie', 'Standard', 'Type', 'Severity', 'Total']

statistiques = collections.defaultdict(lambda: collections.defaultdict(lambda: collections.defaultdict(lambda: collections.defaultdict(int))))
totals = collections.defaultdict(lambda: collections.defaultdict(int))

def analyser_xml(chemin_complet, details_writer, strategie, standard):
    """Fonction pour analyser un fichier XML et collecter les détails et statistiques des erreurs."""
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
        details_writer.writerow([strategie, standard] + list(details.values()))

        # Mise à jour des statistiques par type, sévérité et standard
        statistiques[strategie][standard][details['type']][details['severity']] += 1
        totals[strategie][standard] += 1

def ecrire_synthese(synthese_writer):
    """Fonction pour écrire la synthèse statistique dans le fichier CSV."""
    for strategie, standards in statistiques.items():
        for standard, types in standards.items():
            for type_erreur, severities in types.items():
                for severity, total in severities.items():
                    synthese_writer.writerow([strategie, standard, type_erreur, severity, total])
            # Écriture du total des erreurs pour la stratégie et le standard
            synthese_writer.writerow([strategie, standard, 'Total', 'Toutes', totals[strategie][standard]])

def main(dossier_racine, csv_details_fichier, csv_synthese_fichier):
    with open(csv_details_fichier, 'w', newline='', encoding='utf-8') as details_file, \
         open(csv_synthese_fichier, 'w', newline='', encoding='utf-8') as synthese_file:
        details_writer = csv.writer(details_file)
        synthese_writer = csv.writer(synthese_file)
        details_writer.writerow(entetes_details)
        synthese_writer.writerow(entetes_synthese)

        for strategie in os.listdir(dossier_racine):
            chemin_strategie = os.path.join(dossier_racine, strategie)
            if os.path.isdir(chemin_strategie):
                for root, _, files in os.walk(chemin_strategie):
                    for file in files:
                        if file.endswith('.xml'):
                            standard = 'google' if 'google' in file else 'sun'
                            analyser_xml(os.path.join(root, file), details_writer, strategie, standard)

        ecrire_synthese(synthese_writer)

main(dossier_racine, csv_details_fichier, csv_synthese_fichier)



# import csv
# import os
# import collections
# import xml.etree.ElementTree as ET

# # Chemin vers le dossier racine contenant les dossiers de stratégies
# dossier_racine = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/CHECKSTYLE/reports_strategies_V3-FOR_TEST'
# # Fichiers CSV de sortie
# csv_details_fichier = 'details_erreurs.csv'
# csv_synthese_fichier = 'synthese_statistique.csv'

# # Entêtes pour les fichiers CSV
# entetes_details = ['Strategie', 'Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']
# entetes_synthese = ['Strategie', 'Type', 'Severity', 'Total']

# # Initialisation d'un dictionnaire pour stocker les statistiques
# statistiques = collections.defaultdict(lambda: collections.defaultdict(lambda: collections.defaultdict(int)))
# totals = collections.defaultdict(int)

# def analyser_xml(chemin_complet, details_writer, strategie):
#     """Fonction pour analyser un fichier XML et collecter les détails et statistiques des erreurs."""
#     tree = ET.parse(chemin_complet)
#     root = tree.getroot()

#     for error in root.findall('.//error'):
#         details = {
#             'line': error.get('line'),
#             'column': error.get('column'),
#             'severity': error.get('severity'),
#             'message': error.get('message'),
#             'source': error.get('source'),
#             'type': error.get('source').split('.')[-1],
#         }
#         details_writer.writerow([strategie] + list(details.values()))

#         # Mise à jour des statistiques par type, sévérité et compteur global pour chaque stratégie
#         statistiques[strategie][details['type']][details['severity']] += 1
#         totals[strategie] += 1

# def ecrire_synthese(synthese_writer):
#     """Fonction pour écrire la synthèse statistique dans le fichier CSV."""
#     for strategie, types in statistiques.items():
#         for type_erreur, severities in types.items():
#             for severity, total in severities.items():
#                 synthese_writer.writerow([strategie, type_erreur, severity, total])
#         # Écriture du total des erreurs pour la stratégie
#         synthese_writer.writerow([strategie, 'Total', 'Toutes', totals[strategie]])

# def main(dossier_racine, csv_details_fichier, csv_synthese_fichier):
#     with open(csv_details_fichier, 'w', newline='', encoding='utf-8') as details_file, \
#          open(csv_synthese_fichier, 'w', newline='', encoding='utf-8') as synthese_file:
#         details_writer = csv.writer(details_file)
#         synthese_writer = csv.writer(synthese_file)
#         details_writer.writerow(entetes_details)
#         synthese_writer.writerow(entetes_synthese)

#         for strategie in os.listdir(dossier_racine):
#             chemin_strategie = os.path.join(dossier_racine, strategie)
#             if os.path.isdir(chemin_strategie):
#                 for root, _, files in os.walk(chemin_strategie):
#                     for file in files:
#                         if file.endswith('.xml'):
#                             analyser_xml(os.path.join(root, file), details_writer, strategie)

#         ecrire_synthese(synthese_writer)

# main(dossier_racine, csv_details_fichier, csv_synthese_fichier)



# import csv
# import os
# import xml.etree.ElementTree as ET

# # Chemin vers le dossier racine contenant les dossiers de stratégies
# dossier_racine = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/CHECKSTYLE/reports_strategies_V3-FOR_TEST'

# # Fichiers CSV de sortie
# csv_details_fichier = 'details_erreurs.csv'
# csv_synthese_fichier = 'synthese_statistique.csv'

# # Entêtes pour les fichiers CSV
# entetes_details = ['Strategie', 'Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']
# entetes_synthese = ['Strategie', 'Type', 'Severity', 'Total']

# # Initialisation d'un dictionnaire pour stocker les statistiques
# statistiques = {}

# def analyser_xml(chemin_complet, details_writer, strategie):
#     """Fonction pour analyser un fichier XML et collecter les détails et statistiques des erreurs."""
#     tree = ET.parse(chemin_complet)
#     root = tree.getroot()

#     for error in root.findall('.//error'):
#         details = {
#             'line': error.get('line'),
#             'column': error.get('column'),
#             'severity': error.get('severity'),
#             'message': error.get('message'),
#             'source': error.get('source'),
#             'type': error.get('source').split('.')[-1],
#         }
#         details_writer.writerow([strategie] + list(details.values()))

#         # Mise à jour des statistiques par type et sévérité
#         type_severity_key = (strategie, details['type'], details['severity'])
#         statistiques[type_severity_key] = statistiques.get(type_severity_key, 0) + 1

#         # Compteur global pour chaque stratégie
#         global_key = (strategie, 'Total', 'Toutes')
#         statistiques[global_key] = statistiques.get(global_key, 0) + 1

# def ecrire_synthese(synthese_writer):
#     """Fonction pour écrire la synthèse statistique dans le fichier CSV."""
#     for key, total in statistiques.items():
#         strategie, type_erreur, severity = key
#         synthese_writer.writerow([strategie, type_erreur, severity, total])

# def main(dossier_racine, csv_details_fichier, csv_synthese_fichier):
#     with open(csv_details_fichier, 'w', newline='', encoding='utf-8') as details_file, \
#          open(csv_synthese_fichier, 'w', newline='', encoding='utf-8') as synthese_file:
#         details_writer = csv.writer(details_file)
#         synthese_writer = csv.writer(synthese_file)
#         details_writer.writerow(entetes_details)
#         synthese_writer.writerow(entetes_synthese)

#         for strategie in os.listdir(dossier_racine):
#             chemin_strategie = os.path.join(dossier_racine, strategie)
#             if os.path.isdir(chemin_strategie):
#                 for root, _, files in os.walk(chemin_strategie):
#                     for file in files:
#                         if file.endswith('.xml'):
#                             analyser_xml(os.path.join(root, file), details_writer, strategie)

#         ecrire_synthese(synthese_writer)

# main(dossier_racine, csv_details_fichier, csv_synthese_fichier)




# import csv
# import os
# import xml.etree.ElementTree as ET

# # Chemin vers le dossier racine contenant les dossiers de stratégies
# dossier_racine = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/CHECKSTYLE/reports_strategies_V3-FOR_TEST'
# # Fichiers CSV de sortie
# csv_details_fichier = 'details_erreurs.csv'
# csv_synthese_fichier = 'synthese_statistique.csv'

# # Entêtes pour les fichiers CSV
# entetes_details = ['Strategie', 'Class_name', 'Line', 'Column', 'Severity', 'Message', 'Error Source', 'Type']
# entetes_synthese = ['Strategie', 'Type', 'Severity', 'Total']

# def analyser_xml(chemin_complet, details_writer, statistiques, strategie):
#     """Fonction pour analyser un fichier XML et collecter les détails et statistiques des erreurs."""
#     tree = ET.parse(chemin_complet)
#     root = tree.getroot()

#     for error in root.findall('.//error'):
#         details = {
#             'line': error.get('line'),
#             'column': error.get('column'),
#             'severity': error.get('severity'),
#             'message': error.get('message'),
#             'source': error.get('source'),
#             'type': error.get('source').split('.')[-1],
#         }
#         details_writer.writerow([strategie] + list(details.values()))

#         # Mise à jour des statistiques par type et sévérité
#         cle_stat = (strategie, details['type'], details['severity'])
#         statistiques[cle_stat] = statistiques.get(cle_stat, 0) + 1

#         # Mise à jour du total des erreurs pour la stratégie
#         cle_total = (strategie, 'Total erreurs', 'Toutes')
#         statistiques[cle_total] = statistiques.get(cle_total, 0) + 1

# def ecrire_synthese(statistiques, synthese_writer):
#     """Fonction pour écrire la synthèse statistique dans le fichier CSV."""
#     for (strategie, type_erreur, severity), total in sorted(statistiques.items()):
#         synthese_writer.writerow([strategie, type_erreur, severity, total])

# def main(dossier_racine, csv_details_fichier, csv_synthese_fichier):
#     statistiques = {}

#     with open(csv_details_fichier, 'w', newline='', encoding='utf-8') as details_file, \
#          open(csv_synthese_fichier, 'w', newline='', encoding='utf-8') as synthese_file:
#         details_writer = csv.writer(details_file)
#         synthese_writer = csv.writer(synthese_file)
#         details_writer.writerow(entetes_details)
#         synthese_writer.writerow(entetes_synthese)

#         for strategie in os.listdir(dossier_racine):
#             chemin_strategie = os.path.join(dossier_racine, strategie)
#             if os.path.isdir(chemin_strategie):
#                 for root, _, files in os.walk(chemin_strategie):
#                     for file in files:
#                         if file.endswith('.xml'):
#                             analyser_xml(os.path.join(root, file), details_writer, statistiques, strategie)

#         ecrire_synthese(statistiques, synthese_writer)

# main(dossier_racine, csv_details_fichier, csv_synthese_fichier)




