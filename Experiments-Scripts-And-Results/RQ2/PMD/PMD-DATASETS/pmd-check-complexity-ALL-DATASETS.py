import os
import json
import subprocess
import tempfile
import logging

# Configuration du logging
logging.basicConfig(level=logging.INFO, filename='pmd_analysis_from_json.log',
                    format='%(asctime)s - %(levelname)s - %(message)s',
                    filemode='a')

# Chemin vers le binaire de PMD
pmd_bin_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/pmd-bin-7.0.0-rc4/bin/"
os.environ["PATH"] += os.pathsep + pmd_bin_path

def generate_ruleset_xml(ruleset_path, threshold, rule_key):
    """Generates a ruleset XML string directly."""
    property_key = "reportLevel" if rule_key == "cognitive" else "methodReportLevel"
    rule_ref = "category/java/design.xml/CognitiveComplexity" if rule_key == "cognitive" else "category/java/design.xml/CyclomaticComplexity"
    
    xml_content = f"""
<ruleset xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset_2_0_0.xsd"
         name="Custom Ruleset for {rule_key} Complexity">
    <description>Custom ruleset for {rule_key} Complexity</description>
    <rule ref="{rule_ref}">
        <properties>
            <property name="{property_key}" value="{threshold}"/>
        </properties>
    </rule>
</ruleset>
"""
    with open(ruleset_path, 'w') as f:
        f.write(xml_content)

def run_pmd(java_source_code, ruleset_path, report_path):
    """Runs PMD on Java source code stored in a string."""
    with tempfile.NamedTemporaryFile(delete=False, suffix=".java", mode='w+') as temp_file:
        temp_file.write(java_source_code)
        temp_file.flush()
        command = [
            "pmd", "check", "-d", temp_file.name, "-R", ruleset_path, "-f", "xml", "-r", report_path
        ]
        try:
            subprocess.run(command, check=True)
            logging.info(f"Analysis successfully completed for temporary file {temp_file.name}")
        except subprocess.CalledProcessError as e:
            if e.returncode == 4:
                logging.warning(f"Analysis completed with violations. Report saved to {report_path}")
            else:
                logging.error(f"Error running PMD: {e}")

def process_json_file(json_file_path, dataset_name, ruleset_base_path, report_base_path):
    with open(json_file_path, 'r') as file:
        projects = json.load(file)

    dataset_report_path = os.path.join(report_base_path, dataset_name)  # Include dataset name in path
    os.makedirs(dataset_report_path, exist_ok=True)  # Create dataset-specific directory

    for project in projects:
        project_name = project.get("project_name")
        class_name = project.get("class")
        source_code = project.get("source_code")

        # Specify paths for ruleset and report
        for complexity_type, thresholds in [("cognitive", [(1, 4, "low"), (5, 10, "moderate"), (11, 20, "high"), (21, 50, "very_high")]), 
                                            ("cyclomatic", [(1, 4, "low"), (5, 7, "moderate"), (8, 10, "high"), (11, 50, "very_high")])]:
            for _, max_thresh, category in thresholds:
                ruleset_file = f"{complexity_type}_{category}_ruleset.xml"
                ruleset_path = os.path.join(ruleset_base_path, ruleset_file)
                generate_ruleset_xml(ruleset_path, max_thresh, complexity_type)

                report_filename = f"{project_name}_{class_name}_{category}.xml"
                report_dir = os.path.join(dataset_report_path, project_name, complexity_type)
                os.makedirs(report_dir, exist_ok=True)
                report_path = os.path.join(report_dir, report_filename)

                run_pmd(source_code, ruleset_path, report_path)

if __name__ == "__main__":
    datasets = {
        "sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/Datasets/sf110.json",
        "defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/Datasets/defects4j.json",
        "custom_dataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/Datasets/custom_dataset.json"
    }
    ruleset_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/RULESETS"
    report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/PMD-DATASETS/REPORTS"

    for dataset_name, json_file_path in datasets.items():
        process_json_file(json_file_path, dataset_name, ruleset_base_path, report_base_path)

