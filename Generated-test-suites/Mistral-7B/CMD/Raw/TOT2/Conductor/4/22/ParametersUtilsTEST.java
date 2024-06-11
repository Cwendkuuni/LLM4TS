import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

 private final ParametersUtils parametersUtils;
 private final ObjectMapper objectMapper;

 @Autowired
 public ParametersUtilsTest(ParametersUtils parametersUtils, ObjectMapper objectMapper) {
 this.parametersUtils = parametersUtils;
 this.objectMapper = objectMapper;
 }

 @Test
 public void testGetTaskInput_NullWorkflow_ReturnsEmptyMap() {
 // Given
 WorkflowModel workflow = null;
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = mock(TaskDef.class);
 String taskId = "testTaskId";

 // When
 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

 // Then
 Assertions.assertEquals(Collections.emptyMap(), result);
 }

 @Test
 public void testGetTaskInput_NullTaskDefinition_ReturnsInputParams() {
 // Given
 WorkflowModel workflow = mock(WorkflowModel.class);
 Map<String, Object> inputParams = new LinkedHashMap<>();
 inputParams.put("key1", "value1");
 String taskId = "testTaskId";

 // When
 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, null, taskId);

 // Then
 Assertions.assertEquals(inputParams, result);
 }

 @Test
 public void testGetTaskInput_ValidInput_ReturnsReplacedInputMap() {
 // Given
 WorkflowModel workflow = mock(WorkflowModel.class);
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("key1", "value1");
 Map<String, Object> inputParams = new LinkedHashMap<>();
 inputParams.put("$workflow.key1", "${workflow.key1}");
 inputParams.put("input.key2", "inputValue2");
 String taskId = "testTaskId";
 TaskDef taskDefinition = mock(TaskDef.class);
 when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());
 when(workflow.getInput()).thenReturn(workflowInput);

 // When
 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

 // Then
 Map<String, Object> expectedResult = new LinkedHashMap<>();
 expectedResult.put("key1", "value1");
 expectedResult.put("input.key2", "inputValue2");
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInput_ValidInputWithTemplate_ReturnsReplacedInputMapWithTemplateValues() {
 // Given
 WorkflowModel workflow = mock(WorkflowModel.class);
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("key1", "value1");
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("key1", "templateValue1");
 when(workflow.getInput()).thenReturn(workflowInput);
 when(workflow.getInputTemplate()).thenReturn(inputTemplate);
 Map<String, Object> inputParams = new LinkedHashMap<>();
 inputParams.put("$workflow.key1", "${workflow.key1}");
 inputParams.put("input.key2", "inputValue2");
 String taskId = "testTaskId";
 TaskDef taskDefinition = mock(TaskDef.class);
 when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());

 // When
 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

 // Then
 Map<String, Object> expectedResult = new LinkedHashMap<>();
 expectedResult.put("key1", "templateValue1");
 expectedResult.put("input.key2", "inputValue2");
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace_NullJson_ReturnsOriginalString() {
 // Given
 String inputString = "test string";

 // When
 Object result = parametersUtils.replace(inputString);
 // Then
 Assertions.assertEquals(inputString, result);
 }

 @Test
 public void testReplace_EmptyJson_ReturnsOriginalString() {
 // Given
 String inputString = "{}";
 // When
 Object result = parametersUtils.replace(inputString); // Then
 Assertions.assertEquals(inputString, result);
 }

 @Test
 public void testReplace_ValidJson_ReturnsReplacedString() {
 // Given
 String inputString = "{\"key1\": \"value1\", \"key2\": \"${workflow.key1}\"}";
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("key1", "templateValue1");
 WorkflowModel workflow = mock(WorkflowModel.class);
 when(workflow.getInput()).thenReturn(workflowInput);
 // When
 Object result = parametersUtils.replace(inputString, workflow);
 // Then
 String expectedResult = "{\"key1\": \"templateValue1\", \"key2\": \"templateValue1\"}";
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testReplaceList_EmptyList_ReturnsEmptyList() {
 // Given
 List<Object> inputList = Collections.emptyList();
 // When
 List<Object> result = parametersUtils.replaceList(inputList, "testTaskId", null);
 // Then
 Assertions.assertEquals(inputList, result);
 }

 @Test
 public void testReplaceList_NullJson_ReturnsOriginalList() {
 // Given
 List<Object> inputList = new LinkedList<Object>() {{
 add("test string 1");
 add("test string 2");
 }};
 // When
 List<Object> result = parametersUtils.replaceList(inputList, "testTaskId", null);
 // Then
 Assertions.assertEquals(inputList, result);
 }

 @Test
 public void testReplaceList_ValidJson_ReturnsReplacedList() {
 // Given
 List<Object> inputList = new LinkedList<Object>() {{
 add("{\"key1\": \"value1\", \"key2\": \"${workflow.key1}\"}");
 add("test string 2");
 }};
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("key1", "templateValue1");
 WorkflowModel workflow = mock(WorkflowModel.class);
 when(workflow.getInput()).thenReturn(Collections.emptyMap());
 when(workflow.getInputTemplate()).thenReturn(workflowInput);
 // When
 List<Object> result = parametersUtils.replaceList(inputList, "testTaskId", workflow);
 // Then
 List<Object> expectedResult = new LinkedList<Object>() {{
 add("{\"key1\": \"templateValue1\", \"key2\": \"templateValue1\"}");
 add("test string 2");
 }};
 Assertions.assertEquals(expectedResult, result);
 }
}