package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.model.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.WorkflowDefinition;

public class ParametersUtilsTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
 private static final String VALID_WORKFLOW_JSON =
         "{\"name\":\"testWorkflow\",\"input\":{\"input1\":\"value1\",\"input2\":\"value2\"},\"tasks\":[{\"name\":\"testTask\",\"inputTemplate\":{\"input1\":\"${workflow.input.input1}\",\"input2\":\"${workflow.input.input2}\"}}]}";
 private static final String VALID_TASK_JSON = "{\"name\":\"testTask\",\"inputData\":{\"input1\":\"value1\",\"input2\":\"value2\"}}";
 private static final String INVALID_WORKFLOW_JSON =
         "{\"name\":\"testWorkflow\",\"input\":{\"input1\":\"value1\",\"input2\":\"value2\"},\"tasks\":[{\"name\":\"testTask\",\"inputTemplate\":\"${workflow.input.input1}\"}}]}";
 private static final String INVALID_TASK_JSON = "{\"name\":\"testTask\",\"inputTemplate\":\"${workflow.input.input1}\"}";
 private static final String TASK_ID = "taskId";
 private ParametersUtils utils;
 private ObjectMapper objectMapper;
 private DocumentContext context;
 private TaskDef taskDef;
 private WorkflowModel workflowModel;
 private WorkflowDefinition workflowDef;

 @Before
 public void setup() {
 utils = new ParametersUtils(mock(ObjectMapper.class));
 objectMapper = mock(ObjectMapper.class);
 context = mock(DocumentContext.class);
 taskDef = mock(TaskDef.class);
 workflowModel = mock(WorkflowModel.class);
 workflowDef = mock(WorkflowDefinition.class);
 when(objectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class)))
         .thenReturn(new HashMap<>());
 when(workflowModel.getInput()).thenReturn(new HashMap<>());
 when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskDef));
 when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());
 }

 @Test
 public void testGetTaskInputV1_ValidWorkflowAndInput() {
 // Given
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("input1", "value1");
 inputParams.put("input2", "value2");
 when(workflowModel.getInput()).thenReturn(inputParams);
 when(workflowModel.getTasks().get(0).getInputTemplate()).thenReturn(null);

 // When
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("input1", "value1");
 expectedResult.put("input2", "value2");
 Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, null, null);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInputV1_InvalidWorkflowJson() {
 // Given
 when(workflowModel.getInput()).thenReturn(new HashMap<>());
 when(workflowModel.getTasks().get(0).getInputTemplate()).thenReturn(null);
 when(workflowDef.getInputTemplate()).thenReturn(INVALID_WORKFLOW_JSON);

 // When & Then
 try {
 utils.getTaskInput(Collections.emptyMap(), workflowDef, null, null);
 fail();
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInputV1_InvalidTaskJson() {
 // Given
 when(workflowModel.getInput()).thenReturn(new HashMap<>());
 when(workflowModel.getTasks().get(0).getInputTemplate()).thenReturn(null);
 when(workflowDef.getInputTemplate()).thenReturn(VALID_WORKFLOW_JSON);
 when(taskDef.getInputTemplate()).thenReturn(INVALID_TASK_JSON);

 // When & Then
 try {
 utils.getTaskInput(Collections.emptyMap(), workflowDef, null, null);
 fail();
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testGetTaskInputV2_ValidWorkflowAndInput() {
 // Given
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("input1", "value1");
 inputParams.put("input2", "value2");
 when(workflowModel.getInput()).thenReturn(inputParams);
 when(workflowModel.getTasks().get(0).getInputTemplate()).thenReturn(new HashMap<>());

 // When
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("input1", "value1");
 expectedResult.put("input2", "value2");
 expectedResult.put("workflow", expectedResult);
 Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, null, null);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplaceVariables_ValidString() {
 // Given
 String input = "${workflow.input.input1}";
 String expectedOutput = "value1";
 DocumentContext documentContext = JsonPath.parse("{}");

 // When
 Object replacement = utils.replaceVariables(input, documentContext, null);

 // Then
 assertEquals(expectedOutput, replacement);
 }

 @Test
 public void testReplaceVariables_ValidMap() {
 // Given
 Map<String, Object> inputMap = new HashMap<>();
 inputMap.put("workflow", new HashMap<>());
 inputMap.put("workflow.input", new HashMap<>());
 inputMap.put("workflow.input.input1", "value1");
 String expectedOutput = "value1";
 DocumentContext documentContext = JsonPath.parse("{}");

 // When
 Object replacement = utils.replaceVariables("${workflow.input.input1}", documentContext, null);

 // Then
 assertEquals(expectedOutput, replacement);
 }

 @Test
 public void testReplaceVariables_ValidList() {
 // Given
 List<Object> inputList = new LinkedList<>();
 inputList.add("${workflow.input.input1}");
 inputList.add("${workflow.input.input2}");
 String expectedOutput = "value1, value2";
 DocumentContext documentContext = JsonPath.parse("{}");

 // When
 Object replacement = utils.replaceList(inputList, null, documentContext).get(0);

 // Then
 assertEquals(expectedOutput, replacement);
 }

 @Test
 public void testReplaceVariables_InvalidString() {
 // Given
 String input = "${workflow.input.input1}";
 DocumentContext documentContext = JsonPath.parse("{}");

 // When & Then
 assertNull(utils.replaceVariables(input, documentContext, null));
 }

 @Test
 public void testReplaceVariables_InvalidMap() {
 // Given
 Map<String, Object> inputMap = new HashMap<>();
 String input = "${workflow.input.input1}";
 DocumentContext documentContext = JsonPath.parse("{}");

 // When & Then
 assertNull(utils.replaceVariables(input, documentContext, null));
 }

 @Test
 public void testReplaceVariables_InvalidList() {
 // Given
 List<Object> inputList = new LinkedList<>();
 String input = "${workflow.input.input1}";
 DocumentContext documentContext = JsonPath.parse("{}");

 // When & Then
 assertNull(utils.replaceList(inputList, null, documentContext).get(0));
 }

 @Test
 public void testReplaceVariables_EnvironmentVariable() {
 // Given
 String input = "${ENV_VAR}";
 String expectedOutput = "test";
 Map<String, String> env = new HashMap<>();
 env.put("ENV_VAR", "test");
 DocumentContext documentContext = JsonPath.parse("{}", new ObjectMapper());

 // When
 Object replacement = utils.replaceVariables(input, documentContext, null);

 // Then
 assertEquals(expectedOutput, replacement);
 }

 @Test
 public void testReplaceVariables_EnvironmentVariableNotFound() {
 // Given
 String input = "${ENV_VAR}";
 DocumentContext documentContext = JsonPath.parse("{}", new ObjectMapper());

 // When & Then
 assertNull(utils.replaceVariables(input, documentContext, null));
 }

 @Test
 public void testReplaceVariables_InvalidJsonPath() {
 // Given
 String input = "${workflow.input.invalidJsonPath}";
 DocumentContext documentContext = JsonPath.parse("{}", new ObjectMapper());

 // When & Then
 assertNull(utils.replaceVariables(input, documentContext, null));
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceVariables_InvalidJsonPathInMap() {
 // Given
 Map<String, Object> inputMap = new HashMap<>();
 inputMap.put("workflow", new HashMap<>());
 inputMap.put("workflow.input", new HashMap<>());
 inputMap.put("workflow.input.invalidJsonPath", "${workflow.input.input1}");
 DocumentContext documentContext = JsonPath.parse("{}", new ObjectMapper());

 // When & Then
 try {
 utils.replaceVariables("${workflow.input.invalidJsonPath}", documentContext, null);
 fail();
 } catch (RuntimeException e) {
 // Expected
 }
 }

}