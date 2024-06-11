###Imports
import openai
import os
import time
import sys
import json
import requests
import IPython
from langchain.llms import OpenAI
from dotenv import load_dotenv
import codecs

####API key loading
load_dotenv()

# API configuration
openai.api_key = os.getenv("OPENAI_API_KEY_GPT-V4")


def save_to_file(filename, content):
    with open(filename, "w") as file:
        file.write(content)


def log_error(filename, error_message):
    with open(filename, "a") as file:
        file.write(error_message + "\n")


####Model params
def set_open_params(
        # model="text-davinci-003",
        # model="gpt-3.5-turbo-instruct",
        model="gpt-4",
        # model="gpt-3.5-turbo-16k",
        # temperature=0.7,
        temperature=0.7,
        # max_tokens=256,
        max_tokens=4096,  # 950#4000
        # top_p=1,
        # frequency_penalty=0,
        # presence_penalty=0,
):
    """ set openai parameters"""

    openai_params = {}

    openai_params['model'] = model
    openai_params['temperature'] = temperature
    openai_params['max_tokens'] = max_tokens
    # openai_params['top_p'] = top_p
    # openai_params['frequency_penalty'] = frequency_penalty
    # openai_params['presence_penalty'] = presence_penalty
    return openai_params


#########for gpt-3.5-turbo
def get_completion(params, prompt):
    """ GET completion from openai api"""

    completion = openai.ChatCompletion.create(
        model="gpt-4",
        messages=[
            {"role": "user", "content": f"{prompt}"}
        ]
    )
    response = completion['choices'][0]['message']['content']

    return response


####Basic prompt example:

# loading parameters
params = set_open_params()

# Number of files to be created for each class
num_files = 30  # Change this as per your requirement
# Number of retries on failure
retries = 50

# Load JSON data
# with open('OK.json', 'r') as file:
#    data = json.load(file)

with codecs.open('/home/christian/CHATGPT/CHATGPT_FOR_NEW_DATASET-V4/dataset.json', 'r', encoding="utf-8", errors="ignore") as file:
#with codecs.open('dataset.json', 'r', encoding="utf-8", errors="ignore") as file:
    data = json.load(file)

# Current directory where the 'classes' folder is located
current_directory = os.getcwd()

for item in data:
    project_name = item['project_name']
    bug_id = item['bug-id']
    class_name = item['class']
    source_code = item['source_code']

    # Create a directory path
    #dir_path = os.path.join(current_directory, 'Classes', project_name, bug_id)
    dir_path = os.path.join(current_directory, '/home/christian/CHATGPT/CHATGPT_FOR_NEW_DATASET-V4/Classes', project_name, bug_id)
    
    #Create directory
    if not os.path.exists(dir_path):
        os.makedirs(dir_path)
    else:
        pass

    # prompt = f"As a professional software tester who writes Java test methods, generate a complete Junit 4 test file ({class_name}Test.java) to comprehensively test all methods in the following class named {class_name}. Your output file must start with ###Test START## annd finish with ###Test END##. Here is the source code:\n{source_code}"
    # prompt = f"As a professional software tester who writes Java test methods, consider the following examples:\nNow, generate JUnit 4 test cases to comprehensively test all methods in the following class named {class_name}.Your output file must start with ###Test START## annd finish with ###Test END##. Here is the source code:\n{source_code}."
    # prompt = f"As a professional software tester who writes Java test methods, generate Junit 4 test cases to comprehensively test all methods in the following class named {class_name} by following these steps:\n- Extract and list all the public methods including their signatures\n- For each methods, generate a basic JUnit 4 test case that checks the method's functionality\n- Given the source code of the class and the listed methods, identify potential edge cases and exception handling scenarios that should be tested\n- Generate JUnit 4 test cases that specifically test for the identified edge cases and exceptions\n- Merge all the individual test cases into a complete JUnit 4 test file ({class_name}Test.java) for the given Java class. The complete JUnit test file must start with ###Test START## annd finish with ###Test END##\nHere is the {class_name} class:\n{source_code}"

    prompt = f"""Imagine three different experts in software testing who are tasked with developing comprehensive JUnit 4 test cases for the following Java class.To comprehensively test all methods in the following class named {class_name} they must following these steps:\n- Extract and list all the public methods including their signatures\n- For each methods, generate a basic JUnit 4 test case that checks the method's functionality\n- Given the source code of the class and the listed methods, identify potential edge cases and exception handling scenarios that should be tested\n- Generate JUnit 4 test cases that specifically test for the identified edge cases and exceptions\n- Merge all the individual test cases into a complete JUnit 4 test file ({class_name}Test.java) for the given Java class.All experts will propose one test cases for each method, share it with the group, and then proceed to the next step. If any expert realizes they're wrong at any point, they leave.\nThe Java class is:\n{source_code}\nAt the end they must propose one complete (including typical use cases, edge cases, and error scenarios) JUnit 4 test file ({class_name}Test.java) for the given Java class. The complete JUnit test file must start with ###Test START## and finish with ###Test END##"""

    # Check if the directory exists
    if os.path.exists(dir_path):
        for i in range(num_files):
            # Create a file path
            file_path = os.path.join(dir_path, f'{class_name}TEST_TOT2-{i}.java')

            # retries = 3  # Number of retries on failure
            for attempt in range(retries):
                try:
                    ##chatgpt calling
                    response = get_completion(params, prompt)

                    # Write source code to the file
                    with open(file_path, 'w') as file:
                        file.write(response)

                    print(f"File {file_path} created successfully.")
                    break  # Break the loop if the request was successful

                except (requests.exceptions.Timeout, openai.error.OpenAIError) as e:
                    if attempt < retries - 1:  # Retry on certain types of errors
                        print(f"An error occurred: {e}. Retrying in 5 seconds...")
                        time.sleep(5)
                        continue
                    else:
                        log_error(f"error_log_{class_name}TEST_{i}.txt", f"Error for prompt {class_name}TEST_{i}: {e}")
                        print(f"Failed to get response for prompt {class_name}TEST_{i} after {retries} retries.")
                        break

                except Exception as e:
                    log_error(f"error_log_{class_name}TEST_{i}.txt", f"Error for prompt {class_name}TEST_{i}: {e}")
                    print(f"An unexpected error occurred for prompt {i}. Check the error log for details.")
                    break


    else:
        print(f"Directory {dir_path} does not exist.")

print("Test generation finished!!!")
