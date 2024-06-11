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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Unit tests for {@link ParametersUtils} class.
 */
public class ParametersUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);

    @Test
    public void testGetTaskInput() {
        // Given
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Configuration configuration = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = createTaskDef();
        ParametersUtils utils = new ParametersUtils(objectMapper);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey1", "testValue1");
        inputParams.put("testKey2.testKey3", "testValue3");
        inputParams.put(
                "testKey4.testKey5.testKey6",
                new HashMap<>() {{
                    put("testKey7", "testValue7");
                }});
        String taskId = "testTaskId";

        // When
        Map<String, Object> result = utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Then
        assertEquals(11, result.size());
        assertEquals("testValue1", result.get("testKey1"));
        assertEquals("testValue3", result.get("testKey2.testKey3"));
        Map<String, Object> testKey4 = (Map<String, Object>) result.get("testKey4.testKey5");
        assertEquals("testValue7", testKey4.get("testKey6.testKey7"));

        // Given
        inputParams = Collections.emptyMap();

        // When
        result = utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testReplace() {
        // Given
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Configuration configuration = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);
        ParametersUtils utils = new ParametersUtils(objectMapper);

        String paramString = "${workflow.input.testKey1}.${testKey2}.${testKey3}";
        String workflowInputString = "{\"testKey1\": \"testValue1\"}";

        // When
        Object result = utils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("testValue1.${testKey2}.${testKey3}", result.toString());

        // Given
        when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(Map.class)))
                .thenReturn(new HashMap<>());
        documentContext = JsonPath.parse(workflowInputString, configuration);

        // When
        result = utils.replace(paramString, documentContext, null);

        // Then
        assertEquals("testValue1.testValue2.testValue3", result.toString());
    }

    private WorkflowModel createWorkflowModel() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey1", "testValue1");
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(input);
        return workflow;
    }

    private TaskDef createTaskDef() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey2", "testValue2");
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(inputTemplate);
        return taskDefinition;
    }
}