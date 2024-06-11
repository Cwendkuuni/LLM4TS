import os
import subprocess
import logging

# Configuration du logging
logging.basicConfig(level=logging.INFO, filename='pmd_analysis_2.log',
                    format='%(asctime)s - %(levelname)s - %(message)s',
                    filemode='a')  # 'w' pour écrire en écrasant les logs existants à chaque exécution

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

def run_pmd(java_file, ruleset_path, report_path):
    """Runs PMD on a single Java file."""
    command = [
        "pmd", "check", "-d", java_file, "-R", ruleset_path, "-f", "xml", "-r", report_path
    ]
    #subprocess.run(command, check=True)

    try:
        subprocess.run(command, check=True)
        logging.info(f"Analysis successfully completed for file {java_file}")
    except subprocess.CalledProcessError as e:
        if e.returncode == 4:
            logging.warning(f"Analysis completed with violations for file {java_file}. Report saved to {report_path}")
        else:
            logging.error(f"Error running PMD for file {java_file}: {e}")


def main():
    pmd_bin_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/pmd-bin-7.0.0-rc4/bin/"
    os.environ["PATH"] += os.pathsep + pmd_bin_path

    model_dataset_paths = {
        #"gpt3-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests",
         "gpt3-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/COMPILED-CLASSES/SF110-generated-tests",
        # "gpt3-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V3/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
        # "gpt4-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V4/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
        # "mistral-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/DEFECTS4J/AVEC-PACKAGE_GLOBAL/Defects4J-generated-tests",
        # "mistral-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/SF110/MISTRAL7B_FOR_SF110_DATASET/AVEC-PACKAGE_GLOBAL/SF110-generated-tests",
        # "mistral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/NEWDATASET/MISTRAL7B_FOR_NEW_DATASET/AVEC-PACKAGE_GLOBAL/NewDataset-generated-tests",
        # "mixtral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MIXTRAL8x7B/MIXTRAL8x7B_FOR_NEW_DATASET/NewDataset-generated-tests"
    }

    complexities = {
        "cognitive": [(1, 4, "low"), (5, 10, "moderate"), (11, 20, "high"), (21, 50, "very_high")],
        "cyclomatic": [(1, 4, "low"), (5, 7, "moderate"), (8, 10, "high"), (11, 50, "very_high")]
    }

    ruleset_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/RULESETS-NEW/"
    report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/REPORT-NEW"
    os.makedirs(report_base_path, exist_ok=True)

    for model_dataset, base_path in model_dataset_paths.items():
        for prompt in ["COT", "FEWSHOT", "TOT", "TOT2", "ZEROSHOT"]:
            java_directory = os.path.join(base_path, prompt)
            if not os.path.isdir(java_directory):
                continue

            for rule_key, thresholds in complexities.items():
                for _, max_thresh, category in thresholds:
                    ruleset_file = f"{rule_key}_{category}_ruleset.xml"
                    ruleset_path = os.path.join(ruleset_base_path, ruleset_file)
                    generate_ruleset_xml(ruleset_path, max_thresh, rule_key)

                    for root, _, files in os.walk(java_directory):
                        for file in files:
                            if file.endswith(".java"):
                                java_file_path = os.path.join(root, file)
                                relative_path = os.path.relpath(java_file_path, base_path)
                                sanitized_path = relative_path.replace(os.path.sep, "-")
                                report_filename = f"{model_dataset}_{prompt}_{category}_{sanitized_path}.xml"
                                report_dir = os.path.join(report_base_path, model_dataset, rule_key, prompt, category)
                                os.makedirs(report_dir, exist_ok=True)
                                report_path = os.path.join(report_dir, report_filename)
                                run_pmd(java_file_path, ruleset_path, report_path)

if __name__ == "__main__":
    main()






# import os
# import subprocess
# import xml.etree.ElementTree as ET

# def generate_ruleset_xml(ruleset_path, threshold, rule_key):
#     """Generates a ruleset XML string directly."""
#     if rule_key == "cognitive":
#         xml_content = f"""
# <ruleset xmlns="http://pmd.sourceforge.net/ruleset/2.0.0" 
#          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
#          name="Custom Ruleset" xsi:schemaLocation="http://pmd.sourceforge.net/ruleset_2_0_0.xsd">
#     <description>Custom ruleset for Cognitive Complexity</description>
#     <rule ref="category/java/design.xml/CognitiveComplexity">
#         <properties>
#             <property name="reportLevel" value="{threshold}"/>
#         </properties>
#     </rule>
# </ruleset>
# """
#     elif rule_key == "cyclomatic":
#         xml_content = f"""
# <ruleset xmlns="http://pmd.sourceforge.net/ruleset/2.0.0" 
#          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
#          name="Custom Ruleset" xsi:schemaLocation="http://pmd.sourceforge.net/ruleset_2_0_0.xsd">
#     <description>Custom ruleset for Cyclomatic Complexity</description>
#     <rule ref="category/java/design.xml/CyclomaticComplexity">
#         <properties>
#             <property name="methodReportLevel" value="{threshold}"/>
#         </properties>
#     </rule>
# </ruleset>
# """
#     # Save this XML string to the file
#     with open(ruleset_path, 'w') as f:
#         f.write(xml_content)

