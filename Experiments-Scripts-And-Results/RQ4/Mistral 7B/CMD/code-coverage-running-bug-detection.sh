#!/bin/bash

#export PATH=$PATH:/home/wendkuuni/defects4j/framework/bin
ulimit -c unlimited

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

source projects-pair-for-coverage-bug.sh
#source class_list_for_defects4j.sh

WORK_DIR=$PWD
CURRENT_DIR=$WORK_DIR

#PROJECTS_PATH=$CURRENT_DIR/Fixed_version

JUNIT_PATH="$CURRENT_DIR/JUNIT-HAMCREST/junit-4.13.2.jar"
HAMCREST_CORE_PATH="$CURRENT_DIR/JUNIT-HAMCREST/hamcrest-core-1.3.jar"
EVOSUITE_PATH="$CURRENT_DIR/evosuite-master-1.2.1-SNAPSHOT.jar"

SL4J_PATH="$CURRENT_DIR/slf4j-api-1.6.4.jar"
JACOCO_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacococli.jar"
JACOCO_AGENT_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacocoagent.jar"

#JARS_PATH="$PWD/JARS/Buggy_version_Jar/"
JAR_PATH="JARS/dependencies"

TIME_IN_SECONDS=10
ITER_NUMBER=2

STRATEGY_LIST=("COT" "TOT" "TOT2" "FEWSHOT" "ZEROSHOT")
#STRATEGY_LIST=("TOT2")



#export EVOSUITE="java -Xmx1024m -Xss512k -jar $EVOSUITE_PATH"
export EVOSUITE="java -Xmx512M -jar $EVOSUITE_PATH"

#iter == strategy



# Chemin du fichier CSV de sortie
OUTPUT_CSV="BugDetection_results_tests.csv"

# En-tête du fichier CSV
echo "Strategy,Project,Bug-ID,Iteration,Class-Name,Buggy_Version,Fixed_Version,Bug-Detected" > "$OUTPUT_CSV"


