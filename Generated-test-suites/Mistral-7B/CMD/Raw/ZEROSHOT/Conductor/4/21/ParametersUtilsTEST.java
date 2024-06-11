package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowTask;
import com.netflix.conductor.utils.ParametersUtils;
import com.netflix.conductor.utils.Utils;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaFactory"})
@PrepareForTest({ParametersUtils.class})
public class ParametersUtilsTest {

 private static final ObjectMapper objectMapper = mock(ObjectMapper.class);
 private static final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

 @Test
 public void testGetTaskInput() throws Exception {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowDef workflowDef = mock(WorkflowDef.class);
 TaskDef taskDef = mock(TaskDef.class);
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "testValue");
 when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey", "testValue2"));
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey2", "testValue3"));
 when(workflowDef.getSchemaVersion()).thenReturn(1L);
 WorkflowModel expectedWorkflowModel = new WorkflowModel();
 expectedWorkflowModel.setWorkflowDefinition(workflowDef);
 expectedWorkflowModel.setTasks(Collections.singletonList(new TaskModel()));
 Map<String, Object> expectedTaskInput = parametersUtils.getTaskInput(inputParams, expectedWorkflowModel, taskDef, "testTaskId");
 assertNotNull(expectedTaskInput);
 assertNull(expectedTaskInput.get("testKey2"));
 assertNull(expectedTaskInput.get("testKey2"));
 assertNotNull(expectedTaskInput.get("testKey"));
 assertNotNull(expectedTaskInput.get("testKey2"));
 assertEquals("testValue", expectedTaskInput.get("testKey"));
 assertEquals("testValue2", expectedTaskInput.get("testKey"));
 }

 @Test
 public void testGetTaskInputV2() throws Exception {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowDef workflowDef = mock(WorkflowDef.class);
 TaskDef taskDef = mock(TaskDef.class);
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "testValue");
 when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey", "testValue2"));
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey2", "testValue3"));
 when(workflowDef.getSchemaVersion()).thenReturn(2L);
 WorkflowModel expectedWorkflowModel = new WorkflowModel();
 expectedWorkflowModel.setWorkflowDefinition(workflowDef);
 expectedWorkflowModel.setTasks(Collections.singletonList(new TaskModel()));
 Map<String, Object> expectedTaskInput = parametersUtils.getTaskInput(inputParams, expectedWorkflowModel, taskDef, "testTaskId");
 assertNotNull(expectedTaskInput);
 assertNull(expectedTaskInput.get("testKey2"));
 assertNull(expectedTaskInput.get("testKey2"));
 assertNotNull(expectedTaskInput.get("testKey"));
 assertNotNull(expectedTaskInput.get("testKey"));
 assertEquals("testValue", expectedTaskInput.get("testKey"));
 assertEquals("testValue", expectedTaskInput.get("testKey"));
 }

 @Test
 public void testReplaceVariables() throws Exception {
 String jsonString = "{\"workflow\": {\"input\": {\"testKey\": \"$input.testKey\"}, \"output\": {}}, \"tasks\": [{\"inputData\": {}, \"outputData\": {}}]}";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 PowerMockito.whenNew(DocumentContext.class).withArguments(new JacksonJsonProvider(), jsonString).thenReturn(documentContext);
 String taskId = "testTaskId";
 Object replacement = "testReplacement";
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", replacement);
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("workflow", expectedWorkflow());
 expectedResult.put("tasks", tasksList(new TaskModel()));
 expectedResult.put("tasks[0]", task("inputData", expectedInputData()));
 expectedResult.put("tasks[0].inputData", expectedInputData());
 String expectedResultJsonString =
             "{\"workflow\":{\"input\":{\"testKey\":\"testReplacement\"},\"output\":{}},"
             + "\"tasks\":[{\"inputData\":{\"testKey\":\"testReplacement\"},\"outputData\":{}}]}";
 Object result = parametersUtils.replace(inputParams, documentContext, taskId);
 assertNotNull(result);
 assertEquals(expectedResultJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithoutDocumentContext() throws Exception {
 String jsonString = "{\"workflow\": {\"input\": {\"testKey\": \"$input.testKey\"}, \"output\": {}}, \"tasks\": [{\"inputData\": {}, \"outputData\": {}}]}";
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "testReplacement");
 String expectedResultJsonString =
             "{\"workflow\":{\"input\":{\"testKey\":\"testReplacement\"},\"output\":{}},"
             + "\"tasks\":[{\"inputData\":{},\"outputData\":{}}]}";
 Object result = parametersUtils.replace(inputParams, jsonString, taskId);
 assertNotNull(result);
 assertEquals(expectedResultJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() throws Exception {
 String jsonString = "{\"workflow\": {\"input\": {\"testKey\": \"$input.testKey\"}, \"output\": {}}, \"tasks\": [{\"inputData\": {}, \"outputData\": {}}]}";
 DocumentContext documentContext = null;
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "testReplacement");
 Object result = parametersUtils.replace(inputParams, documentContext, taskId);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithEnvironmentVariable() throws Exception {
 String jsonString = "{\"workflow\": {\"input\": {\"testKey\": \"$env.TEST_VARIABLE\"}, \"output\": {}}, \"tasks\": [{\"inputData\": {}, \"outputData\": {}}]}";
 String testVariable = "testValue";
 System.setProperty("TEST_VARIABLE", testVariable);
 Map<String, Object> inputParams = new HashMap<>();
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("workflow", expectedWorkflow());
 expectedResult.put("tasks", tasksList(new TaskModel()));
 String expectedResultJsonString =
             "{\"workflow\":{\"input\":{\"testKey\":\"" + testVariable + "\"},\"output\":{}},"
             + "\"tasks\":[{\"inputData\":{},\"outputData\":{}}]}";
 Object result = parametersUtils.replace(inputParams, jsonString, taskId);
 assertNotNull(result);
 assertEquals(expectedResultJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithNullInput() throws Exception {
 Map<String, Object> inputParams = null;
 String jsonString = "{\"workflow\": {\"input\": {\"testKey\": \"$input.testKey\"}, \"output\": {}}, \"tasks\": [{\"inputData\": {}, \"outputData\": {}}]}";
 Object result = parametersUtils.replace(inputParams, jsonString, taskId);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithEmptyInput() throws Exception {
 Map<String, Object> inputParams = new HashMap<>();
 String jsonString = "{\"workflow\": {\"input\": {\"testKey\": \"$input.testKey\"}, \"output\": {}}, \"tasks\": [{\"inputData\": {}, \"outputData\": {}}]}";
 Object result = parametersUtils.replace(inputParams, jsonString, taskId);
 assertNotNull(result);
 assertEquals("{}", Objects.toString(result));
 }

 private Map<String, Object> expectedWorkflow() {
 Map<String, Object> expectedWorkflow = new HashMap<>();
 expectedWorkflow.put("input", inputData());
 expectedWorkflow.put("output", outputData());
 return expectedWorkflow;
 }

 private Map<String, Object> inputData() {
 Map<String, Object> inputData = new HashMap<>();
 inputData.put("testKey", "testValue");
 return inputData;
 }

 private Map<String, Object> outputData() {
 Map<String, Object> outputData = new HashMap<>();
 return outputData;
 }

 private List<TaskModel> tasksList(TaskModel taskModel) {
 List<TaskModel> tasksList = new LinkedList<>();
 tasksList.add(taskModel);
 return tasksList;
 }

 private TaskModel task(String key, Map<String, Object> value) {
 Map<String, Object> inputData = new HashMap<>();
 inputData.put(key, value);
 return new TaskModel() {{setInputData(inputData);}};
 }

 private Map<String, Object> expectedInputData() {
 Map<String, Object> expectedInputData = new HashMap<>();
 expectedInputData.put("testKey", "testValue");
 return expectedInputData;
 }
}