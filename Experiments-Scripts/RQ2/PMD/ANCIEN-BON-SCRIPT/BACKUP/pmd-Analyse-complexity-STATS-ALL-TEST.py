import os
import csv
import xml.etree.ElementTree as ET

def count_classes_and_methods(xml_file):
    """Counts the number of classes and methods in a given XML file."""
    tree = ET.parse(xml_file)
    root = tree.getroot()

    class_set = set()
    method_set = set()

    for file_element in root.findall('.//file', namespaces={"": "http://pmd.sourceforge.net/report/2.0.0"}):
        for violation in file_element.findall('.//violation', namespaces={"": "http://pmd.sourceforge.net/report/2.0.0"}):
            class_name = violation.get("class")
            method_name = violation.get("method")

            if class_name:
                class_set.add(class_name)
            if method_name:
                method_set.add(method_name)

    return len(class_set), len(method_set)

def main():
    # Chemins de base pour les rapports
    report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports"

    # Les répertoires de types de rapport à traiter
    report_types = ["Cognitive-report", "Cyclomatic-report"]

    for report_type in report_types:
        report_dir = os.path.join(report_base_path, report_type)
        
        for root, dirs, files in os.walk(report_dir):
            for file in files:
                if file.endswith(".xml"):
                    xml_file = os.path.join(root, file)
                    classes_count, methods_count = count_classes_and_methods(xml_file)

                    # Créez le fichier CSV correspondant
                    csv_file = xml_file.replace(".xml", ".csv")

                    with open(csv_file, "w", newline="") as csvfile:
                        writer = csv.writer(csvfile)
                        writer.writerow(["Classes", "Methods"])
                        writer.writerow([classes_count, methods_count])

if __name__ == "__main__":
    main()

