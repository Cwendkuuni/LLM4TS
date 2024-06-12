#!/bin/bash

#export PATH=$PATH:/home/wendkuuni/defects4j/framework/bin
ulimit -c unlimited

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

source projects-pair-for-coverage.sh
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
JARS_PATH="$PWD/JARS/Buggy_version_Jar/"

TIME_IN_SECONDS=10
ITER_NUMBER=2

STRATEGY_LIST=("COT" "TOT" "FEWSHOT" "ZEROSHOT")


#export EVOSUITE="java -Xmx1024m -Xss512k -jar $EVOSUITE_PATH"
export EVOSUITE="java -Xmx512M -jar $EVOSUITE_PATH"

##iter == strategy

for elem in "${PROJECT_CLASS_PAIR[@]}"; do
	read -a strarr <<< "$elem"  # uses default whitespace IFS
		
		
	if [ -d "$WORK_DIR/SORTIES-RESULTATS" ]; then
    		echo ""
	else
    		echo ""
    		mkdir -p "$WORK_DIR/SORTIES-RESULTATS"
	fi
		
		
	if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Class-${strarr[2]}" ]; then #$WORK_DIR/SORTIES-RESULTATS/COT/Cli/result-Class-1 
    		echo ""
	else
    		echo ""
    		mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Class-${strarr[2]}"
	fi
			
	####Logs directory for unit tes case generation
	if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/${strarr[1]}/logs" ]; then
    		echo ""
	else
    		echo ""
    		mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/${strarr[1]}/logs"
	fi
		
	LOG_DIR="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/${strarr[1]}/logs"
		
		
	#BASE_DIR="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}"
	#D_REPORT="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}"
		
	##CLASS_DIR=$CURRENT_DIR/${strarr[3]}
	##PROJECTS_PATH=$WORK_DIR/${strarr[0]}/${strarr[1]}

	CLASS_DIR=$CURRENT_DIR/SOURCES/${strarr[1]}/${strarr[2]}
	

	#######Test generation
	#echo "Project ${strarr[0]} Class ${strarr[1]} iter ${iter} test generation....."
	#echo "$PWD"
		
	##$EVOSUITE -class ${strarr[2]} -projectCP $PWD/${strarr[3]} -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=${BASE_DIR} -Dsearch_budget=${TIME_IN_SECONDS} -Dreport_dir=${D_REPORT} -Dstopping_condition=MaxTime -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false > $LOG_DIR/${strarr[1]}-${iter}.txt
		
	#$EVOSUITE -class ${strarr[2]} -projectCP $PWD/${strarr[0]}/${strarr[1]}:$PWD/JARS/ -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=${BASE_DIR} -Dsearch_budget=${TIME_IN_SECONDS} -Dreport_dir=${D_REPORT} -Dstopping_condition=MaxTime -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false > $LOG_DIR/${strarr[1]}-${iter}.txt
		
	##$EVOSUITE -class org.apache.commons.cli.CommandLine -projectCP $PWD/Cli/1:$PWD/JARS/ -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=resultats -Dsearch_budget=10 -Dreport_dir=resultats -Dstopping_condition=MaxTime  -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false
		
		
	###Class_loader_to_false
	#cd $BASE_DIR

	#find ${BASE_DIR}/. -type f -name "*.java" -exec sed -i 's/separateClassLoader = true/separateClassLoader = false/g' {} +
			
	#cd ${CURRENT_DIR} 
	#echo "${CURRENT_DIR}"
		
	####Code coverage
	BASE_DIR="SF110-generated-tests/${strarr[0]}/${strarr[1]}/${strarr[2]}/${strarr[3]}"
	OUTPUT="${BASE_DIR}"


	echo "${strarr[0]}-${strarr[1]}-${strarr[2]} itération N°${strarr[3]} code coverage....."
		
	###Code compilation
	#find ${OUTPUT}/ -type f -name "*.java" | xargs javac -classpath ${PROJECTS_PATH}:${EVOSUITE_PATH}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH}
		
	###coverage
	java -Xmx512M -javaagent:${JACOCO_AGENT_PATH} -cp ${CLASS_DIR}:${JARS_PATH}:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]}
	java -Xmx512M -jar ${JACOCO_PATH} report jacoco.exec --classfiles ${OUTPUT}/ --html $WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Class-${strarr[2]}/${strarr[3]} --sourcefiles $CLASS_DIR
	
		
	if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/EXEC" ]; then
    	echo ""
	else
    	echo ""
    	mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Class-${strarr[2]}/EXEC"
	fi
		
	EXEC_PATH="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Class-${strarr[2]}/EXEC"
		
	java -Xmx512M -javaagent:${JACOCO_AGENT_PATH}=output=file,destfile=$EXEC_PATH/${strarr[6]}-${strarr[3]}-coverage.exec -cp $CLASS_DIR:${JARS_PATH}:${OUTPUT}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[4]}

	echo "*************"
	echo "Prompt ${strarr[0]} Project ${strarr[1]} Bug-ID ${strarr[2]} iter ${strarr[3]} finish"
	echo "${CURRENT_DIR}"
		
done



for elem in "${PROJECT_CLASS_PAIR[@]}"; do
	read -a strarr <<< "$elem"  # uses default whitespace IFS
	
	BASE_DIR="SF110-generated-tests/${strarr[0]}/${strarr[1]}/${strarr[2]}/${strarr[3]}"
	OUTPUT="${BASE_DIR}"
	CLASS_DIR=$CURRENT_DIR/SOURCES/${strarr[1]}/${strarr[2]}
	EXEC_PATH="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-Class-${strarr[2]}/EXEC"

				
	###Merging the exec files
	java -Xmx512M -jar $JACOCO_PATH merge $EXEC_PATH/*-coverage.exec --destfile $EXEC_PATH/${strarr[6]}-merged.exec


	####Generate a Single HTML Report
	#java -Xmx512M -jar $JACOCO_PATH  report $EXEC_PATH/${strarr[6]}-merged.exec --classfiles $OUTPUT --sourcefiles $OUTPUT --html $EXEC_PATH/${strarr[0]}/${strarr[1]}/${strarr[2]}
	java -Xmx512M -jar $JACOCO_PATH  report $EXEC_PATH/${strarr[6]}-merged.exec --classfiles $OUTPUT --html $EXEC_PATH/${strarr[0]}/${strarr[1]}/${strarr[2]} --sourcefiles $CLASS_DIR


done



#/home/wendkuuni/EVOSUITE/LLMS-PROJECT/SF110/1_tullibee/com/ib/client/ExecutionFilter/result-com/ib/client/ExecutionFilter-1/evosuite-tests/com/ib/client/ExecutionFilter_ESTest.class

		
		
