# Dossier de sortie pour les rapports
#output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS"

import os
import subprocess
import xml.etree.ElementTree as ET
from xml.dom import minidom

# Obtenez le chemin du répertoire courant
current_directory = os.getcwd()

# Dictionnaire des chemins par modèle-dataset
model_dataset_paths = {
    "gpt3-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests",
    #"gpt3-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/COMPILED-CLASSES/SF110-generated-tests",
    #"gpt3-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V3/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
    #"gpt4-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V4/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
    #"mistral-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/DEFECTS4J/AVEC-PACKAGE_GLOBAL/Defects4J-generated-tests",
    #"mistral-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/SF110/MISTRAL7B_FOR_SF110_DATASET/AVEC-PACKAGE_GLOBAL/SF110-generated-tests",
    #"mistral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/NEWDATASET/MISTRAL7B_FOR_NEW_DATASET/AVEC-PACKAGE_GLOBAL/NewDataset-generated-tests",
    #"mixtral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MIXTRAL8x7B/MIXTRAL8x7B_FOR_NEW_DATASET/NewDataset-generated-tests",
}

# Liste des différentes techniques de prompt engineering
prompts = ["COT", "FEWSHOT", "TOT", "TOT2", "ZEROSHOT"]

# Dossier de sortie pour les rapports
#output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS"
output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/NEW-GOOD-SCRIPTS"

##################################
import os
import subprocess
import xml.etree.ElementTree as ET
from xml.dom import minidom
from collections import defaultdict

# Dictionnaire des chemins par modèle-dataset
model_dataset_paths = {
    "gpt3-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests",
    #"gpt3-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/COMPILED-CLASSES/SF110-generated-tests",
    #"gpt3-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V3/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
    #"gpt4-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V4/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
    #"mistral-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/DEFECTS4J/AVEC-PACKAGE_GLOBAL/Defects4J-generated-tests",
    #"mistral-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/SF110/MISTRAL7B_FOR_SF110_DATASET/AVEC-PACKAGE_GLOBAL/SF110-generated-tests",
    #"mistral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/NEWDATASET/MISTRAL7B_FOR_NEW_DATASET/AVEC-PACKAGE_GLOBAL/NewDataset-generated-tests",
    #"mixtral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MIXTRAL8x7B/MIXTRAL8x7B_FOR_NEW_DATASET/NewDataset-generated-tests",
}

# Liste des techniques de prompt engineering
prompts = ["COT", "FEWSHOT", "TOT", "TOT2", "ZEROSHOT"]

# Dossier de sortie pour les rapports
output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/NEW-GOOD-SCRIPTS"

# Assurez-vous que les dossiers de sortie existent
os.makedirs(f"{output_folder}/GLOBAL", exist_ok=True)
os.makedirs(f"{output_folder}/PROMPT", exist_ok=True)

# Commande SpotBugs
spotbugs_command = f"java -jar {os.getcwd()}/spotbugs-4.8.3/lib/spotbugs.jar -textui"

# Fonction pour obtenir les derniers sous-dossiers d'une arborescence
def get_final_subdirs(root_path):
    final_subdirs = []
    for root, dirs, _ in os.walk(root_path):
        if not dirs:
            final_subdirs.append(root)
    return final_subdirs

# Fonction pour extraire les informations d'un fichier XML
def extract_info(file_path):
    # Vérifiez si le fichier est vide ou de petite taille
    if os.path.getsize(file_path) < 10:
        return 0, 0, []

    try:
        tree = ET.parse(file_path)
        root = tree.getroot()

        num_bugs = int(root.get("total_bugs", 0))
        num_classes = int(root.get("total_classes", 1))

        bugs_info = []

        for bug_instance in root.findall("BugInstance"):
            bug_type = bug_instance.get("type")
            priority = int(bug_instance.get("priority", 0))
            rank = int(bug_instance.get("rank", 0))
            category = bug_instance.get("category")
            bugs_info.append({"type": bug_type, "priority": priority, "rank": rank, "category": category})

        # Mettre à jour le nombre de bugs à partir des instances de bugs
        num_bugs = len(bugs_info)

        return num_bugs, num_classes, bugs_info

    except ET.ParseError:
        print(f"Error parsing {file_path}. Ignoring this file.")
        return 0, 0, []

