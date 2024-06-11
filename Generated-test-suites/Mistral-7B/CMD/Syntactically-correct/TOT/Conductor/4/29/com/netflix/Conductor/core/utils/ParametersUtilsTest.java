package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ParametersUtilsTest {

 private final ObjectMapper objectMapper = new ObjectMapper();
 private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
 private final WorkflowDef workflowDef = mock(WorkflowDef.class);
 private final TaskDef taskDef = mock(TaskDef.class);
 private final WorkflowModel workflowModel = new WorkflowModel();

 @Test
 @DisplayName("Test getTaskInputV2 method with valid input")
 public void testGetTaskInputV2WithValidInput() throws IOException {
 // Given
 mapInputParams();
 when(workflowDef.getSchemaVersion()).thenReturn(2L);
 when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("key", "value"));
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("key2", "value2"));

 // When
 Map<String, Object> result =
 parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

 // Then
 Map<String, Object> expectedResult = new LinkedHashMap<>();
 expectedResult.put("key", "value");
 expectedResult.put("key2", "value2");
 expectedResult.put("workflow", workflowParams());
 expectedResult.put("input", workflowInput());
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 @DisplayName("Test getTaskInputV2 method with null inputTemplate")
 public void testGetTaskInputV2WithNullInputTemplate() {
 // Given
 mapInputParams();
 when(workflowDef.getSchemaVersion()).thenReturn(2L);
 when(taskDef.getInputTemplate()).thenReturn(null);

 // When
 Map<String, Object> result =
 parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

 // Then
 Assertions.assertNull(result);
 }

 @Test
 @DisplayName("Test getTaskInputV2 method with empty inputTemplate")
 public void testGetTaskInputV2WithEmptyInputTemplate() {
 // Given
 mapInputParams();
 when(workflowDef.getSchemaVersion()).thenReturn(2L);
 when(taskDef.getInputTemplate()).thenReturn(Collections.emptyMap());

 // When
 Map<String, Object> result =
 parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

 // Then
 Assertions.assertNull(result);
 }

 @Test
 @DisplayName("Test getTaskInputV2 method with invalid JSONPath")
 public void testGetTaskInputV2WithInvalidJsonPath() {
 // Given
 mapInputParams();
 when(workflowDef.getSchemaVersion()).thenReturn(2L);
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("invalid.$$.", "value"));

 // When/Then
 Assertions.assertThrows(
             RuntimeException.class, () -> parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId"));
 }

 @Test
 @DisplayName("Test replace method with valid input and json document")
 public void testReplaceWithValidInputAndJsonDocument() {
 // Given
 String jsonDocument = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
 Map<String, Object> input = new HashMap<>();
 input.put("name", "${name}");
 input.put("age", 35);
 input.put("cars", "${cars[0]}");

 // When
 Object result =
 parametersUtils.replace(input, JsonPath.parse(jsonDocument, Configuration.defaultConfiguration()));

 // Then
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("name", "John");
 expectedResult.put("age", 35);
 expectedResult.put("cars", "Ford");
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 @DisplayName("Test replace method with null input and json document")
 public void testReplaceMethodWithNullInputAndJsonDocument() {
 // Given
 String jsonDocument = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";

 // When
 Object result = parametersUtils.replace(null, JsonPath.parse(jsonDocument, Configuration.defaultConfiguration()));

 // Then
 Assertions.assertNull(result);
 }

 @Test
 @DisplayName("Test replace method with empty input and json document")
 public void testReplaceMethodWithEmptyInputAndJsonDocument() {
 // Given
 String jsonDocument = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
 Map<String, Object> input = new HashMap<>();

 // When
 Object result = parametersUtils.replace(input, JsonPath.parse(jsonDocument, Configuration.defaultConfiguration()));

 // Then
 Assertions.assertNotNull(result);
 Assertions.assertEquals(input, result);
 }

 @Test
 @DisplayName("Test replaceVariables method with valid input and json document")
 public void testReplaceVariablesWithValidInputAndJsonDocument() {
 // Given
 String jsonDocument = "{\"name\": \"${name}\", \"age\": ${age}, \"cars\": [${cars[0]}]}";
 Map<String, Object> input = new HashMap<>();
 input.put("name", "John");
 input.put("age", 35);
 input.put("cars", Arrays.asList("Ford", "BMW", "Fiat"));

 // When
 Object result =
 parametersUtils.replaceVariables(jsonDocument, null, null, 0);

 // Then
 String expectedResult = "{\"name\": \"John\", \"age\": 35, \"cars\": [\"Ford\"]}";
 Assertions.assertEquals(expectedResult, result.toString());
 }

 @Test
 @DisplayName("Test replaceVariables method with invalid input and json document")
 public void testReplaceVariablesWithInvalidInputAndJsonDocument() {
 // Given
 String jsonDocument = "{\"name\": \"${name}\", \"age\": ${age}, \"cars\": [${cars[0]}]}";
 Map<String, Object> input = new HashMap<>();

 // When/Then
 Assertions.assertThrows(
             RuntimeException.class,
             () -> parametersUtils.replaceVariables(jsonDocument, null, null, 0));
 }

 @Test
 @DisplayName("Test getWorkflowInput method with valid input")
 public void testGetWorkflowInputWithValidInput() {
 // Given
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("key", "value");
 when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);

 // When
 Map<String, Object> result =
 parametersUtils.getWorkflowInput(workflowDef, Collections.emptyMap());

 // Then
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("key", "value");
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 @DisplayName("Test getWorkflowInput method with null inputTemplate")
 public void testGetWorkflowInputWithNullInputTemplate() {
 // Given

 // When
 Map<String, Object> result =
 parametersUtils.getWorkflowInput(workflowDef, Collections.emptyMap());

 // Then
 Assertions.assertNull(result);
 }

 @Test
 @DisplayName("Test getWorkflowInput method with empty inputTemplate")
 public void testGetWorkflowInputWithEmptyInputTemplate() {
 // Given
 when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());

 // When
 Map<String, Object> result =
 parametersUtils.getWorkflowInput(workflowDef, Collections.emptyMap());

 // Then
 Assertions.assertNull(result);
 }

 @Test
 @DisplayName("Test getTaskInputV1 method with valid input")
 public void testGetTaskInputV1WithValidInput() {
 // Given
 Map<String, Object> inputParams = Map.of("workflow.input.inputParam.name", "John");
 Map<String, Object> expectedResult = new LinkedHashMap<>();
 expectedResult.put("name", "John");
 expectedResult.put("input", workflowInput());

 // When
 Map<String, Object> result =
 parametersUtils.getTaskInputV1(workflowModel, inputParams);

 // Then
 Assertions.assertEquals(expectedResult, result);
 }

 // Add other test cases as needed for edge cases and error scenarios

 private void mapInputParams() {
 ReflectionTestUtils.setFieldValue(workflowModel, "input", Map.of("inputParam", Map.of("name", "John")));
 ReflectionTestUtils.setFieldValue(workflowModel, "tasks",
         List.of(createTaskModel("task1", Map.of("inputParam", Map.of("name", "John"))),
                 createTaskModel("task2", Map.of("outputParam", Map.of("name", "John")))));
 }

 private TaskDef createTaskDef() {
 return mock(TaskDef.class);
 }

 private TaskModel createTaskModel(String name, Map<String, Object> inputData) {
 TaskModel taskModel = mock(TaskModel.class);
 when(taskModel.getReferenceTaskName()).thenReturn(name);
 when(taskModel.getInputData()).thenReturn(inputData);
 return taskModel;
 }

 private WorkflowModel workflowModel() {
 return new WorkflowModel();
 }

 private Map<String, Object> workflowInput() {
 return Map.of("inputParam", Map.of("name", "John"));
 }

 private Map<String, Object> workflowParams() {
 Map<String, Object> params = new LinkedHashMap<>();
 params.put("input", workflowInput());
 return params;
 }
}