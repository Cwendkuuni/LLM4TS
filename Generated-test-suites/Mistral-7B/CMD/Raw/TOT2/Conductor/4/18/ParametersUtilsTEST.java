import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowInstance;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

 private final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
 private final ParametersUtils utils = new ParametersUtils(objectMapper);

 @Test
 public void testGetTaskInput_ValidInput() {
 WorkflowModel workflow = createValidWorkflow();
 TaskDef taskDefinition = createValidTaskDefinition();
 String taskId = "testTaskId";
 Map<String, Object> expectedOutput = createExpectedOutput();
 Map<String, Object> actualOutput = utils.getTaskInput(null, workflow, taskDefinition, taskId);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInput_NullWorkflow() {
 Map<String, Object> actualOutput = utils.getTaskInput(null, null, null, "taskId");
 Assert.assertNull(actualOutput);
 }

 @Test
 public void testGetTaskInput_NullTaskDefinition() {
 Map<String, Object> actualOutput = utils.getTaskInput(null, createValidWorkflow(), null, "taskId");
 Assert.assertNull(actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetTaskInput_InvalidWorkflow() {
 Map<String, Object> actualOutput = utils.getTaskInput(null, null, null, "taskId");
 Assert.assertNull(actualOutput);
 }

 @Test
 public void testGetTaskInputV2_ValidInput() {
 WorkflowModel workflow = createValidWorkflowV2();
 TaskDef taskDefinition = createValidTaskDefinition();
 String taskId = "testTaskId";
 Map<String, Object> expectedOutput = createExpectedOutputV2();
 Map<String, Object> actualOutput = utils.getTaskInputV2(
new HashMap<>(), workflow, taskId, taskDefinition);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplace_ValidInput() {
 String inputString = "Hello ${workflow.variables.name} ${task.input.name}!";
 Map<String, Object> workflowVariables = new LinkedHashMap<>();
 workflowVariables.put("name", "John Doe");
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("name", "Jane Doe");
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 Mockito.when(documentContext.read(Mockito.anyString())).thenReturn("John Doe");
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("Hello", "Hello John Doe Jane Doe!");
 Map<String, Object> actualOutput = utils.replace(inputString, documentContext, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplace_NullDocumentContext() {
 String inputString = "Hello ${workflow.variables.name} ${task.input.name}!";
 Map<String, Object> workflowVariables = new LinkedHashMap<>();
 workflowVariables.put("name", "John Doe");
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("name", "Jane Doe");
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("Hello", "Hello null Jane Doe!");
 Map<String, Object> actualOutput = utils.replace(inputString, null, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplace_NullInputString() {
 String inputString = null;
 Map<String, Object> workflowVariables = new LinkedHashMap<>();
 workflowVariables.put("name", "John Doe");
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("name", "Jane Doe");
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("", "");
 Map<String, Object> actualOutput = utils.replace(inputString, documentContext, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceList_ValidInput() {
 List<Object> inputList = List.of("Hello", "World");
 String taskId = "testTaskId";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 Map<String, Object> expectedOutput = List.of("Hello", "WorldJohn Doe");
 List<Object> actualOutput = utils.replaceList(inputList, taskId, documentContext);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceList_NullDocumentContext() {
 List<Object> inputList = List.of("Hello", "World");
 String taskId = "testTaskId";
 List<Object> expectedOutput = List.of("Hello", "Worldnull");
 List<Object> actualOutput = utils.replaceList(inputList, taskId, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceVariables_ValidInput() {
 String inputString = "${workflow.variables.name}.${workflow.variables.age}";
 WorkflowInstance workflowInstance = createValidWorkflowInstance();
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 Map<String, Object> expectedOutput = "John Doe.25";
 Map<String, Object> actualOutput = utils.replaceVariables(
inputString, documentContext, null, 0);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceVariables_NullDocumentContext() {
 String inputString = "${workflow.variables.name}.${workflow.variables.age}";
 WorkflowInstance workflowInstance = createValidWorkflowInstance();
 Map<String, Object> expectedOutput = "";
 Map<String, Object> actualOutput = utils.replaceVariables(
inputString, null, null, 0);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 private WorkflowModel createValidWorkflow() {
 WorkflowDef workflowDef = new WorkflowDef();
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowDefinition(workflowDef);
 return workflow;
 }

 private WorkflowModel createValidWorkflowV2() {
 WorkflowDef workflowDef = new WorkflowDef();
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowDefinition(workflowDef);
 List<WorkflowTask> tasks = List.of(createValidTask(), createValidTask());
 workflow.setTasks(tasks);
 return workflow;
 }

 private TaskDef createValidTaskDefinition() {
 TaskDef taskDefinition = new TaskDef();
 return taskDefinition;
 }

 private Map<String, Object> createExpectedOutput() {
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("input", "testInputValue");
 return expectedOutput;
 }

 private Map<String, Object> createExpectedOutputV2() {
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("input", List.of("testInputValue1", "testInputValue2"));
 return expectedOutput;
 }

 private Map<String, Object> createValidTaskInput() {
 Map<String, Object> input = new HashMap<>();
 input.put("input", "testInputValue");
 return input;
 }

 private Map<String, Object> createValidTaskInputV2() {
 Map<String, Object> input = new HashMap<>();
 input.put("input", List.of("testInputValue1", "testInputValue2"));
 return input;
 }

 private Map<String, Object> createValidOutput() {
 Map<String, Object> output = new HashMap<>();
 output.put("output", "testOutputValue");
 return output;
 }

 private WorkflowInstance createValidWorkflowInstance() {
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setVariables(Collections.singletonMap("name", "John Doe"));
 workflowInstance.setAge(25);
 return workflowInstance;
 }

 private WorkflowTask createValidTask() {
 WorkflowTask task = new WorkflowTask();
 Map<String, Object> taskInput = createValidTaskInput();
 Map<String, Object> taskOutput = createValidOutput();
 task.setInputData(taskInput);
 task.setOutputData(taskOutput);
 return task;
 }

}