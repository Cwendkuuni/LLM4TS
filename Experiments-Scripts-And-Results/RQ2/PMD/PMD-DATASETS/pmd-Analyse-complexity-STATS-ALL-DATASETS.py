import os
import xml.etree.ElementTree as ET
import csv
import pandas as pd

def process_xml_file(file_path):
    tree = ET.parse(file_path)
    root = tree.getroot()
    namespaces = {'ns': 'http://pmd.sourceforge.net/report/2.0.0'}
    violations = root.findall('.//ns:violation', namespaces=namespaces)
    return len(violations)

def aggregate_violations(report_base_path):
    results = []
    for root, dirs, files in os.walk(report_base_path):
        for file in files:
            if file.endswith('.xml'):
                file_path = os.path.join(root, file)
                method_count = process_xml_file(file_path)
                parts = root.split(os.path.sep)

                dataset = parts[-3]
                complexity = parts[-1]
                
                # Exemple de récupération de la sévérité à partir du nom de fichier
                # Adaptation nécessaire selon le format réel du nom de fichier
                severity = file.split('_')[-1].replace('.xml', '')

                results.append({
                    'Dataset': dataset,
                    'Complexity': complexity,
                    'Severity': severity,
                    'Violations': method_count
                })
    return results



def save_results_to_csv(results, csv_path):
    with open(csv_path, mode='w', newline='') as file:
        fieldnames = ['Dataset', 'Complexity', 'Severity', 'Violations']
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        writer.writeheader()
        for result in results:
            writer.writerow(result)

report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/REPORTS"
csv_output_path = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/output_statistics.csv'

results = aggregate_violations(report_base_path)
save_results_to_csv(results, csv_output_path)
print("L'agrégation et l'exportation des données sont terminées.")

data = pd.read_csv(csv_output_path)
filtered_data = data[data['Violations'] > 0]
aggregated_data = filtered_data.groupby(['Dataset', 'Complexity', 'Severity']).agg(
    Total_Classes_With_Violations=('Violations', 'size'),
    Total_Violations=('Violations', 'sum')
).reset_index()

output_aggregated_csv_path = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/aggregated_output_statistics.csv'
aggregated_data.to_csv(output_aggregated_csv_path, index=False)
print("Les données agrégées ont été enregistrées dans:", output_aggregated_csv_path)

