package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final String WORKFLOW_JSON = "{\"workflowDefinition\":{\"schemaVersion\":1,\"input\":{\"input1\":\"value1\"},\"tasks\":[{\"referenceTaskName\":\"task1\",\"inputData\":{\"input2\":\"value2\"}}]},\"input\":{\"input3\":\"value3\"}}";
    private static final String TASK_DEF_JSON = "{\"inputTemplate\":{\"input4\":\"${workflow.input.input1}\"}}";
    private static final String TASK_INPUT_JSON = "{\"input5\":\"${workflow.input.input1}.input2\"}";

    private ParametersUtils utils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        this.objectMapper = objectMapper;
        this.utils = new ParametersUtils(objectMapper);
        this.workflowDef = Mockito.mock(WorkflowDef.class);
        this.taskDef = Mockito.mock(TaskDef.class);
        this.workflowModel = new WorkflowModel();
        this.workflowModel.setWorkflowDefinition(this.workflowDef);
    }

    @Test
    public void testGetTaskInputV1() {
        // Given
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(workflowDef.getInput()).thenReturn(Map.of("input1", "inputValue"));
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(new TaskModel()));
        Map<String, Object> inputParams = Map.of("input6", "inputValue6");

        // When
        Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, null, null);

        // Then
        assertEquals("inputValue", result.get("input5"));
        assertEquals(null, result.get("input6"));
    }

    @Test
    public void testGetTaskInputV2() {
        // Given
        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowDef.getInputTemplate()).thenReturn(Map.of("input4", TASK_INPUT_JSON));
        when(workflowDef.getInput()).thenReturn(Map.of("input1", "inputValue"));
        when(taskDef.getInputTemplate()).thenReturn(Map.of());

        Configuration configuration =
                JsonPath.using(Configuration.defaultConfiguration()).addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(WORKFLOW_JSON, configuration);

        when(workflowModel.getTasks()).thenReturn(List.of(new TaskModel()));
        Map<String, Object> inputParams = Map.of("input6", "inputValue6");

        // When
        Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDef, null);

        // Then
        assertEquals("inputValue.input2", result.get("input5"));
        assertEquals(null, result.get("input6"));
    }

    @Test
    public void testReplaceVariables() {
        // Given
        Map<String, Object> workflowInput = Map.of("input1", "workflowValue");
        String paramString = "${workflow.input.input1}.input2";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        String result = utils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("workflowValue.input2", result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        // Given
        String paramString = "${env.VARIABLE}";

        // When
        String result = utils.replaceVariables(paramString, null, null);

        // Then
        assertEquals(System.getenv().get("VARIABLE"), result);
    }
}