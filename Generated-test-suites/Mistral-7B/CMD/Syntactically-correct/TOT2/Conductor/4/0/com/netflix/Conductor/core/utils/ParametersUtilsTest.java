import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.workflow.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowModel.WorkflowInput;

@SpringBootTest
public class ParametersUtilsTest {

 @Autowired
 private ParametersUtils parametersUtils;

 @Test
 public void testGetTaskInput() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInput workflowInput = new WorkflowInput();
 workflowInput.put("inputParam1", "value1");
 workflowModel.setInput(workflowInput);

 TaskDef taskDef = new TaskDef();
 Map<String, Object> taskInputTemplate = new HashMap<>();
 taskInputTemplate.put("inputParam2", "value2");
 taskDef.setInputTemplate(taskInputTemplate);

 Map<String, Object> expectedTaskInput = new HashMap<>();
 expectedTaskInput.put("taskInputParam1", "value1");
 expectedTaskInput.put("taskInputParam2", "value2");

 Map<String, Object> actualTaskInput =
 parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");
 Assertions.assertEquals(expectedTaskInput, actualTaskInput);
 }

 @Test
 public void testGetTaskInputWithNullWorkflowModel() {
 Map<String, Object> expectedTaskInput = new HashMap<>();
 Map<String, Object> actualTaskInput = parametersUtils.getTaskInput(null, null, null, "taskId");
 Assertions.assertNull(actualTaskInput);
 Assertions.assertEquals(expectedTaskInput, actualTaskInput);
 }

 @Test
 public void testGetTaskInputWithNullTaskDefinition() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInput workflowInput = new WorkflowInput();
 workflowInput.put("inputParam1", "value1");
 workflowModel.setInput(workflowInput);

 Map<String, Object> expectedTaskInput = new HashMap<>();
 expectedTaskInput.put("inputParam1", "value1");

 Map<String, Object> actualTaskInput =
 parametersUtils.getTaskInput(null, workflowModel, null, "taskId");
 Assertions.assertEquals(expectedTaskInput, actualTaskInput);
 }

 @Test
 public void testGetTaskInputWithEmptyInputParamMap() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInput workflowInput = new WorkflowInput();
 workflowInput.put("inputParam1", "value1");
 workflowModel.setInput(workflowInput);

 TaskDef taskDef = new TaskDef();
 Map<String, Object> taskInputTemplate = new HashMap<>();

 Map<String, Object> expectedTaskInput = new HashMap<>();

 Map<String, Object> actualTaskInput =
 parametersUtils.getTaskInput(new HashMap<>(), workflowModel, taskDef, "taskId");
 Assertions.assertEquals(expectedTaskInput, actualTaskInput);
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() {
 String inputString = "${workflow.input.inputParam1}";
 Object actualReplacedString = parametersUtils.replaceVariables(inputString, null, "taskId");
 Assertions.assertEquals(inputString, actualReplacedString);
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContext() {
 String inputString = "${workflow.input.inputParam1}";
 DocumentContext documentContext = new DocumentContext(new Object());
 Object actualReplacedString = parametersUtils.replaceVariables(inputString, documentContext, "taskId");
 Assertions.assertEquals(inputString, actualReplacedString);
 }

 @Test
 public void testReplaceVariablesWithValidDocumentContext() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInput workflowInput = new WorkflowInput();
 WorkflowInput.Input input = new WorkflowInput.Input();
 input.put("inputParam1", "value1");
 workflowInput.put("input", input);
 workflowModel.setInput(workflowInput);

 String inputString = "${workflow.input.inputParam1}";
 DocumentContext documentContext =
 JsonPath.parse(new HashMap<>() {{ put("workflow", workflowModel); }}, Configuration.defaultConfiguration());
 Object actualReplacedString = parametersUtils.replaceVariables(inputString, documentContext, "taskId");
 Assertions.assertEquals("value1", actualReplacedString);
 }

 @Test
 public void testReplaceVariablesWithNullEnvironmentVariable() {
 String inputString = "${envVariable}";
 Object actualReplacedString = parametersUtils.replaceVariables(inputString, null, "taskId");
 Assertions.assertEquals(inputString, actualReplacedString);
 }

 @Test
 public void testReplaceVariablesWithValidEnvironmentVariable() {
 String inputString = "${envVariable}";
 String envVariable = "testEnvVariable";
 System.setProperty(envVariable, "value");
 Object actualReplacedString = parametersUtils.replaceVariables(inputString, null, "taskId");
 Assertions.assertEquals("value", actualReplacedString);
 }

 @Test
 public void testReplaceVariablesWithEmptyList() {
 List list = new ArrayList();
 Object actualReplacedList = parametersUtils.replaceList(list, "taskId", null);
 Assertions.assertEquals(list, actualReplacedList);
 }

 @Test
 public void testReplaceVariablesWithValidList() {
 List list = Arrays.asList("item1", "item2");
 Object actualReplacedList = parametersUtils.replaceList(list, "taskId", null);
 List expectedReplacedList = Arrays.asList("item1", "item2");
 Assertions.assertEquals(expectedReplacedList, actualReplacedList);
 }

 @Test
 public void testReplaceVariablesWithNullMap() {
 Map map = null;
 Object actualReplacedMap = parametersUtils.replace(map, null, "taskId");
 Assertions.assertNull(actualReplacedMap);
 }

 @Test
 public void testReplaceVariablesWithEmptyMap() {
 Map map = new HashMap<>();
 Object actualReplacedMap = parametersUtils.replace(map, null, "taskId");
 Assertions.assertEquals(map, actualReplacedMap);
 }

 @Test
 public void testReplaceVariablesWithValidMap() {
 Map<String, Object> map = new HashMap<>();
 map.put("mapKey1", "mapValue1");
 Object actualReplacedMap = parametersUtils.replace(map, null, "taskId");
 Map<String, Object> expectedReplacedMap = new HashMap<>();
 expectedReplacedMap.put("mapKey1", "mapValue1");
 Assertions.assertEquals(expectedReplacedMap, actualReplacedMap);
 }
}