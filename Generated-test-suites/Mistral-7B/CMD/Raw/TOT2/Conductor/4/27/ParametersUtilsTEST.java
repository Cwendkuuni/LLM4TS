import static org.junit.Assert.*;
import static com.jayway.jsonpath.JsonPath.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.core.utils.EnvironmentUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowDefinition;

public class ParametersUtilsTest {

 private static final Logger log = LoggerFactory.getLogger(ParametersUtilsTest.class);
 private static final String TASK_ID = "taskId";
 private static final String WORKFLOW_ID = "workflowId";
 private static final String INPUT_KEY = "inputKey";
 private static final String INPUT_VALUE = "inputValue";
 private static final String WORKFLOW_NAME = "workflowName";
 private static final String WORKFLOW_VERSION = "1.0.0";
 private static final String WORKFLOW_TYPE = "workflowType";
 private static final String VAR_KEY = "varKey";
 private static final String VAR_VALUE = "varValue";
 private static final String ENV_VAR = "ENV_VAR";
 private static final String ENV_VAR_VALUE = "ENV_VAR_VALUE";
 private static final String WORKFLOW_REF = "workflowRef";
 private static final String TASK_REF = "taskRef";
 private static final String TASK_INPUT_KEY = "taskInputKey";
 private static final String TASK_OUTPUT_KEY = "taskOutputKey";
 private static final Object MAP = new HashMap<>();
 private static final Object LIST = Arrays.asList("listValue1", "listValue2");
 private static final Object WORKFLOW_DEF = new WorkflowDefinition();
 private static final Object WORKFLOW_MODEL = new WorkflowModel();
 private static final Object TASK_DEF = new Object();
 private static final Object TASK_MODEL = new TaskModel();
 private static final Object INPUT_TEMPLATE = new Object();
 private static final Object OBJECT_MAPPER = new ObjectMapper();
 private ParametersUtils utils;

 @Before
 public void setUp() {
 utils = new ParametersUtils(new ObjectMapper());
 }

 @Test
 public void testGetTaskInputV1() {
 // Given
 WorkflowModel workflowModel = new WorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put(INPUT_KEY, INPUT_VALUE);
 workflowModel.setInput(workflowInput);
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put(TASK_INPUT_KEY, "Task Input");
 workflowModel.setTasks(Arrays.asList(new TaskModel(TASK_REF, taskInput)));
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put(INPUT_KEY, INPUT_VALUE);
 expectedInput.put("Task Input", taskInput);

 // When
 Map<String, Object> result = utils.getTaskInputV1(workflowModel, null);

 // Then
 assertEquals(expectedInput, result);
 }

 @Test
 public void testGetTaskInputV2() {
 // Given
 WorkflowModel workflowModel = new WorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put(INPUT_KEY, INPUT_VALUE);
 workflowModel.setInput(workflowInput);
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put(TASK_INPUT_KEY, "Task Input");
 workflowModel.getTasks().add(new TaskModel(TASK_REF, taskInput));
 Map<String, Object> expectedInput = new HashMap<>();
 expectedInput.put(INPUT_KEY, INPUT_VALUE);
 expectedInput.put("Task Input", taskInput);
 WorkflowDef workflowDef = new WorkflowDefinition();
 workflowDef.setName(WORKFLOW_NAME);
 workflowDef.setVersion(WORKFLOW_VERSION);
 workflowDef.setType(WORKFLOW_TYPE);
 workflowModel.setWorkflowDefinition(workflowDef);

 // When
 Map<String, Object> result = utils.getTaskInput(
 MAP, workflowModel, null, TASK_REF);

 // Then
 assertEquals(expectedInput, result);
 }

