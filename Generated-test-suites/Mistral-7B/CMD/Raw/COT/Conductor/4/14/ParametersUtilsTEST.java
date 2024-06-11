import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.mockito.ArgumentMatchers;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        MockitoAnnotations.initMocks(this);
        workflowDef = mock(WorkflowDef.class);
        taskDef = mock(TaskDef.class);
        workflowModel = new WorkflowModel();
    }

    @Test
    public void testGetTaskInput_Basic() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        TaskDef taskDef = new TaskDef();
        String taskId = "taskId";

        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));
        when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("test", "value"));
        when(taskDef.getReferenceTaskName()).thenReturn("testTask");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

        // Then
        assertEquals("value", result.get("test"));
    }

    @Test
    public void testGetTaskInput_SchemaVersion2() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        TaskDef taskDef = new TaskDef();
        String taskId = "taskId";

        when(workflowDef.getSchemaVersion()).thenReturn(2L);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("test", "value"));
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks())
                .thenReturn(Collections.singletonList(taskModelWithInputAndOutput("testInput", "testOutput")));
        when(taskDef.getReferenceTaskName()).thenReturn("testTask");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

        // Then
        assertEquals("testOutput", result.get("test"));
    }

    @Test
    public void testGetTaskInput_NoTasksOrInputTemplate() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        String taskId = "taskId";

        when(workflowDef.getSchemaVersion()).thenReturn(2L);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);

        // When & Then
        assertNull(parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTaskInput_NullWorkflowModel() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "taskId";

        // When & Then
        parametersUtils.getTaskInput(inputParams, null, taskDef, taskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTaskInput_NullTaskDef() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        String taskId = "taskId";

        // When & Then
        parametersUtils.getTaskInput(inputParams, workflowModel, null, taskId);
    }

    @Test
    public void testGetTaskInputV2_InputTemplateOverridesEmptyTaskInput() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        String taskId = "taskId";

        when(workflowDef.getSchemaVersion()).thenReturn(2L);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("test", "value"));
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks())
                .thenReturn(Collections.singletonList(taskModelWithInputAndOutput("testInput", null)));
        when(taskDef.getReferenceTaskName()).thenReturn("testTask");

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

        // Then
        assertEquals("value", result.get("test"));
    }

    @Test
    public void testGetTaskInputV2_TaskDefInputTemplateOverridesEmptyTaskInput() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        String taskId = "taskId";

        when(workflowDef.getSchemaVersion()).thenReturn(2L);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks())
                .thenReturn(Collections.singletonList(taskModelWithInputAndOutput("testInput", "testInputTemplateValue")));
        when(taskDef.getReferenceTaskName()).thenReturn("testTask");

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

        // Then
        assertEquals("testInputTemplateValue", result.get("test"));
    }

    @Test
    public void testReplaceVariables_EmptyDocumentContext() {
        // Given
        String paramString = "${workflow.input.test}";

        // When
        Object result = parametersUtils.replaceVariables(paramString, null, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_EmptyParamString() {
        // Given
        DocumentContext documentContext = mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables("", documentContext, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_SimpleExpression() {
        // Given
        String paramString = "${workflow.input.test}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_ComplexExpression() {
        // Given
        String paramString = "${workflow.input.taskTasks[0].output.test}";
        DocumentContext documentContext = JsonPath.parse(getComplexJson(), Configuration.defaultConfiguration());

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("testValue", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceVariables_NullDocumentContext() {
        // Given
        String paramString = "${workflow.input.test}";

        // When & Then
        parametersUtils.replaceVariables(paramString, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceVariables_NullParamString() {
        // Given
        DocumentContext documentContext = mock(DocumentContext.class);

        // When & Then
        parametersUtils.replaceVariables(null, documentContext, null);
    }

    private TaskModel taskModelWithInputAndOutput(String inputKey, String inputValue) {
        TaskModel taskModel = new TaskModel();
        taskModel.setInputData(new HashMap<>());
        if (inputValue != null) {
            taskModel.getInputData().put(inputKey, inputValue);
        }
        taskModel.setOutputData(new HashMap<>());
        return taskModel;
    }

    private static JsonNode getComplexJson() {
        return JsonPath.parse(
                "{"
                        + "\"workflow\": {"
                        + "\"input\": {"
                        + "\"taskTasks\":"
                        + "["
                        + "{"
                        + "\"output\": {"
                        + "\"test\": \"testValue\""
                        + "}"
                        + "}"
                        + "]"
                        + "}"
                        + "}"
                        + "}",
                Configuration.defaultConfiguration());
    }

}