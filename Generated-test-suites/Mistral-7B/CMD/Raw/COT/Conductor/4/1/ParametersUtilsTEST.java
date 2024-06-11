import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@Component
@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @SuppressWarnings("unchecked")
    private static final ObjectMapper objectMapper = mock(ObjectMapper.class);

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = getWorkflowModel();
        TaskDef taskDefinition = getTaskDefinition();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> expectedResult = getExpectedResult();

        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(expectedResult);

        Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWithNullWorkflow() {
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = getTaskDefinition();
        String taskId = "taskId";

        Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, null, taskDefinition, taskId);

        assertNull(result);
    }

    @Test
    public void testGetTaskInputWithNullTaskDefinition() {
        WorkflowModel workflow = getWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "taskId";

        Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflow, null, taskId);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.name}${${envVar}}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envVar = "envValue";
        String taskId = "taskId";

        when(documentContext.read("$.name")).thenReturn("John Doe");
        when(documentContext.read("$[0].variable")).thenReturn("MY_ENV_VAR");
        when(EnvUtils.getSystemParametersValue("MY_ENV_VAR", taskId)).thenReturn(envVar);

        Object result = new ParametersUtils().replaceVariables(paramString, documentContext, taskId);

        assertEquals("John Doe" + envVar, result);
    }

    @Test
    public void testReplaceVariablesWithEmptyValue() {
        String paramString = "${workflow.input.name}${${envVar}}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envVar = "";
        String taskId = "taskId";

        when(documentContext.read("$.name")).thenReturn("John Doe");
        when(documentContext.read("$[0].variable")).thenReturn(envVar);

        Object result = new ParametersUtils().replaceVariables(paramString, documentContext, taskId);

        assertEquals("John Doe", result);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String paramString = "${workflow.input.name}${${envVar}}";
        String envVar = "envValue";
        String taskId = "taskId";

        Object result = new ParametersUtils().replaceVariables(paramString, null, taskId);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariablesWithEmptyParamString() {
        String paramString = "";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        Object result = new ParametersUtils().replaceVariables(paramString, documentContext, taskId);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceList() {
        List<Object> values = List.of("${workflow.input.name}", "${${envVar}}");
        String taskId = "taskId";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envVar = "envValue";

        when(documentContext.read("$.name")).thenReturn("John Doe");
        when(documentContext.read("$[0].variable")).thenReturn("MY_ENV_VAR");
        when(EnvUtils.getSystemParametersValue("MY_ENV_VAR", taskId)).thenReturn(envVar);

        List<Object> result = new ParametersUtils().replaceList(values, taskId, documentContext);

        List<Object> expectedResult = List.of("John Doe" + envVar, envVar);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceListWithEmptyList() {
        List<Object> values = Collections.emptyList();
        String taskId = "taskId";
        DocumentContext documentContext = mock(DocumentContext.class);

        List<Object> result = new ParametersUtils().replaceList(values, taskId, documentContext);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testReplaceVariablesWithNestedPath() {
        String paramString = "${workflow.input.name.firstName}${${envVar}}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envVar = "envValue";
        String taskId = "taskId";

        Map<String, Object> workflowInput = new LinkedHashMap<>();
        workflowInput.put("name", Map.of("firstName", "John"));
        when(documentContext.read("$.input")).thenReturn(workflowInput);
        when(documentContext.read("$[0].variable")).thenReturn(envVar);

        Object result = new ParametersUtils().replaceVariables(paramString, documentContext, taskId);

        assertEquals("John" + envVar, result);
    }

    private WorkflowModel getWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setInput(Map.of("name", "workflowName"));
        return workflowModel;
    }

    private TaskDef getTaskDefinition() {
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(Map.of("input.name", "taskName"));
        return taskDefinition;
    }

    private Map<String, Object> getExpectedResult() {
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("taskName", "expectedTaskName");
        return expectedResult;
    }
}