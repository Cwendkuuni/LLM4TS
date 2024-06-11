package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.netflix.conductor.model.WorkflowModel.WorkflowInput;
import com.netflix.conductor.model.WorkflowModel.WorkflowOutput;

/** Tests for ParametersUtils class. */
public class ParametersUtilsTest {

    @Mock private ObjectMapper objectMapper;
    @Mock private TaskUtils taskUtils;
    @Mock private EnvUtils envUtils;
    @InjectMocks private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInputEmptyInputParams() {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskDef taskDef = createTaskDef();
        Map<String, Object> expectedResult = new HashMap<>();
        // When
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputEmptyWorkflowVariables() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("var1", "value1");
        TaskDef taskDef = createTaskDef();
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("var1", "value1");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWorkflowVariables() {
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.put("var1", "value1");
        workflowModel.setInput(workflowInput);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("var1", "newValue1");
        TaskDef taskDef = createTaskDef();
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("var1", "newValue1");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputTaskInputTemplate() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> workflowVariables = new HashMap<>();
        workflowVariables.put("var1", "value1");
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.put("inputTemplate", workflowVariables);
        workflowModel.setInput(workflowInput);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("var1", "newValue1");
        TaskDef taskDef = createTaskDef();
        taskDef.setInputTemplate(new HashMap<String, String>() {{
            put("var1", "inputValue1");
        }});
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("var1", "inputValue1");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputTaskInputTemplateMissingVariable() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> workflowVariables = new HashMap<>();
        workflowVariables.put("var1", "value1");
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.put("inputTemplate", workflowVariables);
        workflowModel.setInput(workflowInput);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("var2", "newValue1");
        TaskDef taskDef = createTaskDef();
        taskDef.setInputTemplate(new HashMap<String, String>() {{
            put("var1", "inputValue1");
        }});
        Map<String, Object> expectedResult = new HashMap<>();
        Map<String, Object> actualResult = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        assertEquals(expectedResult, actualResult);
        assertEquals(0, actualResult.size());
    }

    @Test
    public void testGetTaskInputTaskInputTemplateWithEnvVariable() {
        WorkflowModel workflowModel = createWorkflowModel();
        Map<String, Object> workflowVariables = new HashMap<>();
        workflowVariables.put("var1", "value1");
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.put("inputTemplate", workflowVariables);
        workflowModel.setInput(workflowInput);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("var1", "${VARIABLE_NAME}");
        TaskDef taskDef = createTaskDef();
        String expectedValue = "expectedValue";
        when(envUtils.getSystemParametersValue(eq("VARIABLE_NAME"), anyString())).thenReturn(expectedValue);
        taskDef.setInputTemplate(new HashMap<String, String>() {{
            put("VARIABLE_NAME", "inputValue1");
        }});
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("var1", expectedValue);

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariablesStringParam() {
        String jsonString = "{\"var1\": \"${VARIABLE_NAME}\"}";
        DocumentContext documentContext = createDocumentContext();
        String varName = "VARIABLE_NAME";
        String expectedValue = "expectedValue";
        when(envUtils.getSystemParametersValue(eq(varName), anyString())).thenReturn(expectedValue);
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"var1\": \"expectedValue\"}", result);
    }

    @Test
    public void testReplaceVariablesNullParam() {
        String jsonString = null;
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariablesEmptyJsonString() {
        String jsonString = "";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParam() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String varName = "VARIABLE_NAME";
        String expectedValue = "expectedValue";
        when(envUtils.getSystemParametersValue(eq(varName), anyString())).thenReturn(expectedValue);
        when(objectMapper.readValue(anyString(), Map.class)).thenReturn(new HashMap<String, Object>() {{
            put("taskDef", new HashMap<String, Object>() {{
                put("inputTemplate", new HashMap<String, String>() {{
                    put("var1", "inputValue1");
                }});
            }});
        }});
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"expectedValue\"}}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamMissingVariable() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var2']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String varName = "VARIABLE_NAME";
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":{\"$['taskDef.inputTemplate.var2']\":null}}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEnvVariable() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String varName = "VARIABLE_NAME";
        String expectedValue = "expectedValue";
        when(envUtils.getSystemParametersValue(eq(varName), anyString())).thenReturn(expectedValue);
        when(objectMapper.readValue(anyString(), Map.class)).thenReturn(new HashMap<String, Object>() {{
            put("taskDef", new HashMap<String, Object>() {{
                put("inputTemplate", new HashMap<String, String>() {{
                    put("var1", "inputValue1");
                }});
            }});
        }});
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"expectedValue\"}}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyJsonString() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyValueInJsonPath() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String varName = "VARIABLE_NAME";
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":{}}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyMap() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":{}}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithNullMap() {
        String jsonString = "{\"input\":{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}}";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":null}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyList() {
        String jsonString = "{\"input\":[{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}]}";
        DocumentContext documentContext = createDocumentContext();
        String varName = "VARIABLE_NAME";
        String expectedValue = "expectedValue";
        when(envUtils.getSystemParametersValue(eq(varName), anyString())).thenReturn(expectedValue);
        when(objectMapper.readValue(anyString(), Map.class)).thenReturn(new HashMap<String, Object>() {{
            put("taskDef", new HashMap<String, Object>() {{
                put("inputTemplate", new HashMap<String, List<Object>>() {{
                    put("var1", Collections.singletonList(new HashMap<String, String>() {{
                        put("inputValue1", "value1");
                    }}));
                }});
            }});
        }});
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":[{\"$['taskDef.inputTemplate.var1']\":\"expectedValue\"}]}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyListAndEmptyValueInJsonPath() {
        String jsonString = "{\"input\":[{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}]}";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":[]}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyListAndNullValueInJsonPath() {
        String jsonString = "{\"input\":[{\"$['taskDef.inputTemplate.var1']\":\"${VARIABLE_NAME}\"}]}";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":null}", result);
    }

    @Test
    public void testReplaceVariablesJsonPathParamWithEmptyListAndMapWithEmptyValueInJsonPath() {
        String jsonString = "{\"input\":[{\"$['taskDef.inputTemplate.var1.inputValue1']\":\"${VARIABLE_NAME}\"}]}";
        DocumentContext documentContext = createDocumentContext();
        String result = parametersUtils.replaceVariables(jsonString, documentContext, null);

        // Then
        assertEquals("{\"input\":[]}", result);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.put("inputTemplate", new HashMap<String, Object>() {{
            put("var1", "value1");
        }});
        workflowDef.setInput(workflowInput);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        return workflowModel;
    }

    private TaskDef createTaskDef() {
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<String, String>() {{
            put("var1", "inputValue1");
        }});
        return taskDef;
    }

    private DocumentContext createDocumentContext() {
        Configuration configuration = Configuration.defaultConfiguration();
        configuration.addOptions(Option.SUPPRESS_EXCEPTIONS);
        return JsonPath.parse(Collections.emptyMap(), configuration);
    }
}