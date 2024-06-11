

ruleset_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Rulesets/"
report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports"

# Paths to XML files categorized by cognitive complexity level
files = {
    "Low Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_low.xml",
    "Moderate Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_moderate.xml",
    "High Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_high.xml",
    "Very High Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_very_high.xml",
}

import xml.etree.ElementTree as ET
import pandas as pd
import os

# Définir les chemins des fichiers XML à analyser
xml_files = {
    "Low Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_low.xml",
    "Moderate Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_moderate.xml",
    "High Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_high.xml",
    "Very High Complexity": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports/Cognitive-report/gpt3-defects4j/gpt3-defects4j_very_high.xml",
}

# Fonction pour extraire les données d'un fichier XML
def extract_data(file_path):
    classes_methods = []
    if os.path.exists(file_path):
        tree = ET.parse(file_path)
        root = tree.getroot()

        for file in root.findall(".//file"):
            class_name = None
            methods = set()
            for violation in file.findall(".//violation"):
                class_name = violation.attrib.get("class")
                method_name = violation.attrib.get("method")
                if class_name and method_name:
                    methods.add(method_name)
            if class_name:
                classes_methods.append((class_name, len(methods)))

    return classes_methods

# Extraire les données pour chaque catégorie
data = {complexity: extract_data(path) for complexity, path in xml_files.items()}

# Créer un DataFrame avec les statistiques finales
df_stats = pd.DataFrame(
    [
        (
            complexity,
            len(data[complexity]),
            sum(methods for _, methods in data[complexity]),
        )
        for complexity in xml_files.keys()
    ],
    columns=["Cognitive Complexity Level", "Num. of Classes", "Num. of Methods"],
)

# Affichage du résultat final
print(df_stats)

