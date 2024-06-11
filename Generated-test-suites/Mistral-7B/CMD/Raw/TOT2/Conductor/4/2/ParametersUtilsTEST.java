import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstance;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

 private ParametersUtils utils;
 private ObjectMapper objectMapper;
 private WorkflowDef workflowDef;
 private TaskDef taskDef;
 private WorkflowModel workflowModel;
 private WorkflowInstance workflowInstance;

 @Before
 public void setup() {
 utils = new ParametersUtils(objectMapper);
 workflowDef = Mockito.mock(WorkflowDef.class);
 taskDef = Mockito.mock(TaskDef.class);
 workflowModel = new WorkflowModel();
 workflowInstance = new WorkflowInstance();
 }

 @Test
 public void testGetTaskInputWithValidInputParamsAndWorkflowModel() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflowParam", "workflowValue");
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setInput(new HashMap<String, Object>() {{
 put("workflowParam", "expectedWorkflowValue");
 }});
 workflowModel.setTasks(Arrays.asList(
 new TaskModel() {{
 put("inputData", new HashMap<String, Object>() {{
 put("taskParam", "expectedTaskValue");
 }});
 put("referenceTaskName", "taskReferenceName");
 }}));

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("workflowParam", "expectedWorkflowValue");
 expectedResult.putAll(workflowModel.getTasks().get(0).getInputData());

 Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDef, null);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputWithNullWorkflowModel() {
 Map<String, Object> inputParams = new HashMap<>();
 Map<String, Object> expectedResult = new HashMap<>();

 Map<String, Object> result = utils.getTaskInput(inputParams, null, taskDef, null);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputWithNullTaskDefinition() {
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setInput(new HashMap<String, Object>() {{
 put("workflowParam", "expectedWorkflowValue");
 }});
 workflowModel.setTasks(Arrays.asList(
 new TaskModel() {{
 put("inputData", new HashMap<String, Object>() {{
 put("taskParam", "expectedTaskValue");
 }});
 put("referenceTaskName", "taskReferenceName");
 }}));

 Map<String, Object> expectedResult = new LinkedHashMap<>(workflowModel.getTasks().size() + 1) {{
 put("workflowParam", "expectedWorkflowValue");
 putAll(workflowModel.getTasks().get(0).getInputData());
 }};

 Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, null, null);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputWithValidInputParamsAndTaskDefinitionAndNullWorkflowModel() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflowParam", "workflowValue");
 TaskDef taskDef = new TaskDef();
 taskDef.setInputTemplate(new HashMap<String, Object>() {{
 put("taskParam", "expectedTaskValue");
 }});

 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("workflowParam", null);
 expectedResult.putAll(taskDef.getInputTemplate());

 Map<String, Object> result = utils.getTaskInput(inputParams, null, taskDef, null);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testReplaceWithEmptyInputParams() {
 String inputString = "${workflow.input.workflowParam}";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "taskId";

 Map<String, Object> inputParams = new HashMap<>();
 Object expectedReplacement = "";

 Map<String, Object> result = utils.replace(inputParams, documentContext, taskId);
 Assert.assertEquals(expectedReplacement, result.get("workflow.input.workflowParam"));
 }

 @Test
 public void testReplaceWithValidJsonPathAndDocumentContext() {
 String inputString = "${workflow.input.workflowParam.taskParam}";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "taskId";

 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("workflowParam", new HashMap<String, Object>() {{
 put("taskParam", "expectedTaskValue");
 }});

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.workflowParam.taskParam", "expectedInputValue");

 Object expectedReplacement = "expectedInputValue";

 Mockito.when(documentContext.read("$.workflowParam.taskParam")).thenReturn("expectedDocumentContextValue");

 Map<String, Object> result = utils.replace(inputParams, documentContext, taskId);
 Assert.assertEquals(expectedReplacement, result.get("workflow.input.workflowParam.taskParam"));
 }

 @Test
 public void testReplaceWithNullDocumentContext() {
 String inputString = "${workflow.input.workflowParam.taskParam}";
 String taskId = "taskId";

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.workflowParam.taskParam", "expectedInputValue");

 Object expectedReplacement = null;

 Map<String, Object> result = utils.replace(inputParams, null, taskId);
 Assert.assertEquals(expectedReplacement, result.get("workflow.input.workflowParam.taskParam"));
 }

 @Test
 public void testReplaceWithInvalidJsonPathAndDocumentContext() {
 String inputString = "${workflow.input.workflowParam.invalidParamPath}";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "taskId";

 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("workflowParam", new HashMap<String, Object>() {{
 put("taskParam", "expectedTaskValue");
 }});

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.workflowParam.invalidParamPath", "expectedInputValue");

 Mockito.when(documentContext.read("$.workflowParam.invalidParamPath")).thenThrow(new RuntimeException());

 Map<String, Object> result = utils.replace(inputParams, documentContext, taskId);
 Assert.assertEquals(null, result.get("workflow.input.workflowParam.invalidParamPath"));
 }

 @Test
 public void testReplaceListWithValidJsonPathAndDocumentContext() {
 String inputString = "[${workflow.tasks[0].output.outputParam}]";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "taskId";

 List<Object> expectedReplacement = Arrays.asList("expectedDocumentContextValue1", "expectedDocumentContextValue2");

 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setTasks(Arrays.asList(
 new TaskModel() {{
 put("outputData", new HashMap<String, Object>() {{
 put("outputParam", "expectedDocumentContextValue1");
 }});
 put("referenceTaskName", "taskReferenceName1");
 }},
 new TaskModel() {{
 put("outputData", new HashMap<String, Object>() {{
 put("outputParam", "expectedDocumentContextValue2");
 }});
 put("referenceTaskName", "taskReferenceName2");
 }}));
 workflowModel.setTasks(workflowInstance.getTasks());

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.tasks[0].output.outputParam", "expectedInputValue");
 inputParams.put("workflow.tasks[1].output.outputParam", "expectedInputValue");

 Mockito.when(documentContext.read("$.workflow.tasks[0].output.outputParam")).thenReturn("expectedDocumentContextValue1");
 Mockito.when(documentContext.read("$.workflow.tasks[1].output.outputParam")).thenReturn("expectedDocumentContextValue2");

 List<Object> result = utils.replaceList((List<Object>)Arrays.asList("expectedInputValue1", "expectedInputValue2"), taskId, documentContext);
 Assert.assertEquals(expectedReplacement, result);
 }

 @Test
 public void testReplaceListWithNullDocumentContext() {
 String inputString = "[${workflow.tasks[0].output.outputParam}]";
 String taskId = "taskId";

 List<Object> expectedReplacement = Arrays.asList("expectedInputValue1", "expectedInputValue2");
 List<Object> inputList = Arrays.asList("expectedInputValue1", "expectedInputValue2");

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.tasks[0].output.outputParam", "expectedInputValue1");
 inputParams.put("workflow.tasks[1].output.outputParam", "expectedInputValue2");

 List<Object> result = utils.replaceList(inputList, taskId, null);
 Assert.assertEquals(expectedReplacement, result);
 }

 @Test
 public void testReplaceListWithInvalidJsonPathAndDocumentContext() {
 String inputString = "[${workflow.tasks[0].output.invalidOutputParam}]";
 DocumentContext documentContext = Mockito.mock(DocumentContext.class);
 String taskId = "taskId";

 List<Object> expectedReplacement = Arrays.asList("expectedDocumentContextValue1", "expectedDocumentContextValue2");
 List<Object> inputList = Arrays.asList("expectedInputValue1", "expectedInputValue2");

 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setTasks(Arrays.asList(
 new TaskModel() {{
 put("outputData", new HashMap<String, Object>() {{
 put("outputParam", "expectedDocumentContextValue1");
 }});
 put("referenceTaskName", "taskReferenceName1");
 }},
 new TaskModel() {{
 put("outputData", new HashMap<String, Object>() {{
 put("outputParam", "expectedDocumentContextValue2");
 }});
 put("referenceTaskName", "taskReferenceName2");
 }}));
 workflowModel.setTasks(workflowInstance.getTasks());

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.tasks[0].output.invalidOutputParam", "expectedInputValue1");
 inputParams.put("workflow.tasks[1].output.invalidOutputParam", "expectedInputValue2");

 Mockito.when(documentContext.read("$.workflow.tasks[0].output.invalidOutputParam")).thenThrow(new RuntimeException());
 Mockito.when(documentContext.read("$.workflow.tasks[1].output.invalidOutputParam")).thenThrow(new RuntimeException());

 List<Object> result = utils.replaceList(inputList, taskId, documentContext);
 Assert.assertEquals(inputList, result);
 }
}