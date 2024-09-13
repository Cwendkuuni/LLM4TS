import csv
from collections import defaultdict

# Chemin vers le fichier CSV d'entrée et de sortie
input_csv_file_path = 'BugDetection_results_tests.csv'
output_csv_file_path = 'BugDetection_results_summary-uniqueBug.csv'


# Structures de données pour stocker les résumés
summary = defaultdict(lambda: defaultdict(lambda: defaultdict(lambda: {'Detected': False, 'Need Manual checks': 0})))
totals_per_project = defaultdict(lambda: defaultdict(lambda: 0))

# Lire le fichier CSV en utilisant une virgule comme délimiteur
with open(input_csv_file_path, mode='r', encoding='utf-8') as csv_file:
    csv_reader = csv.DictReader(csv_file, delimiter=',')
    for row in csv_reader:
        strategy = row['Strategy']
        project = row['Project']
        bug_id = row['Bug-ID']
        bug_detected = row['Bug-Detected']
        
        # Logique pour déterminer si un bug a été détecté
        if bug_detected in ['True', 'Potential bug']:
            summary[strategy][project][bug_id]['Detected'] = True
        if bug_detected == 'Need Manual checks':
            summary[strategy][project][bug_id]['Need Manual checks'] += 1

# Calcul des totaux par projet
for strategy, projects in summary.items():
    for project, bug_ids in projects.items():
        for bug_id, status in bug_ids.items():
            if status['Detected']:
                totals_per_project[strategy][project] += 1

# Écrire le résumé dans un autre fichier CSV
with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as csv_file:
    fieldnames = ['Strategy', 'Project', 'Total Detected Bugs']
    csv_writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
    csv_writer.writeheader()

    for strategy, projects in totals_per_project.items():
        for project, total_detected in projects.items():
            row = {
                'Strategy': strategy,
                'Project': project,
                'Total Detected Bugs': total_detected
            }
            csv_writer.writerow(row)

print(f"Résumé par projet enregistré dans {output_csv_file_path}")



# import csv
# from collections import defaultdict

# ## Chemin vers le fichier CSV d'entrée et de sortie
# input_csv_file_path = 'BugDetection_results_tests.csv'
# output_csv_file_path = 'BugDetection_results_summary-unique.csv'

# # Initialisation des structures de données pour stocker les résumés
# summary = defaultdict(lambda: defaultdict(lambda: defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0})))
# unique_detection_per_bug_id = defaultdict(lambda: defaultdict(lambda: defaultdict(lambda: {'Detected': False, 'Potential': False, 'Need Manual checks': 0})))

# # Lire le fichier CSV en utilisant une virgule comme délimiteur
# with open(input_csv_file_path, mode='r', encoding='utf-8') as csv_file:
#     csv_reader = csv.DictReader(csv_file, delimiter=',')
#     for row in csv_reader:
#         strategy = row['Strategy']
#         project = row['Project']
#         bug_id = row['Bug-ID']
#         bug_detected = row['Bug-Detected']
        
#         # Mise à jour des compteurs détaillés
#         summary[strategy][project][bug_id][bug_detected] += 1
        
#         # Mise à jour des compteurs uniques pour les détections de bug
#         if bug_detected == 'True':
#             unique_detection_per_bug_id[strategy][project][bug_id]['Detected'] = True
#         elif bug_detected == 'Potential bug':
#             unique_detection_per_bug_id[strategy][project][bug_id]['Potential'] = True
#         elif bug_detected == 'Need Manual checks':
#             unique_detection_per_bug_id[strategy][project][bug_id]['Need Manual checks'] += 1

# # Écrire le résumé dans un autre fichier CSV
# with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as csv_file:
#     fieldnames = ['Strategy', 'Project', 'Bug-ID', 'Detected', 'Potential bug', 'Need Manual checks']
#     csv_writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
#     csv_writer.writeheader()

#     for strategy, projects in unique_detection_per_bug_id.items():
#         for project, bug_ids in projects.items():
#             for bug_id, status in bug_ids.items():
#                 row = {
#                     'Strategy': strategy,
#                     'Project': project,
#                     'Bug-ID': bug_id,
#                     'Detected': 1 if status['Detected'] else 0,
#                     'Potential bug': 1 if status['Potential'] else 0,
#                     'Need Manual checks': status['Need Manual checks']
#                 }
#                 csv_writer.writerow(row)

# print(f"Résumé enregistré dans {output_csv_file_path}")


