package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.model.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.WorkflowInstanceInput;
import com.netflix.conductor.model.WorkflowModelV1;

@Component
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtils.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private WorkflowModel workflowModel;
    private TaskDef taskDefinition;
    private DocumentContext documentContext;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        documentContext = mock(DocumentContext.class);
        workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        taskDefinition = mock(TaskDef.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Test case 1: Input params and workflow are null
        Map<String, Object> expectedResult = new HashMap<>();
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(expectedResult);
        Map<String, Object> result = parametersUtils.getTaskInput(null, null, null, null);
        assertEquals(expectedResult, result);

        // Test case 2: Input params, workflow, and task definition are provided
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("workflowInput1", "workflowValue1");
        workflowModel.setInput(workflowInput);
        when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());
        when(taskDefinition.getInputData()).thenReturn(new HashMap<>());
        Map<String, Object> expectedTaskInput = new HashMap<>();
        expectedTaskInput.put("inputParam1", "workflowValue1.workflowInput1");
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(expectedTaskInput);
        Map<String, Object> result2 = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        assertEquals(expectedTaskInput, result2);

        // Test case 3: Input params and workflow are provided, but task definition is null
        Map<String, Object> inputParams2 = new HashMap<>();
        inputParams2.put("inputParam2", "value2");
        Map<String, Object> workflowInput2 = new HashMap<>();
        workflowInput2.put("workflowInput2", "workflowValue2");
        workflowModel.setInput(workflowInput2);
        Map<String, Object> expectedTaskInput2 = new HashMap<>();
        expectedTaskInput2.put("inputParam2", "workflowValue2.workflowInput2.inputParam2");
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(expectedTaskInput2);
        Map<String, Object> result3 = parametersUtils.getTaskInput(inputParams2, workflowModel, null, "taskId");
        assertEquals(expectedTaskInput2, result3);
    }

    @Test
    public void testGetTaskInputV2() {
        // Test case 1: Input, workflow, and task definition are null
        Map<String, Object> expectedResult = new HashMap<>();
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(expectedResult);
        Map<String, Object> result = parametersUtils.getTaskInputV2(null, null, null, null, null);
        assertEquals(expectedResult, result);

        // Test case 2: Input, workflow, and task definition are provided
        Map<String, Object> input = new HashMap<>();
        input.put("input1", "inputValue1");
        Map<String, Map<String, Object>> inputMap = new HashMap<>();
        Map<String, Object> workflow = new HashMap<>();
        workflow.put("input", workflowInput);
        workflow.put("tasks", Collections.singletonList(new TaskModel()));
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(inputMap);
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), new TypeReference<WorkflowDef>() {})).thenReturn(workflowModel.getWorkflowDefinition());
        when(objectMapper.convertValue(workflowModel.getWorkflowDefinition(), Map.class)).thenReturn(workflow);
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(input);
        Map<String, Object> expectedTaskInput = new HashMap<>();
        expectedTaskInput.put("input1", "workflowInput.tasks[0].inputData.input1");
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(map))).thenReturn(expectedTaskInput);
        Map<String, Object> result2 = parametersUtils.getTaskInputV2(input, workflow, null, "taskId", null);
        assertEquals(expectedTaskInput, result2);
    }

    @Test
    public void testReplace() {
        // Test case 1: String replacement
        String paramString = "${workflowInput.workflowInput1.inputParam1}";
        when(documentContext.read(ArgumentMatchers.eq("$.workflowInput.workflowInput1.inputParam1"))).thenReturn("replacedValue");
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacedValue", replaced);

        // Test case 2: Map replacement
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "${workflowInput.workflowInput1.inputParam1}");
        when(documentContext.read(ArgumentMatchers.eq("$.workflowInput.workflowInput1.inputParam1"))).thenReturn("replacedValue");
        Map<String, Object> replacedMap = parametersUtils.replace(inputParams, documentContext, null);
        assertEquals("replacedValue", replacedMap.get("inputParam1"));

        // Test case 3: List replacement
        List<Object> list = new LinkedList<>();
        list.add("${workflowInput.workflowInput1.inputParam1}");
        when(documentContext.read(ArgumentMatchers.eq("$.workflowInput.workflowInput1.inputParam1"))).thenReturn("replacedValue");
        List<Object> replacedList = parametersUtils.replaceList(list, null, documentContext);
        assertEquals("replacedValue", replacedList.get(0));
    }

    @Test
    public void testReplaceVariables() {
        // Test case 1: Empty string replacement
        String paramString1 = "${}";
        when(documentContext.read(ArgumentMatchers.eq("$.inputParams.inputParam1"))).thenReturn("");
        Object replaced = parametersUtils.replaceVariables(paramString1, documentContext, "taskId");
        assertEquals("", replaced);

        // Test case 2: Null replacement
        String paramString2 = "${}";
        when(documentContext.read(ArgumentMatchers.eq("$.inputParams.inputParam1"))).thenReturn(null);
        Object replaced2 = parametersUtils.replaceVariables(paramString2, documentContext, "taskId");
        assertNull(replaced2);

        // Test case 3: Environment variable replacement
        String envVar = "TEST_ENV_VAR";
        String paramString3 = "${" + envVar + "}";
        when(EnvUtils.getSystemParametersValue(envVar, "taskId")).thenReturn("envValue");
        Object replaced3 = parametersUtils.replaceVariables(paramString3, documentContext, "taskId");
        assertEquals("envValue", replaced3);

        // Test case 4: JSONPath replacement
        String paramString4 = "${$.workflowInput.workflowInput1.inputParam1}";
        Map<String, Object> workflowInput = new HashMap<>();
        Map<String, Object> workflowInput1 = new HashMap<>();
        workflowInput.put("workflowInput1", workflowInput1);
        Map<String, Object> inputParam1 = new HashMap<>();
        inputParam1.put("key", "value");
        workflowInput1.put("inputParam1", inputParam1);
        when(documentContext.read(ArgumentMatchers.eq("$.workflowInput.workflowInput1.inputParam1"))).thenReturn(inputParam1);
        Object replaced4 = parametersUtils.replaceVariables(paramString4, documentContext, "taskId");
        assertEquals("value", replaced4);
    }
}