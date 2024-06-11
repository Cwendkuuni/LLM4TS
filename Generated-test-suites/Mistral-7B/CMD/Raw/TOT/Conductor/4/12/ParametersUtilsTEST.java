package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstance;

@Component
public class ParametersUtilsTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
 private static final Pattern PATTERN =
         Pattern.compile(
                 "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                 Pattern.DOTALL);

 @Mock
 private ObjectMapper objectMapper;
 @Mock
 private WorkflowModel workflow;
 @Mock
 private TaskDef taskDefinition;
 @Mock
 private WorkflowDef workflowDef;
 @InjectMocks
 private ParametersUtils utils;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetTaskInputV1() {
 // Given
 String workflowName = "TestWorkflow";
 String taskName = "TestTask";
 TaskModel task = new TaskModel();
 task.setTaskDefName("TestTaskDef");
 task.setInputData(new HashMap<>());
 task.getInputData().put("TestParam", "TestValue");
 WorkflowModel workflow = new WorkflowModel();
 workflow.setName(workflowName);
 workflow.setTasks(new LinkedList<>());
 workflow.getTasks().add(task);

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(null);
 when(workflow.getTaskByRefName(anyString())).thenReturn(task);

 // When
 Map<String, Object> result = utils.getTaskInput(null, workflow, taskDefinition, taskName);

 // Then
 assertNull(result);
 }

 @Test
 public void testGetTaskInputV1WithInputParams() {
 // Given
 String workflowName = "TestWorkflow";
 String taskName = "TestTask";
 TaskModel task = new TaskModel();
 task.setTaskDefName("TestTaskDef");
 task.setInputData(new HashMap<>());
 task.getInputData().put("TestParam", "TestValue");
 WorkflowModel workflow = new WorkflowModel();
 workflow.setName(workflowName);
 workflow.setTasks(new LinkedList<>());
 workflow.getTasks().add(task);
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("TestParam", "TestInputValue");

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputParams);
 when(workflow.getTaskByRefName(anyString())).thenReturn(task);

 // When
 Map<String, Object> result = utils.getTaskInput(null, workflow, taskDefinition, taskName);

 // Then
 assertEquals(inputParams, result);
 }

 @Test
 public void testGetTaskInputV1WithInputTemplate() {
 // Given
 String workflowName = "TestWorkflow";
 String taskName = "TestTask";
 TaskModel task = new TaskModel();
 task.setTaskDefName("TestTaskDef");
 task.setInputData(new HashMap<>());
 task.getInputData().put("TestParam", "TestValue");
 WorkflowModel workflow = new WorkflowModel();
 workflow.setName(workflowName);
 workflow.setTasks(new LinkedList<>());
 workflow.getTasks().add(task);
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("TestParam", "TestInputValue");

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(inputTemplate);
 when(workflow.getTaskByRefName(anyString())).thenReturn(task);
 when(taskDefinition.getInputTemplate()).thenReturn(inputTemplate);

 // When
 Map<String, Object> result = utils.getTaskInput(null, workflow, taskDefinition, taskName);

 // Then
 assertEquals(inputTemplate, result);
 }

 @Test
 public void testGetTaskInputV2() {
 // Given
 String workflowName = "TestWorkflow";
 String taskName = "TestTask";
 Map<String, Object> workflow = new HashMap<>();
 workflow.put("input", new HashMap<>());
 workflow.put("output", new HashMap<>());
 Map<String, Object> input = new HashMap<>();
 input.put("TestTask", new HashMap<>());
 input.put("TestTask.TestParam", "TestValue");
 workflow.put("input", input);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setName(workflowName);
 workflowModel.setInput(workflow);
 List<WorkflowInstance> tasks = new LinkedList<>();
 WorkflowInstance taskInstance = new WorkflowInstance();
 taskInstance.setTask(new TaskModel());
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("TestParam", "TestInputValue");
 taskInstance.setInputData(taskInput);
 tasks.add(taskInstance);
 WorkflowModel expectedWorkflow = new WorkflowModel();
 expectedWorkflow.setName(workflowName);
 expectedWorkflow.setInput(workflow);
 expectedWorkflow.setTasks(tasks);

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(expectedWorkflow);
 when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);
 when(taskDefinition.getInputTemplate()).thenReturn(Collections.emptyMap());

 // When
 Map<String, Object> result = utils.getTaskInput(new HashMap<>(), workflowModel, taskDefinition, taskName);

 // Then
 assertEquals(expectedWorkflow.getInput().get("input"), result);
 }

 @Test
 public void testGetTaskInputV2WithInputTemplate() {
 // Given
 String workflowName = "TestWorkflow";
 String taskName = "TestTask";
 Map<String, Object> workflow = new HashMap<>();
 workflow.put("input", new HashMap<>());
 workflow.put("output", new HashMap<>());
 Map<String, Object> input = new HashMap<>();
 input.put("TestTask", new HashMap<>());
 input.put("TestTask.TestParam", "TestValue");
 workflow.put("input", input);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setName(workflowName);
 workflowModel.setInput(workflow);
 List<WorkflowInstance> tasks = new LinkedList<>();
 WorkflowInstance taskInstance = new WorkflowInstance();
 taskInstance.setTask(new TaskModel());
 taskInstance.setInputData(new HashMap<>());
 taskInstance.getInputData().put("TestParam", "TestInputValue");
 tasks.add(taskInstance);
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("TestParam", "TestInputTemplateValue");
 WorkflowModel expectedWorkflow = new WorkflowModel();
 expectedWorkflow.setName(workflowName);
 expectedWorkflow.setInput(workflow);
 expectedWorkflow.setTasks(tasks);
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put("TestParam", "TestTaskInputTemplateValue");
 expectedInput.putAll(inputTemplate);
 Map<String, Object> expectedTaskInput = new HashMap<>();
 expectedTaskInput.put("TestParam", "TestInputValue");
 expectedTaskInput.putAll(inputTemplate);
 WorkflowDef expectedWorkflowDef = new WorkflowDef();
 expectedWorkflowDef.setInputTemplate(expectedInput);
 expectedWorkflowDef.setTasks(Collections.singletonList(expectedTaskInput));
 WorkflowDef workflowDef = mock(WorkflowDef.class);
 when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);
 when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(workflowModel);

 // When
 Map<String, Object> result = utils.getTaskInput(new HashMap<>(), workflowModel, taskDefinition, taskName);

 // Then
 assertEquals(expectedTaskInput, result);
 }

 @Test
 public void testGetWorkflowInput() {
 // Given
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("TestParam", "TestValue");
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setInputTemplate(inputTemplate);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefinition(workflowDef);
 Map<String, Object> input = new HashMap<>();
 input.put("TestParam", "TestInputValue");

 // When
 Map<String, Object> result = utils.getWorkflowInput(workflowDef, input);

 // Then
 assertEquals(inputTemplate, result);
 }

 @Test
 public void testReplaceVariables() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "${workflow.input.TestParam}";
 String output = "${workflow.input.TestParam}";
 String taskId = "TestTaskId";

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(workflow);
 when(documentContext.read(anyString())).thenThrow(new PathNotFoundException());
 when(documentContext.read(eq("$..TestParam"))).thenReturn("TestValue");
 when(documentContext.read(eq("$..TestParam2"))).thenReturn(null);

 // When
 Object replacement = utils.replaceVariables(input, documentContext, taskId);

 // Then
 assertEquals("TestValue", replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyString() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "${workflow.input.TestParam}";
 String output = "${workflow.input.TestParam}";
 String taskId = "TestTaskId";

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(workflow);
 when(documentContext.read(anyString())).thenThrow(new PathNotFoundException());
 when(documentContext.read(eq("$..TestParam"))).thenReturn("");

 // When
 Object replacement = utils.replaceVariables(input, documentContext, taskId);

 // Then
 assertEquals("", replacement);
 }

 @Test
 public void testReplaceVariablesWithNull() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "${workflow.input.TestParam}";
 String taskId = "TestTaskId";

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(documentContext.read(eq("$..TestParam"))).thenReturn("TestValue");
 when(documentContext.read(anyString())).thenThrow(new PathNotFoundException());
 when(documentContext.read(anyString())).thenReturn(null);

 // When
 Object replacement = utils.replaceVariables(input, documentContext, taskId);

 // Then
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithEnvironmentVariable() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "${env.TEST_VARIABLE}";
 String taskId = "TestTaskId";
 String envVariable = "TestValue";

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(documentContext.read(anyString())).thenThrow(new PathNotFoundException());
 when(EnvUtils.getSystemParametersValue(eq("TEST_VARIABLE"), anyString())).thenReturn(envVariable);

 // When
 Object replacement = utils.replaceVariables(input, documentContext, taskId);

 // Then
 assertEquals(envVariable, replacement);
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceVariablesWithException() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "${workflow.input.TestParam}";
 String taskId = "TestTaskId";

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException("TestError"));
 when(documentContext.read(anyString())).thenThrow(new PathNotFoundException());

 // When, Then
 utils.replaceVariables(input, documentContext, taskId);
 }

 @Test
 public void testReplaceVariablesNested() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "test.$..TestParam";
 String taskId = "TestTaskId";
 Map<String, Object> expected = new HashMap<>();
 expected.put("TestParam", "TestValue");

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(documentContext.read(anyString())).thenThrow(new PathNotFoundException());
 when(documentContext.read(eq("$..TestParam"))).thenReturn("TestValue");

 // When
 Object replacement = utils.replaceVariables(input, documentContext, taskId);

 // Then
 assertEquals(expected.get("TestParam"), replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyJson() {
 // Given
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 DocumentContext documentContext = mock(DocumentContext.class);
 String input = "Test.";
 String taskId = "TestTaskId";

 when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new IOException());
 when(documentContext.read(anyString())).thenReturn(null);

 // When
 Object replacement = utils.replaceVariables(input, documentContext, taskId);

 // Then
 assertNull(replacement);
 }
}