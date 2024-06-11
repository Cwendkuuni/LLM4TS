import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.core.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    private static final String WORKFLOW_DEF_JSON = "{\n"
            + "  \"input\": {\n"
            + "    \"test\": \"value1\"\n"
            + "  },\n"
            + "  \"tasks\": [\n"
            + "    {\n"
            + "      \"referenceTaskName\": \"task1\",\n"
            + "      \"inputData\": {\n"
            + "        \"test\": \"value2\"\n"
            + "      }\n"
            + "    }\n"
            + "  ],\n"
            + "  \"schemaVersion\": 2\n"
            + "}";
    private static final String TASK_DEF_JSON = "{\n"
            + "  \"inputTemplate\": {\n"
            + "    \"test\": \"${workflow.input.test}\"\n"
            + "  },\n"
            + "  \"taskType\": \"TestTask\"\n"
            + "}";
    private static final String TASK_INPUT_JSON = "{\n"
            + "  \"input\": {\n"
            + "    \"test\": \"value3\"\n"
            + "  },\n"
            + "  \"tasks\": [\n"
            + "    {\n"
            + "      \"referenceTaskName\": \"task1\",\n"
            + "      \"inputData\": {\n"
            + "        \"test\": \"value4\"\n"
            + "      },\n"
            + "      \"outputData\": {\n"
            + "        \"test\": \"value5\"\n"
            + "      }\n"
            + "    }\n"
            + "  ],\n"
            + "  \"status\": \"running\"\n"
            + "}";

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TaskDef taskDefinition;

    @Mock
    private WorkflowDef workflowDefinition;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private EnvUtils envUtils;

    @Mock
    private TaskUtils taskUtils;

    @InjectMocks
    private ParametersUtils parametersUtils;

    private ObjectMapper objectMapperSpy;
    private DocumentContext documentContext;
    private Configuration configuration;

    @Before
    public void setUp() throws IOException {
        objectMapperSpy = mock(ObjectMapper.class);
        when(objectMapper.readTree(anyString())).thenReturn(JsonNode.class, mock(JsonNode.class));
        when(objectMapper.writeValueAsString(anyObject())).thenAnswer(
                invocation -> invocation.getArgument(0).toString());
        when(objectMapperSpy.writeValueAsBytes(anyObject())).thenReturn(new byte[0]);

        documentContext = JsonPath.using(Configuration.defaultConfiguration());
        configuration = JsonPath.using(Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("envValue");
        when(taskUtils.getTaskById(anyString(), anyList())).thenReturn(taskModel);

        when(workflowDefinition.getInputTemplate()).thenReturn(new ObjectMapper().readTree(TASK_DEF_JSON));
        when(workflowDefinition.getSchemaVersion()).thenReturn(2L);

        when(workflowModel.getTaskByRefName(anyString())).thenReturn(taskModel);
        when(workflowModel.getReferenceTaskName(anyString())).thenReturn("task1");
        when(workflowModel.getWorkflowId()).thenReturn("workflowId");
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(workflowModel));

        when(taskDefinition.getInputTemplate()).thenReturn(new ObjectMapper().readTree(TASK_DEF_JSON));
        when(taskDefinition.getTaskType()).thenReturn("TestTask");
        when(taskModel.getCorrelationId()).thenReturn("correlationId");
        when(taskModel.getEndTime()).thenReturn(1L);
        when(taskModel.getInputData()).thenReturn(new ObjectMapper().readTree(TASK_INPUT_JSON));
        when(taskModel.getOutputData()).thenReturn(new ObjectMapper().readTree(TASK_INPUT_JSON));
        when(taskModel.getReferenceTaskName()).thenReturn("task1");
        when(taskModel.getScheduledTime()).thenReturn(2L);
        when(taskModel.getStartTime()).thenReturn(3L);
        when(taskModel.getTaskDefName()).thenReturn("TestTaskDef");
        when(taskModel.getTaskId()).thenReturn("taskId");
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowInstanceId");
        when(taskModel.getReasonForIncompletion()).thenReturn("reasonForIncompletion");
        when(taskModel.getCallbackAfterSeconds()).thenReturn(4L);
        when(taskModel.getWorkerId()).thenReturn("workerId");
        when(taskModel.getIteration()).thenReturn(1);
    }

    @Test
    public void testGetTaskInput_WorkflowVersion1() {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        when(workflowModel.getInput()).thenReturn(new ObjectMapper().readTree(WORKFLOW_DEF_JSON));

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        // Then
        assertEquals(1, result.size());
        assertEquals("value1", result.get("test"));
    }

    @Test
    public void testGetTaskInput_WorkflowVersion2() throws IOException {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        when(workflowModel.getInput()).thenReturn(new ObjectMapper().readTree(WORKFLOW_DEF_JSON));

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, taskDefinition);

        // Then
        assertEquals(3, result.size());
        assertEquals("value1", result.get("test"));
        assertEquals("value2", result.get("tasks.0.inputData.test"));
        assertNull(result.get("tasks.0.outputData.test"));
    }

    @Test
    public void testReplaceVariables() throws IOException {
        // Given
        DocumentContext documentContext = JsonPath.parse(new ObjectMapper().readTree(TASK_INPUT_JSON), configuration);
        String paramString = "${workflow.workflowId}.${workflow.tasks[0].referenceTaskName}.${tasks.0.inputData.test}";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("workflowId.task1.value3", result.toString());
    }

    @Test
    public void testReplaceVariables_EnvVariable() throws IOException {
        // Given
        DocumentContext documentContext = JsonPath.parse(new ObjectMapper().readTree(TASK_INPUT_JSON), configuration);
        String paramString = "${envValue}";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("envValue", result.toString());
    }

    @Test
    public void testReplaceVariables_EmptyParamPath() throws IOException {
        // Given
        DocumentContext documentContext = JsonPath.parse(new ObjectMapper().readTree(TASK_INPUT_JSON), configuration);
        String paramString = "{}";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceVariables_InvalidParamPath() throws IOException {
        // Given
        DocumentContext documentContext = JsonPath.parse(new ObjectMapper().readTree(TASK_INPUT_JSON), configuration);
        String paramString = "${workflow.tasks[0].inputData}";

        // When - Should throw an IllegalArgumentException
        parametersUtils.replaceVariables(paramString, documentContext, null);
    }

    @Test
    public void testReplaceVariables_NullDocumentContext() throws IOException {
        // Given
        String paramString = "${workflow.workflowId}.${workflow.tasks[0].referenceTaskName}.${tasks.0.inputData.test}";

        // When
        Object result = parametersUtils.replaceVariables(paramString, null, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_TaskDefinitionNull() {
        // Given
        String paramString = "${taskDefinition.inputTemplate.test}";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_TaskDefinitionInputTemplateNull() {
        // Given
        String paramString = "${taskDefinition.inputTemplate.test}";

        // When
        when(taskDefinition.getInputTemplate()).thenReturn(null);
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_TaskDefinitionInputTemplateEmpty() throws IOException {
        // Given
        String paramString = "${taskDefinition.inputTemplate.test}";

        // When
        when(taskDefinition.getInputTemplate()).thenReturn(new ObjectMapper().readTree("{}"));
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

        // Then
        assertNull(result);
    }

    @Test
    public void testGetTaskInput_WorkflowVersion2_InputTemplateMissing() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("test", "workflowValue");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, taskDefinition);

        // Then
        assertEquals("workflowValue", result.get("test"));
        assertEquals("value3", result.get("tasks.0.inputData.test"));
        assertEquals("value5", result.get("tasks.0.outputData.test"));
    }

    @Test
    public void testReplaceVariables_TaskInputMissing() throws IOException {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("test", "workflowValue");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        // Then
        assertEquals("workflowValue", result.get("test"));
        assertNull(result.get("tasks.0.inputData.test"));
        assertNull(result.get("tasks.0.outputData.test"));
    }

    @Test
    public void testReplaceVariables_NullInputParams() throws IOException {
        // Given
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("test", "workflowValue");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, null, null);

        // Then
        assertEquals(0, result.size());
    }
}