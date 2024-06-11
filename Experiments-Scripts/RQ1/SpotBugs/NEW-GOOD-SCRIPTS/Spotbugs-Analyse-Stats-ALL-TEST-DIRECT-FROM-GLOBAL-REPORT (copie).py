# Exemple d'utilisation
#directory = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS'
#output_dir = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS/STATS-OUTPUTS'

# Variables de configuration
#base_output_folder = '/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS'

# Base directory for outputs
base_output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/NEW-GOOD-SCRIPTS"  # Path to the output directory

model_datasets = ["gpt3-defects4j", "gpt3-sf110", "gpt3-newdataset", "gpt4-newdataset",
                  "mistral-defects4j", "mistral-sf110", "mistral-newdataset", "mixtral-newdataset"]
prompts = ["COT", "FEWSHOT", "TOT", "TOT2", "ZEROSHOT"]

import os
import xml.etree.ElementTree as ET
import csv
import json
from collections import defaultdict

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

def load_bug_categories(file_path):
    """Charger le fichier JSON contenant les catégories de bug."""
    with open(file_path, 'r') as json_file:
        return json.load(json_file)

def generate_stats(xml_file_path, model_dataset, base_output_folder, bug_categories):
    """Générer des statistiques à partir du fichier XML global."""
    # Charger et parser le fichier XML
    tree = ET.parse(xml_file_path)
    root = tree.getroot()

    # Dictionnaires pour stocker les informations
    rank_stats = {}
    bug_patterns_stats = {}
    category_stats = {}

    # Inverser le dictionnaire des catégories pour obtenir un mappage de type à catégorie
    type_to_category = {type_: category for category, types in bug_categories.items() for type_ in types}

    # Parcourir chaque prompt dans le fichier XML
    for prompt_element in root.findall('.//Prompt'):
        prompt_name = prompt_element.get("name")
        num_classes = int(prompt_element.find("Classes").text)

        # Pour suivre les classes affectées par rank et autres catégories
        rank_to_class_count = defaultdict(int)
        pattern_to_class_count = defaultdict(int)
        category_to_class_count = defaultdict(int)

        # Parcourir chaque bug pour ce prompt
        for bug_instance in prompt_element.findall('.//BugInstance'):
            bug_type = bug_instance.get("type")
            rank = int(bug_instance.get("rank"))
            category = type_to_category.get(bug_type, "Unknown")
            rank_category = rank_to_category(rank)

            rank_to_class_count[rank_category] += 1
            pattern_to_class_count[bug_type] += 1
            category_to_class_count[category] += 1

        # Sauvegarder les statistiques agrégées
        rank_stats[prompt_name] = {k: {'count': v, 'classes': num_classes} for k, v in rank_to_class_count.items()}
        bug_patterns_stats[prompt_name] = {k: {'count': v, 'classes': num_classes} for k, v in pattern_to_class_count.items()}
        category_stats[prompt_name] = {k: {'count': v, 'classes': num_classes} for k, v in category_to_class_count.items()}

    # Sauvegarder les statistiques dans des fichiers CSV
    save_csv(rank_stats, base_output_folder, 'Ranks', model_dataset)
    save_csv(bug_patterns_stats, base_output_folder, 'Bug_Patterns', model_dataset)
    save_csv(category_stats, base_output_folder, 'Categories', model_dataset)

def save_csv(data, base_output_folder, stats_type, model_dataset):
    """Sauvegarder les statistiques dans des fichiers CSV."""
    for prompt, stats in data.items():
        stats_folder = os.path.join(base_output_folder, stats_type, prompt)
        os.makedirs(stats_folder, exist_ok=True)
        filename = os.path.join(stats_folder, f"{model_dataset}_{prompt}_{stats_type.lower()}_stats.csv")

        with open(filename, 'w', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(['Type', 'Count', 'Classes Affected'])
            for key, values in stats.items():
                writer.writerow([key, values['count'], values['classes']])

# Base directory for outputs
base_output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/NEW-GOOD-SCRIPTS"

# Liste des modèles-datasets
model_datasets = ["mistral-newdataset", "mixtral-newdataset"]

# Charger les catégories de bugs
bug_categories_file = "bug_categories_refined.json"
bug_categories = load_bug_categories(bug_categories_file)

# Process reports
for model_dataset in model_datasets:
    global_xml_path = f"{base_output_folder}/GLOBAL/{model_dataset}-global-summary.xml"
    if os.path.exists(global_xml_path):
        print(f"Traitement du fichier XML: {global_xml_path}")
        generate_stats(global_xml_path, model_dataset, base_output_folder, bug_categories)

