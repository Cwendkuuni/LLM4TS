package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class ParametersUtilsTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtils.class);
 private static final ObjectMapper objectMapper = new ObjectMapper();
 private static final Configuration configuration =
         Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);

 private WorkflowModel workflow;
 private TaskDef taskDefinition;
 private Map<String, Object> inputParams;

 @Before
 public void setup() {
 workflow = new WorkflowModel();
 workflow.setWorkflowDefinition(new WorkflowDef());
 workflow.getWorkflowDefinition().setSchemaVersion(1);
 workflow.setWorkflowName("testWorkflow");
 workflow.setWorkflowId("testWorkflowId");
 workflow.setCorrelationId("testCorrelationId");
 workflow.setVariables(Collections.singletonMap("testVariable", "testValue"));
 workflow.setInput(new HashMap<>());
 workflow.getInput().put("testKey", "testValue");

 taskDefinition = new TaskDef();
 taskDefinition.setTaskName("testTask");
 taskDefinition.setTaskType("testType");
 taskDefinition.setInputTemplate(new HashMap<>());
 taskDefinition.getInputTemplate().put("testKey", "${'testVariable'}");
 }

 @Test
 public void testGetTaskInputV1() {
 inputParams = new HashMap<>();
 inputParams.put("testKey", "testInputValue");
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put("testKey", "testInputValue");
 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, null);
 assertEquals(expectedInput, result);
 }

 @Test
 public void testGetTaskInputV2() {
 inputParams = new HashMap<>();
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put("testKey", "testValue");
 workflow.setWorkflowDefinition(new WorkflowDef());
 workflow.getWorkflowDefinition().setSchemaVersion(2);
 Map<String, Object> result =
         new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, null);
 assertEquals(expectedInput, result);
 }

 @Test
 public void testReplaceVariables() {
 String paramString = "${testVariable}";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);
 String expected = "testValue";
 Object result = new ParametersUtils(objectMapper).replaceVariables(paramString, documentContext, null);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() {
 String paramString = "${testVariable}";
 Object result = new ParametersUtils(objectMapper).replaceVariables(paramString, null, null);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContext() {
 String paramString = "${testVariable}";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);
 Object result = new ParametersUtils(objectMapper).replaceVariables(paramString, documentContext, null);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithNoMatch() {
 String paramString = "testString";
 DocumentContext documentContext = JsonPath.parse(new HashMap<>(), configuration);
 Object result = new ParametersUtils(objectMapper).replaceVariables(paramString, documentContext, null);
 assertEquals(paramString, result.toString());
 }

 @Test
 public void testReplaceVariablesWithEnvironmentVariable() {
 String paramString = "${TEST_VARIABLE}";
 Map<String, String> systemEnvironment = new HashMap<>();
 systemEnvironment.put("TEST_VARIABLE", "testEnvironmentValue");
 DocumentContext documentContext = JsonPath.parse(new HashMap<>(), configuration);
 EnvUtils envUtils = new EnvUtils();
 envUtils.setSystemParameters(systemEnvironment);
 Object result = new ParametersUtils(objectMapper).replaceVariables(paramString, documentContext, null);
 assertEquals("testEnvironmentValue", result);
 }

 @Test
 public void testReplaceVariablesWithJsonPath() {
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowDefinition(new WorkflowDef());
 workflow.getWorkflowDefinition().setSchemaVersion(2);
 workflow.setWorkflowName("testWorkflow");
 workflow.setWorkflowId("testWorkflowId");
 workflow.setCorrelationId("testCorrelationId");
 workflow.setVariables(new HashMap<>());
 workflow.getVariables().put("testVariable", "testValue");
 workflow.setInput(new HashMap<>());
 workflow.getInput().put("testKey", "testValue");

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "${testVariable}");
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put("testKey", "testValue");
 DocumentContext documentContext = JsonPath.parse(workflow, configuration);
 Object result = new ParametersUtils(objectMapper).replaceVariables(paramString, documentContext, null);
 assertEquals(expectedInput, result);
 }

 @Test
 public void testReplaceList() {
 List<Object> values = Arrays.asList("${testVariable}", 1, 2, new HashMap<>());
 String taskId = "testTaskId";
 DocumentContext documentContext = JsonPath.parse(new HashMap<>(), configuration);
 Object result = new ParametersUtils(objectMapper).replaceList(values, taskId, documentContext);
 List<Object> expectedList = Arrays.asList("testValue", 1, 2, new HashMap<>());
 assertEquals(expectedList, result);
 }

 @Test
 public void testReplaceMap() {
 Map<String, Object> input = new HashMap<>();
 input.put("testKey1", "${testVariable1}");
 input.put("testKey2", "${testVariable2}");
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put("testKey1", "testValue1");
 expectedInput.put("testKey2", "testValue2");
 DocumentContext documentContext = JsonPath.parse(new HashMap<>(), configuration);
 Object result = new ParametersUtils(objectMapper).replace(input, documentContext, null);
 assertEquals(expectedInput, result);
 }

 @Test
 public void testGetTaskInputWithNullWorkflow() {
 inputParams = new HashMap<>();
 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, null, taskDefinition, null);
 assertNull(result);
 }

 @Test
 public void testGetTaskInputWithNullTaskDefinition() {
 inputParams = new HashMap<>();
 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, null, null);
 assertNull(result);
 }

 @Test
 public void testGetTaskInputWithNullTaskId() {
 inputParams = new HashMap<>();
 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, null);
 assertNotNull(result);
 }

 @Test
 public void testGetWorkflowInput() {
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put("testKey", "testValue");
 workflow.setInput(new HashMap<>());
 workflow.getInput().put("testKey", "testValue");
 Map<String, Object> result = new ParametersUtils(objectMapper).getWorkflowInput(workflow, null);
 assertEquals(expectedInput, result);
 }
}