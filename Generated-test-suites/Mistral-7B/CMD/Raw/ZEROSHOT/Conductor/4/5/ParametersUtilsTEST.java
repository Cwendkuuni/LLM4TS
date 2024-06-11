package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.Configuration;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

 @InjectMocks
 private ParametersUtils parametersUtils;
 private ObjectMapper objectMapper;
 private WorkflowDef workflowDef;
 private WorkflowModel workflowModel;
 private TaskDef taskDef;
 private Map<String, Object> inputParams;
 private String jsonString;
 private DocumentContext documentContext;

 @Before
 public void setup() {
 objectMapper = Mockito.spy(new ObjectMapper());
 workflowDef = Mockito.mock(WorkflowDef.class);
 workflowModel = Mockito.mock(WorkflowModel.class);
 taskDef = Mockito.mock(TaskDef.class);
 inputParams = new HashMap<>();
 jsonString = "{\"name\":\"John Doe\", \"age\":30, \"tasks\":[{\"name\":\"Task1\", \"input\":{\"param1\":\"value1\"}}]}";
 documentContext = JsonPath.using(Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS))
 .parse(jsonString);
 }

 @Test
 public void testGetTaskInput() {
 // Given
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("name", "John Doe");
 expectedResult.put("age", 30);
 expectedResult.put("tasks", Collections.singletonList(Collections.singletonMap("Task1", Collections.singletonMap("param1", "value1"))));
 when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1L);
 when(workflowModel.getInput()).thenReturn(Collections.singletonMap("name", "John Doe"));
 when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskDef));
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("param1", "input.age"));
 when(taskDef.getInputData()).thenReturn(Collections.singletonMap("param1", "25"));
 when(objectMapper.readValue(Mockito.anyByte[].class, Mockito.eq(mapType))).thenReturn(expectedResult);

 Map<String, Object> result = parametersUtils.getTaskInput(
 inputParams, workflowModel, taskDef, "taskId");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputV2() {
 // Given
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("name", "John Doe");
 expectedResult.put("age", 30);
 expectedResult.put("tasks", Collections.singletonList(Map.of("Task1", Map.of("param1", "value1"))));
 Map<String, Object> inputMap = new HashMap<>();
 inputMap.put("workflow", Map.of("input", Map.of("name", "John Doe"), "tasks", Collections.singletonList(taskDef)));
 inputMap.put("Task1", Map.of("input", Map.of("param1", "value1"), "output", Map.of("param1", "outputValue1")));

 when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2L);
 when(workflowModel.getInput()).thenReturn(Map.of());
 when(workflowModel.getTasks())
 .thenReturn(Collections.singletonList(Map.of("name", "Task1", "inputData", Map.of("param1", "value1"))));
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("param1", "$..tasks[?(@.name=='Task1')].output.param1"));
 when(documentContext.read(Mockito.anyString())).thenReturn("outputValue1");

 Map<String, Object> result =
 parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace() {
 // Given
 String expectedResult = "replacedValue";
 when(documentContext.read(Mockito.anyString())).thenReturn("originalValue");

 String result =
 parametersUtils.replace("${workflow.tasks[?(@.name=='Task1')].output.param1}", documentContext, "taskId");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplaceList() {
 // Given
 List<Object> expectedResult = new LinkedList<Object>() {{
 add("replacedValue1");
 add("replacedValue2");
 }};
 List<Object> input = new LinkedList<Object>() {{
 add("${workflow.tasks[?(@.name=='Task1')].output.param1}");
 add("${workflow.tasks[?(@.name=='Task2')].output.param1}");
 }};

 when(documentContext.read(Mockito.anyString())).thenAnswer(invocation -> "replacedValue");

 List<Object> result =
 parametersUtils.replaceList(input, "taskId", documentContext);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplaceVariables() {
 // Given
 String expectedResult = "replacedValue";
 String json = "{\"name\":\"${workflow.tasks[?(@.name=='Task1')].output.param1}\", \"age\":30}";

 String result =
 parametersUtils.replaceVariables(json, documentContext, "taskId");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputV1() {
 // Given
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("name", "John Doe");
 expectedResult.put("age", 30);
 expectedResult.put("tasks", Collections.singletonList(Map.of("name", "Task1", "input", Map.of("param1", "value1"))));
 when(workflowModel.getInput()).thenReturn(Map.of("name", "John Doe"));
 when(workflowModel.getTasks())
 .thenReturn(Collections.singletonList(Map.of("name", "Task1", "inputData", Map.of("param1", "value1"))));

 Map<String, Object> result =
 parametersUtils.getTaskInputV1(workflowModel, inputParams);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetWorkflowInput() {
 // Given
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("name", "John Doe");
 expectedResult.put("age", 30);
 expectedResult.put("tasks", Collections.singletonList(Map.of("name", "Task1", "input", Map.of("param1", "value1"))));
 when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("param1", "input.age"));
 when(objectMapper.readValue(Mockito.anyByte[].class, Mockito.eq(mapType)))
 .thenReturn(expectedResult);

 Map<String, Object> result =
 parametersUtils.getWorkflowInput(workflowDef, inputParams);
 assertEquals(expectedResult, result);
 }
}