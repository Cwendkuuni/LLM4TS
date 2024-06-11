import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

 private static final String JSON_PATH_EXPRESSION = "$..input.param";
 private static final String WORKFLOW_ID = "workflowId";
 private static final String TASK_ID = "taskId";
 private static final String PARAM_NAME = "paramName";
 private static final String PARAM_VALUE = "paramValue";
 private static final String WORKFLOW_NAME = "workflowName";
 private static final String WORKFLOW_VERSION = "1.0.0";
 private static final String TASK_DEF_NAME = "taskDefName";
 private static final String TASK_TYPE = "taskType";
 private static final String INPUT_TEMPLATE = "{\"input\":{\"param\":\"${workflow.workflowId} ${task.taskId}\"}}";
 private static final String ENV_VAR_NAME = "envVarName";
 private static final String ENV_VAR_VALUE = "envVarValue";
 private static final String WORKFLOW_INPUT = "{\"input\":{\"param\":\"workflowInputParamValue\"}}";
 private static final String TASK_INPUT = "{\"input\":{\"param\":\"taskInputParamValue\"}}";
 private static final String JACKSON_OBJECT_MAPPER = "ObjectMapper";

 @InjectMocks private ParametersUtils parametersUtils;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 objectMapper = ReflectionTestUtils.getFieldValue(parametersUtils, "objectMapper");
 }

 @Test
 public void testGetTaskInputV1() throws IOException {
 WorkflowModel workflowModel = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put(PARAM_NAME, PARAM_VALUE);

 Map<String, Object> result = parametersUtils.getTaskInput(
         inputParams,
         workflowModel,
         null,
         null);

 Assert.assertEquals(3, result.size());
 Map<String, Object> workflowInput = result.get("workflow");
 Assert.assertEquals(WORKFLOW_ID, workflowInput.get("workflowId"));
 Map<String, Object> taskInput = result.get("tasks.");
 Assert.assertNull(taskInput);
 }

 @Test
 public void testGetTaskInputV2() throws IOException {
 WorkflowModel workflowModel = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put(PARAM_NAME, PARAM_VALUE);

 TaskDef taskDef = createTaskDef();
 when(taskDef.getInputTemplate()).thenReturn(INPUT_TEMPLATE.getBytes());

 WorkflowModel workflowModelWithTaskDef = new WorkflowModel();
 workflowModelWithTaskDef.setTasks(Collections.singletonList(createTaskModel()));
 workflowModelWithTaskDef.setTaskDef(taskDef);

 Map<String, Object> result = parametersUtils.getTaskInput(
         inputParams,
         workflowModelWithTaskDef,
         null,
         TASK_ID);

 Assert.assertEquals(4, result.size());
 Map<String, Object> workflowInput = result.get("workflow");
 Assert.assertEquals(WORKFLOW_ID, workflowInput.get("workflowId"));
 Map<String, Object> taskInput = result.get("tasks." + TASK_ID);
 Assert.assertEquals(TASK_TYPE, taskInput.get("taskType"));
 Assert.assertEquals(TASK_DEF_NAME, taskInput.get("taskDefName"));
 Assert.assertEquals(
         "workflowId taskId",
         taskInput.get("input").get("param"));
 }

 @Test
 public void testGetTaskInputV2WithEnvVar() throws IOException {
 WorkflowModel workflowModel = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put(PARAM_NAME, PARAM_VALUE);

 TaskDef taskDef = createTaskDef();
 when(taskDef.getInputTemplate()).thenReturn(INPUT_TEMPLATE.getBytes());
 when(EnvUtils.getSystemParametersValue(any(String.class), any(String.class)))
         .thenReturn(ENV_VAR_VALUE);

 WorkflowModel workflowModelWithTaskDef = new WorkflowModel();
 workflowModelWithTaskDef.setTasks(Collections.singletonList(createTaskModel()));
 workflowModelWithTaskDef.setTaskDef(taskDef);

 Map<String, Object> result = parametersUtils.getTaskInput(
         inputParams,
         workflowModelWithTaskDef,
         null,
         TASK_ID);

 Assert.assertEquals(5, result.size());
 Map<String, Object> workflowInput = result.get("workflow");
 Assert.assertEquals(WORKFLOW_ID, workflowInput.get("workflowId"));
 Map<String, Object> taskInput = result.get("tasks." + TASK_ID);
 Assert.assertEquals(TASK_TYPE, taskInput.get("taskType"));
 Assert.assertEquals(TASK_DEF_NAME, taskInput.get("taskDefName"));
 Assert.assertEquals(
         "workflowId " + ENV_VAR_VALUE + " taskId",
         taskInput.get("input").get("param"));
 }

 @Test
 public void testReplaceVariables() throws IOException {
 String jsonString = "{\"input\":{\"param\":\"${workflow.workflowId} ${task.taskId}\"}}";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), JsonPath.defaultOptions());

 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, TASK_ID);
 String expectedJsonString =
         "{\"input\":{\"param\":\"workflowId " + WORKFLOW_ID + " taskId\"}}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithEnvVar() throws IOException {
 String jsonString = "{\"input\":{\"param\":\"${workflow.workflowId} ${task.taskId}\"}}";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), JsonPath.defaultOptions());
 when(EnvUtils.getSystemParametersValue(any(String.class), any(String.class)))
         .thenReturn(ENV_VAR_VALUE);

 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, TASK_ID);
 String expectedJsonString =
         "{\"input\":{\"param\":\"workflowId " + WORKFLOW_ID + " " + ENV_VAR_VALUE + " taskId\"}}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() throws IOException {
 String jsonString = "{\"input\":{\"param\":\"${workflow.workflowId} ${task.taskId}\"}}";
 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, null, TASK_ID);
 String expectedJsonString = "{" + jsonString + "}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithEmptyJson() throws IOException {
 String jsonString = "{}";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), JsonPath.defaultOptions());
 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, TASK_ID);
 String expectedJsonString = jsonString;
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithWorkflowInput() throws IOException {
 String jsonString = "{\"input\":{\"param\":\"${workflow.input.param}\"}}";
 DocumentContext documentContext =
             JsonPath.parse(WORKFLOW_INPUT, JsonPath.defaultOptions());
 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, null);
 String expectedJsonString = "{" + jsonString.replace("${workflow.input.param}", "workflowInputParamValue") + "}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithTaskInput() throws IOException {
 String jsonString = "{\"input\":{\"param\":\"${task.input.param}\"}}";
 DocumentContext documentContext =
             JsonPath.parse(TASK_INPUT, JsonPath.defaultOptions());
 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, TASK_ID);
 String expectedJsonString = "{" + jsonString.replace("${task.input.param}", "taskInputParamValue") + "}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithDeepNestedJson() throws IOException {
 String jsonString =
         "{\"input\":{\"param\":{\"nestedParam\":\"${workflow.input.input.nestedParam}\"}}}";
 DocumentContext documentContext =
             JsonPath.parse(WORKFLOW_INPUT, JsonPath.defaultOptions());
 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, null);
 String expectedJsonString =
         "{"
         + jsonString
         .replace("${workflow.input.input.nestedParam}", "workflowInputParamValue")
         + "}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 @Test
 public void testReplaceVariablesWithDeepNestedJsonWithTaskInput()
         throws IOException {
 String jsonString =
         "{\"input\":{\"param\":{\"nestedParam\":\"${task.input.input.nestedParam}\"}}}";
 DocumentContext documentContext =
             JsonPath.parse(TASK_INPUT, JsonPath.defaultOptions());
 Map<String, Object> result = parametersUtils.replaceVariables(jsonString, documentContext, TASK_ID);
 String expectedJsonString =
         "{"
         + jsonString
         .replace("${task.input.input.nestedParam}", "taskInputParamValue")
         + "}";
 Assert.assertEquals(expectedJsonString, Objects.toString(result));
 }

 private WorkflowModel createWorkflowModel() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowName(WORKFLOW_NAME);
 workflowModel.setWorkflowId(WORKFLOW_ID);
 workflowModel.setWorkflowDefinition(new WorkflowDef());
 workflowModel.setSchemaVersion(Integer.parseInt(WORKFLOW_VERSION));
 return workflowModel;
 }

 private TaskDef createTaskDef() {
 TaskDef taskDef = mock(TaskDef.class);
 ReflectionTestUtils.setFieldValue(taskDef, "taskType", TASK_TYPE);
 return taskDef;
 }

 private TaskModel createTaskModel() {
 TaskModel taskModel = new TaskModel();
 taskModel.setTaskDefName(TASK_DEF_NAME);
 taskModel.setTaskType(TASK_TYPE);
 taskModel.setOutputData(new HashMap<>());
 taskModel.setInputData(new HashMap<>());
 return taskModel;
 }
}