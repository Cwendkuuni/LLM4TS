#!/bin/bash

#export PATH=$PATH:/home/wendkuuni/defects4j/framework/bin
#ulimit -c unlimited

#export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

source project-class-pair_3.sh

array_mockito=(14 26 28 37 34 38 36 31 15 13 30 32 12 25 23 22 17 29 16 27 24 33)
array_time=(14 15 26)

for elem in "${PROJECT_CLASS_PAIR[@]}"; do

	read -a strarr <<< "$elem"  # uses default whitespace IFS
	
	if [[ ${strarr[0]} == "Closure" ]]; then
	
		cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/build/classes/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
	
	elif [[ ${strarr[0]} == "Mockito" ]]; then
		
		# Vérification si la valeur_exterieure n'est pas dans le tableau
		if ! [[ " ${array_mockito[*]} " =~ " ${strarr[1]} " ]]; then
		    cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/build/classes/main/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
		   
		else
		
			for elt in "${array_mockito[@]}"; do
				cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/target/classes/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
			done
		
		fi
	
	elif [[ ${strarr[0]} == "Time" ]]; then
		
		# Vérification si la valeur_exterieure n'est pas dans le tableau
		if ! [[ " ${array_time[*]} " =~ " ${strarr[1]} " ]]; then
		   cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/target/classes/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
		   
		else
		
			for elt in "${array_time[@]}"; do
				 cp -r /home/wendkuuni/OUTPUT_BUGGY/${strarr[0]}/${strarr[0]}_${strarr[1]}_buggy/build/classes/${strarr[3]} $PWD/${strarr[0]}/${strarr[1]}/${strarr[4]}
			done
		
		fi
				
		
		
	
		
	
	else
		echo ""
	fi
done



##Mockito 
#target/classes
#array_mockito=(14 26 28 37 34 38 36 31 15 13 30 32 12 25 23 22 17 29 16 27 24 33)

##Time
#build/classes
#14 15 26
