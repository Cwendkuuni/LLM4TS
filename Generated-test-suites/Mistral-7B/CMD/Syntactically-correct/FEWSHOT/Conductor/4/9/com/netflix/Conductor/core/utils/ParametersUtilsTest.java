package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;
import org.junit.Test;

@Component
public class ParametersUtilsTest {

 private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
 private Configuration config = Mockito.mock(Configuration.class);
 private DocumentContext documentContext = Mockito.mock(DocumentContext.class);

 @Test
 public void testGetTaskInput() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 TaskDef taskDefinition = Mockito.mock(TaskDef.class);
 Map<String, Object> inputParams = new HashMap<>();

 when(objectMapper.readValue(anyByteArray(), Map.class)).thenAnswer(i -> i.getArguments()[0]);
 when(config.addOptions(any(Option[].class))).thenReturn(config);
 when(JsonPath.parse(any(), eq(config))).thenReturn(documentContext);

 // Test with null workflow, taskDefinition and inputParams
 ParametersUtils utils = new ParametersUtils(objectMapper);
 Map<String, Object> result = utils.getTaskInput(null, null, null, null);
 assertNull(result);

 // Test with valid workflow, taskDefinition and inputParams
 WorkflowDef workflowDef = new WorkflowDef();
 WorkflowModel workflowModelWithInput = new WorkflowModel();
 workflowModelWithInput.setInput(new HashMap<>());
 workflowModelWithInput.setInput().put("input_param", "input_value");
 when(workflowModel.getInputTemplate()).thenReturn(null);
 when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
 when(workflowModel.getWorkflowId()).thenReturn("workflow_id");
 when(workflowModel.getWorkflowName()).thenReturn("workflow_name");
 when(workflowModel.getStatus()).thenReturn("workflow_status");
 when(workflowModel.getParentWorkflowId()).thenReturn("parent_workflow_id");
 when(workflowModel.getParentWorkflowTaskId()).thenReturn("parent_workflow_task_id");
 when(workflowModel.getReasonForIncompletion()).thenReturn("reason_for_incompletion");
 when(workflowModel.getSchemaVersion()).thenReturn(1L);
 when(workflowModel.getVariables()).thenReturn(Collections.emptyMap());

