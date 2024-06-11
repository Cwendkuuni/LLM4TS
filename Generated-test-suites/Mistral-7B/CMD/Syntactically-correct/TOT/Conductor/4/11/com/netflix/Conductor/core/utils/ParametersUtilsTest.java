package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstance.Task;

public class ParametersUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(ParametersUtilsTest.class);

    private static final String WORKFLOW_JSON =
            "{\"workflowName\":\"workflowName\",\"workflowVersion\":1,\"input\":{\"testKey\":\"testValue\"},\"tasks\":[{\"referenceTaskName\":\"taskRefName\",\"inputData\":{\"testKey2\":\"testValue2\"}}]}";
    private static final String TASK_DEF_JSON =
            "{\"inputTemplate\":{\"testKey3\":\"testValue3\"},\"taskType\":\"testTaskType\"}";
    private static final String INPUT_PARAMS_JSON = "{\"testKey4\":\"testValue4\"}";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ParametersUtils parametersUtils;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private Map<String, Object> inputParams;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
        workflowDef = mock(WorkflowDef.class);
        taskDef = mock(TaskDef.class);
        inputParams = new HashMap<>();
        workflowModel = new WorkflowModel();
    }

    @Test
    public void testGetTaskInput_WorkflowInputTemplateEmpty_ReturnsInputParams() {
        when(workflowDef.getInputTemplate()).thenReturn(null);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInput_WorkflowInputTemplatePresent_ClonesAndMergesInputParams() {
        when(workflowDef.getInputTemplate()).thenReturn(new LinkedHashMap<String, Object>() {{
            put("testKey5", "testValue5");
        }});

        inputParams.put("testKey6", "testValue6");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        Map<String, Object> expected = new LinkedHashMap<String, Object>() {{
            putAll(inputParams);
            put("testKey5", "testValue5");
        }};

        assertEquals(expected, result);
    }

    @Test
    public void testGetTaskInput_TaskInputTemplatePresent_ClonesAndMergesTaskInputTemplate()
            throws IOException {
        String jsonTaskDef = objectMapper.writeValueAsString(new TaskDef());
        when(taskDef.getInputTemplate()).thenReturn(objectMapper.readValue(TASK_DEF_JSON, Map.class));

        inputParams.put("testKey7", "testValue7");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);

        Map<String, Object> expected = new LinkedHashMap<String, Object>() {{
            putAll(inputParams);
            putAll(objectMapper.readValue(TASK_DEF_JSON, Map.class));
            put("testKey7", "testValue7");
        }};

        assertEquals(expected, result);
    }

    @Test
    public void testGetTaskInput_WorkflowTasksEmpty_ReturnsEmptyMap() {
        when(workflowDef.getSchemaVersion()).thenReturn(1);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        assertNull(result);
    }

    @Test
    public void testGetTaskInput_WorkflowTasksPresent_ParsesWorkflowInstanceAndTaskInstances() {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setInput(new LinkedHashMap<String, Object>() {{
            put("testKey8", "testValue8");
        }});
        Task task1 = new Task();
        task1.setTaskDefName("taskRefName");
        task1.setInputData(new LinkedHashMap<String, Object>() {{
            put("testKey9", "testValue9");
        }});
        Task task2 = new Task();
        task2.setTaskDefName("taskRefName2");
        task2.setInputData(new LinkedHashMap<String, Object>() {{
            put("testKey10", "testValue10");
        }});
        workflowInstance.setTasks(Arrays.asList(task1, task2));

        when(workflowDef.getSchemaVersion()).thenReturn(1);
        when(workflowModel.getWorkflowInstanceById("instanceId")).thenReturn(workflowInstance);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "instanceId");

        Map<String, Object> expected = new LinkedHashMap<String, Object>() {{
            putAll(inputParams);
            putAll(workflowInstance.getInput());
            putAll(task1.getInputData());
            putAll(task2.getInputData());
        }};

        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariables_NullJson_ReturnsNull() {
        String input = null;

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertNull(replaced);
    }

    @Test
    public void testReplaceVariables_EmptyJson_ReturnsEmptyString() {
        String input = "{}";

        DocumentContext documentContext = JsonPath.parse(WORKFLOW_JSON, Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals("", replaced);
    }

    @Test
    public void testReplaceVariables_SingleVariable_ReplacesVariable() {
        String input = "${testKey}";

        Map<String, Object> variables = new LinkedHashMap<String, Object>() {{
            put("testKey", "testValue");
        }};

        DocumentContext documentContext = JsonPath.parse(WORKFLOW_JSON, Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals("testValue", replaced);
    }

    @Test
    public void testReplaceVariables_EnvironmentVariable_ReplacesEnvironmentVariable() {
        String input = "${env.TEST_ENV_VAR}";

        String testEnvVar = "testEnvValue";

        System.setProperty("TEST_ENV_VAR", testEnvVar);

        DocumentContext documentContext = JsonPath.parse(WORKFLOW_JSON, Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals(testEnvVar, replaced);
    }

    @Test
    public void testReplaceVariables_NestedVariables_ReplacesNestedVariables() {
        String input = "${workflow.input.testKey}";

        Map<String, Object> variables = new LinkedHashMap<String, Object>() {{
            put("workflow", new LinkedHashMap<String, Object>() {{
                put("input", new LinkedHashMap<String, Object>() {{
                    put("testKey", "testValue");
                }});
            }});
        }};

        DocumentContext documentContext = JsonPath.parse(WORKFLOW_JSON, Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals("testValue", replaced);
    }

    @Test
    public void testReplaceVariables_ErrorReadingDocumentContext_ReturnsNull() {
        String input = "${workflow.input.testKey}";

        when(workflowDef.getSchemaVersion()).thenReturn(1);
        when(workflowModel.getWorkflowInstanceById("instanceId")).thenReturn(workflowModel);

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.workflow.input.testKey")).thenThrow(new IOException());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertNull(replaced);
    }

    @Test
    public void testReplaceVariables_VariableNotPresent_ReturnsOriginalValue() {
        String input = "${workflow.input.testKey2}";

        Map<String, Object> variables = new LinkedHashMap<String, Object>() {{
            put("workflow", new LinkedHashMap<String, Object>() {{
                put("input", new LinkedHashMap<String, Object>() {{
                    put("testKey", "testValue");
                }});
            }});
        }};

        DocumentContext documentContext = JsonPath.parse(WORKFLOW_JSON, Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        assertSame("testKey2", replaced);
    }

    @Test
    public void testReplaceVariables_List_ReplacesVariablesInList() {
        String input = "[${testKey}, ${testKey2}]";

        Map<String, Object> variables = new LinkedHashMap<String, Object>() {{
            put("testKey", "testValue");
            put("testKey2", "testValue2");
        }};

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        List<Object> expected = Arrays.asList("testValue", "testValue2");

        assertEquals(expected, replaced);
    }

    @Test
    public void testReplaceVariables_Map_ReplacesVariablesInMap() {
        String input = "{\"${testKey}\":${testKey2}}";

        Map<String, Object> variables = new LinkedHashMap<String, Object>() {{
            put("testKey", "testValue");
            put("testKey2", "testValue2");
        }};

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);

        Map<String, Object> expected = new LinkedHashMap<String, Object>() {{
            put("testValue", "testValue2");
        }};

        assertEquals(expected, replaced);
    }
}