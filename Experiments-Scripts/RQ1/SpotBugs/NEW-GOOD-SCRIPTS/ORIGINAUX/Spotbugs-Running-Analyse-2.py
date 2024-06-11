# Dossier de sortie pour les rapports
#output_folder = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/SPOTBUGS/SPOTBUGS-ANALYZE-OUTPUTS"

import os
import subprocess

# Obtenez le chemin du répertoire courant
current_directory = os.getcwd()

# Dictionnaire des chemins par modèle-dataset
model_dataset_paths = {
    #"gpt3-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests",
    "gpt3-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/COMPILED-CLASSES/SF110-generated-tests",
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

# Assurez-vous que le dossier de sortie existe
os.makedirs(output_folder, exist_ok=True)

# Commande SpotBugs
spotbugs_command = f"java -jar {current_directory}/spotbugs-4.8.3/lib/spotbugs.jar -textui"

# Fonction pour parcourir les derniers sous-dossiers d'une arborescence
def get_final_subdirs(root_path):
    final_subdirs = []
    for root, dirs, _ in os.walk(root_path):
        if not dirs:
            final_subdirs.append(root)
    return final_subdirs

# Génération et exécution des commandes pour l'analyse SpotBugs
for key, path in model_dataset_paths.items():
    # Parcourez les derniers sous-dossiers pour l'analyse globale
    for subdir in get_final_subdirs(path):
        # Créez un dossier de sortie pour les résultats globaux
        general_output_file = f"{output_folder}/{key}-global-{subdir.replace('/', '_')}-rapport.xml"
        general_command = f"{spotbugs_command} -quiet -effort:max -xml:withMessages={general_output_file} {subdir}"
        print(f"Global analysis for {key} in {subdir}...")
        subprocess.run(general_command, shell=True)

    # Analyser par technique de prompt
    for prompt in prompts:
        prompt_path = os.path.join(path, prompt)
        if os.path.exists(prompt_path):  # Vérifiez que le chemin du prompt existe
            for subdir in get_final_subdirs(prompt_path):
                # Créez un dossier de sortie pour les résultats par technique
                prompt_output_file = f"{output_folder}/{key}-{prompt}-{subdir.replace('/', '_')}-rapport.xml"
                prompt_command = f"{spotbugs_command} -effort:max -xml:withMessages={prompt_output_file} {subdir}"
                print(f"Analysis for {key} - {prompt} in {subdir}...")
                subprocess.run(prompt_command, shell=True)

