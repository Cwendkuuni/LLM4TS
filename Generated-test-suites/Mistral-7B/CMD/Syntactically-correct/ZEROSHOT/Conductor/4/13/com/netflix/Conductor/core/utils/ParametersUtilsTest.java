package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.model.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

/** Tests for the ParametersUtils class. */
@Component
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);

    private final ObjectMapper objectMapper;
    private final TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<>() {};

    public ParametersUtilsTest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Test
    public void testGetTaskInput() {
        // Given
        WorkflowDef workflowDef = createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);
        TaskDef taskDef = createTaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        // When
        Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflowModel, taskDef, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflow", expectedWorkflowParams());
        expectedResult.put("task", expectedTaskParams());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWithNullInputParams() {
        // Given
        WorkflowDef workflowDef = createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);
        TaskDef taskDef = createTaskDef();
        String taskId = "task1";

        // When
        Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(null, workflowModel, taskDef, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflow", expectedWorkflowParams());
        expectedResult.put("task", expectedTaskParams());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWithNullTaskDef() {
        // Given
        WorkflowDef workflowDef = createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        // When
        Map<String, Object> result = new ParametersUtils(objectMapper).getTaskInput(inputParams, workflowModel, null, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflow", expectedWorkflowParams());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariables() {
        // Given
        String inputString = "${workflow.input.myParam}${workflow.output.otherParam}${task.input.thirdParam}";
        Map<String, Object> workflowParams = expectedWorkflowParams();
        Map<String, Object> taskParams = expectedTaskParams();

        // When
        Object result = new ParametersUtils(objectMapper).replaceVariables(inputString, null, null);

        // Then
        String expectedResult = String.format(
                "%s%s%s",
                workflowParams.get("input").get("myParam"),
                workflowParams.get("output").get("otherParam"),
                taskParams.get("input").get("thirdParam")
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariablesWithNullInput() {
        // Given
        String inputString = "${workflow.input.myParam}${workflow.output.otherParam}${task.input.thirdParam}";
        DocumentContext documentContext = null;

        // When
        Object result = new ParametersUtils(objectMapper).replaceVariables(inputString, documentContext, null);

        // Then
        assertNull(result);
    }

    // Add more test methods for other methods in the ParametersUtils class here

    private WorkflowDef createWorkflowDef() {
        // Initialize a WorkflowDef object here
    }

    private WorkflowModel createWorkflowModel(WorkflowDef workflowDef) {
        // Initialize a WorkflowModel object here
    }

    private TaskDef createTaskDef() {
        // Initialize a TaskDef object here
    }

    private Map<String, Object> expectedWorkflowParams() {
        // Initialize an expected workflow parameters map here
    }

    private Map<String, Object> expectedTaskParams() {
        // Initialize an expected task parameters map here
    }
}