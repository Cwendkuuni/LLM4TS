import json
import os
import time
import sys
import requests

from dotenv import load_dotenv
import codecs


from mistralai.client import MistralClient
from mistralai.models.chat_completion import ChatMessage

####API key loading
load_dotenv()

# API configuration
api_key_mistral = os.getenv("MISTRAL_API_KEY")



# Constants
#API_KEY = "YOUR_MISTRAL_API_KEY"
MODEL = "open-mistral-7b"
TEMPERATURE = 0.7
MAX_TOKENS = 4096
NUM_FILES = 30  # Number of test files to generate per class
RETRIES = 50
WAIT_TIME = 10  # Wait time between retries in seconds
#DATASET_PATH = "/Users/wendkuuni.ouedraogo/Downloads/NEW-EXPERIMENTS-GPT4-NEW-DATASET/MISTRAL/SF110-dataset.json"
DATASET_PATH = "Datasets/new-small-dataset.json"
DATASETS_LIST = ["new-small-dataset.json", "Defects4J-dataset.json", "SF110-dataset.json"]


OUTPUT_DIRECTORY = "MISTRAL7B_FOR_NEW_DATASET/Classes"



# Initialize Mistral AI client
client = MistralClient(api_key=api_key_mistral)

def load_json_data(filepath):
    with codecs.open(filepath, 'r', encoding="utf-8", errors="ignore") as file:
    #with open(filepath, 'r') as file:
        return json.load(file)

def save_to_file(filepath, content):
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    with codecs.open(filepath, 'w', encoding="utf-8", errors="ignore") as file:
    #with open(filepath, 'w') as file:
        file.write(content)



fewshot_example = """
//Example Java Class:
package org.apache.commons.cli;

final class Util {

    static final String[] EMPTY_STRING_ARRAY = {};

    static String stripLeadingAndTrailingQuotes(String str) {
        final int length = str.length();
        if (length > 1 && str.startsWith("\"") && str.endsWith("\"") && str.substring(1, length - 1).indexOf('"') == -1) {
            str = str.substring(1, length - 1);
        }

        return str;
    }

    static String stripLeadingHyphens(final String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("--")) {
            return str.substring(2);
        }
        if (str.startsWith("-")) {
            return str.substring(1);
        }

        return str;
    }
}


//Corresponding JUnit 4 Test Cases:
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}
        """

def generate_java_test_file(project_name, bug_id, class_name, source_code, iteration):
    #prompt = f"Given the source code of a Java class named {class_name}, generate a JUnit test class."
    #prompt_zeroshot = f"As a professional software tester who writes Java test methods, generate a complete Junit 4 test file ({class_name}Test.java) to comprehensively test all methods in the following class named {class_name}. Your output file must start with ###Test START## and finish with ###Test END##. Here is the source code:\n{source_code}"
    #prompt_fewshot = f"As a professional software tester who writes Java test methods, consider the following examples:{fewshot_example}\nNow, generate JUnit 4 test cases to comprehensively test all methods in the following class named {class_name}. Your output file must start with ###Test START## and finish with ###Test END##. Here is the source code:\n{source_code}."    
    #prompt_cot = f"As a professional software tester who writes Java test methods, generate Junit 4 test cases to comprehensively test all methods in the following class named {class_name} by following these steps:\n- Extract and list all the public methods including their signatures\n- For each methods, generate a basic JUnit 4 test case that checks the method's functionality\n- Given the source code of the class and the listed methods, identify potential edge cases and exception handling scenarios that should be tested\n- Generate JUnit 4 test cases that specifically test for the identified edge cases and exceptions\n- Merge all the individual test cases into a complete JUnit 4 test file ({class_name}Test.java) for the given Java class. The complete JUnit test file must start with ###Test START## and finish with ###Test END##\nHere is the {class_name} class:\n{source_code}" 
    prompt_tot = f"Imagine three different experts in software testing who are tasked with developing comprehensive JUnit 4 test cases for the following Java class. All experts will propose one test cases for each method, share it with the group, and then proceed to the next step. If any expert realizes they're wrong at any point, they leave.\nThe Java class is:\n{source_code}\nAt the end they must propose one complete (including typical use cases, edge cases, and error scenarios) JUnit 4 test file ({class_name}Test.java) for the given Java class. The complete JUnit test file must start with ###Test START## and finish with ###Test END##"



    messages = [ChatMessage(role="user", content=prompt_tot)]

    # Generate file name and path
    #filename = f"{class_name}Test_{iteration}.java"
    filename = f"{class_name}TEST_TOT-{iteration}.java"
    
    #f'{class_name}TEST_ZEROSHOT{i}.java'
    #f'{class_name}TEST_FEWSHOT{i}.java'
    #f'{class_name}TEST_COT{i}.java'
    #f'{class_name}TEST_TOT{i}.java'
    #f'{class_name}TEST_TOT2-{i}.java'
    
    if bug_id:
        dir_path = os.path.join(OUTPUT_DIRECTORY, project_name, bug_id)
    else:
        dir_path = os.path.join(OUTPUT_DIRECTORY, project_name)
    file_path = os.path.join(dir_path, filename)

    for attempt in range(RETRIES):
        try:
            response = client.chat(model=MODEL, messages=messages, temperature=TEMPERATURE, max_tokens=MAX_TOKENS)
            save_to_file(file_path, response.choices[0].message.content)
            print(f"Generated file: {file_path}")
            break
        except Exception as e:
            if attempt < RETRIES - 1:
                print(f"Error: {e}. Retrying in {WAIT_TIME} seconds...")
                time.sleep(WAIT_TIME)
            else:
                print(f"Failed to generate test after {RETRIES} attempts. Error: {e}")

def main():
    data = load_json_data(DATASET_PATH)
    for item in data:
        project_name = item.get("project_name", "UnknownProject")
        bug_id = item.get("bug-id", None)
        class_name = item.get("class", "UnknownClass")
        source_code = item.get("source_code", "")

        for i in range(NUM_FILES):
            generate_java_test_file(project_name, bug_id, class_name, source_code, i)

if __name__ == "__main__":
    main()
