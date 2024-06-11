import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ParametersUtilsTest {

 private ObjectMapper objectMapper;
 private ParametersUtils parametersUtils;
 private WorkflowDef workflowDef;
 private WorkflowModel workflowModel;
 private TaskDef taskDef;

 @Before
 public void setup() {
 objectMapper = Mockito.mock(ObjectMapper.class);
 parametersUtils = new ParametersUtils(objectMapper);
 workflowDef = mock(WorkflowDef.class);
 workflowModel = mock(WorkflowModel.class);
 taskDef = mock(TaskDef.class);
 }

 @Test
 public void testGetTaskInputV1() {
 given(workflowModel.getInput())
         .willReturn(Collections.singletonMap("testKey", "testValue"));
 given(workflowModel.getTasks())
         .willReturn(Collections.singletonList(taskDef));
 given(taskDef.getInputData())
         .willReturn(Collections.singletonMap("testKey2", "testValue2"));

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.testKey", "workflowValue");
 inputParams.put("task.test", taskDef);

 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("workflow.testKey", "workflowValue");
 expectedOutput.put("task.test.testKey2", "testValue2");

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testGetTaskInputV2() {
 WorkflowModel workflowModelWithTasks = mock(WorkflowModel.class);
 given(workflowModelWithTasks.getTasks())
         .willReturn(Collections.singletonList(taskDef));

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.testKey", "workflowValue");
 inputParams.put("workflow.tasks.test.input.testKey2", "testValue2");

 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("workflow.input.testKey", "workflowValue");
 expectedOutput.put("workflow.tasks.test.input.testKey2", "testValue2");
 expectedOutput.put("workflow.status", null);
 expectedOutput.put("workflow.workflowId", null);
 expectedOutput.put("workflow.parentWorkflowId", null);
 expectedOutput.put("workflow.parentWorkflowTaskId", null);
 expectedOutput.put("workflow.workflowName", null);
 expectedOutput.put("workflow.workflowVersion", null);
 expectedOutput.put("workflow.correlationId", null);
 expectedOutput.put("workflow.reasonForIncompletion", null);

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModelWithTasks, null, null);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testGetTaskInputWithTaskDefinitionInputTemplate() {
 WorkflowModel workflowModelWithTasks = mock(WorkflowModel.class);
 given(workflowModelWithTasks.getTasks())
         .willReturn(Collections.singletonList(taskDef));
 given(taskDef.getInputTemplate())
         .willReturn(Collections.singletonMap("testKey3", "testValue3"));

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.testKey", "workflowValue");
 inputParams.put("workflow.tasks.test.input.testKey2", "testValue2");
 inputParams.put("workflow.tasks.test.inputTemplate.testKey3", null);

 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("workflow.input.testKey", "workflowValue");
 expectedOutput.put("workflow.tasks.test.input.testKey2", "testValue2");
 expectedOutput.put("workflow.tasks.test.input.testKey3", "testValue3");

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModelWithTasks, null, null);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testReplaceVariables() {
 String paramString = "${workflow.input.testKey}.${workflow.tasks.test.input.testKey2}";
 DocumentContext documentContext = mock(DocumentContext.class);
 given(documentContext.read("$.workflow.input.testKey"))
         .willReturn("workflowValue");
 given(documentContext.read("$.workflow.tasks.test.input.testKey2"))
         .willReturn("testValue2");

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals("workflowValue.testValue2", replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyString() {
 String paramString = "${}";
 DocumentContext documentContext = mock(DocumentContext.class);

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals("", replacement);
 }

 @Test
 public void testReplaceVariablesWithEnvVariables() {
 String paramString = "${TEST_ENV_VAR}";
 DocumentContext documentContext = mock(DocumentContext.class);
 given(EnvUtils.getSystemParametersValue(paramString, null))
         .willReturn("envValue");

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals("envValue", replacement);
 }

 @Test
 public void testReplaceVariablesWithNullValue() {
 String paramString = "${workflow.input.testKey}";
 DocumentContext documentContext = mock(DocumentContext.class);
 given(documentContext.read("$.workflow.input.testKey"))
         .willReturn(null);

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals(null, replacement);
 }

 @Test
 public void testReplaceVariablesWithException() {
 String paramString = "${workflow.input.testKey}";
 DocumentContext documentContext = mock(DocumentContext.class);
 given(documentContext.read("$.workflow.input.testKey"))
         .willThrow(new RuntimeException("Test exception"));

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals(null, replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContext() {
 String paramString = "${workflow.input.testKey}";
 DocumentContext documentContext = mock(DocumentContext.class);
 given(documentContext.read(Mockito.anyString()))
         .willReturn(null);

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals(null, replacement);
 }

 @Test
 public void testReplaceVariablesWithComplexJsonPath() {
 String paramString = "${workflow.input.testKey.testSubKey.testSubSubKey}";
 DocumentContext documentContext = mock(DocumentContext.class);
 given(documentContext.read("$.workflow.input.testKey.testSubKey.testSubSubKey"))
         .willReturn("subValue");

 Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
 assertEquals("subValue", replacement);
 }
}