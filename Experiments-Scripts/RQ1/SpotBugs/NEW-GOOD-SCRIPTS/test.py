import os
import xml.etree.ElementTree as ET
import csv
import json

def rank_to_category(rank):
    """Convertir un rang en catégorie."""
    if 1 <= rank <= 4:
        return 'Scariest'
    elif 5 <= rank <= 9:
        return 'Scary'
    elif 10 <= rank <= 14:
        return 'Troubling'
    elif 15 <= rank <= 20:
        return 'Of Concern'
    else:
        return 'Unknown'

def generate_stats(xml_file_path, model_dataset, base_output_folder):
    """Générer des statistiques à partir du fichier XML global."""

    # Charger le fichier JSON contenant les catégories de bug
    with open('bug_categories_refined.json', 'r') as json_file:
        bug_categories = json.load(json_file)

    # Inverser le dictionnaire pour obtenir un mappage de type à catégorie
    type_to_category = {type_: category for category, types in bug_categories.items() for type_ in types}

    # Charger et parser le fichier XML
    tree = ET.parse(xml_file_path)
    root = tree.getroot()

    # Dictionnaires pour stocker les informations
    bug_patterns_stats = {}
    category_stats = {}
    rank_stats = {}

    # Parcourir chaque prompt dans le fichier XML
    for prompt_element in root.findall('.//Prompt'):
        prompt_name = prompt_element.get("name")
        num_classes = int(prompt_element.find("Classes").text)

        # Parcourir chaque bug pour ce prompt
        for bug_instance in prompt_element.findall('.//BugInstance'):
            bug_type = bug_instance.get("type")
            rank = int(bug_instance.get("rank"))
            category = type_to_category.get(bug_type, 'Unknown')
            rank_category = rank_to_category(rank)

            # Ajouter aux statistiques
            bug_patterns_stats.setdefault(bug_type, {'count': 0, 'classes': 0})
            bug_patterns_stats[bug_type]['count'] += 1

            category_stats.setdefault(category, {'count': 0, 'classes': 0})
            category_stats[category]['count'] += 1

            rank_stats.setdefault(rank_category, {'count': 0, 'classes': 0})
            rank_stats[rank_category]['count'] += 1

    # Parcourir chaque balise BugRank pour récupérer les classes affectées
    for prompt_element in root.findall('.//Prompt'):
        for bug_rank_element in prompt_element.findall('.//BugRank'):
            rank_category = rank_to_category(int(bug_rank_element.get("rank")))
            classes_affected = int(bug_rank_element.get("count"))

            # Ajouter aux statistiques
            #rank_stats.setdefault(rank_category, {'count': 0, 'classes': 0})
            #rank_stats[rank_category]['count'] += 1
            rank_stats[rank_category]['classes'] += classes_affected

    # Sauvegarder les statistiques
    save_csv(bug_patterns_stats, base_output_folder, 'Bug_Patterns', model_dataset, 'bug_patterns_stats.csv')
    save_csv(category_stats, base_output_folder, 'Categories', model_dataset, 'category_stats.csv')
    save_csv(rank_stats, base_output_folder, 'Ranks', model_dataset, 'rank_stats.csv')

def save_csv(data, base_output_folder, stats_type, model_dataset, filename):
    """Sauvegarder les statistiques dans un fichier CSV."""
    stats_folder = os.path.join(base_output_folder, stats_type, model_dataset)
    os.makedirs(stats_folder, exist_ok=True)
    filepath = os.path.join(stats_folder, f"{model_dataset}_{filename}")

    with open(filepath, 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Type', 'Count', 'Classes Affected'])
        for item, stats in data.items():
            writer.writerow([item, stats['count'], stats['classes']])

# Base directory for outputs
base_output_folder ="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/NEW-GOOD-SCRIPTS"
model_datasets = ["gpt3-defects4j", "gpt3-sf110", "gpt3-newdataset", "gpt4-newdataset",
                  "mistral-defects4j", "mistral-sf110", "mistral-newdataset", "mixtral-newdataset"]

# Process reports
for model_dataset in model_datasets:
    global_xml_path = f"{base_output_folder}/GLOBAL/{model_dataset}-global-summary.xml"
    if os.path.exists(global_xml_path):
        print(f"Traitement du fichier XML: {global_xml_path}")
        generate_stats(global_xml_path, model_dataset, base_output_folder)
