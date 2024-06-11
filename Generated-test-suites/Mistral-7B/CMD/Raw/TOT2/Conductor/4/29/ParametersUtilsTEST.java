import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.MapUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.test.TestUtils;
import com.netflix.conductor.test.mocks.MockWorkflowDef;
import com.netflix.conductor.test.mocks.MockWorkflowModel;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ParametersUtilsTest {

 private ObjectMapper objectMapper;
 private ParametersUtils utils;

 @BeforeEach
 void setup() {
 objectMapper = TestUtils.getObjectMapper();
 utils = new ParametersUtils(objectMapper);
 }

 @Test
 void testGetTaskInput_validInputWithNoTaskDef() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new MockWorkflowModel();
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("input1", "value1");
 expectedResult.put("input2", null);
 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, null, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testGetTaskInput_validInputWithTaskDef() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("taskDefName", "taskDef");
 WorkflowModel workflow = new MockWorkflowModel();
 TaskDef taskDef = new MockTaskDef();
 when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());
 when(taskDef.getName()).thenReturn("taskName");
 workflow.setTaskDefs(Collections.singletonList(taskDef));
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("task1.input1", "value1");
 expectedResult.put("task1.input2", null);
 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, null, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testGetTaskInput_validInputWithWorkflowInputTemplate() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new MockWorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("workflowInput.input1", "value1");
 workflowInput.put("workflowInput.input2", null);
 when(workflow.getInput()).thenReturn(workflowInput);
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("workflowInput.input1", "value1");
 expectedResult.put("workflowInput.input2", null);
 Map<String, Object> result = utils.getTaskInput(null, workflow, null, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_noVariables() {
 String input = "";
 DocumentContext documentContext = JsonPath.parse("{}");
 String expectedResult = "";
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_singleVariable() {
 String input = "${variable}";
 DocumentContext documentContext = JsonPath.parse("{\"variable\": \"value\"}");
 String expectedResult = "value";
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_multipleVariables() {
 String input = "${variable1}.${variable2}";
 DocumentContext documentContext = JsonPath.parse("{\"variable1\": \"value1\", \"variable2\": \"value2\"}");
 String expectedResult = "value1.value2";
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_environmentVariable() {
 String input = "${ENV_VAR}";
 DocumentContext documentContext = JsonPath.parse("{}");
 String expectedResult = System.getenv("ENV_VAR");
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_deepNestedVariables() {
 String input = "${workflow.input.task1.input1}.${workflow.input.task1.input2}";
 WorkflowModel workflow = new MockWorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("input", new HashMap<>());
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("input1", "value1");
 taskInput.put("input2", null);
 workflowInput.put("task1", taskInput);
 when(workflow.getInput()).thenReturn(workflowInput);
 DocumentContext documentContext = JsonPath.parse(workflowInput.toString());
 String expectedResult = "value1.";
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_nullValue() {
 String input = "${workflow.input.task1.input1}";
 WorkflowModel workflow = new MockWorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("input", new HashMap<>());
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("input1", null);
 workflowInput.put("task1", taskInput);
 when(workflow.getInput()).thenReturn(workflowInput);
 DocumentContext documentContext = JsonPath.parse(workflowInput.toString());
 String expectedResult = null;
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_exception() {
 String input = "${workflow.input.task1.input1}";
 WorkflowModel workflow = new MockWorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("input", new HashMap<>());
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("input1", null);
 workflowInput.put("task1", taskInput);
 when(workflow.getInput()).thenThrow(new RuntimeException("Test exception"));
 DocumentContext documentContext = JsonPath.parse(workflowInput.toString());
 String expectedResult = null;
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }

 @Test
 void testReplaceVariables_invalidJsonPath() {
 String input = "${workflow.input.task1.input1}.";
 WorkflowModel workflow = new MockWorkflowModel();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("input", new HashMap<>());
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("input1", "value1");
 workflowInput.put("task1", taskInput);
 when(workflow.getInput()).thenReturn(workflowInput);
 DocumentContext documentContext = JsonPath.parse(workflowInput.toString());
 String expectedResult = null;
 String result = utils.replaceVariables(input, documentContext, null);
 assertEquals(expectedResult, result);
 }
}

class MockWorkflowDef extends WorkflowDef {
 @Override
 public Map<String, Object> getInputTemplate() {
 return new HashMap<>();
 }
}

class MockWorkflowModel extends WorkflowModel {
 @Override
 public Map<String, Object> getInput() {
 return new HashMap<>();
 }

 @Override
 public List<TaskModel> getTasks() {
 return Collections.emptyList();
 }

 @Override
 public String getTaskDefName(String taskRefName) {
 return "taskDef";
 }

 @Override
 public TaskModel getTaskByRefName(String taskRefName) {
 return new MockTaskModel();
 }
}

class MockTaskDef extends TaskDef {
 @Override
 public Map<String, Object> getInputTemplate() {
 return new HashMap<>();
 }
}

class MockTaskModel extends TaskModel {
 @Override
 public Map<String, Object> getInputData() {
 return new HashMap<>();
 }

 @Override
 public Map<String, Object> getOutputData() {
 return new HashMap<>();
 }
}