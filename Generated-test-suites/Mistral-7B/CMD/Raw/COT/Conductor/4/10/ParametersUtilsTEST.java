package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.jayway.jsonpath.Configuration.defaultConfiguration;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the ParametersUtils class.
 */
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ParametersUtils parametersUtils;
    private DocumentContext documentContext;
    private TaskDef taskDefinition;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() throws IOException {
        documentContext = JsonPath.using(defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS))
                .parse("{}");
        taskDefinition = mock(TaskDef.class);
        workflowDef = mock(WorkflowDef.class);
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        parametersUtils = new ParametersUtils(OBJECT_MAPPER);
    }

    @Test
    public void testGetTaskInput_NullInputParams() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, null, null);
        Assert.assertNull(result);
    }

    @Test
    public void testGetTaskInput_NullWorkflowModel() {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, null, null);
        Assert.assertNull(result);
    }

    @Test
    public void testGetTaskInput_EmptyWorkflowModel() {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Assert.assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInput_BasicScenario() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_id";
        String expectedValue = "expected_value";

        inputParams.put("param1", "workflow_param1");
        workflowModel.setInput(Map.of("workflow_param1", expectedValue));

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Assert.assertEquals(expectedValue, result.get("param1"));

        verifyTaskInputParams(inputParams, result, taskId);
    }

    @Test
    public void testGetTaskInput_InputTemplateOverridesWorkflowParam() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_id";
        String expectedValue = "expected_value";

        inputParams.put("param1", "input_template_param1");
        Map<String, Object> inputTemplate = Map.of("param1", expectedValue);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Assert.assertEquals(expectedValue, result.get("param1"));

        verifyTaskInputParams(inputParams, result, taskId);
    }

    @Test
    public void testGetTaskInput_WorkflowInputTemplateOverridesWorkflowParam() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_id";
        String expectedValue = "expected_value";

        inputParams.put("param1", "workflow_param1");
        workflowModel.setInput(Map.of("workflow_param1", "workflow_param1_value"));
        workflowDef.setInputTemplate(inputTemplate());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Assert.assertEquals(expectedValue, result.get("param1"));

        verifyTaskInputParams(inputParams, result, taskId);
    }

    @Test
    public void testGetTaskInput_InputTemplateOverridesTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_id";
        String expectedWorkflowValue = "workflow_param_value";
        String expectedTaskInputValue = "input_template_param_value";

        inputParams.put("param1", "task_input_param1");
        Map<String, Object> workflowInput = Map.of("workflow_param1", expectedWorkflowValue);
        Map<String, Object> taskInput = Map.of("input_template_param1", expectedTaskInputValue);
        workflowModel.setInput(workflowInput);
        taskDefinition.setInputTemplate(taskInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, null);
        Assert.assertEquals(expectedTaskInputValue, result.get("param1"));

        verifyTaskInputParams(inputParams, result, taskId);
    }

    @Test
    public void testGetTaskInput_NestedWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_id";
        String expectedValue = "expected_value";

        inputParams.put("param1.nested.param1", "workflow_param1.nested.param1");
        Map<String, Object> workflowInput = Map.of("workflow_param1.nested", Map.of("param1", expectedValue));
        workflowModel.setInput(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Assert.assertEquals(expectedValue, result.get("param1.nested.param1"));

        verifyTaskInputParams(inputParams, result, taskId);
    }

    @Test
    public void testGetTaskInput_ListInput() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_id";
        List<?> expectedListValue = List.of("list_item1", "list_item2");

        inputParams.put("param1", "workflow_param1");
        Map<String, Object> workflowInput = Map.of("workflow_param1", expectedListValue);
        workflowModel.setInput(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Assert.assertEquals(expectedListValue, result.get("param1"));

        verifyTaskInputParams(inputParams, result, taskId);
    }

    @Test
    public void testReplace_NullParamString() {
        Object replacement = replaceVariables(null, documentContext, null);
        Assert.assertNull(replacement);
    }

    @Test
    public void testReplace_EmptyParamString() {
        Object replacement = replaceVariables("", documentContext, null);
        Assert.assertEquals("", replacement);
    }

    @Test
    public void testReplace_SimpleParamString() {
        String paramString = "${workflow.input.workflow_param1}";
        Object replacement = replaceVariables(paramString, documentContext, null);
        Assert.assertEquals("workflow_param1_value", replacement);
    }

    @Test
    public void testReplace_DeepParamString() {
        String paramString = "${workflow.input.workflow_param1.nested.param1}";
        Object replacement = replaceVariables(paramString, documentContext, null);
        Assert.assertEquals("expected_value", replacement);
    }

    @Test
    public void testReplace_EnvironmentVariableParamString() {
        String paramString = "${ENV_VARIABLE}";
        String envVariable = "env_variable_value";
        System.setProperty("ENV_VARIABLE", envVariable);

        Object replacement = replaceVariables(paramString, documentContext, null);
        Assert.assertEquals(envVariable, replacement);
    }

    @Test
    public void testReplace_EmptyContext() {
        Object replacement = replaceVariables("${workflow.input.workflow_param1}", documentContext, null);
        Assert.assertNull(replacement);
    }

    @Test(expected = RuntimeException.class)
    public void testReplace_NullContext() {
        Object replacement = replaceVariables("${workflow.input.workflow_param1}", null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testReplace_NullTaskId() {
        Object replacement = replaceVariables("${workflow.input.workflow_param1}", documentContext, null);
        when(taskDefinition.getName()).thenReturn("task_name");
        parametersUtils.replaceVariables(taskDefinition, documentContext, null);
    }

    private void verifyTaskInputParams(Map<String, Object> inputParams, Map<String, Object> result, String taskId) {
        for (Entry<String, Object> entry : inputParams.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = result.get(key);
            if (expectedValue instanceof Map) {
                verifyNestedTaskInputParams((Map<String, Object>) expectedValue, (Map<String, Object>) actualValue, key, taskId);
            } else if (expectedValue instanceof List) {
                verifyNestedListTaskInputParams((List<?>) expectedValue, (List<?>) actualValue, key, taskId);
            } else {
                Assert.assertEquals(expectedValue, actualValue);
            }
        }
    }

    private void verifyNestedTaskInputParams(Map<String, Object> expectedMap, Map<String, Object> actualMap, String key, String taskId) {
        for (Entry<String, Object> entry : expectedMap.entrySet()) {
            String subKey = entry.getKey();
            Object expectedSubValue = entry.getValue();
            Object actualSubValue = actualMap.get(subKey);
            if (expectedSubValue instanceof Map) {
                verifyNestedTaskInputParams((Map<String, Object>) expectedSubValue, (Map<String, Object>) actualSubValue, subKey, key);
            } else if (expectedSubValue instanceof List) {
                verifyNestedListTaskInputParams((List<?>) expectedSubValue, (List<?>) actualSubValue, subKey, key);
            } else {
                Assert.assertEquals(expectedSubValue, actualSubValue);
            }
        }
        Assert.assertEquals(expectedMap.size(), actualMap.size());
    }

    private void verifyNestedListTaskInputParams(List<?> expectedList, List<?> actualList, String key, String taskId) {
        Assert.assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Object expectedValue = expectedList.get(i);
            Object actualValue = actualList.get(i);
            if (expectedValue instanceof Map) {
                verifyNestedTaskInputParams((Map<String, Object>) expectedValue, (Map<String, Object>) (Map) actualValue, "", key);
            } else if (expectedValue instanceof List) {
                verifyNestedListTaskInputParams((List<?>) expectedValue, (List<?>) actualValue, "", key);
            } else {
                Assert.assertEquals(expectedValue, actualValue);
            }
        }
    }

    private Properties inputTemplate() {
        Properties inputTemplate = new Properties();
        inputTemplate.put("param1", "expected_value");
        return inputTemplate;
    }

    private Object replaceVariables(String paramString, DocumentContext documentContext, String taskId) {
        return parametersUtils.replaceVariables(paramString, documentContext, taskId);
    }
}