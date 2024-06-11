package com.netflix.conductor.core.utils.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/** Test class for ParametersUtils */
public class ParametersUtilsTest {

  private ParametersUtils utils;
  private ObjectMapper objectMapper;
  private WorkflowDef workflowDef;
  private TaskDef taskDef;
  private WorkflowModel workflowModel;

  @Before
  public void setup() {
    // Initialize mocks
    objectMapper = mock(ObjectMapper.class);
    workflowDef = mock(WorkflowDef.class);
    taskDef = mock(TaskDef.class);
    workflowModel = mock(WorkflowModel.class);
    when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
    when(taskDef.getInputTemplate()).thenReturn(Collections.emptyMap());
    when(workflowDef.getSchemaVersion()).thenReturn(1);
    when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
    when(workflowModel.getTasks()).thenReturn(Collections.emptyList());
    when(workflowModel.getInput()).thenReturn(Collections.emptyMap());
    utils = new ParametersUtils(objectMapper);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetTaskInputV1() {
    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("workflow.input.param1", "value1");
    inputParams.put("task1.input.param2", "value2");

    WorkflowModel workflowModel = new WorkflowModel();
    workflowModel.setInput(Collections.singletonMap("param1", "value1"));
    TaskModel taskModel = new TaskModel();
    taskModel.setInputData(Collections.singletonMap("param2", "value2"));
    when(workflowModel.getTaskByRefName("task1")).thenReturn(taskModel);

    Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

    assertEquals("value1", result.get("workflow.input.param1"));
    assertEquals("value2", result.get("task1.input.param2"));
  }

  @Test
  public void testGetTaskInputV2() {
    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("workflow.input.param1", "value1");
    inputParams.put("workflow.input.param2.$[0].input.param2", "value3");
    inputParams.put("task1.input.param2.$[0].input.param2", "value4");

    WorkflowModel workflowModel = new WorkflowModel();
    WorkflowDef workflowDef = new WorkflowDef();
    workflowDef.setSchemaVersion(2);
    workflowModel.setWorkflowDefinition(workflowDef);
    workflowModel.setInput(Collections.singletonMap("param1", "value1"));
    List<TaskModel> tasks = new LinkedList<>();
    TaskModel taskModel = new TaskModel();
    taskModel.setInputData(Collections.singletonMap("param2", "value3"));
    tasks.add(taskModel);
    workflowModel.setTasks(tasks);

    Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

    assertEquals("value1", result.get("workflow.input.param1"));
    assertEquals("value3", result.get("workflow.input.param2.[0].input.param2"));
    assertEquals("value4", result.get("task1.input.param2.[0].input.param2"));
  }

  @Test
  public void testReplaceVariables() throws IOException {
    String paramString = "${workflow.input.param1}.${workflow.input.param2}.${task1.input.param2}";

    Map<String, Object> documentContext = new HashMap<>();
    documentContext.put("workflow.input.param1", "workflowValue1");
    documentContext.put("workflow.input.param2.param3", "workflowValue2");
    documentContext.put("task1.input.param2.param3", "taskValue1");

    String expectedResult = "workflowValue1.workflowValue2.taskValue1";

    Object result = utils.replaceVariables(paramString, documentContext, null);

    assertEquals(expectedResult, result);
  }

  @Test
  public void testReplaceVariablesWithEmptyString() throws IOException {
    String paramString = "${}";

    Map<String, Object> documentContext = new HashMap<>();

    Object result = utils.replaceVariables(paramString, documentContext, null);

    assertTrue(String.class.isInstance(result));
    assertEquals("", result);
  }

  @Test
  public void testReplaceVariablesWithNullDocumentContext() throws IOException {
    String paramString = "${workflow.input.param1}";

    Object result = utils.replaceVariables(paramString, null, null);

    assertTrue(String.class.isInstance(result));
    assertEquals("", result);
  }

  @Test
  public void testReplaceVariablesWithNullParamString() {
    Map<String, Object> documentContext = new HashMap<>();

    Object result = utils.replaceVariables(null, documentContext, null);

    assertTrue(String.class.isInstance(result));
    assertEquals("", result);
  }

  @Test
  public void testReplaceVariablesWithEmptyDocumentContext() {
    String paramString = "${workflow.input.param1}";

    Object result = utils.replaceVariables(paramString, Collections.emptyMap(), null);

    assertTrue(String.class.isInstance(result));
    assertEquals("", result);
  }

  @Test(expected = RuntimeException.class)
  public void testReplaceVariablesWithNullTaskId() {
    String paramString = "${workflow.input.param1}";

    Object result = utils.replaceVariables(paramString, new HashMap<>(), "nullTaskId");
  }

  @Test(expected = RuntimeException.class)
  public void testReplaceVariablesWithMalformedPattern() {
    String paramString = "${workflow.input.param1}${workflow.input.param2.$[0].input.param2}";

    Map<String, Object> documentContext = new HashMap<>();
    documentContext.put("workflow.input.param1", "workflowValue1");
    documentContext.put("workflow.input.param2.param3", "workflowValue2");

    try {
      Object result = utils.replaceVariables(paramString, documentContext, null);
      fail("Expected RuntimeException to be thrown");
    } catch (RuntimeException ex) {
      // Expected
    }
  }

  @Test(expected = RuntimeException.class)
  public void testReplaceVariablesWithInvalidEnvironmentVariable() {
    String paramString = "${workflow.input.env.invalidVariable}";

    Map<String, Object> documentContext = new HashMap<>();
    documentContext.put("workflow.input.env", new HashMap<>());

    try {
      Object result = utils.replaceVariables(paramString, documentContext, null);
      fail("Expected RuntimeException to be thrown");
    } catch (RuntimeException ex) {
      // Expected
    }
  }

  @Test
  public void testReplaceVariablesWithEnvironmentVariable() {
    String paramString = "${workflow.input.env.TEST_ENV_VAR}";
    String testEnvVar = "testValue";
    System.setProperty("TEST_ENV_VAR", testEnvVar);

    Map<String, Object> documentContext = new HashMap<>();
    documentContext.put("workflow.input.env", new HashMap<>());
    Object result = utils.replaceVariables(paramString, documentContext, null);

    assertEquals(testEnvVar, result);
  }
}