# ########################################################################################################################################################################################################

# import csv
# from collections import defaultdict

# ## Chemin vers le fichier CSV d'entrée et de sortie
# input_csv_file_path = 'BugDetection_results_tests.csv'
# output_csv_file_path = 'BugDetection_results_summary-2.csv'


# # Initialisation des structures de données pour stocker les résumés
# summary = defaultdict(lambda: defaultdict(lambda: defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0})))
# totals_per_strategy = defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0})
# totals_per_strategy_project = defaultdict(lambda: defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0}))

# # Lire le fichier CSV en utilisant une virgule comme délimiteur
# with open(input_csv_file_path, mode='r', encoding='utf-8') as csv_file:
#     csv_reader = csv.DictReader(csv_file, delimiter=',')
#     for row in csv_reader:
#         strategy = row['Strategy']
#         project = row['Project']
#         bug_id = row['Bug-ID']
#         bug_detected = row['Bug-Detected']
        
#         # Mise à jour des compteurs
#         summary[strategy][project][bug_id][bug_detected] += 1
#         totals_per_strategy[strategy][bug_detected] += 1
#         totals_per_strategy_project[strategy][project][bug_detected] += 1

# # Écrire le résumé dans un autre fichier CSV
# with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as csv_file:
#     fieldnames = ['Strategy', 'Project', 'Bug-ID', 'True', 'Potential bug', 'False', 'Need Manual checks']
#     csv_writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
#     csv_writer.writeheader()

#     for strategy, projects in summary.items():
#         for project, bug_ids in projects.items():
#             for bug_id, results in bug_ids.items():
#                 row = {
#                     'Strategy': strategy,
#                     'Project': project,
#                     'Bug-ID': bug_id,
#                     'True': results['True'],
#                     'Potential bug': results['Potential bug'],
#                     'False': results['False'],
#                     'Need Manual checks': results['Need Manual checks']
#                 }
#                 csv_writer.writerow(row)
            
#             # Écrire les totaux par projet sous chaque projet
#             total_row_project = {
#                 'Strategy': strategy,
#                 'Project': project,
#                 'Bug-ID': 'Total',
#                 'True': totals_per_strategy_project[strategy][project]['True'],
#                 'Potential bug': totals_per_strategy_project[strategy][project]['Potential bug'],
#                 'False': totals_per_strategy_project[strategy][project]['False'],
#                 'Need Manual checks': totals_per_strategy_project[strategy][project]['Need Manual checks']
#             }
#             csv_writer.writerow(total_row_project)

#         # Écrire les totaux par stratégie à la fin de chaque stratégie
#         total_row_strategy = {
#             'Strategy': strategy,
#             'Project': 'Total',
#             'Bug-ID': '',
#             'True': totals_per_strategy[strategy]['True'],
#             'Potential bug': totals_per_strategy[strategy]['Potential bug'],
#             'False': totals_per_strategy[strategy]['False'],
#             'Need Manual checks': totals_per_strategy[strategy]['Need Manual checks']
#         }
#         csv_writer.writerow(total_row_strategy)

# print(f"Résumé enregistré dans {output_csv_file_path}")


# ########################################################################################################################################################################################################

# import csv
# from collections import defaultdict

# # Chemin vers le fichier CSV d'entrée et de sortie
# input_csv_file_path = 'BugDetection_results_tests.csv'
# output_csv_file_path = 'BugDetection_results_summary.csv'

# # Initialisation des structures de données pour stocker les résumés
# summary = defaultdict(lambda: defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0}))
# totals_per_strategy = defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0})

# # Lire le fichier CSV en utilisant une virgule comme délimiteur
# with open(input_csv_file_path, mode='r', encoding='utf-8') as csv_file:
#     csv_reader = csv.DictReader(csv_file, delimiter=',')
#     for row in csv_reader:
#         strategy = row['Strategy']
#         project = row['Project']
#         bug_detected = row['Bug-Detected']
        
#         # Mise à jour des compteurs pour la stratégie et le projet actuels
#         summary[strategy][project][bug_detected] += 1
#         totals_per_strategy[strategy][bug_detected] += 1

# # Écrire le résumé dans un autre fichier CSV
# with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as csv_file:
#     fieldnames = ['Strategy', 'Project', 'True', 'Potential bug', 'False', 'Need Manual checks']
#     csv_writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
#     csv_writer.writeheader()

