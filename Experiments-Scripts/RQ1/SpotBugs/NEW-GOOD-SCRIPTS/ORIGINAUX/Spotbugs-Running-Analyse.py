# Dossier de sortie pour les rapports
#output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS"

import os
import subprocess

# Chemin du répertoire actuel
current_directory = os.getcwd()

# Dictionnaire des chemins par modèle-dataset
model_dataset_paths = {
    "gpt3-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests",
    "gpt3-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/COMPILED-CLASSES/SF110-generated-tests",
    "gpt3-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V3/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
    "gpt4-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V4/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
    "mistral-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/DEFECTS4J/AVEC-PACKAGE_GLOBAL/Defects4J-generated-tests",
    "mistral-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/SF110/MISTRAL7B_FOR_SF110_DATASET/AVEC-PACKAGE_GLOBAL/SF110-generated-tests",
    "mistral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/NEWDATASET/MISTRAL7B_FOR_NEW_DATASET/AVEC-PACKAGE_GLOBAL/NewDataset-generated-tests",
    "mixtral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MIXTRAL8x7B/MIXTRAL8x7B_FOR_NEW_DATASET/NewDataset-generated-tests"
}

# Liste des différentes techniques de prompt engineering
prompts = ["COT", "FEWSHOT", "TOT", "TOT2", "ZEROSHOT"]


# Dossier de sortie pour les rapports
output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS"

# Assurez-vous que le dossier de sortie existe
os.makedirs(output_folder, exist_ok=True)

# Fonction pour trouver tous les fichiers Java dans un répertoire
def find_java_files(directory):
    java_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".java"):
                java_files.append(os.path.join(root, file))
    return java_files

# Fonction pour exécuter SpotBugs sur un fichier Java
def run_spotbugs(java_file, output_file, spotbugs_command):
    # Inclure l'exportation et l'utilisation dans une seule ligne de commande
    full_command = f"export SPOTBUGS='{spotbugs_command}'; $SPOTBUGS -effort:max -xml:withMessages={output_file} {java_file}"
    subprocess.run(full_command, shell=True)

# Génération et exécution des commandes pour l'analyse SpotBugs
for key, path in model_dataset_paths.items():
    # Répertoire pour stocker les rapports globaux
    global_output_dir = f"{output_folder}/{key}-global-reports"
    os.makedirs(global_output_dir, exist_ok=True)

    # SpotBugs command avec la bonne syntaxe
    spotbugs_command = f"java -jar {current_directory}/spotbugs-4.8.4/lib/spotbugs.jar"

    # Analyse globale de tous les fichiers Java dans le répertoire racine
    java_files = find_java_files(path)
    for java_file in java_files:
        java_filename = os.path.basename(java_file).replace(".java", "")
        output_file = f"{global_output_dir}/{java_filename}-report.xml"
        run_spotbugs(java_file, output_file, spotbugs_command)

    # Analyse par technique de prompt
    for prompt in prompts:
        prompt_path = os.path.join(path, prompt)
        if os.path.exists(prompt_path):  # Vérifiez que le chemin du prompt existe
            prompt_output_dir = f"{output_folder}/{key}-{prompt}-reports"
            os.makedirs(prompt_output_dir, exist_ok=True)

            java_files_prompt = find_java_files(prompt_path)
            for java_file in java_files_prompt:
                java_filename = os.path.basename(java_file).replace(".java", "")
                output_file = f"{prompt_output_dir}/{java_filename}-report.xml"
                run_spotbugs(java_file, output_file, spotbugs_command)

print("Analyse SpotBugs terminée.")

