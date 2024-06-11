import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParametersUtilsTest {

 private ParametersUtils parametersUtils;
 private WorkflowDef workflowDef;
 private TaskDef taskDef;
 private WorkflowModel workflowModel;

 @Before
 public void setup() {
 parametersUtils = new ParametersUtils(new ObjectMapper());
 workflowDef = new WorkflowDef();
 workflowModel = new WorkflowModel();
 taskDef = new TaskDef();
 }

 @Test
 public void testGetTaskInput_withValidInputParamsAndWorkflowModelAndTaskDef_shouldReturnExpectedMap() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("key1", "value1");
 WorkflowModel expectedWorkflowModel = new WorkflowModel();
 expectedWorkflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
 WorkflowModel inputWorkflowModel = new WorkflowModel();
 inputWorkflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
 TaskDef expectedTaskDef = new TaskDef();
 expectedTaskDef.setInputTemplate(Collections.singletonMap("key2", "value2"));
 Map<String, Object> expectedOutput = parametersUtils.getTaskInput(inputParams, expectedWorkflowModel, expectedTaskDef, null);
 Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInput_withNullWorkflowModel_shouldReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();
 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, null, taskDef, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInput_withNullTaskDef_shouldReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel expectedWorkflowModel = new WorkflowModel();
 expectedWorkflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
 Map<String, Object> expectedOutput = parametersUtils.getTaskInput(inputParams, expectedWorkflowModel, null, null);
 Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInput_withEmptyInputParams_shouldReturnEmptyMap() {
 Map<String, Object> actualOutput = parametersUtils.getTaskInput(Collections.emptyMap(), workflowModel, taskDef, null);
 Assert.assertEquals(Collections.emptyMap(), actualOutput);
 }

 @Test
 public void testGetTaskInputV2_withValidInputParamsAndWorkflowModelAndTaskDef_shouldReturnExpectedMap() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("key1", "value1");
 WorkflowModel expectedWorkflowModel = new WorkflowModel();
 expectedWorkflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
 expectedWorkflowModel.setTasks(
 List.of(new TaskModel().setInputData(Collections.singletonMap("taskInputKey", "taskInputValue"))));
 TaskDef expectedTaskDef = new TaskDef();
 expectedTaskDef.setInputTemplate(Collections.singletonMap("key2", "value2"));
 Map<String, Object> expectedOutput =
 parametersUtils.getTaskInputV2(inputParams, expectedWorkflowModel, "taskId", expectedTaskDef);
 Map<String, Object> actualOutput =
 parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputV2_withNullWorkflowModel_shouldReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();
 Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Object> actualOutput =
 parametersUtils.getTaskInputV2(inputParams, null, "taskId", taskDef);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputV2_withNullTaskDef_shouldReturnEmptyMap() {
 Map<String, Object> inputParams = new HashMap<>();Map<String, Object> expectedOutput = new HashMap<>();
 Map<String, Object> actualOutput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTaskInputV2_withEmptyInputParams_shouldReturnEmptyMap() {
 Map<String, Object> actualOutput =
 parametersUtils.getTaskInputV2(Collections.emptyMap(), workflowModel, "taskId", taskDef);
 Assert.assertEquals(Collections.emptyMap(), actualOutput);
 }

 @Test
 public void testReplace_withNullInputAndJson_shouldReturnNull() {
 Object actualOutput = parametersUtils.replace(null, null);
 Assert.assertNull(actualOutput);
 }

 @Test
 public void testReplace_withEmptyStringAndJson_shouldReturnEmptyMap() {
 DocumentContext jsonContext = new DocumentContext(new Object());
 Object actualOutput = parametersUtils.replaceVariables("", jsonContext, null);
 Assert.assertEquals(Collections.emptyMap(), actualOutput);
 }

 @Test
 public void testReplace_withValidInputAndJson_shouldReturnExpectedMap() {
 DocumentContext jsonContext = new DocumentContext(new Object());
 String input = "{\"key1\":\"value1\",\"key2\":\"${workflow.input.variable1}\"}";
 Object actualOutput = parametersUtils.replaceVariables(input, jsonContext, null);
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("key1", "value1");
 expectedOutput.put("key2", "variable1Value");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplace_withValidInputAndJsonWithException_shouldReturnExpectedMapWithNullValue() {
 DocumentContext jsonContext = new DocumentContext(new Object());
 String input = "{\"key1\":\"value1\",\"key2\":\"${workflow.input.nonExistentVariable}\"}";
 Object actualOutput = parametersUtils.replaceVariables(input, jsonContext, null);
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("key1", "value1");
 expectedOutput.put("key2", null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplace_withValidInputString_shouldReturnExpectedString() {
 String input = "${workflow.input.variable1}";
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 String actualOutput = (String) parametersUtils.replaceVariables(input, documentContext, null).get("key1");
 String expectedOutput = "variable1Value";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplace_withValidInputStringWithException_shouldReturnExpectedStringWithEmptyString() {
 String input = "${workflow.input.nonExistentVariable}";
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 String actualOutput = (String) parametersUtils.replaceVariables(input, documentContext, null).get("key1");
 String expectedOutput = "";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceList_withValidInputList_shouldReturnExpectedList() {
 List<Object> inputList = List.of("${workflow.input.variable1}", "${workflow.input.variable2}");
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 List<Object> actualOutput = (List<Object>) parametersUtils.replaceList(inputList, null, documentContext);
 List<Object> expectedOutput = List.of("variable1Value", "variable2Value");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceList_withNullInputList_shouldReturnEmptyList() {
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 List<Object> actualOutput = (List<Object>) parametersUtils.replaceList(null, null, documentContext);
 List<Object> expectedOutput = Collections.emptyList();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceList_withEmptyInputList_shouldReturnEmptyList() {
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 List<Object> actualOutput = (List<Object>) parametersUtils.replaceList(Collections.emptyList(), null, documentContext);
 List<Object> expectedOutput = Collections.emptyList();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceVariables_withValidInputStringAndJson_shouldReturnExpectedString() {
 String input = "${workflow.input.variable1}${workflow.input.variable2}${workflow.input.variable3}";
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 String actualOutput = (String) parametersUtils.replaceVariables(input, documentContext, null).get("key1");
 String expectedOutput = "variable1Valuevariable2Valuevariable3Value";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testReplaceVariables_withValidInputStringAndJsonWithException_shouldReturnExpectedStringWithEmptyString() {
 String input = "${workflow.input.variable1}${workflow.input.nonExistentVariable}${workflow.input.variable3}";
 Object json = new Object();
 DocumentContext documentContext = JsonPath.parse(json);
 String actualOutput = (String) parametersUtils.replaceVariables(input, documentContext, null).get("key1");
 String expectedOutput = "";
 Assert.assertEquals(expectedOutput, actualOutput);
 }
}