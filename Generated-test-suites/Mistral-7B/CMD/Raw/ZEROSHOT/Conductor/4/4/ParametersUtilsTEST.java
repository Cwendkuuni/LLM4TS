package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

/**
 * Tests for the {@link ParametersUtils} class
 */
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);

    private static final String WORKFLOW_DEF_JSON =
            "{\"name\": \"testWorkflow\", \"version\": 1,\"inputTemplate\": {\"input\": {\"param1\": \"${workflow.workflowId}\", \"param2\": \"${task.input.param3}\"}},"
                    + "\"tasks\": [{\"name\": \"task1\", \"inputTemplate\": {\"input\": {\"param4\": \"${workflow.input.param1}\"}}}]}\n";

    private static final String TASK_DEF_JSON =
            "{\"name\": \"task1\", \"inputTemplate\": {\"input\": {\"param3\": \"${workflow.input.param1}\"}}}"
                    + ",\"outputTemplate\": {\"output\": {\"param5\": \"${task.input.param4}\"}}}"
                    + ",\"inputData\": {\"param1\": \"value1\"}, \"outputData\": {\"param1\": \"value2\"}}";

    @Component
    private ParametersUtils parametersUtils;

    @Test
    public void testGetTaskInput_v1() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        WorkflowDef workflowDef = new ObjectMapper().readValue(WORKFLOW_DEF_JSON.getBytes(), WorkflowDef.class);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        TaskDef taskDef = new ObjectMapper().readValue(TASK_DEF_JSON.getBytes(), TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.workflowId", "testWorkflowId");
        inputParams.put("task.input.param3", "testTaskParam3");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "testTaskId");

        assertEquals("testWorkflowId", result.get("param1"));
        assertEquals("testTaskParam3", result.get("param3"));
        assertNull(result.get("param4"));
        assertNull(result.get("param5"));
    }

    @Test
    public void testGetTaskInput_v2() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        WorkflowDef workflowDef = new ObjectMapper().readValue(WORKFLOW_DEF_JSON.getBytes(), WorkflowDef.class);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        TaskDef taskDef = new ObjectMapper().readValue(TASK_DEF_JSON.getBytes(), TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.workflowId", "testWorkflowId");
        inputParams.put("task.input.param3", "testTaskParam3");

        when(objectMapper.writeValueAsBytes(Collections.emptyMap())).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], Map.class)).thenReturn(Collections.emptyMap());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "testTaskId");

        assertEquals("testWorkflowId", result.get("workflow.input.param1"));
        assertEquals("testTaskParam3", result.get("task.input.param3"));
        assertEquals("testWorkflowId", result.get("workflow.input.param1"));
        assertNull(result.get("task.output.param5"));
    }

    @Test
    public void testReplace() {
        ObjectMapper objectMapper = new ObjectMapper();
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.workflowId}", "testWorkflowId");
        inputParams.put("${task.input.param3}", "testTaskParam3");

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String inputString = "${workflow.workflowId}-${task.input.param3}";
        String expectedResult = "testWorkflowId-testTaskParam3";

        String resultString = parametersUtils.replace(inputString, documentContext, null);

        assertEquals(expectedResult, resultString);
    }

    @Test
    public void testReplaceList() {
        ObjectMapper objectMapper = new ObjectMapper();
        WorkflowModel workflowModel = new WorkflowModel();
        List<Object> inputList = new LinkedList<>();
        inputList.add("${workflow.workflowId}-${task.input.param3}");
        inputList.add(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.workflowId}", "testWorkflowId");
        inputParams.put("${task.input.param3}", "testTaskParam3");

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        List<Object> expectedResult = new LinkedList<>();
        expectedResult.add("testWorkflowId-testTaskParam3");
        expectedResult.add(new HashMap<>());

        List<Object> resultList = parametersUtils.replaceList(inputList, null, documentContext);

        assertEquals(expectedResult, resultList);
    }

    @Test
    public void testReplaceVariables() {
        ObjectMapper objectMapper = new ObjectMapper();
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.workflowId}", "testWorkflowId");
        inputParams.put("${task.input.param3}", "testTaskParam3");

        DocumentContext documentContext = JsonPath.parse("{\"workflowId\": \"testWorkflowId\"}");

        String inputString = "${workflow.workflowId}-${task.input.param3}";
        String expectedResult = "testWorkflowId-testTaskParam3";

        String resultString = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals(expectedResult, resultString);
    }

    @Test
    public void testReplaceVariablesWithTask() {
        ObjectMapper objectMapper = new ObjectMapper();
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.workflowId}", "testWorkflowId");
        inputParams.put("${task.input.param3}", "testTaskParam3");

        TaskModel taskModel = new TaskModel();
        taskModel.setInputData(new HashMap<>());
        taskModel.setInputData().put("param3", "testTaskParam3");

        WorkflowModel taskWorkflowModel = new WorkflowModel();
        taskWorkflowModel.setWorkflowDefinition(new ObjectMapper().readValue(WORKFLOW_DEF_JSON.getBytes(), WorkflowDef.class));
        DocumentContext documentContext = JsonPath.parse(taskWorkflowModel, new Configuration());

        String inputString = "${workflow.workflowId}-${task.input.param3}";
        String expectedResult = "testWorkflowId-testTaskParam3";

        String resultString = parametersUtils.replaceVariables(inputString, documentContext, "testTaskId");

        assertEquals(expectedResult, resultString);
    }

    @Test
    public void testReplaceVariablesWithNullTask() {
        ObjectMapper objectMapper = new ObjectMapper();
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.workflowId}", "testWorkflowId");
        inputParams.put("${task.input.param3}", "testTaskParam3");

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String inputString = "${workflow.workflowId}-${task.input.param3}";
        String expectedResult = null;

        String resultString = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals(expectedResult, resultString);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new ObjectMapper().readValue(WORKFLOW_DEF_JSON.getBytes(), WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "testValue1");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("testValue1", result.get("param1"));
        assertNull(result.get("param2"));
    }
}