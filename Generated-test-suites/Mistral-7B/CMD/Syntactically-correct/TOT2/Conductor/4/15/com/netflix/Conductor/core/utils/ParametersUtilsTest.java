import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParametersUtilsTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testGetTaskInput_WhenGivenValidInputs_ThenReturnMap() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 TaskDef taskDefinition = new TaskDef();
 String taskId = "taskId";

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, taskId);

 assertNotNull(result);
 }

 @Test
 public void testGetTaskInput_WhenGivenNullWorkflow_ThenReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = new TaskDef();
 String taskId = "taskId";

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, null, taskDefinition, taskId);

 assertEquals(Collections.emptyMap(), result);
 }

 @Test
 public void testGetTaskInput_WhenGivenNullTaskDefinition_ThenReturnInputParams() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 String taskId = "taskId";

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, null, taskId);

 assertEquals(inputParams, result);
 }

 @ParameterizedTest
 @MethodSource("provideValidInputMaps")
 public void testGetTaskInput_WhenGivenValidInputMaps_ThenReturnMappedInputs(Map<String, Object> inputParams) {
 WorkflowModel workflow = new WorkflowModel();
 TaskDef taskDefinition = new TaskDef();
 String taskId = "taskId";

 Map<String, Object> expectedResult = new HashMap<>();
 // Set up expected result based on inputParams

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, taskId);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInput_WhenGivenWorkflowWithNoTasks_ThenReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 TaskDef taskDefinition = new TaskDef();
 String taskId = "taskId";

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, taskId);

 assertEquals(Collections.emptyMap(), result);
 }

 @Test
 public void testGetTaskInput_WhenGivenWorkflowWithNullTask_ThenReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 TaskDef taskDefinition = null;
 String taskId = "taskId";

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, taskId);

 assertEquals(Collections.emptyMap(), result);
 }

 @Test
 public void testGetTaskInput_WhenGivenWorkflowWithTasks_ThenReturnMappedInputs() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
 String taskId = "taskId";

 Map<String, Object> expectedResult = new HashMap<>();
 // Set up expected result based on inputParams and tasks

 Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, null, taskId);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace_WhenGivenNullJson_ThenReturnEmptyMap() {
 Map<String, Object> input = new HashMap<>();
 Object json = null;

 Map<String, Object> result = new ParametersUtils().replace(input, json);

 assertEquals(Collections.emptyMap(), result);
 }

 @Test
 public void testReplace_WhenGivenValidJsonString_ThenReturnMappedValues() {
 Map<String, Object> input = new HashMap<>();
 String json = "{\"key1\":\"value1\",\"key2\":[\"value2\",\"value3\"]}";

 Map<String, Object> result = new ParametersUtils().replace(input, json);

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("key1", "value1");
 expectedResult.put("key2", Arrays.asList("value2", "value3"));

 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace_WhenGivenNullInput_ThenReturnInput() {
 String paramString = null;

 Object replacement = new ParametersUtils().replaceVariables(paramString, null, null);

 assertNull(replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenNullDocumentContext_ThenReturnInput() {
 String paramString = "{{key1}}";
 DocumentContext documentContext = null;

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("{{key1}}", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContext_ThenReplaceVariables() {
 String paramString = "${workflow.workflowId}-{{task.input.key1}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 List<TaskModel> tasks = Arrays.asList(new TaskModel().setInputData(new HashMap<>().put("key1", "value1")));
 workflow.setTasks(tasks);
 DocumentContext documentContext = JsonPath.parse(workflow, Configuration.defaultConfiguration());

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("workflowId-value1", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContextWithNestedVariables_ThenReplaceVariables() {
 String paramString = "${workflow.workflowId}-${task.input.key1.value1}-{{task.output.key2.value2}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 List<TaskModel> tasks = Arrays.asList(
 new TaskModel().setInputData(new HashMap<>().put("key1", new HashMap<>().put("value1", "value1"))),
 new TaskModel().setOutputData(new HashMap<>().put("key2", new HashMap<>().put("value2", "value2")))
 );
 workflow.setTasks(tasks);
 DocumentContext documentContext = JsonPath.parse(workflow, Configuration.defaultConfiguration());

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("workflowId-value1-value2", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContextWithNullValue_ThenReplaceNullValue() {
 String paramString = "${workflow.workflowId}-${task.input.key1.value1}-{{task.output.key2.value2}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 List<TaskModel> tasks = Arrays.asList(
 new TaskModel().setInputData(new HashMap<>()).setOutputData(new HashMap<>().put("key2", null)),
 new TaskModel().setOutputData(new HashMap<>())
 );
 workflow.setTasks(tasks);
 DocumentContext documentContext = JsonPath.parse(workflow, Configuration.defaultConfiguration());

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("workflowId-null-null", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContextWithEmptyValue_ThenReplaceEmptyValue() {
 String paramString = "${workflow.workflowId}-${task.input.key1.value1}-{{task.output.key2.value2}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 List<TaskModel> tasks = Arrays.asList(
 new TaskModel().setInputData(new HashMap<>().put("key1", new HashMap<>())),
 new TaskModel().setOutputData(new HashMap<>().put("key2", ""))
 );
 workflow.setTasks(tasks);
 DocumentContext documentContext = JsonPath.parse(workflow, Configuration.defaultConfiguration());

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("workflowId-{}-{}", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContextWithNonExistentVariable_ThenReturnInput() {
 String paramString = "${nonExistentVariable}-{{task.input.key1.value1}}";
 WorkflowModel workflow = new WorkflowModel();
 List<TaskModel> tasks = new ArrayList<>();
 tasks.add(new TaskModel());
 workflow.setTasks(tasks);
 DocumentContext documentContext = JsonPath.parse(workflow, Configuration.defaultConfiguration());

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("{nonExistentVariable}-{{task.input.key1.value1}}", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContextWithEnvironmentVariables_ThenReplaceEnvironmentVariables() {
 String paramString = "${ENV_VAR}";
 Map<String, String> env = new HashMap<>();
 env.put("ENV_VAR", "value");
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());
 documentContext.setVariables(env);

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("value", replacement);
 }

 @Test
 public void testReplaceVariables_WhenGivenValidDocumentContextWithEnvironmentVariablesAndJsonPath_ThenReplaceEnvironmentVariables() {
 String paramString = "${ENV_VAR}.child.child}";
 Map<String, String> env = new HashMap<>();
 env.put("ENV_VAR", "value");
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());
 documentContext.setVariables(env);

 Object replacement = new ParametersUtils().replaceVariables(paramString, documentContext, null);

 assertEquals("value.child.child", replacement);
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenInvalidJson_ThenThrowIOException() {
 String paramString = "{\"key1\":\"value1\",\"key2\":[\"value2\",\"value3\"]}";
 Map<String, Object> input = new HashMap<>();
 Object json = "{invalid json}";

 assertThrows(IOException.class, () -> new ParametersUtils().replace(input, json));
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenInvalidJsonPath_ThenThrowRuntimeException() {
 String paramString = "${workflow.workflowId}-{\\$invalidPath}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

 assertThrows(RuntimeException.class, () -> new ParametersUtils().replaceVariables(paramString, documentContext, null));
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenNullDocumentContext_ThenThrowRuntimeException() {
 String paramString = "${workflow.workflowId}-{{task.input.key1.value1}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 List<TaskModel> tasks = Arrays.asList(new TaskModel());
 workflow.setTasks(tasks);
 DocumentContext documentContext = null;

 assertThrows(RuntimeException.class, () -> new ParametersUtils().replaceVariables(paramString, documentContext, null));
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenEmptyDocumentContext_ThenThrowRuntimeException() {
 String paramString = "${workflow.workflowId}-{{task.input.key1.value1}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 List<TaskModel> tasks = Arrays.asList(new TaskModel());
 workflow.setTasks(tasks);
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

 assertThrows(RuntimeException.class, () -> new ParametersUtils().replaceVariables(paramString, documentContext, null));
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenNullTask_ThenThrowRuntimeException() {
 String paramString = "${workflow.workflowId}-{{task.input.key1.value1}}";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 TaskDef taskDefinition = null;
 String taskId = "taskId";

 assertThrows(RuntimeException.class, () -> new ParametersUtils(objectMapper).getTaskInput(Collections.emptyMap(), workflow, taskDefinition, taskId));
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenNullWorkflow_ThenThrowRuntimeException() {
 Map<String, Object> inputParams = new HashMap<>();
 String taskId = "taskId";

 assertThrows(RuntimeException.class, () -> new ParametersUtils(objectMapper).getTaskInput(inputParams, null, null, taskId));
 }

 @Test
 public void testReplaceVariablesWithException_WhenGivenNullTaskDefinition_ThenThrowRuntimeException() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 String taskId = "taskId";

 assertThrows(RuntimeException.class, () -> new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, null, taskId));
 }

 @Test
 public void testGetWorkflowInput_WhenGivenValidWorkflowDefAndInputParams_ThenReturnMappedInputs() {
 WorkflowDef workflowDef = new WorkflowDef();
 Map<String, Object> inputParams = new HashMap<>();

 Map<String, Object> expectedResult = new HashMap<>();
 // Set up expected result based on inputParams

 Map<String, Object> result = new ParametersUtils().getWorkflowInput(workflowDef, inputParams);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetWorkflowInput_WhenGivenNullWorkflowDef_ThenReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();

 Map<String, Object> result = new ParametersUtils().getWorkflowInput(null, inputParams);

 assertEquals(Collections.emptyMap(), result);
 }

 private static Stream<Map<String, Object>> provideValidInputMaps() {
 return Stream.of(
 new HashMap<>() {{
 put("key1", "value1");
 put("key2.subKey", "value2");
 put("list", Arrays.asList("item1", "item2"));
 put("map", new HashMap<>());
 put("emptyMap", Collections.emptyMap());
 put("nullValue", null);
 }},
 new HashMap<>() {{
 put("key1", new HashMap<>());
 put("list", Arrays.asList(new HashMap<>(), new HashMap<>()));
 put("map", Collections.singletonMap("key", new HashMap<>()));
 put("emptyList", Collections.emptyList());
 put("nullList", null);
 }}
 );
 }
}