# def run_pmd(java_directory, ruleset_path, report_path, cache_path):
#     """Runs PMD with the provided directory and ruleset."""
#     command = [
#         "pmd", "check", "-d", java_directory, "-R", ruleset_path,
#         "-f", "xml", "-r", report_path, "--cache", cache_path
#     ]
#     result = subprocess.run(command, capture_output=True)
#     if result.returncode != 0:
#         print(f"Error running PMD: {result.stderr.decode()}")

# def main():
#     # Initial configuration
#     pmd_bin_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/pmd-bin-7.0.0-rc4/bin/"
#     os.environ["PATH"] += os.pathsep + pmd_bin_path

#     # Datasets and prompts configurations
#     model_dataset_paths = {
#         "gpt3-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/Defects4J-generated-tests",
#         "gpt3-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/SF-110/COMPILED-CLASSES/SF110-generated-tests",
#         "gpt3-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V3/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
#         "gpt4-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SORTIES-V4/COMPILED-CLASSES/AVEC-PACKAGE_GLOBAL",
#         "mistral-defects4j": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/DEFECTS4J/AVEC-PACKAGE_GLOBAL/Defects4J-generated-tests",
#         "mistral-sf110": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/SF110/MISTRAL7B_FOR_SF110_DATASET/AVEC-PACKAGE_GLOBAL/SF110-generated-tests",
#         "mistral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MISTRAL7B/NEWDATASET/MISTRAL7B_FOR_NEW_DATASET/AVEC-PACKAGE_GLOBAL/NewDataset-generated-tests",
#         "mixtral-newdataset": "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/MIXTRAL8x7B/MIXTRAL8x7B_FOR_NEW_DATASET/NewDataset-generated-tests"
#     }

#     prompts = ["COT", "FEWSHOT", "TOT", "TOT2", "ZEROSHOT"]

#     ruleset_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Rulesets/"
#     report_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/Reports"
#     cache_base_path = "/home/wendkuuni/EVOSUITE/LLMS-PROJECT/CHECKSTYLE-PMD-SPOTBUGS/PMD/GOOD-SCRIPTS/cache"

#     # Complexity thresholds and categories
#     complexities = {
#         "cognitive": [(1, 4, "low"), (5, 10, "moderate"), (11, 20, "high"), (21, 50, "very_high")],
#         "cyclomatic": [(1, 4, "low"), (5, 7, "moderate"), (8, 10, "high"), (11, 50, "very_high")]
#     }

#     for model_dataset, model_dataset_dir in model_dataset_paths.items():
#         for rule_key, thresholds in complexities.items():
#             ruleset_file = f"{rule_key}ComplexityRule.xml"
#             ruleset_path = os.path.join(ruleset_base_path, ruleset_file)

#             for min_thresh, max_thresh, category in thresholds:
#                 # Generate XML string directly
#                 generate_ruleset_xml(ruleset_path, max_thresh, rule_key)

#                 # Define base directory for reports
#                 report_type = "Cognitive-report" if rule_key == "cognitive" else "Cyclomatic-report"
                
#                 # Generate the report filename and path
#                 report_path = os.path.join(report_base_path, report_type, model_dataset, f"{model_dataset}_{category}.xml")
#                 os.makedirs(os.path.dirname(report_path), exist_ok=True)

#                 # Cache path
#                 cache_filename = f"{rule_key}_cache.bin"
#                 cache_path = os.path.join(cache_base_path, cache_filename)

#                 # Run PMD with the generated ruleset
#                 run_pmd(model_dataset_dir, ruleset_path, report_path, cache_path)

#                 # Handle reports with prompt techniques
#                 for prompt in prompts:
#                     java_directory = os.path.join(model_dataset_dir, prompt)
#                     if not os.path.isdir(java_directory):
#                         continue

#                     # Generate a separate report for each prompt
#                     report_path_prompt = os.path.join(report_base_path, report_type, prompt, f"{model_dataset}-{prompt}_{category}.xml")
#                     os.makedirs(os.path.dirname(report_path_prompt), exist_ok=True)

#                     run_pmd(java_directory, ruleset_path, report_path_prompt, cache_path)

# if __name__ == "__main__":
#     main()

