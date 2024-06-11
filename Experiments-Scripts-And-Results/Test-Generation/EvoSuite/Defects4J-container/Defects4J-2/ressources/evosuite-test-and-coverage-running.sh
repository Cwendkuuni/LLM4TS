#!/bin/bash

#export PATH=$PATH:/home/wendkuuni/defects4j/framework/bin
ulimit -c unlimited

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

#source project-class-pair.sh
source project-class-pair-part_2.sh


WORK_DIR=/usr/ressources
#WORK_DIR=$PWD

CURRENT_DIR=$WORK_DIR

#PROJECTS_PATH=$CURRENT_DIR/Fixed_version

JUNIT_PATH="$CURRENT_DIR/JUNIT-HAMCREST/junit-4.13.2.jar"
HAMCREST_CORE_PATH="$CURRENT_DIR/JUNIT-HAMCREST/hamcrest-core-1.3.jar"
EVOSUITE_PATH="$CURRENT_DIR/evosuite-master-1.2.1-SNAPSHOT.jar"

SL4J_PATH="$CURRENT_DIR/slf4j-api-1.6.4.jar"
JACOCO_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacococli.jar"
JACOCO_AGENT_PATH="$WORK_DIR/jacoco-0.8.8/lib/jacocoagent.jar"
JARS_PATH="$PWD/JARS/Buggy_version_Jar/"

TIME_IN_SECONDS=180
ITER_NUMBER=30

#export EVOSUITE="java -Xmx1024m -Xss512k -jar $EVOSUITE_PATH"
export EVOSUITE="java -Xmx512M -jar $EVOSUITE_PATH"


for iter in $(seq 1 $ITER_NUMBER); do

	for elem in "${PROJECT_CLASS_PAIR[@]}"; do
		read -a strarr <<< "$elem"  # uses default whitespace IFS
		
		
		if [ -d "$WORK_DIR/SORTIES-RESULTATS" ]; then
    			echo ""
		else
    			echo ""
    			mkdir -p "$WORK_DIR/SORTIES-RESULTATS"
		fi
		
		
		if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}" ]; then
    			echo ""
		else
    			echo ""
    			mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}"
		fi
			
		####Logs directory for unit tes case generation
		if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/logs" ]; then
    			echo ""
		else
    			echo ""
    			mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/logs"
		fi
		
		LOG_DIR="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/logs"
		
		#BASE_DIR=$WORK_DIR/${strarr[0]}/${strarr[1]}/result-${strarr[1]}-${iter}
		#D_REPORT=$WORK_DIR/${strarr[0]}/${strarr[1]}/result-${strarr[1]}-${iter}
		
		BASE_DIR="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}"
		D_REPORT="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}"
		
		#CLASS_DIR=$CURRENT_DIR/${strarr[3]}
		CLASS_DIR=$CURRENT_DIR/${strarr[0]}/${strarr[1]}
		PROJECTS_PATH=$WORK_DIR/${strarr[0]}/${strarr[1]}


		#######Test generation
		echo "Project ${strarr[0]} Class ${strarr[1]} iter ${iter} test generation....."
		echo "$PWD"
		
		#$EVOSUITE -class ${strarr[2]} -projectCP $PWD/${strarr[3]} -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=${BASE_DIR} -Dsearch_budget=${TIME_IN_SECONDS} -Dreport_dir=${D_REPORT} -Dstopping_condition=MaxTime -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false > $LOG_DIR/${strarr[1]}-${iter}.txt
		
		$EVOSUITE -class ${strarr[2]} -projectCP $PWD/${strarr[0]}/${strarr[1]}:$PWD/JARS/ -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=${BASE_DIR} -Dsearch_budget=${TIME_IN_SECONDS} -Dreport_dir=${D_REPORT} -Dstopping_condition=MaxTime -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false > $LOG_DIR/${strarr[1]}-${iter}.txt
		
		#$EVOSUITE -class org.apache.commons.cli.CommandLine -projectCP $PWD/Cli/1:$PWD/JARS/ -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=resultats -Dsearch_budget=10 -Dreport_dir=resultats -Dstopping_condition=MaxTime  -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false
		
		
		###Class_loader_to_false
		cd $BASE_DIR

		find ${BASE_DIR}/. -type f -name "*.java" -exec sed -i 's/separateClassLoader = true/separateClassLoader = false/g' {} +
			
		cd ${CURRENT_DIR} 
		echo "${CURRENT_DIR}"
		
		####Code coverage
		OUTPUT="${BASE_DIR}/evosuite-tests"
		echo "${strarr[2]}-${iter} code coverage....."
		
		###Code compilation
		find ${OUTPUT}/ -type f -name "*.java" | xargs javac -classpath ${PROJECTS_PATH}:${EVOSUITE_PATH}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH}
		
		###coverage
		java -Xmx512M -javaagent:${JACOCO_AGENT_PATH} -cp ${CLASS_DIR}:${JARS_PATH}:${OUTPUT}:${EVOSUITE_PATH}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[2]}_ESTest
		java -Xmx512M -jar ${JACOCO_PATH} report jacoco.exec --classfiles ${OUTPUT}/ --html $WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/REPORTS/${strarr[2]}-${iter} --sourcefiles $CLASS_DIR
		#java -jar ${JACOCO_PATH} report jacoco.exec --classfiles ${OUTPUT}/ --html $WORK_DIR/${strarr[0]}/${strarr[1]}/REPORTS/${strarr[1]}-${iter} --sourcefiles $CLASS_DIR
		
		
		
		if [ -d "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/EXEC" ]; then
    			echo ""
		else
    			echo ""
    			mkdir -p "$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/EXEC"
		fi
		EXEC_PATH="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/EXEC"
		
		java -Xmx512M -javaagent:${JACOCO_AGENT_PATH}=output=file,destfile=$EXEC_PATH/${strarr[2]}-${iter}-coverage.exec -cp $CLASS_DIR:${JARS_PATH}:${OUTPUT}:${EVOSUITE_PATH}:${JUNIT_PATH}:${HAMCREST_CORE_PATH}:${SL4J_PATH} org.junit.runner.JUnitCore ${strarr[2]}_ESTest

		echo "*************"
		echo "Project ${strarr[0]} Class ${strarr[2]} iter ${iter} finish"
		echo "${CURRENT_DIR}"
		
	done
	
	touch Iter-${iter}-end.txt

	