 when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());
 when(taskDefinition.getTaskDefName()).thenReturn("task_def_name");
 when(taskDefinition.getReferenceTaskName()).thenReturn("task_ref_name");
 when(taskDefinition.getInputData()).thenReturn(new HashMap<>());
 when(taskDefinition.getInputData().get("input_param")).thenReturn("task_input_value");

 inputParams.put("input_param_1", "input_value_1");
 inputParams.put("input_param_2", null);

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("input_param_1", "input_value_1");
 expectedResult.put("input_param_2", "task_input_value");
 expectedResult.put("workflow_id", "workflow_id");
 expectedResult.put("workflow_name", "workflow_name");
 expectedResult.put("workflow_status", "workflow_status");
 expectedResult.put("parent_workflow_id", "parent_workflow_id");
 expectedResult.put("parent_workflow_task_id", "parent_workflow_task_id");
 expectedResult.put("reason_for_incompletion", "reason_for_incompletion");
 expectedResult.put("input_param_2", "task_input_value");

 ParametersUtils utils = new ParametersUtils(objectMapper);
 Map<String, Object> result = utils.getTaskInput(workflowModelWithInput, inputParams, taskDefinition, "task_id");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputV2() {
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 TaskDef taskDefinition = Mockito.mock(TaskDef.class);
 Map<String, Object> inputParams = new HashMap<>();

 when(objectMapper.readValue(anyByteArray(), Map.class)).thenAnswer(i -> i.getArguments()[0]);
 when(config.addOptions(any(Option[].class))).thenReturn(config);
 when(JsonPath.parse(any(), eq(config))).thenReturn(documentContext);

 // Test with null workflow, taskDefinition and inputParams
 ParametersUtils utils = new ParametersUtils(objectMapper);
 Map<String, Object> result = utils.getTaskInputV2(null, null, null, null, null);
 assertNull(result);

 // Test with valid workflow, taskDefinition, inputParams and taskId
 WorkflowDef workflowDef = new WorkflowDef();
 WorkflowModel workflowModelWithTasks = new WorkflowModel();
 workflowModelWithTasks.setInput(new HashMap<>());
 workflowModelWithTasks.setInput().put("input_param", "input_value");
 workflowModelWithTasks.setTasks(Collections.singletonList(Mockito.mock(TaskModel.class)));
 TaskModel taskModel = Mockito.mock(TaskModel.class);
 when(taskModel.getReferenceTaskName()).thenReturn("task_ref_name");
 when(taskModel.getTaskDefName()).thenReturn("task_def_name");
 when(taskModel.getStatus()).thenReturn("task_status");
 when(taskModel.getScheduledTime()).thenReturn(1L);
 when(taskModel.getStartTime()).thenReturn(2L);
 when(taskModel.getEndTime()).thenReturn(3L);
 when(taskModel.getCorrelationId()).thenReturn("correlation_id");
 when(taskModel.getReasonForIncompletion()).thenReturn("reason_for_incompletion");
 when(taskModel.getIteration()).thenReturn(1);
 when(taskModel.getRetryCount()).thenReturn(0);
 when(taskModel.getCallbackAfterSeconds()).thenReturn(0);
 when(taskModel.getInputData()).thenReturn(new HashMap<>());
 when(taskModel.getInputData().get("input_param")).thenReturn("task_input_value");
 when(workflowModel.getInputTemplate()).thenReturn(new HashMap<>());

 inputParams.put("input_param_1", "input_value_1");
 inputParams.put("input_param_2", null);

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("input_param_1", "input_value_1");
 expectedResult.put("input_param_2", "task_input_value");
 expectedResult.put("workflow_id", "workflow_id");
 expectedResult.put("workflow_name", "workflow_name");
 expectedResult.put("workflow_status", "workflow_status");
 expectedResult.put("parent_workflow_id", null);
 expectedResult.put("parent_workflow_task_id", null);
 expectedResult.put("reason_for_incompletion", "reason_for_incompletion");
 expectedResult.put("input_param_2", "task_input_value");
 expectedResult.put("task_id", "task_id");
 expectedResult.put("task_status", "task_status");
 expectedResult.put("scheduled_time", 1L);
 expectedResult.put("start_time", 2L);
 expectedResult.put("end_time", 3L);
 expectedResult.put("correlation_id", "correlation_id");
 expectedResult.put("reason_for_incompletion", "reason_for_incompletion");
 expectedResult.put("task_def_name", "task_def_name");
 expectedResult.put("task_ref_name", "task_ref_name");
 expectedResult.put("iteration", 1);
 expectedResult.put("retry_count", 0);
 expectedResult.put("callback_after_seconds", 0);

 ParametersUtils utils = new ParametersUtils(objectMapper);
 Map<String, Object> result = utils.getTaskInputV2(workflowModelWithTasks, inputParams, null, "task_id");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace() {
 String paramString = "${workflow.workflow_param.input_param}${task_ref_name.task_param}";
 Map<String, Object> workflow = new HashMap<>();
 workflow.put("workflow_param", new HashMap<>());
 workflow.put("workflow_param.input_param", "workflow_value");
 Map<String, Object> taskRefName = new HashMap<>();
 taskRefName.put("task_param", "task_value");

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("workflow_value", "${workflow_param.input_param}");
 expectedResult.put("task_value", "${task_param}");

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Map<String, Object> result = utils.replace(paramString, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplaceVariables() {
 String paramString = "${workflow.workflow_param.input_param}${task_ref_name.task_param}";
 WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
 TaskModel taskModel = Mockito.mock(TaskModel.class);
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "task_id";

 when(workflowModel.getInput()).thenReturn(new HashMap<>());
 when(workflowModel.getInput().get("workflow_param")).thenReturn(new HashMap<>());
 when(workflowModel.getInput().get("workflow_param").get("input_param")).thenReturn("workflow_value");
 when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));
 when(taskModel.getInputData()).thenReturn(new HashMap<>());
 when(taskModel.getInputData().get("task_param")).thenReturn("task_value");

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Object result = utils.replaceVariables(paramString, documentContext, taskId);
 assertEquals("workflow_value", result);
 }

 @Test
 public void testReplaceVariablesWithEnvironmentVariable() {
 String paramString = "${ENV_VAR}";
 String envVariable = "env_value";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "task_id";

 when(EnvUtils.getSystemParametersValue(envVariable, taskId)).thenReturn("env_value_from_task");

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Object result = utils.replaceVariables(paramString, documentContext, taskId);
 assertEquals("env_value_from_task", result);
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() {
 String paramString = "${workflow.workflow_param.input_param}${task_ref_name.task_param}";
 String workflowId = "workflow_id";
 String taskId = "task_id";

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Object result = utils.replaceVariables(paramString, null, taskId);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContext() {
 String paramString = "${workflow.workflow_param.input_param}${task_ref_name.task_param}";
 DocumentContext documentContext = new DocumentContext(new HashMap<>());
 String taskId = "task_id";

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Object result = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContextAndNullTaskId() {
 String paramString = "${workflow.workflow_param.input_param}${task_ref_name.task_param}";
 DocumentContext documentContext = new DocumentContext(new HashMap<>());
 String taskId = null;

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Object result = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContextAndNullTaskId() {
 String paramString = "${workflow.workflow_param.input_param}${task_ref_name.task_param}";
 DocumentContext documentContext = null;
 String taskId = null;

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Object result = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(result);
 }
}