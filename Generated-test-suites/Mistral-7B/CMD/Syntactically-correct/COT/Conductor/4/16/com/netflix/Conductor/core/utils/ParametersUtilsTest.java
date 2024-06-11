import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowInstanceState;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParametersUtilsTest {

 private ParametersUtils parametersUtils;
 private ObjectMapper objectMapper;
 private WorkflowDef workflowDef;
 private WorkflowModel workflowModel;
 private TaskDef taskDef;

 @Before
 public void setup() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 this.objectMapper = Mockito.spy(objectMapper);
 this.parametersUtils = new ParametersUtils(objectMapper);
 this.workflowDef = mock(WorkflowDef.class);
 this.workflowModel = mock(WorkflowModel.class);
 this.taskDef = mock(TaskDef.class);
 }

 @Test
 public void testGetTaskInputV1() {
 // Given
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setWorkflowDefinition(this.workflowDef);
 workflowModel.setWorkflowInstance(workflowInstance);
 workflowModel.setInput(new HashMap<>());
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("inputParam", "workflowValue");
 workflowModel.getInput().put("workflow", workflowInput);
 Map<String, Object> taskInput = parametersUtils.getTaskInputV1(workflowModel, inputParams);

 // When

 // Then
 Assert.assertEquals("workflowValue", taskInput.get("inputParam"));
 }

 @Test
 public void testGetTaskInputV2() {
 // Given
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowInstance workflowInstance = new WorkflowInstance();
 workflowInstance.setWorkflowDefinition(this.workflowDef);
 workflowModel.setWorkflowInstance(workflowInstance);
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setInputTemplate(new HashMap<>());
 workflowDef.getInputTemplate().put("inputParam", "workflowValue");
 workflowModel.setWorkflowDefinition(workflowDef);
 when(workflowDef.getInputTemplate()).thenReturn(workflowDef.getInputTemplate());
 when(workflowDef.getName()).thenReturn("workflowName");
 when(workflowModel.getId()).thenReturn("workflowInstanceId");
 when(taskDef.getName()).thenReturn("taskName");
 when(taskDef.getInputTemplate()).thenReturn(Collections.emptyMap());
 when(workflowModel.getTasks()).thenReturn(Collections.singletonList(new TaskModel()));
 TaskModel taskModel = new TaskModel();
 taskModel.setTaskDefName("taskName");
 taskModel.setReferenceTaskName("taskReferenceName");
 taskModel.setInputData(new LinkedHashMap<>());
 taskModel.getInputData().put("taskInputParam", "taskValue");
 workflowModel.getTasks().get(0).setTaskModel(taskModel);

 // When
 Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, "workflowInstanceId");

 // Then
 Assert.assertEquals("workflowValue", taskInput.get("workflow.input.workflow.inputParam"));
 Assert.assertEquals("taskValue", taskInput.get("tasks.taskReferenceName.input.taskInputParam"));
 }

 @Test
 public void testReplace() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks.taskReferenceName.input.taskInputParam", "taskValue");

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals("workflowValue${tasks.taskReferenceName.input.taskInputParam}", replacedString.toString());
 }

 @Test
 public void testReplaceWithNullContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, null, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithEmptyContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithNullValueContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", null);

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithEmptyValueContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("tasks.taskReferenceName.input.taskInputParam", "");

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithNullValue() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks.taskReferenceName.input.taskInputParam", null);

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals("workflowValue", replacedString.toString());
 }

 @Test
 public void testReplaceWithEmptyStringValue() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks.taskReferenceName.input.taskInputParam", "");

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals("workflowValue", replacedString.toString());
 }

 @Test
 public void testReplaceWithEmptyMapContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithNullMapContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam}";
 Map<String, Object> documentContext = null;

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidJsonPath() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam.notValid}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks.taskReferenceName.input.taskInputParam", "taskValue");

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test
 public void testReplaceWithInvalidJsonPathInContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.taskReferenceName.input.taskInputParam.notValid}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks.taskReferenceName.input.taskInputParam", "taskValue");
 documentContext.put("tasks.taskReferenceName.input.notValid", "notValidValue");

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithEmptyListContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 List<Map<String, Object>> listContext = new LinkedList<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks", listContext);

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals(paramString, replacedString.toString());
 }

 @Test
 public void testReplaceWithEmptyListContextAndValidValues() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].input.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 List<Map<String, Object>> listContext = new LinkedList<>();
 Map<String, Object> taskContext = new LinkedHashMap<>();
 taskContext.put("input", new HashMap<>());
 taskContext.put("input.taskInputParam", "validValue");
 listContext.add(taskContext);
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks", listContext);

 // When
 Object replacedString = parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");

 // Then
 Assert.assertEquals("workflowValue${tasks[0].input.taskInputParam}", replacedString.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidListPath() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks", Collections.singletonList(Collections.emptyMap()));

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithEmptyListContextAndInvalidPath() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 List<Map<String, Object>> listContext = new LinkedList<>();
 listContext.add(Collections.emptyMap());
 documentContext.put("tasks", listContext);

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidListPathAndContext() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks", Collections.singletonList(Collections.emptyMap()));
 documentContext.put("tasks[0].notValid", "notValidValue");

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidListPathAndContextWithEmptyMap() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 List<Map<String, Object>> listContext = new LinkedList<>();
 listContext.add(Collections.emptyMap());
 documentContext.put("tasks", listContext);
 documentContext.put("tasks[0].notValid", new HashMap<>());

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidListPathAndContextWithEmptyList() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 List<Map<String, Object>> listContext = new LinkedList<>();
 documentContext.put("tasks", listContext);

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidListPathAndContextWithNullMap() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 List<Map<String, Object>> listContext = new LinkedList<>();
 listContext.add(null);
 documentContext.put("tasks", listContext);

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceWithInvalidListPathAndContextWithNullList() {
 // Given
 String paramString = "${workflow.input.workflow.inputParam}${tasks.[].notValid.taskInputParam}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow.input.workflow.inputParam", "workflowValue");
 documentContext.put("tasks", null);

 // When
 // Then exception should be thrown
 parametersUtils.replaceVariables(paramString, documentContext, "workflowInstanceId");
 }
}