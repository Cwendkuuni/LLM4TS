package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        DocumentContext documentContext = JsonPath.parse("{}");

        Map<String, Object> replaced = parametersUtils.replace(input, documentContext);

        assertNotNull(replaced);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testReplaceVariables() {
        DocumentContext documentContext = JsonPath.parse("{}");
        String paramString = "${param}";
        String taskId = "task123";

        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertNotNull(replaced);
        // Add more assertions based on the expected behavior
    }

    // Add more test cases for other methods in ParametersUtils

}