 @Test
 public void testReplaceVariables() {
 // Given
 String paramString = "${WORKFLOW_ID}.${INPUT_KEY}";
 DocumentContext documentContext = null;
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithDocumentContext() {
 // Given
 String paramString = "${WORKFLOW_ID}.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(WORKFLOW_ID, replacement);
 }

 @Test
 public void testReplaceVariablesWithTaskId() {
 // Given
 String paramString = "${WORKFLOW_ID}.${INPUT_KEY}";
 DocumentContext documentContext = null;
 String taskId = "taskId";

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(WORKFLOW_ID + "." + taskId + "." + INPUT_KEY, replacement);
 }

 @Test
 public void testReplaceVariablesWithEnvVar() {
 // Given
 String paramString = "${ENV_VAR}";
 DocumentContext documentContext = null;
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(ENV_VAR_VALUE, replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPath() {
 // Given
 String paramString = "$.workflowDef.name";
 DocumentContext documentContext = createDocumentContext();
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(WORKFLOW_NAME, replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndTaskId() {
 // Given
 String paramString = "$.workflowDef.tasks[0].name";
 DocumentContext documentContext = createDocumentContext();
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(TASK_REF, replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndMap() {
 // Given
 String paramString = "$[*].inputTemplate.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = null;
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put(INPUT_KEY, INPUT_VALUE);

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(INPUT_VALUE, replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndMapAndTaskId() {
 // Given
 String paramString = "$[*].inputTemplate.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = TASK_ID;
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put(INPUT_KEY, INPUT_VALUE);

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(INPUT_VALUE, replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndList() {
 // Given
 String paramString = "$.tasks[0].input.${INPUT_KEY}"
 + "[$].tasks[1].output.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = null;
 List<Object> tasks = Arrays.asList(MAP, LIST);

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(Arrays.asList(INPUT_VALUE, INPUT_VALUE), replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndListAndTaskId() {
 // Given
 String paramString = "$.tasks[0].input.${INPUT_KEY}"
 + "[$].tasks[1].output.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = TASK_ID;
 List<Object> tasks = Arrays.asList(MAP, LIST);

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(Arrays.asList(INPUT_VALUE, INPUT_VALUE), replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndNestedMap() {
 // Given
 String paramString = "$.tasks[0].input.inputTemplate.${INPUT_KEY}"
 + "[$].tasks[1].output.outputTemplate.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = null;
 Map<String, Object> inputTemplateMap = new HashMap<>();
 inputTemplateMap.put(INPUT_KEY, INPUT_VALUE);
 List<Object> tasks = Arrays.asList(MAP, LIST);

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(Arrays.asList(INPUT_VALUE, INPUT_VALUE), replacement);
 }

 @Test
 public void testReplaceVariablesWithJsonPathAndNestedMapAndTaskId() {
 // Given
 String paramString = "$.tasks[0].input.inputTemplate.${INPUT_KEY}"
 + "[$].tasks[1].output.outputTemplate.${INPUT_KEY}";
 DocumentContext documentContext = createDocumentContext();
 String taskId = TASK_ID;
 Map<String, Object> inputTemplateMap = new HashMap<>();
 inputTemplateMap.put(INPUT_KEY, INPUT_VALUE);
 List<Object> tasks = Arrays.asList(MAP, LIST);

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertEquals(Arrays.asList(INPUT_VALUE, INPUT_VALUE), replacement);
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceVariablesWithInvalidJsonPath() {
 // Given
 String paramString = "$.tasks[0].input.invalid";
 DocumentContext documentContext = null;
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 fail("ReplaceVariables method should throw a RuntimeException for invalid JSON paths");
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() {
 // Given
 String paramString = "${WORKFLOW_ID}.${INPUT_KEY}";
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, null, taskId);

 // Then
 fail("ReplaceVariables method should throw a RuntimeException for null document context");
 }

 @Test
 public void testReplaceVariablesWithNullInput() {
 // Given
 String paramString = "${WORKFLOW_ID}.${INPUT_KEY}";
 DocumentContext documentContext = null;
 String taskId = null;

 // When
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);

 // Then
 assertNull(replacement);
 }

 private DocumentContext createDocumentContext() {
 WorkflowDef workflowDef = new WorkflowDefinition();
 workflowDef.setName(WORKFLOW_NAME);
 workflowDef.setVersion(WORKFLOW_VERSION);
 workflowDef.setType(WORKFLOW_TYPE);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefinition(workflowDef);
 ObjectMapper objectMapper = new ObjectMapper();
 try {
 return JsonPath.parse(objectMapper.writeValueAsString(workflowModel), JsonPath.configuration.builder().build());
 } catch (IOException e) {
 log.error("Error creating document context", e);
 throw new RuntimeException("Error creating document context", e);
 }
 }
}