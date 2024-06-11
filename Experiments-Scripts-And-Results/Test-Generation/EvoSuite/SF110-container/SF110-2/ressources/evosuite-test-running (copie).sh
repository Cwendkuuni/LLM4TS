#!/bin/bash

#export PATH=$PATH:/home/wendkuuni/defects4j/framework/bin
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

source project-class-pair.sh
#source class_list_for_defects4j.sh

WORK_DIR=$PWD
CURRENT_DIR=$WORK_DIR

#PROJECTS_PATH=$CURRENT_DIR/Fixed_version

JUNIT_PATH=$CURRENT_DIR/JUNIT-HAMCREST/junit-4.13.2.jar
HAMCREST_CORE_PATH=$CURRENT_DIR/JUNIT-HAMCREST/hamcrest-core-1.3.jar
EVOSUITE_PATH=$CURRENT_DIR/evosuite-master-1.2.1-SNAPSHOT.jar

SL4J_PATH="$CURRENT_DIR/slf4j-api-1.6.4.jar"

TIME_IN_SECONDS=180
ITER_NUMBER=30

export EVOSUITE="java -jar $EVOSUITE_PATH"


for iter in $(seq 1 $ITER_NUMBER); do

	for elem in "${PROJECT_CLASS_PAIR[@]}"; do
		read -a strarr <<< "$elem"  # uses default whitespace IFS
		
		mkdir -p $WORK_DIR/${strarr[0]}/result-${iter}
		
		BASE_DIR=$WORK_DIR/${strarr[0]}/result-${iter}
		D_REPORT=$WORK_DIR/${strarr[0]}/result-${iter}
		CLASS_DIR=$CURRENT_DIR/${strarr[0]}/


		####Test generation
		echo "${strarr[0]} iter ${iter} test generation....."
		$EVOSUITE -target ${PROJECT_JAR_REPO}/${strarr[0]} -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=${BASE_DIR} -Dsearch_budget=${TIME_IN_SECONDS} -Dreport_dir=${D_REPORT} -Dstopping_condition=MaxTime -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false > ${strarr[3]}-${iter}.txt
		
		$EVOSUITE -class com.ib.client.ExecutionFilter -projectCP $PWD/1_tullibee/ -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=$PWD/1_tullibee/resultats -Dsearch_budget=180 -Dreport_dir=$PWD/1_tullibee/resultats -Dstopping_condition=MaxTime  -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false
		
		$EVOSUITECLI -class org.apache.commons.cli.CommandLine -projectCP ${CLASS_DIR} -generateMOSuite -Dalgorithm=DynaMOSA -base_dir=${OUTPUT_DIR} -Dsearch_budget=${TIME_IN_SECONDS} -Dreport_dir=${OUTPUT_DIR} -Dstopping_condition=MaxTime  -Dcriterion=BRANCH:METHOD:LINE -Dshow_progress=false >> ${project}_${elt}_fixed-${iter}.txt


		

