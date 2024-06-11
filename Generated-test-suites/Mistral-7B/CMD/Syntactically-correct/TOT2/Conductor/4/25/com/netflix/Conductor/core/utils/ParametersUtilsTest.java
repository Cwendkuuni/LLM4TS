import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.TaskModel;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

 private final ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

 @Test
 public void testGetTaskInput_NullWorkflow_ReturnsEmptyMap() {
 WorkflowModel workflow = null;
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = new TaskDef();
 String taskId = "testTaskId";

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 assertEquals(0, result.size());
 }

 @Test
 public void testGetTaskInput_NullTaskDefinition_UsesWorkflowInput() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = null;
 String taskId = "testTaskId";

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 Map<String, Object> workflowInput = workflow.getInput();
 workflowInput.forEach(entry -> result.put(entry.getKey(), entry.getValue()));
 assertEquals(workflowInput.size(), result.size());
 }

 @Test
 public void testGetTaskInput_ValidInput_ReturnsCorrectMap() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey1", "testValue1");
 TaskDef taskDefinition = new TaskDef();
 String taskId = "testTaskId";

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("testKey1", "testValue1");
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("testKey2", "testValue2");
 expectedResult.putAll(workflowInput);

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 assertEquals(expectedResult.size(), result.size());
 expectedResult.forEach(entry -> assertEquals(entry.getValue(), result.get(entry.getKey())));
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInput_InvalidInput_ThrowsRuntimeException() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = new TaskDef();
 String taskId = "testTaskId";

 Map<String, Object> result = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);
 fail("Should have thrown a RuntimeException");
 }

 @Test
 public void testGetTaskInputV2_ValidInput_ReturnsCorrectMap() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> input = new HashMap<>();
 input.put("testKey1", "testValue1");
 WorkflowModel newWorkflow = new WorkflowModel();
 newWorkflow.setInput(input);
 Map<String, TaskModel> tasks = new HashMap<>();
 TaskModel task = new TaskModel();
 task.setReferenceTaskName("testTaskRefName");
 task.setInputData(new HashMap<>());
 task.getInputData().put("testKey2", "testValue2");
 tasks.put("testTaskRefName", task);
 newWorkflow.setTasks(tasks);
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("testKey1", "testValue1");
 expectedResult.putAll(task.getInputData());
 Map<String, Object> result = parametersUtils.getTaskInputV2(
 input, workflow, "", taskDefinition, taskId);
 assertEquals(expectedResult.size(), result.size());
 expectedResult.forEach(entry -> assertEquals(entry.getValue(), result.get(entry.getKey())));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetTaskInputV2_NullInput_ThrowsIllegalArgumentException() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> input = null;
 WorkflowModel newWorkflow = new WorkflowModel();
 newWorkflow.setInput(input);
 Map<String, TaskModel> tasks = new HashMap<>();
 TaskModel task = new TaskModel();
 task.setReferenceTaskName("testTaskRefName");
 task.setInputData(new HashMap<>());
 task.getInputData().put("testKey2", "testValue2");
 tasks.put("testTaskRefName", task);
 newWorkflow.setTasks(tasks);
 Map<String, Object> result = parametersUtils.getTaskInputV2(
 input, workflow, "", null, "testTaskId");
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetTaskInputV2_NullWorkflow_ThrowsNullPointerException() {
 Map<String, Object> input = new HashMap<>();
 Map<String, Object> result = parametersUtils.getTaskInputV2(
 input, null, "", null, "testTaskId");
 fail("Should have thrown a NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetTaskInputV2_NullTaskDefinition_ThrowsNullPointerException() {
 Map<String, Object> input = new HashMap<>();
 Map<String, Object> result = parametersUtils.getTaskInputV2(
 input, new WorkflowModel(), "", null, "testTaskId");
 fail("Should have thrown a NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetTaskInputV2_NullTaskId_ThrowsNullPointerException() {
 Map<String, Object> input = new HashMap<>();
 Map<String, Object> result = parametersUtils.getTaskInputV2(
 input, new WorkflowModel(), "", taskDefinition, null);
 fail("Should have thrown a NullPointerException");
 }

 @Test
 public void testReplace_ValidString_ReturnsReplacedString() {
 String inputString = "${workflow.output.testKey} ${task.output.testKey}";
 DocumentContext documentContext = JsonPath.parse(new HashMap<>());
 Map<String, Object> result = parametersUtils.replace(inputString, documentContext, "");
 String expectedResult = "${workflow.output.testKey} null ${task.output.testKey}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace_ValidMap_ReturnsReplacedMap() {
 Map<String, Object> inputMap = new HashMap<>();
 inputMap.put("${workflow.output.testKey}", "testValue1");
 inputMap.put("${task.output.testKey}", "testValue2");
 DocumentContext documentContext = JsonPath.parse(new HashMap<>());
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("testValue1", "testValue1");
 expectedResult.put("testValue2", "testValue2");
 Map<String, Object> result = parametersUtils.replace(inputMap, documentContext, "");
 assertEquals(expectedResult.size(), result.size());
 expectedResult.forEach(entry -> assertEquals(entry.getValue(), result.get(entry.getKey())));
 }

 @Test
 public void testReplace_NullInput_ReturnsOriginalInput() {
 Map<String, Object> inputMap = null;
 DocumentContext documentContext = JsonPath.parse(new HashMap<>());
 Map<String, Object> result = parametersUtils.replace(inputMap, documentContext, "");
 assertTrue(inputMap == result);
 }
}