package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jmock.lib.legacy.ClassImposterizer;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceStatus;

/** Tests for the {@link ParametersUtils} class. */
public class ParametersUtilsTest extends MockObjectTestCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
  private static final Pattern PATTERN = Pattern.compile("(?=(?<!\\$)\\$\\{)(.*?)(?=.*?\\}\\})(.*)");

  private ObjectMapper objectMapper;
  private TypeReference<Map<String, Object>> map;
  private Configuration jsonPathConfig;
  private DocumentContext documentContext;
  private ParametersUtils parametersUtils;
  private WorkflowDef workflowDef;
  private TaskDef taskDef;
  private WorkflowModel workflowModel;
  private Map<String, Object> inputParams;

  @Before
  public void setUp() {
    objectMapper = mock(ObjectMapper.class);
    map = new TypeReference<>() {};
    jsonPathConfig = new Configuration.Builder().addOptions(Option.SUPPRESS_EXCEPTIONS).build();
    documentContext = JsonPath.using(jsonPathConfig);
    when(objectMapper.readValue(anyString(), eq(Map.class))).then(invocation -> mapper.readValue(invocation.getArgument(0), map));
    when(objectMapper.writeValueAsBytes(anyMap())).then(invocation -> mapper.writeValueAsBytes(invocation.getArgument(0)));

    parametersUtils = new ParametersUtils(objectMapper);

    workflowDef = new WorkflowDef();
    workflowDef.setInputTemplate(Collections.singletonMap("inputParam", "{{workflow.input.inputParam}}"));

    taskDef = new TaskDef();
    taskDef.setInputTemplate(Collections.singletonMap("taskInputParam", "{{task.input.inputParam}}"));

    workflowModel = new WorkflowModel();
    workflowModel.setWorkflowDefinition(workflowDef);
    workflowModel.setTasks(Collections.singletonList(new TaskModel()));

    inputParams = new HashMap<>();
  }

  @Test
  public void testGetTaskInput() {
    // Test with null input, workflow and taskDefinition
    Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, null, null);
    assertEquals(Collections.emptyMap(), result);

    // Test with null input and valid workflow and taskDefinition
    when(workflowDef.getSchemaVersion()).thenReturn(1);
    when(workflowModel.getInput()).thenReturn(Collections.singletonMap("inputParam", "inputValue"));

    result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
    assertEquals("inputValue", result.get("inputParam"));

    // Test with valid input, workflow and taskDefinition
    inputParams.put("inputParam1", "inputValue1");
    when(workflowDef.getSchemaVersion()).thenReturn(2);
    when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("inputParam2", "{{task.input.inputParam}}"));
    when(workflowModel.getInput()).thenReturn(Collections.singletonMap("inputParam1", "inputValue1"));

    result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
    assertEquals("inputValue1", result.get("inputParam1"));
    assertEquals("inputValue1", result.get("inputParam2"));
  }

  @Test
  public void testReplace() {
    String inputString = "${workflow.input.inputParam1}.${task.output.outputParam1}";
    Map<String, Object> inputMap = new HashMap<>();
    inputMap.put("workflow", mapOf("input", mapOf("inputParam1", "input1")));
    inputMap.put("task", mapOf("output", mapOf("outputParam1", "output1")));

    String result = parametersUtils.replace(inputMap, documentContext, null);
    assertEquals("input1.output1", result);

    inputString = "${workflow.input.inputParam1}.${task.output.outputParam1}.${workflow.input.inputParam2}";
    inputMap.put("inputParams2", "input2");

    result = parametersUtils.replace(inputMap, documentContext, null);
    assertEquals("input1.output1.input2", result);

    result = parametersUtils.replace("${inputParam}", documentContext, null);
    assertEquals(null, result);

    inputMap.put("inputParams2", mapOf("inputParam", "input2"));

    result = parametersUtils.replace("${inputParam}", documentContext, null);
    assertEquals("input2", result);
  }

  @Test
  public void testReplaceVariables() {
    String inputString = "${workflow.input.inputParam}";
    Map<String, Object> workflowInput = mapOf("input", mapOf("inputParam", "inputValue"));
    Map<String, Object> inputMap = mapOf("workflow", workflowInput);

    String result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("inputValue", result);

    inputString = "${workflow.input.inputParam}.${task.output.outputParam}";
    WorkflowModel workflowModel = new WorkflowModel();
    workflowModel.setWorkflowDefinition(workflowDef);
    workflowModel.setTasks(Collections.singletonList(new TaskModel()));
    when(workflowModel.getInput()).thenReturn(workflowInput);

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("inputValue.output1", result);

    inputString = "${workflow.input.inputParam}.${task.output.outputParam}.${workflow.input.inputParam2}";
    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("inputParam2", "input2");
    when(workflowModel.getInput()).thenReturn(mapOf("inputParam1", "input1", "inputParam2", "input2"));

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("inputValue.output1.input2", result);

    inputString = "${workflow.input.inputParam}.${workflow.input.inputParam2}";
    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("inputValue.input2", result);

    inputString = "${workflow.input.inputParam}.${workflow.input.inputParam2}.${workflow.input.inputParam3}";
    Map<String, Object> inputParams2 = new HashMap<>();
    inputParams2.put("inputParam3", "input3");
    when(workflowModel.getInput()).thenReturn(mapOf("inputParam1", "input1", "inputParam2", "input2", "inputParam3", "input3"));

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("inputValue.input2.input3", result);

    inputString = "${env.VAR1}";
    String envVar = "VAR1";
    String envValue = "envValue";
    when(EnvUtils.getSystemParametersValue(eq(envVar), anyString())).thenReturn(envValue);

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("envValue", result);

    inputString = "${env.VAR1}.${env.VAR2}";
    String envVar2 = "VAR2";
    String envValue2 = "envValue2";
    when(EnvUtils.getSystemParametersValue(eq(envVar), anyString())).thenReturn(envValue);
    when(EnvUtils.getSystemParametersValue(eq(envVar2), anyString())).thenReturn(envValue2);

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals(envValue + "." + envValue2, result);

    inputString = "${workflow.status}";
    when(workflowModel.getStatus()).thenReturn(WorkflowInstanceStatus.COMPLETED);

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("COMPLETED", result);

    inputString = "${workflow.status}.${workflow.input.inputParam}";
    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("COMPLETED.inputValue", result);

    inputString = "${task.status}";
    when(workflowModel.getTasks().get(0).getStatus()).thenReturn(WorkflowInstanceStatus.FAILED);

    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("FAILED", result);

    inputString = "${task.status}.${task.output.outputParam}";
    result = parametersUtils.replaceVariables(inputString, documentContext, null);
    assertEquals("FAILED.output1", result);
  }

  @Test
  public void testReplaceList() {
    List<Object> values = new LinkedList<>();
    values.add("value1");
    values.add("value2");

    List<Object> replacedList =
        parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, replacedList);

    String inputString = "${[${workflow.input.inputParam}]}}";
    Map<String, Object> inputMap = mapOf("input", mapOf("inputParam", values));

    List<Object> result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);

    inputString = "${[${workflow.input.inputParam[0]}]}.${[${workflow.input.inputParam[1]}]}";
    inputMap.put("inputParams", mapOf("inputParam", values));

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(Collections.singletonList(mapOf("value1", "value1")), result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(Collections.singletonList(mapOf("value2", "value2")), result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);

    inputString = "${[${workflow.input.inputParam[1]}]}.${[${workflow.input.inputParam[0]}]}";
    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(Collections.singletonList(mapOf("value2", "value2")), result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(Collections.singletonList(mapOf("value1", "value1")), result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);

    inputString = "${[${workflow.input.inputParam[0]}.${workflow.input.inputParam[1]}]}.${[${workflow.input.inputParam[1]}.${workflow.input.inputParam[0]}]}";
    inputMap.put("inputParams2", mapOf("inputParam", values));

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(Collections.singletonList(mapOf("value1.value1", "value1.value1")), result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(Collections.singletonList(mapOf("value2.value2", "value2.value2")), result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);

    result = parametersUtils.replaceList(values, null, documentContext);
    assertEquals(values, result);
  }

  @Test
  public void testGetWorkflowInput() {
    Map<String, Object> inputParams = mapOf("inputParam1", "inputValue1", "inputParam2", "inputValue2");
    Map<String, Object> workflowInput = mapOf("inputParam1", "workflowInputValue1", "inputParam2", "workflowInputValue2");

    Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
    assertEquals(workflowInput, result);

    inputParams.put("inputParam3", "inputValue3");

    result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
    assertEquals(workflowInput, result);

    workflowDef.setInputTemplate(mapOf("inputParam3", "{{workflow.input.inputParam3}}"));

    result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
    assertEquals(mapOf("inputParam3", "inputValue3"), result);
  }

  @Test
  public void testGetTaskInputV1() {
    Map<String, Object> inputParams = mapOf("inputParam1", "inputValue1");
    WorkflowModel workflowModel = new WorkflowModel();
    TaskModel taskModel = new TaskModel();
    taskModel.setInputData(mapOf("inputParam2", "taskInputValue1"));

    workflowModel.setTasks(Collections.singletonList(taskModel));
    workflowModel.setInput(mapOf("inputParam1", "workflowInputValue1"));

    Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);
    assertEquals("workflowInputValue1.taskInputValue1.inputParam2", result.get("inputParam2"));

    inputParams.put("inputParam3", "inputValue3");

    result = parametersUtils.getTaskInputV1(workflowModel, inputParams);
    assertEquals("workflowInputValue1.taskInputValue1.inputParam3", result.get("inputParam3"));
  }

  private static Map<String, Object> mapOf(Object... keyValue) {
    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < keyValue.length; i += 2) {
      map.put(keyValue[i].toString(), keyValue[i + 1]);
    }
    return map;
  }

  private static Mockery mockery = new ClassImposterizer();
  private static Mapper mapper = new Mapper(mockery.mock(ObjectMapper.class));
}