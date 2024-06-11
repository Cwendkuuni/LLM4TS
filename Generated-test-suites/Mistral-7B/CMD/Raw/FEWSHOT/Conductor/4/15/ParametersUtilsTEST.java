package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

/** Tests for the ParametersUtils class. */
@Component
public class ParametersUtilsTest {

    private static final String WORKFLOW_JSON =
            "{\"input\":{},\"output\":{},\"status\":\"CREATED\",\"workflowId\":\"workflowId1\",\"parentWorkflowId\":null,"
                    + "\"parentWorkflowTaskId\":null,\"workflowName\":\"workflowName1\",\"workflowVersion\":1.0,\"correlationId\":null,"
                    + "\"reasonForIncompletion\":null,\"schemaVersion\":1,\"variables\":{}}";

    private static final String TASK_JSON =
            "{\"inputData\":{\"param1\":\"value1\"},\"outputData\":{\"param2\":\"value2\"},\"taskType\":\"TaskType1\","
                    + "\"status\":null,\"referenceTaskName\":\"TaskRefName1\",\"retryCount\":0,\"correlationId\":null,\"pollCount\":0,"
                    + "\"taskDefName\":\"TaskDefName1\",\"scheduledTime\":null,\"startTime\":null,\"endTime\":null,\"workflowInstanceId\":null,"
                    + "\"taskId\":\"taskId1\",\"reasonForIncompletion\":null,\"callbackAfterSeconds\":null,\"workerId\":null,\"iteration\":null}";

    private static final String TASK_DEF_JSON =
            "{\"inputTemplate\":{\"param3\":\"${workflow.output.param1}\"},\"taskType\":\"TaskType1\"}";

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private Configuration config;
    private DocumentContext docContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.objectMapper = mock(ObjectMapper.class);
        this.config = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        this.docContext = JsonPath.parse(Collections.emptyMap(), config);
        this.parametersUtils = new ParametersUtils(this.objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new TaskDef.InputTemplate());
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName("TaskRefName1");
        taskModel.setTaskDefName("TaskDefName1");
        workflowModel.getTasks().add(taskModel);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.output.param1", "value1");

        Map<String, Object> result = this.parametersUtils.getTaskInput(
                inputParams, workflowModel, taskDef, "taskId1");

        assertEquals("value1", result.get("param3"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);
        workflowModel.setWorkflowDefinition(workflowDef);
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new TaskDef.InputTemplate());
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName("TaskRefName1");
        taskModel.setTaskDefName("TaskDefName1");
        workflowModel.getTasks().add(taskModel);
        Map<String, Object> input = new HashMap<>();
        input.put("input", new HashMap<>());
        input.put("output", new HashMap<>());

        Map<String, Object> result = this.parametersUtils.getTaskInputV2(
                null, workflowModel, "taskId1", taskDef);

        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void testReplace() {
        String paramString = "${workflow.output.param1}${workflow.output.param2}${env.VAR1}${workflow.output.param3}";
        JsonNode jsonNode = JsonPath.parse(TASK_JSON);
        Map<String, Object> workflowOutput = new HashMap<>();
        workflowOutput.put("param1", "value1");
        workflowOutput.put("param2", "value2");
        workflowOutput.put("param3", "value3");

        String result = this.parametersUtils.replaceVariables(
                paramString, docContext, "taskId1", 0);

        String expectedResult = String.format(
                "%s%s%s%s", "value1", "value2", System.getenv("VAR1"), "value3");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceList() {
        String paramString = "[${workflow.output.param1}, ${workflow.output.param2}, ${env.VAR1}, ${workflow.output.param3}]";
        JsonNode jsonNode = JsonPath.parse(TASK_JSON);
        Map<String, Object> workflowOutput = new HashMap<>();
        workflowOutput.put("param1", "value1");
        workflowOutput.put("param2", "value2");

        List<Object> result = this.parametersUtils.replaceList(
                JsonPath.read("$", jsonNode), "taskId1", docContext);

        List<String> expectedResult = List.of("value1", "value2", System.getenv("VAR1"), "value3");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.output.param1}.${workflow.output.param2}.${workflow.output.param3}";
        JsonNode jsonNode = JsonPath.parse(TASK_JSON);
        Map<String, Object> workflowOutput = new HashMap<>();
        workflowOutput.put("param1", "value1");
        workflowOutput.put("param2", "value2");
        workflowOutput.put("param3", "value3");

        Object result = this.parametersUtils.replaceVariables(
                paramString, docContext, "taskId1", 0);

        String expectedResult = String.format("%s.%s.%s", "value1", "value2", "value3");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String paramString = "${workflow.output.param1}";
        Map<String, Object> workflowOutput = new HashMap<>();
        workflowOutput.put("param1", "value1");

        Object result = this.parametersUtils.replaceVariables(
                paramString, null, "taskId1", 0);

        assertEquals("value1", result);
    }

    @Test
    public void testReplaceVariablesWithEmptyMap() {
        String paramString = "${workflow.output.param1}";
        Map<String, Object> workflowOutput = new HashMap<>();

        Object result = this.parametersUtils.replaceVariables(
                paramString, docContext, "taskId1", 0);

        assertEquals("", result);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowInput = new WorkflowModel();
        workflowInput.setInput(new HashMap<>());
        workflowDef.setInputTemplate(workflowInput);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("TaskType1");
        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put("param1", "value1");
        taskModel.setInputData(taskInput);
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.getTasks().add(taskModel);

        Map<String, Object> result = this.parametersUtils.getTaskInputV1(workflowModel, null);

        assertEquals("value1", result.get("param1"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowInput = new WorkflowModel();
        workflowInput.setInput(new HashMap<>());
        workflowDef.setInputTemplate(workflowInput);
        Map<String, Object> result = this.parametersUtils.getWorkflowInput(workflowDef, null);

        assertEquals(workflowInput, result);
    }
}