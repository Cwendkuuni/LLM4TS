import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.DocumentContext;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.core.utils.ParametersUtils.Replacement;
import com.netflix.conductor.model.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EnvUtils envUtils;

    @Mock
    private TaskDef taskDefinition;

    @Mock
    private WorkflowDef workflowDef;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput_ValidInput() throws IOException {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> expectedOutput = createExpectedOutput();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, null);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetTaskInput_nullWorkflowModel() {
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, taskDefinition, null);

        assertNull(result);
    }

    @Test
    public void testGetTaskInput_nullTaskDefinition() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = createWorkflowModel();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables_ValidInput() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("name someValue age 25 status RUNNING", result.toString());
    }

    @Test
    public void testReplaceVariables_nullDocumentContext() {
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}";

        assertNull(parametersUtils.replaceVariables(paramString, null, null));
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariables_InvalidInput() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}${workflow.output.status}";

        parametersUtils.replaceVariables(paramString, documentContext, null);
    }

    @Test
    public void testReplaceVariables_EnvironmentVariable() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${ENV_VAR}";
        String envVar = "someValue";

        when(envUtils.getSystemParametersValue(paramString, null)).thenReturn(envVar);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(envVar, result);
    }

    @Test
    public void testReplaceVariables_NULLValue() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${workflow.input.age}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables_NullDocumentContextAndNullParamString() {
        Object result = parametersUtils.replaceVariables(null, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables_EmptyParamString() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("", result.toString());
    }

    @Test
    public void testReplaceVariables_EmptyDocumentContext() {
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}";

        DocumentContext documentContext = new DocumentContext(Collections.emptyMap());

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("", result.toString());
    }

    @Test
    public void testReplaceVariables_NullParamString() {
        DocumentContext documentContext = createDocumentContext();

        Object result = parametersUtils.replaceVariables(null, documentContext, null);

        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariables_InvalidJSONPath() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${workflow.input.age}{workflow.output.status}";

        parametersUtils.replaceVariables(paramString, documentContext, null);
    }

    @Test
    public void testReplaceVariables_ListElement() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${workflow.tasks[0].input.name} ${workflow.tasks[0].output.status}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("someName RUNNING", result.toString());
    }

    @Test
    public void testReplaceVariables_MapElement() {
        DocumentContext documentContext = createDocumentContext();
        String paramString = "${workflow.tasks[0].input.params.name} ${workflow.tasks[0].output.status}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("someNameValue RUNNING", result.toString());
    }

    @Test
    public void testGetTaskInputV1_ValidInput() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        Map<String, Object> expectedOutput = createExpectedOutput();
        expectedOutput.putAll(inputParams);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetTaskInputV1_NullWorkflowModel() {
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInputV1(null, inputParams);

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1_NullInputParams() {
        WorkflowModel workflowModel = createWorkflowModel();

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, null);

        assertNull(result);
    }

    @Test
    public void testReplace_ValidInput() {
        Object json = createJsonObject();
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}";

        Object result = parametersUtils.replace(paramString, json);

        assertEquals("someName someValue RUNNING", result.toString());
    }

    @Test
    public void testReplace_NullJson() {
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}";

        Object result = parametersUtils.replace(paramString, null);

        assertEquals("", result.toString());
    }

    @Test
    public void testReplace_InvalidJson() {
        String paramString = "${workflow.input.name} ${workflow.input.age} ${workflow.output.status}";

        Object json = new Object();

        Object result = parametersUtils.replace(paramString, json);

        assertEquals("", result.toString());
    }

    @Test
    public void testReplace_NullParamString() {
        Object json = createJsonObject();

        Object result = parametersUtils.replace(null, json);

        assertEquals("", result.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testReplace_AllNull() {
        Object json = null;
        String paramString = null;

        parametersUtils.replace(paramString, json);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.getInput().put("name", "someName");
        workflowModel.getInput().put("age", 25);
        workflowModel.setTasks(new LinkedList<>());
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName("someTask");
        taskModel.setInputData(new HashMap<>());
        taskModel.getInputData().put("name", "someNameValue");
        workflowModel.getTasks().add(taskModel);
        return workflowModel;
    }

    private Map<String, Object> createExpectedOutput() {
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("name", "someName");
        expectedOutput.put("age", 25);
        expectedOutput.put(
                "someTask",
                Map.of(
                        "name", "someNameValue",
                        "status", "RUNNING"
                )
        );
        return expectedOutput;
    }

    private JsonNode createJsonObject() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readTree(anyString())).thenReturn(createJsonNode());
        return objectMapper.readTree("{name: 'someName'}");
    }

    private JsonNode createJsonNode() {
        return mock(JsonNode.class);
    }

    private DocumentContext createDocumentContext() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readTree(anyString())).thenReturn(createJsonNode());
        return new DocumentContext(objectMapper);
    }
}