#     for strategy, projects in summary.items():
#         for project, results in projects.items():
#             row = {
#                 'Strategy': strategy,
#                 'Project': project,
#                 'True': results['True'],
#                 'Potential bug': results['Potential bug'],
#                 'False': results['False'],
#                 'Need Manual checks': results['Need Manual checks']
#             }
#             csv_writer.writerow(row)
        
#         # Après avoir écrit tous les projets pour une stratégie, écrire les totaux pour la stratégie
#         total_row = {
#             'Strategy': strategy,
#             'Project': 'Total',
#             'True': totals_per_strategy[strategy]['True'],
#             'Potential bug': totals_per_strategy[strategy]['Potential bug'],
#             'False': totals_per_strategy[strategy]['False'],
#             'Need Manual checks': totals_per_strategy[strategy]['Need Manual checks']
#         }
#         csv_writer.writerow(total_row)

# print(f"Résumé enregistré dans {output_csv_file_path}")








# ########################################################################################################################################################################################################


# import csv
# from collections import defaultdict

# # Chemin vers le fichier CSV d'entrée et de sortie
# input_csv_file_path = 'BugDetection_results_tests.csv'
# output_csv_file_path = 'BugDetection_results_summary.csv'

# # Initialisation des structures de données pour stocker les résumés
# summary = defaultdict(lambda: defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0}))
# totals_per_strategy = defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0})

# # Lire le fichier CSV en utilisant une virgule comme délimiteur
# with open(input_csv_file_path, mode='r', encoding='utf-8') as csv_file:
#     csv_reader = csv.DictReader(csv_file, delimiter=',')
#     for row in csv_reader:
#         strategy = row['Strategy']
#         project = row['Project']
#         bug_detected = row['Bug-Detected']
        
#         # Mise à jour des compteurs pour la stratégie et le projet actuels
#         summary[strategy][project][bug_detected] += 1
#         totals_per_strategy[strategy][bug_detected] += 1

# # Écrire le résumé dans un autre fichier CSV
# with open(output_csv_file_path, mode='w', encoding='utf-8', newline='') as csv_file:
#     fieldnames = ['Strategy', 'Project', 'True', 'Potential bug', 'False', 'Need Manual checks', 'Total True', 'Total Potential bug', 'Total False', 'Total Need Manual checks']
#     csv_writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
#     csv_writer.writeheader()

#     for strategy, projects in summary.items():
#         for project, results in projects.items():
#             row = {
#                 'Strategy': strategy,
#                 'Project': project,
#                 'True': results['True'],
#                 'Potential bug': results['Potential bug'],
#                 'False': results['False'],
#                 'Need Manual checks': results['Need Manual checks'],
#                 'Total True': totals_per_strategy[strategy]['True'],
#                 'Total Potential bug': totals_per_strategy[strategy]['Potential bug'],
#                 'Total False': totals_per_strategy[strategy]['False'],
#                 'Total Need Manual checks': totals_per_strategy[strategy]['Need Manual checks']
#             }
#             csv_writer.writerow(row)

# print(f"Résumé enregistré dans {output_csv_file_path}")

# ########################################################################################################################################################################################################


# import csv
# from collections import defaultdict

# # Chemin vers le fichier CSV
# csv_file_path = 'BugDetection_results_tests.csv'


# # Initialisation des structures de données pour stocker les résumés
# summary = defaultdict(lambda: defaultdict(lambda: {'True': 0, 'Potential bug': 0, 'False': 0, 'Need Manual checks': 0}))

# # Lire le fichier CSV en utilisant une virgule comme délimiteur
# with open(csv_file_path, mode='r', encoding='utf-8') as csv_file:
#     csv_reader = csv.DictReader(csv_file, delimiter=',')  # Changement ici
#     for row in csv_reader:
#         strategy = row['Strategy']
#         project = row['Project']
#         bug_detected = row['Bug-Detected']
        
#         # Mise à jour des compteurs pour la stratégie et le projet actuels
#         summary[strategy][project][bug_detected] += 1

# # Affichage du résumé
# for strategy, projects in summary.items():
#     print(f"Strategy: {strategy}")
#     for project, results in projects.items():
#         print(f"  Project: {project}")
#         for result, count in results.items():
#             print(f"    {result}: {count}")



# # # Lire le fichier CSV et imprimer les en-têtes
# # with open(csv_file_path, mode='r', encoding='utf-8') as csv_file:
# #     csv_reader = csv.DictReader(csv_file, delimiter='\t')
# #     print(csv_reader.fieldnames)


