package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceIdentifier;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametersUtilsTest {

    private static final String WORKFLOW_JSON = "{\"input\":{\"param1\":\"value1\"},\"output\":{\"param2\":\"value2\"}}";
    private static final String TASK_DEF_JSON = "{\"inputTemplate\":{\"param3\":\"${workflow.output.param2}\"}}";
    private static final String TASK_INPUT_JSON = "{\"input\":{\"inputParam\":\"${workflow.input.param1}\"}}";

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private DocumentContext documentContext;
    @Mock
    private EnvUtils envUtils;
    @Mock
    private TaskDef taskDef;
    @Mock
    private WorkflowDef workflowDef;
    @Mock
    private WorkflowModel workflowModel;
    @Mock
    private TaskModel taskModel;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(objectMapper.readValue(anyByteArray(), Map.class)).thenAnswer(new Answer<Map<String, Object>>() {
            @Override
            public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
                byte[] bytes = (byte[]) invocation.getArguments()[0];
                return objectMapper.readValue(bytes, mapType);
            }
        });
        when(objectMapper.writeValueAsBytes(anyMap())).thenAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> map = (Map<String, Object>) invocation.getArguments()[0];
                return objectMapper.writeValueAsBytes(map);
            }
        });
        when(documentContext.read(anyString())).thenReturn("valueFromDocumentContext");
        when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("valueFromEnvUtils");

        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput_nullInputParams_returnsEmptyMap() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInput_noTaskDef_returnsInputParams() {
        when(taskDef == null).thenReturn(null);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");
        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInput_taskDefWithInputTemplate_replaceInputTemplate() {
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<String, Object>() {{
            put("param3", "${workflow.output.param2}");
        }});

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam", "value2");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_emptyWorkflow_returnsInputParams() {
        when(workflowModel.getInput()).thenReturn(Collections.emptyMap());
        when(workflowModel.getTasks()).thenReturn(Collections.emptyList());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");
        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInput_workflowWithInput_getWorkflowInput() {
        when(workflowModel.getInput()).thenReturn(new HashMap<String, Object>() {{
            put("param1", "value1");
        }});

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, null, "taskId");
        assertEquals(workflowModel.getInput(), result);
    }

    @Test
    public void testGetTaskInput_workflowWithTask_getTaskInputV1() {
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));

        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put("inputParam", "${workflow.input.param1}");

        when(taskModel.getInputData()).thenReturn(taskInput);

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam", "value1");

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, null, "taskId");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_nullJson_returnsNull() {
        assertNull(parametersUtils.replace(null));
    }

    @Test
    public void testReplace_emptyJson_returnsEmptyString() {
        assertEquals("", parametersUtils.replace(""));
    }

    @Test
    public void testReplace_simpleString_replacesVariable() {
        String input = "${workflow.output.param2}";
        String expectedOutput = "valueFromDocumentContext";

        when(documentContext.read("$.output.param2")).thenReturn("valueFromDocumentContext");

        assertEquals(expectedOutput, parametersUtils.replace(input, documentContext, null));
    }

    @Test
    public void testReplace_deepNestedString_replacesVariable() {
        String input = "${workflow.output.param3.param4.param5}";
        String expectedOutput = "valueFromDocumentContext";

        when(documentContext.read("$.output.param3.param4.param5")).thenReturn("valueFromDocumentContext");

        assertEquals(expectedOutput, parametersUtils.replace(input, documentContext, null));
    }

    @Test
    public void testReplace_stringWithMap_replacesVariableInMap() {
        String input = "${workflow.output.inputTemplate.param3}";
        Map<String, Object> expectedOutput = new HashMap<String, Object>() {{
            put("param3", "valueFromDocumentContext");
        }};

        when(documentContext.read("$.output.inputTemplate")).thenReturn(TASK_DEF_JSON);
        when(objectMapper.readValue(anyByteArray(), Map.class)).thenReturn(expectedOutput);

        assertEquals(expectedOutput, parametersUtils.replace(input, documentContext, null));
    }

    @Test
    public void testReplace_stringWithList_replacesVariableInList() {
        String input = "${workflow.output.tasks[0].outputData.param1}";
        List<Object> expectedOutput = new LinkedList<Object>() {{
            add(new HashMap<String, Object>() {{
                put("param1", "valueFromDocumentContext");
            }});
        }};

        when(documentContext.read("$.output.tasks[0].outputData")).thenReturn(TASK_INPUT_JSON);
        when(objectMapper.readValue(anyByteArray(), List.class)).thenReturn(expectedOutput);

        assertEquals(expectedOutput, parametersUtils.replace(input, documentContext, null));
    }

    @Test(expected = RuntimeException.class)
    public void testReplace_jsonWithEmptyMap_throwsException() {
        assertThrows(RuntimeException.class, () -> parametersUtils.replace("", documentContext, null));
    }

    @Test
    public void testReplace_jsonWithNullValue_returnsNull() {
        assertEquals(null, parametersUtils.replace(null, documentContext, null));
    }

    @Test
    public void testReplaceVariables_simpleString_replacesVariable() {
        String input = "${workflow.output.param2}";
        String expectedOutput = "valueFromEnvUtils";

        when(envUtils.getSystemParametersValue("param2", "taskId")).thenReturn("valueFromEnvUtils");

        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, "taskId"));
    }

    @Test
    public void testReplaceVariables_deepNestedString_replacesVariable() {
        String input = "${workflow.output.param3.param4.param5}";
        String expectedOutput = "valueFromEnvUtils";

        when(envUtils.getSystemParametersValue("param5", "taskId")).thenReturn("valueFromEnvUtils");

        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, "taskId"));
    }

    @Test
    public void testReplaceVariables_stringWithMap_replacesVariableInMap() {
        String input = "${workflow.output.inputTemplate.param3}";
        Map<String, Object> expectedOutput = new HashMap<String, Object>() {{
            put("param3", "valueFromEnvUtils");
        }};

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(1);

        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, "taskId"));
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariables_jsonWithEmptyMap_throwsException() {
        assertThrows(RuntimeException.class, () -> parametersUtils.replaceVariables("", documentContext, "taskId"));
    }

    @Test
    public void testReplaceVariables_jsonWithNullValue_returnsNull() {
        assertEquals(null, parametersUtils.replaceVariables(null, documentContext, null));
    }

    @Test
    public void testGetWorkflowInput_nullWorkflowDef_returnsEmptyMap() {
        Map<String, Object> result = parametersUtils.getWorkflowInput(null, Collections.emptyMap());
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetWorkflowInput_workflowDefWithInputTemplate_clonesInputTemplate() {
        when(workflowDef.getInputTemplate()).thenReturn(new HashMap<String, Object>() {{
            put("param1", "value1");
        }});

        Map<String, Object> expectedResult = new HashMap<String, Object>() {{
            putAll(workflowDef.getInputTemplate());
        }};

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, Collections.emptyMap());
        assertEquals(expectedResult, result);
    }

    private static final TypeReference<Map<String, Object>> mapType =
            new TypeReference<Map<String, Object>>() {};
}