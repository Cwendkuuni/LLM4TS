import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;

public class ParametersUtilsTest {

 private ParametersUtils parametersUtils;
 private ObjectMapper objectMapperMock;

 @Before
 public void setUp() {
 objectMapperMock = mock(ObjectMapper.class);
 parametersUtils = new ParametersUtils(objectMapperMock);
 }

 @Test
 public void testGetTaskInput_basic() {
 WorkflowModel workflow = mock(WorkflowModel.class);
 TaskDef taskDefinition = mock(TaskDef.class);
 Map<String, Object> inputParams = new HashMap<>();

 when(workflow.getInput()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));
 when(workflow.getTasks()).thenReturn(Collections.singletonList(new TaskModel()));
 when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

 assertEquals("inputValue", ReflectionTestUtils.getFieldValue(result, "input.inputKey"));
 }

 @Test
 public void testGetTaskInput_nullInputParams() {
 WorkflowModel workflow = mock(WorkflowModel.class);
 TaskDef taskDefinition = mock(TaskDef.class);

 Map<String, Object> result = parametersUtils.getTaskInput(null, workflow, taskDefinition, "taskId");

 assertTrue(result.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInput_invalidWorkflow() {
 WorkflowModel workflow = mock(WorkflowModel.class);
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = mock(TaskDef.class);

 when(workflow.getInput()).thenThrow(new RuntimeException("Error getting workflow input"));

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

 fail("Expected RuntimeException to be thrown");
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInput_invalidTaskDefinition() {
 WorkflowModel workflow = mock(WorkflowModel.class);
 Map<String, Object> inputParams = new HashMap<>();
 TaskDef taskDefinition = mock(TaskDef.class);
 when(taskDefinition.getInputTemplate()).thenThrow(new RuntimeException("Error getting input template"));

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

 fail("Expected RuntimeException to be thrown");
 }

 @Test
 public void testReplace_basic() {
 String paramString = "${workflow.input.inputKey} ${inputTemplate.inputKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", Collections.singletonMap("input", Collections.singletonMap("inputKey", "workflowValue")));
 documentContext.put("inputTemplate", Collections.singletonMap("inputKey", "templateValue"));

 Object result = parametersUtils.replace(paramString, documentContext, null);

 assertEquals("workflowValue templateValue", result);
 }

 @Test
 public void testReplace_nullDocumentContext() {
 String paramString = "${workflow.input.inputKey} ${inputTemplate.inputKey}";

 Object result = parametersUtils.replace(paramString, null, null);

 assertNull(result);
 }

 @Test(expected = RuntimeException.class)
 public void testReplace_invalidDocumentContext() {
 String paramString = "${workflow.input.inputKey} ${inputTemplate.inputKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", null);

 Object result = parametersUtils.replace(paramString, documentContext, null);

 fail("Expected RuntimeException to be thrown");
 }

 @Test
 public void testReplaceVariables_basic() {
 String paramString = "${workflow.input.inputKey}.${inputTemplate.inputKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", Collections.singletonMap("input", Collections.singletonMap("inputKey", "workflowValue")));
 documentContext.put("inputTemplate", Collections.singletonMap("inputKey", "templateValue"));

 Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

 assertEquals("workflowValue.templateValue", result);
 }

 @Test
 public void testReplaceVariables_emptyDocumentContext() {
 String paramString = "${workflow.input.inputKey}.${inputTemplate.inputKey}";
 Map<String, Object> documentContext = new HashMap<>();

 Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

 assertNull(result);
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceVariables_invalidDocumentContext() {
 String paramString = "${workflow.input.inputKey}.${inputTemplate.inputKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", null);

 Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

 fail("Expected RuntimeException to be thrown");
 }
}