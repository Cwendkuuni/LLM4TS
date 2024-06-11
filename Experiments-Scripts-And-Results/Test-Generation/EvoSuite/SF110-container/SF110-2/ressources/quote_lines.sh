#!/bin/bash

# Check if a file name is provided as an argument
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <filename>"
    exit 1
fi

# File to be processed
FILENAME="$1"

# Check if the file exists
if [ ! -f "$FILENAME" ]; then
    echo "Error: File does not exist."
    exit 1
fi

# Process each line of the file
while IFS= read -r line; do
    echo "\"$line\""
done < "$FILENAME"

