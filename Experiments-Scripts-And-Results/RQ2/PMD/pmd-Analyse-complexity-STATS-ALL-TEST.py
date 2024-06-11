import os
import xml.etree.ElementTree as ET
import csv

def process_xml_file(file_path):
    tree = ET.parse(file_path)
    root = tree.getroot()
    
    namespaces = {'ns': 'http://pmd.sourceforge.net/report/2.0.0'}
    
    # Utilisez cette ligne pour le débogage : comptez et affichez chaque violation trouvée
    violations = root.findall('.//ns:violation', namespaces=namespaces)
    print("Violations found:", len(violations))  # Debug: afficher le nombre de violations trouvées
    
    return len(violations)


def aggregate_violations(report_base_path):
    results = []
    
    # Parcourir les répertoires de façon récursive
    for root, dirs, files in os.walk(report_base_path):
        for file in files:
            if file.endswith('.xml'):
                file_path = os.path.join(root, file)
                print("Processing file:", file_path)  # Debug: afficher le chemin du fichier traité
                method_count = process_xml_file(file_path)
                
                parts = root.split(os.path.sep)
                dataset, complexity, prompt, severity = parts[-4], parts[-3], parts[-2], parts[-1]
                
                results.append({
                    'Dataset': dataset,
                    'Complexity': complexity,
                    'Prompt': prompt,
                    'Severity': severity,
                    'Violations': method_count
                })
    
    return results

def save_results_to_csv(results, csv_path):
    with open(csv_path, mode='w', newline='') as file:
        fieldnames = ['Dataset', 'Complexity', 'Prompt', 'Severity', 'Violations']
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        writer.writeheader()
        for result in results:
            writer.writerow(result)

# Chemin de base où les rapports XML sont stockés
report_base_path = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/REPORT-NEW'

# Chemin du fichier CSV de sortie
csv_output_path = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/output_statistics.csv'

# Agréger les violations et sauvegarder les résultats
results = aggregate_violations(report_base_path)
save_results_to_csv(results, csv_output_path)

print("L'agrégation et l'exportation des données sont terminées.")

import pandas as pd

# Charger le fichier CSV
csv_path = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/output_statistics.csv'
data = pd.read_csv(csv_path)

# Filtrer les données pour ne garder que les lignes avec des violations
filtered_data = data[data['Violations'] > 0]

# Afficher les premières lignes pour vérifier la structure des données
print(filtered_data.head())

# Grouper les données par les colonnes spécifiées et calculer les totaux
aggregated_data = filtered_data.groupby(['Dataset', 'Complexity', 'Prompt', 'Severity']).agg(
    Total_Classes_With_Violations=('Violations', 'size'),  # Compte le nombre de fichiers avec des violations
    Total_Violations=('Violations', 'sum')  # Somme des violations par groupe
).reset_index()

# Afficher les résultats agrégés pour vérification
print(aggregated_data.head())


# Chemin pour le nouveau fichier CSV agrégé
output_aggregated_csv_path = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/aggregated_output_statistics.csv'
aggregated_data.to_csv(output_aggregated_csv_path, index=False)

print("Les données agrégées ont été enregistrées dans:", output_aggregated_csv_path)





# import os
# import xml.etree.ElementTree as ET
# import csv

# def parse_xml(xml_file):
#     """ Parse the given XML file and extract violation details. """
#     tree = ET.parse(xml_file)
#     root = tree.getroot()
#     violations = {}

#     for file in root.findall('file'):
#         class_name = file.get('name').split('/')[-1]
#         if class_name not in violations:
#             violations[class_name] = {}

#         for violation in file.findall('violation'):
#             method = violation.get('method')
#             if method not in violations[class_name]:
#                 violations[class_name][method] = 1
#             else:
#                 violations[class_name][method] += 1

#     return violations

# def generate_statistics(report_base_path):
#     """ Generate statistics for all XML files in the report base path. """
#     stats = {}
#     for root, dirs, files in os.walk(report_base_path):
#         print(f"Processing directory: {root}")
#         for file in files:
#             if file.endswith('.xml'):
#                 file_path = os.path.join(root, file)
#                 print(f"Processing file: {file_path}")
#                 path_parts = root.split(os.sep)
#                 severity = path_parts[-1]
#                 prompt = path_parts[-2]
#                 rule_key = path_parts[-3]
#                 dataset = path_parts[-4]

#                 # Initialize dictionary structure
#                 if dataset not in stats:
#                     stats[dataset] = {}
#                 if rule_key not in stats[dataset]:
#                     stats[dataset][rule_key] = {}
#                 if prompt not in stats[dataset][rule_key]:
#                     stats[dataset][rule_key][prompt] = {}
#                 if severity not in stats[dataset][rule_key][prompt]:
#                     stats[dataset][rule_key][prompt][severity] = {'classes': 0, 'methods': 0}

#                 violations = parse_xml(file_path)
#                 stats[dataset][rule_key][prompt][severity]['classes'] += len(violations)
#                 methods_count = sum(len(methods) for methods in violations.values())
#                 stats[dataset][rule_key][prompt][severity]['methods'] += methods_count

#     # Write statistics to a CSV file
#     with open('pmd_statistics.csv', 'w', newline='') as csvfile:
#         fieldnames = ['dataset', 'rule_key', 'prompt', 'severity', 'classes', 'methods']
#         writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
#         writer.writeheader()

#         for dataset, rule_data in stats.items():
#             for rule_key, prompt_data in rule_data.items():
#                 for prompt, severity_data in prompt_data.items():
#                     for severity, count_data in severity_data.items():
#                         writer.writerow({
#                             'dataset': dataset,
#                             'rule_key': rule_key,
#                             'prompt': prompt,
#                             'severity': severity,
#                             'classes': count_data['classes'],
#                             'methods': count_data['methods']
#                         })

# # Usage
# if __name__ == "__main__":
#     report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/REPORT-NEW"
#     generate_statistics(report_base_path)