for elem in "${PROJECT_CLASS_PAIR[@]}"; do
	read -a strarr <<< "$elem"  # uses default whitespace IFS
		
		
	if [ -d "$WORK_DIR/SORTIES-RESULTATS" ]; then
    		echo ""
	else
    		echo ""
    		mkdir -p "$WORK_DIR/SORTIES-RESULTATS"
	fi
		
		
	if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Bug-ID-${strarr[2]}" ]; then #$WORK_DIR/SORTIES-RESULTATS/COT/Cli/result-Bug-ID-1 
    		echo ""
	else
    		echo ""
    		mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Bug-ID-${strarr[2]}"
	fi
			
	####Logs directory for unit tes case generation
	if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/${strarr[1]}/logs" ]; then
    		echo ""
	else
    		echo ""
    		mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/${strarr[1]}/logs"
	fi
		
	LOG_DIR="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/${strarr[1]}/logs"
		

	
	CLASS_DIR="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SOURCES-FILES/${strarr[1]}/${strarr[2]}"
	            #/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SOURCES-FILES/Odc/2
	


	#CLASS_DIR_FIXED=$CURRENT_DIR/FILTERED-FIXED-VERSION/${strarr[1]}/${strarr[2]}
	#CLASS_DIR_FIXED=/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/DEFECTS4J/COMPILED-CLASSES/FILTERED-FIXED-VERSION/${strarr[1]}/${strarr[2]}
	#CLASS_DIR_FIXED=/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/FIX-VERSION/${strarr[1]}/${strarr[2]}
	CLASS_DIR_FIXED=/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/FILTERED-FIXED-VERSION/${strarr[1]}/${strarr[2]}

	
		
	####Code coverage
	#BASE_DIR="Defects4J-generated-tests/${strarr[0]}/${strarr[1]}/${strarr[2]}/${strarr[3]}"
	BASE_DIR="NewDataset-generated-tests/${strarr[0]}/${strarr[1]}/${strarr[2]}/${strarr[3]}"
	#AVEC-PACKAGE_GLOBAL/COT/Odc/2/0




	OUTPUT="${BASE_DIR}"
	echo "$OUTPUT"
	echo "${strarr[4]}"

	echo "${strarr[0]}-${strarr[1]}-${strarr[2]} itération N°${strarr[3]} code coverage....."
		
	###Code compilation
	#find ${OUTPUT}/ -type f -name "*.java" | xargs javac -classpath ${PROJECTS_PATH}:${EVOSUITE_PATH}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH}
		
	###coverage
	# Exécuter les tests sur la version buggée
	echo "Exécution des tests sur la version buggée"
	java -Xmx512M -javaagent:${JACOCO_AGENT_PATH} -cp ${CLASS_DIR}:${JAR_PATH}/*:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]}
	buggy_result=$(java -Xmx512M -javaagent:${JACOCO_AGENT_PATH} -cp ${CLASS_DIR}:${JAR_PATH}/*:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]} 2>&1)
	#echo "$buggy_result"

	if echo "$buggy_result" | grep -q "FAILURES!!!"; then
	    echo "Le test a échoué sur la version buggée"
	    #buggy_failed=true
	    buggy_version="Failed"

	else
	    echo "Le test a réussi sur la version buggée"
	    #buggy_failed=false
	    buggy_version="Passed"

	fi


	# Exécuter les tests sur la version corrigée
	echo "Exécution des tests sur la version corrigée"
	java -Xmx512M -javaagent:${JACOCO_AGENT_PATH} -cp ${CLASS_DIR_FIXED}:${JAR_PATH}/*:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]}
	fixed_result=$(java -Xmx512M -javaagent:${JACOCO_AGENT_PATH} -cp ${CLASS_DIR_FIXED}:${JAR_PATH}/*:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]} 2>&1)
	#echo "$fixed_result"

	if echo "$fixed_result" | grep -q "OK"; then
		echo "Le test a réussi sur la version fixée"
	    #fixed_passed=true
	    fixed_version="Passed"

	else
		echo "Le test a échoué sur la version fixée"
	    #fixed_passed=false
	    fixed_version="Failed"
	fi



	# # Mise à jour des statistiques
	# if $buggy_failed && $fixed_passed; then
	#     ((detectedBugs++))
	# elif $buggy_failed && ! $fixed_passed; then
	#     ((manualCheck++))
	# fi

	# ((totalTests++))


	# Détermination si le bug a été détecté
    if [ "$buggy_version" == "Failed" ] && [ "$fixed_version" == "Passed" ]; then
        
        bug_detected="True"

    elif [ "$buggy_version" == "Failed" ] && [ "$fixed_version" == "Failed" ]; then
        
        #bug_detected="Need Manual checks"
        
        echo "Need  further checks ..."
        buggy_failures=$(echo "$buggy_result" | grep -o "Failures: [0-9]*" | grep -o "[0-9]*")
		echo "$buggy_result"
		
		fixed_failures=$(echo "$fixed_result" | grep -o "Failures: [0-9]*" | grep -o "[0-9]*")
		echo "$fixed_result"

		if [ "$buggy_failures" -eq "$fixed_failures" ]; then

        	echo "After further checks, the test failed on the fix version for the same reasons."
        	bug_detected="Need Manual checks"

    	else

        	echo "After further checks, the behavior has changed indicating a potential bug detection."
        	bug_detected="Potential bug"

        fi

    else
        
        bug_detected="False"

    fi


    # Écriture dans le fichier CSV
    echo "${strarr[0]},${strarr[1]},${strarr[2]},${strarr[3]},${strarr[6]},$buggy_version,$fixed_version,$bug_detected" >> "$OUTPUT_CSV"


#####################################################################CODE COVERAGE COMPUTING#############################################################################################################################

	java -Xmx512M -jar ${JACOCO_PATH} report jacoco.exec --classfiles ${OUTPUT}/ --html $WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Bug-ID-${strarr[2]}/${strarr[3]} --sourcefiles $CLASS_DIR
	
		
	if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/EXEC" ]; then
    	echo ""
	else
    	echo ""
    	mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Bug-ID-${strarr[2]}/EXEC"
	fi
		
	EXEC_PATH="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Bug-ID-${strarr[2]}/EXEC"
		
	java -Xmx512M -javaagent:${JACOCO_AGENT_PATH}=output=file,destfile=$EXEC_PATH/${strarr[6]}-${strarr[3]}-coverage.exec -cp $CLASS_DIR:${JAR_PATH}/*:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]}

	echo "*************"
	echo "Prompt ${strarr[0]} Project ${strarr[1]} Bug-ID ${strarr[2]} iter ${strarr[3]} finish"
	echo "${CURRENT_DIR}"
		
done

# # Affichage des statistiques
# echo "Total des tests exécutés: $totalTests"
# echo "Bugs détectés: $detectedBugs"
# echo "Vérification manuelle requise: $manualCheck"



#####################################################################EXEC MERGING PART FOR JACOCO#############################################################################################################################

for elem in "${PROJECT_CLASS_PAIR[@]}"; do
	read -a strarr <<< "$elem"  # uses default whitespace IFS
	
	#BASE_DIR="Defects4J-generated-tests/${strarr[0]}/${strarr[1]}/${strarr[2]}/${strarr[3]}"
	BASE_DIR="AVEC-PACKAGE_GLOBAL/${strarr[0]}/${strarr[1]}/${strarr[2]}/${strarr[3]}"

	OUTPUT="${BASE_DIR}"
	#CLASS_DIR=$CURRENT_DIR/SOURCES/${strarr[1]}/${strarr[2]}
	CLASS_DIR=="/home/wendkuuni/EVOSUITE/LLMS-PROJECT/COMPILABILITY-CODECOVERAGE/NEW-DATASET/SOURCES-FILES/${strarr[1]}/${strarr[2]}"

	EXEC_PATH="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Bug-ID-${strarr[2]}/EXEC"

				
	###Merging the exec files
	java -Xmx512M -jar $JACOCO_PATH merge $EXEC_PATH/*-coverage.exec --destfile $EXEC_PATH/${strarr[6]}-merged.exec


	####Generate a Single HTML Report
	#java -Xmx512M -jar $JACOCO_PATH  report $EXEC_PATH/${strarr[6]}-merged.exec --classfiles $OUTPUT --sourcefiles $OUTPUT --html $EXEC_PATH/${strarr[0]}/${strarr[1]}/${strarr[2]}
	java -Xmx512M -jar $JACOCO_PATH  report $EXEC_PATH/${strarr[6]}-merged.exec --classfiles $OUTPUT --html $EXEC_PATH/${strarr[0]}/${strarr[1]}/${strarr[2]} --sourcefiles $CLASS_DIR


done



#/home/wendkuuni/EVOSUITE/LLMS-PROJECT/SF110/1_tullibee/com/ib/client/ExecutionFilter/result-com/ib/client/ExecutionFilter-1/evosuite-tests/com/ib/client/ExecutionFilter_ESTest.class

		
		