done		



for iter in $(seq 1 $ITER_NUMBER); do

	for elem in "${PROJECT_CLASS_PAIR[@]}"; do
		read -a strarr <<< "$elem"  # uses default whitespace IFS
		#BASE_DIR=$WORK_DIR/${strarr[0]}/${strarr[1]}/result-${strarr[1]}-${iter}
		BASE_DIR="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/result-${strarr[2]}-${iter}"
		OUTPUT="$BASE_DIR/evosuite-tests"
		CLASS_DIR=$CURRENT_DIR/${strarr[0]}/${strarr[1]}
		#EXEC_PATH="$WORK_DIR/${strarr[0]}/${strarr[1]}/EXEC"
		EXEC_PATH="$WORK_DIR/SORTIES-RESULTATS/${strarr[0]}/${strarr[1]}/EXEC"
			
			
			
		###Merging the exec files
		#java -Xmx512M -jar $JACOCO_PATH merge $EXEC_PATH/*.exec --destfile $EXEC_PATH/merged.exec
		java -Xmx512M -jar $JACOCO_PATH merge $EXEC_PATH/${strarr[2]}-*.exec --destfile $EXEC_PATH/${strarr[2]}-merged.exec


		####Generate a Single HTML Report
		#java -jar $JACOCO_PATH  report $EXEC_PATH/merged.exec --classfiles $OUTPUT --sourcefiles $CLASS_DIR --html $EXEC_PATH/${strarr[1]}
		#java -Xmx512M -jar $JACOCO_PATH  report $EXEC_PATH/merged.exec --classfiles $OUTPUT --sourcefiles $OUTPUT --html $EXEC_PATH/${strarr[1]}
		java -Xmx512M -jar $JACOCO_PATH  report $EXEC_PATH/${strarr[2]}-merged.exec --classfiles $OUTPUT --sourcefiles $OUTPUT --html $EXEC_PATH/${strarr[1]}/${strarr[2]}


	done

done

touch EXP-final_end.txt
#/home/wendkuuni/EVOSUITE/LLMS-PROJECT/SF110/1_tullibee/com/ib/client/ExecutionFilter/result-com/ib/client/ExecutionFilter-1/evosuite-tests/com/ib/client/ExecutionFilter_ESTest.class

		
		