# Fonction pour synthétiser les résultats
def synthesize_results(output_folder, prompts, model_dataset_paths):
    summary = {}

    # Parcourir les fichiers XML dans le dossier PROMPT
    for prompt in prompts:
        prompt_dir = f"{output_folder}/PROMPT"
        for root, _, files in os.walk(prompt_dir):
            for file in files:
                if file.endswith(".xml") and f"-{prompt}-" in file:
                    # Charger et analyser le fichier XML
                    file_path = os.path.join(root, file)
                    num_bugs, num_classes, bugs_info = extract_info(file_path)

                    # Extraire la clé complète pour le dataset
                    dataset_key = "-".join(file.split("-")[:2])

                    # Ajouter à la synthèse
                    if dataset_key not in summary:
                        summary[dataset_key] = {"prompts": {}, "total": {"bugs": 0, "classes": 0}}

                    if prompt not in summary[dataset_key]["prompts"]:
                        summary[dataset_key]["prompts"][prompt] = {"bugs": 0, "classes": 0, "bug_instances": []}

                    summary[dataset_key]["prompts"][prompt]["bugs"] += num_bugs
                    summary[dataset_key]["prompts"][prompt]["classes"] += num_classes
                    summary[dataset_key]["prompts"][prompt]["bug_instances"].extend(bugs_info)

                    summary[dataset_key]["total"]["bugs"] += num_bugs
                    summary[dataset_key]["total"]["classes"] += num_classes

    # Création d'un fichier XML pour chaque dataset avec la synthèse
    for dataset_key, data in summary.items():
        dataset_xml = ET.Element("Dataset", name=dataset_key)

        total_element = ET.SubElement(dataset_xml, "Total")
        ET.SubElement(total_element, "Bugs").text = str(data["total"]["bugs"])
        ET.SubElement(total_element, "Classes").text = str(data["total"]["classes"])

        prompts_element = ET.SubElement(dataset_xml, "Prompts")

        for prompt, prompt_data in data["prompts"].items():
            prompt_element = ET.SubElement(prompts_element, "Prompt", name=prompt)
            ET.SubElement(prompt_element, "Bugs").text = str(prompt_data["bugs"])
            ET.SubElement(prompt_element, "Classes").text = str(prompt_data["classes"])

            bugs_element = ET.SubElement(prompt_element, "BugsInstances")
            bug_ranks = defaultdict(int)

            for bug_info in prompt_data["bug_instances"]:
                rank = bug_info["rank"]
                bug_ranks[rank] += 1

                ET.SubElement(bugs_element, "BugInstance", type=bug_info["type"], priority=str(bug_info["priority"]), rank=str(bug_info["rank"]), category=bug_info["category"])

            # Ajouter les rangs agrégés
            for rank, count in bug_ranks.items():
                ET.SubElement(prompt_element, "BugRank", rank=str(rank), count=str(count))

        xml_string = minidom.parseString(ET.tostring(dataset_xml)).toprettyxml(indent="  ")

        # Utilisez la clé complète pour nommer le fichier global
        output_file = f"{output_folder}/GLOBAL/{dataset_key}-global-summary.xml"
        with open(output_file, "w") as f:
            f.write(xml_string)

    # Affichage de la synthèse dans la console
    print("Synthèse des résultats :")
    for dataset_key, data in summary.items():
        print(f"\nDataset: {dataset_key}")
        print(f"  Total Bugs: {data['total']['bugs']}")
        print(f"  Total Classes: {data['total']['classes']}")
        for prompt, prompt_data in data["prompts"].items():
            print(f"  Prompt: {prompt}")
            print(f"    Bugs: {prompt_data['bugs']}")
            print(f"    Classes: {prompt_data['classes']}")
            print(f"    Bugs Instances:")
            for bug_info in prompt_data["bug_instances"]:
                print(f"      - Type: {bug_info['type']}, Rank: {bug_info['rank']}, Priority: {bug_info['priority']}, Category: {bug_info['category']}")
            print(f"    Bug Ranks:")
            for rank, count in bug_ranks.items():
                print(f"      Rank {rank}: {count} bugs")

# Génération et exécution des commandes pour l'analyse SpotBugs
for key, path in model_dataset_paths.items():
    # Analyser par technique de prompt
    for prompt in prompts:
        prompt_path = os.path.join(path, prompt)
        if os.path.exists(prompt_path):
            for subdir in get_final_subdirs(prompt_path):
                simplified_subdir = "_".join(subdir.split("/")[-4:])
                prompt_output_file = f"{output_folder}/PROMPT/{key}-{prompt}-{simplified_subdir}-rapport.xml"
                prompt_command = f"export SPOTBUGS='{spotbugs_command}'; $SPOTBUGS -quiet -effort:max -xml:withMessages={prompt_output_file} {subdir}"
                subprocess.run(prompt_command, shell=True)

# Synthétiser les résultats
synthesize_results(output_folder, prompts, model_dataset_paths)

