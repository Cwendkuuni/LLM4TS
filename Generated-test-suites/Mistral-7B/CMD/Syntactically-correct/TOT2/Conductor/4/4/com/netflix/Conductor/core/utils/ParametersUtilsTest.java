import org.junit.Test;
import org.junit.Assert;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParametersUtilsTest {

 private final ObjectMapper objectMapper = new ObjectMapper();
 private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

 @Test
 public void testGetTaskInputEmptyInputParams() {
 WorkflowModel workflow = createWorkflowModel();
 TaskDef taskDefinition = createTaskDef();
 String taskId = "testTaskId";

 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Map<String, Object>> inputMap = new HashMap<>();
 Map<String, Object> workflowParams = new HashMap<>();
 workflowParams.put("input", workflow.getInput());
 workflowParams.put("output", workflow.getOutput());
 workflowParams.put("status", WorkflowModel.Status.STARTED);
 workflowParams.put("workflowId", "testWorkflowId");
 workflowParams.put("variables", Collections.emptyMap());
 inputMap.put("workflow", workflowParams);

 Map<String, Object> actualOutput = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputNullWorkflow() {
 WorkflowModel workflow = null;
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = createTaskDef();
 String taskId = "testTaskId";

 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Map<String, Object>> inputMap = new HashMap<>();
 Map<String, Object> workflowParams = new HashMap<>();
 workflowParams.put("input", Collections.emptyMap());
 workflowParams.put("output", Collections.emptyMap());
 workflowParams.put("status", WorkflowModel.Status.STARTED);
 workflowParams.put("workflowId", "testWorkflowId");
 workflowParams.put("variables", Collections.emptyMap());
 inputMap.put("workflow", workflowParams);

 Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputNullTaskDefinition() {
 WorkflowModel workflow = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = null;
 String taskId = "testTaskId";

 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Map<String, Object>> inputMap = new HashMap<>();
 Map<String, Object> workflowParams = new HashMap<>();
 workflowParams.put("input", workflow.getInput());
 workflowParams.put("output", workflow.getOutput());
 workflowParams.put("status", WorkflowModel.Status.STARTED);
 workflowParams.put("workflowId", "testWorkflowId");
 workflowParams.put("variables", Collections.emptyMap());
 inputMap.put("workflow", workflowParams);

 Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputWithInputParams() {
 WorkflowModel workflow = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "testValue");
 TaskDef taskDefinition = createTaskDef();
 String taskId = "testTaskId";

 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Object> input = new HashMap<>();
 input.put("testKey", "testValue");
 Map<String, Map<String, Object>> inputMap = new HashMap<>();
 Map<String, Object> workflowParams = new HashMap<>();
 workflowParams.put("input", workflow.getInput());
 workflowParams.put("output", workflow.getOutput());
 workflowParams.put("status", WorkflowModel.Status.STARTED);
 workflowParams.put("workflowId", "testWorkflowId");
 workflowParams.put("variables", Collections.emptyMap());
 inputMap.put("workflow", workflowParams);
 inputMap.put("task", createTaskMap());
 expectedOutput.putAll(inputMap);
 expectedOutput.putAll(input);

 Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputV2WithNullInputTemplate() {
 WorkflowModel workflow = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = createTaskDef();
 String taskId = "testTaskId";

 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Map<String, Object>> inputMap = new HashMap<>();
 Map<String, Object> workflowParams = new HashMap<>();
 workflowParams.put("input", workflow.getInput());
 workflowParams.put("output", workflow.getOutput());
 workflowParams.put("status", WorkflowModel.Status.STARTED);
 workflowParams.put("workflowId", "testWorkflowId");
 workflowParams.put("variables", Collections.emptyMap());
 inputMap.put("workflow", workflowParams);
 Map<String, Object> taskParams = new HashMap<>();
 taskParams.put("input", Collections.emptyMap());
 taskParams.put("output", Collections.emptyMap());
 taskParams.put("taskType", "testTaskType");
 inputMap.put("task", taskParams);
 expectedOutput.putAll(inputMap);

 Map<String, Object> actualOutput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputV2WithInputTemplate() {
 WorkflowModel workflow = createWorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("testKey", "testValue");
 TaskDef taskDefinition = createTaskDef();
 String taskId = "testTaskId";

 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Object> input = new HashMap<>();
 input.put("testKey", "testValue");
 Map<String, Map<String, Object>> inputMap = new HashMap<>();
 Map<String, Object> workflowParams = new HashMap<>();
 workflowParams.put("input", workflow.getInput());
 workflowParams.put("output", workflow.getOutput());
 workflowParams.put("status", WorkflowModel.Status.STARTED);
 workflowParams.put("workflowId", "testWorkflowId");
 workflowParams.put("variables", Collections.emptyMap());
 Map<String, Object> taskParams = new HashMap<>();
 taskParams.put("input", Collections.singletonMap("testKey2", "testValue2"));
 taskParams.put("output", Collections.singletonMap("testKey3", "testValue3"));
 taskParams.put("taskType", "testTaskType");
 inputMap.put("workflow", workflowParams);
 inputMap.put("task", taskParams);
 expectedOutput.putAll(inputMap);
 expectedOutput.putAll(input);
 expectedOutput.putAll(taskParams);

 Map<String, Object> actualOutput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceWithNullJson() {
 String paramString = null;
 DocumentContext documentContext = null;
 String taskId = "testTaskId";
 Object replacement = "testReplacement";

 Object actualOutput = parametersUtils.replace(paramString, documentContext, taskId);
 Assert.assertEquals(null, actualOutput);
 }

 @Test
 public void testReplaceWithEmptyString() {
 String paramString = "";
 DocumentContext documentContext = null;
 String taskId = "testTaskId";
 Object replacement = "testReplacement";

 Object actualOutput = parametersUtils.replace(paramString, documentContext, taskId);
 Assert.assertEquals(replacement, actualOutput);
 }

 @Test
 public void testReplaceWithSingleVariable() {
 String paramString = "${testVar}";
 DocumentContext documentContext = null;
 String taskId = "testTaskId";
 Object replacement = "testReplacement";

 Object actualOutput = parametersUtils.replace(paramString, documentContext, taskId);
 Assert.assertEquals(replacement, actualOutput);
 }

 @Test
 public void testReplaceWithMultipleVariables() {
 String paramString = "${testVar1}${testVar2}";
 DocumentContext documentContext = null;
 String taskId = "testTaskId";
 Object replacementVar1 = "testReplacement1";
 Object replacementVar2 = "testReplacement2";

 DocumentContext documentContextMock = new DocumentContext() {};
 documentContextMock.setRoot(Map.of("testVar1", replacementVar1, "testVar2", replacementVar2));
 Object actualOutput = parametersUtils.replace(paramString, documentContextMock, taskId);
 Assert.assertEquals(replacementVar1 + replacementVar2, actualOutput);
 }

 @Test
 public void testReplaceWithNestedVariables() {
 String paramString = "${workflow.variables.testVar}";
 DocumentContext documentContext = null;
 String taskId = "testTaskId";
 Object replacementVar = "testReplacement";

 Map<String, Object> variables = new HashMap<>();
 variables.put("testVar", replacementVar);
 WorkflowModel workflow = new WorkflowModel();
 workflow.setVariables(variables);
 DocumentContext documentContextMock = new DocumentContext() {};
 documentContextMock.setRoot(Map.of("workflow", workflow));
 Object actualOutput = parametersUtils.replace(paramString, documentContextMock, taskId);
 Assert.assertEquals(replacementVar, actualOutput);
 }

 @Test
 public void testReplaceListWithEmptyList() {
 List<Object> values = Collections.emptyList();
 String taskId = "testTaskId";
 DocumentContext documentContext = null;

 List<Object> actualOutput = parametersUtils.replaceList(values, taskId, documentContext);
 Assert.assertEquals(Collections.emptyList(), actualOutput);
 }

 @Test
 public void testReplaceListWithSingleValue() {
 List<Object> values = Collections.singletonList("testValue");
 String taskId = "testTaskId";
 DocumentContext documentContext = null;

 List<Object> actualOutput = parametersUtils.replaceList(values, taskId, documentContext);
 Assert.assertEquals(Collections.singletonList("testValue"), actualOutput);
 }

 @Test
 public void testReplaceListWithMultipleValues() {
 List<Object> values = Arrays.asList("testValue1", "testValue2");
 String taskId = "testTaskId";
 DocumentContext documentContext = null;

 List<Object> actualOutput = parametersUtils.replaceList(values, taskId, documentContext);
 Assert.assertEquals(values, actualOutput);
 }

 @Test
 public void testReplaceListWithNestedValues() {
 List<Object> values = Arrays.asList(
 Map.of("testKey1", "value1"),
 Map.of("testKey2", "value2"));
 String taskId = "testTaskId";
 DocumentContext documentContext = null;

 List<Object> actualOutput = parametersUtils.replaceList(values, taskId, documentContext);
 Assert.assertEquals(values, actualOutput);
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceListWithNullValue() {
 List<Object> values = Arrays.asList("testValue", null);
 String taskId = "testTaskId";
 DocumentContext documentContext = null;

 List<Object> actualOutput = parametersUtils.replaceList(values, taskId, documentContext);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceVariablesWithEmptyString() {
 String paramString = "";
 DocumentContext documentContext = null;
 String taskId = "testTaskId";

 Object actualOutput = parametersUtils.replaceVariables(paramString, documentContext, taskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceVariablesWithNullDocumentContext() {
 String paramString = "${workflow.variables.testVar}";
 DocumentContext documentContext = null;
 String taskId = "testTaskId";

 Object actualOutput = parametersUtils.replaceVariables(paramString, documentContext, taskId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceVariablesWithNullTaskId() {
 String paramString = "${workflow.variables.testVar}";
 DocumentContext documentContext = null;
 String taskId = null;

 Object actualOutput = parametersUtils.replaceVariables(paramString, documentContext, taskId);
 }

 private WorkflowModel createWorkflowModel() {
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowName("testWorkflowName");
 workflow.setWorkflowVersion("testWorkflowVersion");
 return workflow;
 }

 private TaskDef createTaskDef() {
 TaskDef taskDefinition = new TaskDef();
 return taskDefinition;
 }

 private Map<String, Object> createTaskMap() {
 Map<String, Object> task = new HashMap<>();
 task.put("inputData", new HashMap<>());
 task.put("outputData", new HashMap<>());
 task.put("taskType", "testTaskType");
 return task;
 }
}