package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.Configuration;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowInstanceEvent;
import com.netflix.conductor.model.WorkflowModel.TaskInstance;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceRecord;
import com.netflix.conductor.testutils.TestUtils;

/**
 * Tests for the {@link ParametersUtils} class.
 */
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ParametersUtils parametersUtils;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;
    private WorkflowInstance workflowInstance;
    private DocumentContext documentContext;
    private Configuration configuration;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(OBJECT_MAPPER);
        workflowDef = mock(WorkflowDef.class);
        taskDef = mock(TaskDef.class);
        workflowModel = mock(WorkflowModel.class);
        workflowInstance = mock(WorkflowInstance.class);
        documentContext = mock(DocumentContext.class);
        configuration = new Configuration();
        configuration.addOptions(Option.SUPPRESS_EXCEPTIONS);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(workflowInstance));
        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowInstance.getTaskDefName()).thenReturn("mockTaskDefName");
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
        assertEquals(Collections.emptyMap(), result);

        inputParams.put("mockKey", "mockValue");
        inputParams.put("workflow.input.mockKey", "mockValueFromWorkflowInput");

        when(workflowDef.getSchemaVersion()).thenReturn(null);
        result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
        assertEquals(new LinkedHashMap<String, Object>() {{
            put("mockKey", "mockValueFromWorkflowInput");
        }}, result);

        when(workflowDef.getSchemaVersion()).thenReturn(2L);
        result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
        assertEquals(new LinkedHashMap<String, Object>() {{
            put("mockKey", "mockValue");
            put("mockKeyFromWorkflowInput", "mockValueFromWorkflowInput");
        }}, result);

        // Test with task reference
        WorkflowInstanceRef workflowInstanceRef = new WorkflowInstanceRef();
        workflowInstanceRef.setName("mockWorkflowInstanceRefName");
        when(workflowInstance.getWorkflowInstanceRef()).thenReturn(workflowInstanceRef);
        when(workflowModel.getTaskByRefName(workflowInstanceRef.getName()))
                .thenReturn(taskDef);

        result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        assertEquals(new LinkedHashMap<String, Object>() {{
            put("mockKey", "mockValue");
            put("mockKeyFromWorkflowInput", "mockValueFromWorkflowInput");
        }}, result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(workflowInstance));
        when(workflowDef.getSchemaVersion()).thenReturn(2L);
        when(workflowInstance.getTaskDefName()).thenReturn("mockTaskDefName");
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());

        Map<String, Object> expectedResult = new LinkedHashMap<String, Object>() {{
            put("mockKey", "mockValue");
            put("mockKeyFromWorkflowInput", "mockValueFromWorkflowInput");
            put("mockKeyFromTaskInputTemplate", "mockValueFromTaskInputTemplate");
        }};

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, null, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace() {
        String input = "${workflow.input.mockKey} ${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";

        when(documentContext.read("$.workflow.input.mockKey"))
                .thenReturn("mockValueFromWorkflowInput");
        when(EnvUtils.getSystemParametersValue("mockEnvironmentVariable", null))
                .thenReturn("mockValueFromEnvironmentVariable");
        when(documentContext.read("$.paramPathToNull"))
                .thenReturn(null);

        Object result = parametersUtils.replace(input, documentContext, null);
        assertEquals("mockValueFromWorkflowInput mockValueFromWorkflowInput mockValueFromEnvironmentVariable null", result);

        input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";

        result = parametersUtils.replace(input, documentContext, null);
        assertEquals("mockValueFromWorkflowInputmockValueFromWorkflowInput mockValueFromEnvironmentVariable null", result);

        input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";
        when(documentContext.read("$.workflow.input.mockKey"))
                .thenThrow(new RuntimeException("mockException"));

        assertThrows(RuntimeException.class, () -> parametersUtils.replace(input, documentContext, null));
    }

    @Test
    public void testReplaceList() {
        String input = "[${workflow.input.mockKey} ${workflow.input.mockKey}]";

        when(documentContext.read("$.workflow.input.mockKey"))
                .thenReturn(new LinkedList<String>() {{
                    add("mockValueFromWorkflowInput1");
                    add("mockValueFromWorkflowInput2");
                }});

        Object result = parametersUtils.replaceList(new ArrayList<>(), null, documentContext);
        assertEquals(new ArrayList<Object>() {{
            add("mockValueFromWorkflowInput1");
            add("mockValueFromWorkflowInput2");
        }}, result);

        input = "[${workflow.input.mockKey} ${workflow.input.mockKey}]";
        when(documentContext.read("$.workflow.input.mockKey"))
                .thenThrow(new RuntimeException("mockException"));

        assertThrows(RuntimeException.class, () -> parametersUtils.replaceList(new ArrayList<>(), null, documentContext));

        input = "[${workflow.input.mockKey} ${workflow.input.mockKey}]";
        when(documentContext.read("$.workflow.input.mockKey"))
                .thenReturn(new LinkedList<String>() {{
                    add(null);
                    add("mockValueFromWorkflowInput2");
                }});

        result = parametersUtils.replaceList(new ArrayList<>(), null, documentContext);
        assertEquals(new ArrayList<Object>() {{
            add(null);
            add("mockValueFromWorkflowInput2");
        }}, result);
    }

    @Test
    public void testReplaceVariables() {
        String input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}";

        Object expectedResult = "mockValueFromWorkflowInputmockValueFromWorkflowInput mockValueFromEnvironmentVariable null";

        when(documentContext.read("$.workflow.input.mockKey"))
                .thenReturn("mockValueFromWorkflowInput");
        when(EnvUtils.getSystemParametersValue("mockEnvironmentVariable", null))
                .thenReturn("mockValueFromEnvironmentVariable");
        when(documentContext.read("$.paramPathToNull"))
                .thenReturn(null);

        Object result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals(expectedResult, result);

        input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";

        result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("mockValueFromWorkflowInputmockValueFromWorkflowInput mockValueFromEnvironmentVariable null", result);

        input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";
        when(documentContext.read("$.workflow.input.mockKey"))
                .thenThrow(new RuntimeException("mockException"));

        assertThrows(RuntimeException.class, () -> parametersUtils.replaceVariables(input, documentContext, null));

        input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";
        when(documentContext.read("$.workflow.input.mockKey"))
                .thenReturn("mockValueFromWorkflowInput");
        when(documentContext.read("$.paramPathToNull"))
                .thenThrow(new RuntimeException("mockException"));

        assertThrows(RuntimeException.class, () -> parametersUtils.replaceVariables(input, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";

        Object expectedResult = "mockValueFromWorkflowInputmockValueFromWorkflowInput mockValueFromEnvironmentVariable ";

        when(documentContext.read("$.workflow.input.mockKey"))
                .thenReturn("mockValueFromWorkflowInput");
        when(EnvUtils.getSystemParametersValue("mockEnvironmentVariable", null))
                .thenReturn("mockValueFromEnvironmentVariable");
        when(documentContext.read("$.paramPathToNull"))
                .thenReturn("");

        Object result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariablesWithNullDocContext() {
        String input = "${workflow.input.mockKey}${workflow.input.mockKey} ${mockEnvironmentVariable} ${{paramPathToNull}}}";

        Object expectedResult = "mockValueFromWorkflowInputmockValueFromWorkflowInput mockValueFromEnvironmentVariable null";

        when(documentContext.read(TestUtils.ANY_STRING)).thenReturn(null);

        Object result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getInputTemplate()).thenReturn(new HashMap<>());

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(new LinkedHashMap<String, Object>() {{
            putAll(inputParams);
            put("workflow.input", inputParams);
        }}, result);
    }

    @Test
    public void testReplaceVariablesWithEmptyDocument() {
        String input = "${workflow.input.mockKey}";

        Object result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("", result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithNullTaskDef() {
        String input = "${workflow.input.mockKey}";

        Object result = parametersUtils.replaceVariables(input, documentContext, null, (TaskDef) null);
        fail("Expected RuntimeException but got: " + result);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String input = "${workflow.input.mockKey}";

        Object result = parametersUtils.replaceVariables(input, null, null);
        assertEquals("", result);
    }
}