import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

public class ParametersUtilsTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
 private ObjectMapper objectMapper;
 private ParametersUtils parametersUtils;
 private WorkflowModel workflowModel;
 private WorkflowDef workflowDef;
 private TaskDef taskDef;

 @Before
 public void setup() {
 objectMapper = mock(ObjectMapper.class);
 parametersUtils = new ParametersUtils(objectMapper);
 workflowDef = new WorkflowDef();
 workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefinition(workflowDef);
 taskDef = mock(TaskDef.class);
 }

 @Test
 public void testGetTaskInput() throws Exception {
 // Test with null input, workflow, and taskDefinition
 Map<String, Object> inputParams = null;
 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
 assertNull(result);

 // Test with null workflow and taskDefinition
 inputParams = new HashMap<>();
 workflowModel = null;
 result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
 assertNull(result);

 // Test with null taskDefinition
 inputParams = new HashMap<>();
 workflowModel = new WorkflowModel();
 taskDef = null;
 result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
 assertNull(result);

 // Test with valid input, workflow, and taskDefinition
 inputParams = new HashMap<>();
 inputParams.put("testKey", "testValue");
 workflowDef.setInput(Collections.singletonMap("testKey", "testValue"));
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey2", "testValue2"));
 result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("testKey", "testValue");
 expectedResult.put("testKey2", "testValue2");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTaskInputV2() throws Exception {
 // Test with null input, workflow, and taskDefinition
 Map<String, Object> inputParams = null;
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("testKey", "testValue");
 workflowDef.setInputTemplate(workflowInput);
 when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey2", "testValue2"));
 Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
 assertNull(result);

 // Test with null workflow and taskDefinition
 inputParams = new HashMap<>();
 workflowModel = null;
 result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
 assertNull(result);

 // Test with null taskDefinition
 inputParams = new HashMap<>();
 workflowModel = new WorkflowModel();
 taskDef = null;
 result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
 assertNull(result);

 // Test with valid input, workflow, and taskDefinition
 inputParams = new HashMap<>();
 inputParams.put("testKey", "testValue");
 workflowInput = new HashMap<>();
 workflowInput.put("testKey", "testValue");
 workflowDef.setInputTemplate(workflowInput);
 List<TaskModel> tasks = new LinkedList<>();
 tasks.add(new TaskModel());
 workflowModel.setTasks(tasks);
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("testKey2", "testValue2");
 when(taskDef.getInputTemplate()).thenReturn(taskInput);
 result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
 Map<String, Object> expectedResult = new HashMap<>();
 expectedResult.put("testKey", "testValue");
 expectedResult.put("testKey2", "testValue2");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testReplace() {
 String paramString = "${workflow.input.testKey} ${task.input.testKey2}";
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("testKey", "testValue");
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("testKey2", "testValue2");
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());
 Map<String, Object> result = parametersUtils.replace(paramString, documentContext, null);
 assertEquals("testValue testValue2", result);

 documentContext = JsonPath.parse(workflowInput);
 result = parametersUtils.replace(paramString, documentContext, null);
 assertEquals("testValue testValue2", result);

 documentContext = JsonPath.parse(Collections.singletonMap("testKey2", "testValue3"));
 result = parametersUtils.replace(paramString, documentContext, "taskId");
 assertEquals("testValue testValue3", result);
 }

 @Test
 public void testReplaceList() {
 List<String> values = new LinkedList<>();
 values.add("${workflow.input.testKey}");
 values.add("${task.input.testKey2}");
 String paramString = String.join(" ", values.toArray(new String[0]));
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("testKey", "testValue");
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("testKey2", "testValue2");
 DocumentContext documentContext = JsonPath.parse(workflowInput);
 Map<String, Object> result = parametersUtils.replaceList(values, "taskId", documentContext);
 assertEquals(Arrays.asList("testValue", "testValue2"), result);
 }

 @Test
 public void testReplaceVariables() throws Exception {
 String paramString = "${workflow.input.testKey} ${task.input.testKey2}";
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("testKey", "testValue");
 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("testKey2", "testValue2");
 DocumentContext documentContext = JsonPath.parse(workflowInput);
 String result = (String) MethodUtils.invokeStaticMethod(ParametersUtils.class, "replaceVariables", paramString, documentContext, null);
 assertEquals("testValue testValue2", result);

 documentContext = JsonPath.parse(Collections.singletonMap("testKey2", "testValue3"));
 result = (String) MethodUtils.invokeStaticMethod(ParametersUtils.class, "replaceVariables", paramString, documentContext, "taskId");
 assertEquals("testValue testValue3", result);
 }
}