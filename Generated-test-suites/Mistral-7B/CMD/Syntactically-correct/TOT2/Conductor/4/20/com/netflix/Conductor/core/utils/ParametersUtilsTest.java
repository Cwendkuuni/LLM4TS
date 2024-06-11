import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.utils.WorkflowModel;
import com.netflix.conductor.core.utils.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.utils.EnvUtils;
import com.netflix.conductor.utils.TaskUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ParametersUtilsTest {

 private final ObjectMapper objectMapper = mock(ObjectMapper.class);
 private final WorkflowModel workflow = mock(WorkflowModel.class);
 private final TaskDef taskDefinition = mock(TaskDef.class);
 private final String taskId = "testTaskId";

 @Test
 public void testGetTaskInput() {
 // Basic test case for getTaskInput() method
 Map<String, Object> inputParams = new HashMap<>();
 WorkflowModel workflow = new WorkflowModel();
 TaskDef taskDefinition = new TaskDef();
 ParametersUtils utils = new ParametersUtils(objectMapper);
 Map<String, Object> expectedOutput = new HashMap<>();
 expectedOutput.put("testKey", "testValue");
 when(objectMapper.readValue(anyString(), map.getType())).thenReturn(expectedOutput);
 when(workflow.getInput()).thenReturn(Collections.singletonMap("testKey", "testValue"));
 when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("testKey", "testValue"));

 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testGetTaskInputWithNullInputParams() {
 // Test case for getTaskInput() method with null inputParams
 ParametersUtils utils = new ParametersUtils(objectMapper);

 Map<String, Object> result = utils.getTaskInput(null, workflow, taskDefinition, taskId);
 assertNull(result);
 }

 @Test
 public void testGetTaskInputWithNullWorkflow() {
 // Test case for getTaskInput() method with null workflow
 ParametersUtils utils = new ParametersUtils(objectMapper);

 Map<String, Object> result = utils.getTaskInput(inputParams, null, taskDefinition, taskId);
 assertNull(result);
 }

 @Test
 public void testGetTaskInputWithNullTaskDefinition() {
 // Test case for getTaskInput() method with null taskDefinition
 ParametersUtils utils = new ParametersUtils(objectMapper);

 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, null, taskId);
 assertNull(result);
 }

 @Test
 public void testGetTaskInputWithWorkflowSchemaVersionGreaterThanOne() {
 // Test case for getTaskInput() method with workflow schema version greater than one
 ParametersUtils utils = new ParametersUtils(objectMapper);
 when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2L);

 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 // Since getTaskInputV2() method is called in this scenario, we cannot directly assert
 // the result as we don't have the exact expected output. Instead we can test for the
 // presence of certain keys in the result.
 assertNotNull(result);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testGetTaskInputWithEmptyTasksList() {
 // Test case for getTaskInput() method with empty tasks list in workflow
 ParametersUtils utils = new ParametersUtils(objectMapper);
 WorkflowModel workflow = new WorkflowModel();
 TaskDef taskDefinition = new TaskDef();
 workflow.setTasks(Collections.emptyList());

 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
 assertNull(result);
 }

 @Test
 public void testReplaceVariablesWithValidInput() {
 // Test case for replaceVariables() method with valid input
 String paramString = "$${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", new HashMap<>());
 documentContext.put("workflow.output", new HashMap<>());
 documentContext.put("workflow.output.task1", new HashMap<>());
 documentContext.put("workflow.output.task1.output", new HashMap<>());
 documentContext.put("workflow.output.task1.output.testKey", "testValue");

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertEquals("testValue", replacement);
 }

 @Test
 public void testReplaceVariablesWithInvalidInput() {
 // Test case for replaceVariables() method with invalid input
 String paramString = "InvalidInput";
 Map<String, Object> documentContext = new HashMap<>();

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContext() {
 // Test case for replaceVariables() method with empty documentContext
 String paramString = "${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = new HashMap<>();

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithNullDocumentContext() {
 // Test case for replaceVariables() method with null documentContext
 String paramString = "${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = null;

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyWorkflowOutput() {
 // Test case for replaceVariables() method with empty workflow output
 String paramString = "${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", new HashMap<>());
 documentContext.put("workflow.output", new HashMap<>());

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithEmptyTaskOutput() {
 // Test case for replaceVariables() method with empty task output
 String paramString = "${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", new HashMap<>());
 documentContext.put("workflow.output", new HashMap<>());
 documentContext.put("workflow.output.task1", new HashMap<>());

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithNullTaskOutput() {
 // Test case for replaceVariables() method with null task output
 String paramString = "${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", new HashMap<>());
 documentContext.put("workflow.output", new HashMap<>());
 documentContext.put("workflow.output.task1", null);

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithEnvironmentVariable() {
 // Test case for replaceVariables() method with environment variable
 String paramString = " ${ENV_VARIABLE}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("ENV_VARIABLE", "testValue");

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertEquals("testValue", replacement);
 }

 @Test
 public void testReplaceVariablesWithUnresolvedEnvironmentVariable() {
 // Test case for replaceVariables() method with unresolved environment variable
 String paramString = " ${UNRESOLVED_ENV_VARIABLE}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("ENV_VARIABLE", "testValue");

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithUnresolvedJsonPath() {
 // Test case for replaceVariables() method with unresolved json path
 String paramString = "$${workflow.output.unresolvedJsonPath}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", new HashMap<>());

 ParametersUtils utils = new ParametersUtils();
 assertThrows(IllegalArgumentException.class, () -> utils.replaceVariables(paramString, documentContext, taskId));
 }

 @Test
 public void testReplaceVariablesWithEmptyDocumentContextPath() {
 // Test case for replaceVariables() method with empty documentContext path
 String paramString = "${workflow.output.task1.output.testKey}";
 Map<String, Object> documentContext = new HashMap<>();
 documentContext.put("workflow", new HashMap<>());
 documentContext.put("workflow.output", new HashMap<>());
 documentContext.put("workflow.output.task1", new HashMap<>());
 documentContext.put("workflow.output.task1.output", null);

 ParametersUtils utils = new ParametersUtils();
 Object replacement = utils.replaceVariables(paramString, documentContext, taskId);
 assertNull(replacement);
 }
}