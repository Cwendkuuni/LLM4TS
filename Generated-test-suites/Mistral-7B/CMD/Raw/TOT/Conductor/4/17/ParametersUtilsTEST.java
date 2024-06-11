package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.apache.commons.lang3.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ParametersUtilsTest {

 private static final String WORKFLOW_DEF_JSON =
         "{\"workflowDefinition\":{\"schemaVersion\":1,"
         + "\"input\":{\"myInputKey\":\"myInputValue\"},"
         + "\"tasks\":[{"
         + "\"referenceTaskName\":\"task1\","
         + "\"inputData\":{\"myInputKey1\":\"myInputValue1\"},"
         + "\"outputData\":{\"myOutputKey1\":\"myOutputValue1\"}}],"
         + "\"workflowId\":\"workflowId1\""
         + "}}";

 private static final String TASK_DEF_JSON =
         "{\"inputTemplate\":{\"myInputKey2\":\"${\\$\\{workflow.myInputKey\\}}\"}}";

 private static final String TASK_INPUT_JSON =
         "{\"input\":{\"myInputKey\":\"workflowInputValue\"},\"tasks\":"
         + "[{\"inputData\":{\"myInputKey1\":\"taskInputValue1\"},\"outputData\":{"
         + "\"myOutputKey1\":\"taskOutputValue1\"}}}]";

 private static final String WORKFLOW_INPUT_JSON = "{\"myInputKey\":\"workflowInputValue\"}";

 private static final String WORKFLOW_OUTPUT_JSON =
         "{\"myInputKey\":\"workflowInputValue\",\"tasks\":[{\"inputData\":{},\"outputData\":"
         + "{\"myOutputKey1\":\"taskOutputValue1\",\"myOutputKey2\":\"taskOutputValue2\"}}]}";

 private WorkflowDef workflowDef;
 private WorkflowModel workflowModel;
 private TaskDef taskDef;
 private ParametersUtils parametersUtils;

 @Before
 public void setup() throws IOException {
     ObjectMapper objectMapper = Mockito.spy(new ObjectMapper());
     when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
             .thenAnswer(invocation -> invocation.getArgument(0));
     workflowDef = MapUtils.fromJsonString(WORKFLOW_DEF_JSON, WorkflowDef.class);
     workflowModel = new WorkflowModel();
     workflowModel.setWorkflowDefinition(workflowDef);
     taskDef = MapUtils.fromJsonString(TASK_DEF_JSON, TaskDef.class);
     parametersUtils = new ParametersUtils(objectMapper);
 }

 @Test
 public void getTaskInput_workflowInputIsUsed() {
     Map<String, Object> expectedResult = MapUtils.fromString(TASK_INPUT_JSON);
     Map<String, Object> result = parametersUtils.getTaskInput(
             new HashMap<>(), workflowModel, null, "task1");
     assertEquals(expectedResult, result);
 }

 @Test
 public void getTaskInput_inputTemplateIsUsed() {
     Map<String, Object> expectedResult = new LinkedHashMap<>();
     expectedResult.put("myInputKey2", "workflowInputValue");
     Map<String, Object> result = parametersUtils.getTaskInput(
             new HashMap<>(), workflowModel, taskDef, "task1");
     assertEquals(expectedResult, result);
 }

 @Test
 public void getTaskInput_inputTemplateIsEmpty() {
     Map<String, Object> result = parametersUtils.getTaskInput(
             new HashMap<>(), workflowModel, taskDef, "task1");
     assertNull(result);
 }

 @Test
 public void replace_systemVariableIsUsed() {
     String expectedResult = "systemVariableValue";
     String paramString = "${systemVariable}";
     Map<String, Object> documentContext = new HashMap<>();
     documentContext.put("systemVariable", "systemVariableValue");
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test
 public void replace_jsonPathExpressionIsUsed() {
     String expectedResult = "taskOutputValue1";
     String paramString = "${tasks[0].outputData.myOutputKey1}";
     Map<String, Object> documentContext = MapUtils.fromString(TASK_INPUT_JSON, Map.class);
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test
 public void replace_nestedJsonPathExpressionIsUsed() {
     String expectedResult = "workflowInputValue.tasks[0].outputData.myOutputKey1";
     String paramString = "${workflow.tasks[0].outputData.myOutputKey1}";
     Map<String, Object> documentContext = MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class);
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test
 public void replace_environmentVariableIsUsed() {
     String expectedResult = "environmentVariableValue";
     String paramString = "${environmentVariable}";
     Map<String, Object> documentContext = new HashMap<>();
     documentContext.put("environmentVariable", "environmentVariableValue");
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test
 public void replace_emptyJsonPathExpressionIsUsed() {
     String expectedResult = "";
     String paramString = "${}";
     Map<String, Object> documentContext = new HashMap<>();
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test
 public void replace_invalidJsonPathExpressionIsUsed() {
     String paramString = "${tasks[0].outputData.myOutputKey1}a}";
     Map<String, Object> documentContext = MapUtils.fromString(TASK_INPUT_JSON, Map.class);
     assertNull(parametersUtils.replaceVariables(paramString, documentContext, null));
 }

 @Test
 public void replace_inputTemplateAndSystemVariableAreUsed() {
     String expectedResult = "workflowInputValue.taskInputValue1";
     String paramString = "${inputTemplate.myInputKey2}";
     Map<String, Object> documentContext = MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class);
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test
 public void replace_inputTemplateAndEnvironmentVariableAreUsed() {
     String expectedResult = "workflowInputValue.systemVariableValue";
     String paramString = "${inputTemplate.myInputKey2}${systemVariable}";
     Map<String, Object> documentContext = new HashMap<>();
     documentContext.put("systemVariable", "systemVariableValue");
     String result = (String) parametersUtils.replaceVariables(paramString, documentContext, null);
     assertEquals(expectedResult, result);
 }

 @Test(expected = RuntimeException.class)
 public void replace_unknownJsonPathExpressionIsUsed() {
     String paramString = "${workflow.unknownPath}";
     Map<String, Object> documentContext = MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class);
     parametersUtils.replaceVariables(paramString, documentContext, null);
 }

 @Test
 public void replace_inputTemplateAndInvalidJsonPathExpressionAreUsed() {
     String paramString = "${inputTemplate.myInputKey2}a}";
     Map<String, Object> documentContext = MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class);
     assertNull(parametersUtils.replaceVariables(paramString, documentContext, null));
 }

 @Test
 public void getWorkflowInput_inputTemplateIsUsed() {
     Map<String, Object> expectedResult = MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class);
     Map<String, Object> result = parametersUtils.getWorkflowInput(
             MapUtils.fromJsonString(WORKFLOW_DEF_JSON, WorkflowDef.class), new HashMap<>());
     assertEquals(expectedResult, result);
 }

 @Test
 public void getWorkflowInput_inputTemplateIsEmpty() {
     Map<String, Object> result = parametersUtils.getWorkflowInput(
             MapUtils.fromJsonString(WORKFLOW_DEF_JSON, WorkflowDef.class), new HashMap<>());
     assertNull(result);
 }

 @Test
 public void getTaskInput_workflowInputIsUsed() {
     Map<String, Object> expectedResult = MapUtils.fromString(TASK_INPUT_JSON, Map.class);
     Map<String, Object> inputParams = new HashMap<>();
     inputParams.put("workflowInput", MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class));
     Map<String, Object> result = parametersUtils.getTaskInput(
             inputParams, workflowModel, null, "task1");
     assertEquals(expectedResult, result);
 }

 @Test
 public void getTaskInput_workflowInputIsEmpty() {
     Map<String, Object> result = parametersUtils.getTaskInput(
             new HashMap<>(), workflowModel, null, "task1");
     assertNull(result);
 }

 @Test
 public void getTaskInput_workflowInputAndInputTemplateIsUsed() {
     Map<String, Object> expectedResult = new LinkedHashMap<>();
     expectedResult.put("myInputKey2", "workflowInputValue");
     Map<String, Object> inputParams = new HashMap<>();
     inputParams.put("workflowInput", MapUtils.fromString(WORKFLOW_INPUT_JSON, Map.class));
     Map<String, Object> result = parametersUtils.getTaskInput(
             inputParams, workflowModel, taskDef, "task1");
     assertEquals(expectedResult, result);
 }
}