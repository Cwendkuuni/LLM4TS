package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.stereotype.Component;

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

/** Tests for the ParametersUtils class. */
@Component
public class ParametersUtilsTest {

    private final ObjectMapper objectMapper;

    public ParametersUtilsTest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Test
    public void testGetTaskInput() {
        // Given a workflow model and task definition
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());
        WorkflowDef workflowDef = workflow.getWorkflowDefinition();
        workflowDef.setSchemaVersion(1);
        workflowDef.setName("testWorkflow");
        workflowDef.setVersion("1.0.0");
        TaskDef taskDef = new TaskDef();
        taskDef.setTaskDefName("testTask");
        taskDef.setInputTemplate(Map.of("testKey", "testValue"));
        workflow.setTasks(List.of(new TaskModel("testTaskRef", taskDef)));

        // When getting the task input with an empty input map
        Map<String, Object> inputParams = Map.of();
        Map<String, Object> taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInput", inputParams, workflow, taskDef, null);
        assertNull(taskInput);

        // When getting the task input with the given workflow model and task definition
        Map<String, Object> input = Map.of("testKey", "testInput");
        Map<String, Object> expectedTaskInput = Map.of("testKey", "testValue", "testInput", "testInput");
        inputParams = Map.of("testInput", input);
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInput", inputParams, workflow, taskDef, null);
        assertEquals(expectedTaskInput, taskInput);

        // Given a workflow model and task definition with schema version 2
        workflowDef.setSchemaVersion(2);
        workflow.setTasks(Collections.emptyList());

        // When getting the task input with an empty input map
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInput", inputParams, workflow, taskDef, null);
        assertNull(taskInput);

        // Given a workflow model, task definition, and input map
        Map<String, Object> workflowInput = Map.of("testKey", "testValue");
        workflow.setInput(workflowInput);
        inputParams = Map.of("testInput", input);

        // When getting the task input with the given workflow model, task definition, and input map
        expectedTaskInput = Map.of("testKey", "testValue", "testInput", Map.of("testKey", "testInput"));
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInput", inputParams, workflow, taskDef, null);
        assertEquals(expectedTaskInput, taskInput);
    }

    @Test
    public void testGetTaskInputV2() {
        // Given a workflow model, task definition, input map, and task id
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());
        WorkflowDef workflowDef = workflow.getWorkflowDefinition();
        workflowDef.setSchemaVersion(2);
        workflowDef.setName("testWorkflow");
        workflowDef.setVersion("1.0.0");
        TaskDef taskDef = new TaskDef();
        taskDef.setTaskDefName("testTask");
        taskDef.setInputTemplate(Map.of("testKey", "${'testVariable'}"));
        workflow.setTasks(List.of(new TaskModel("testTaskRef", taskDef)));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testVariable", "testValue");

        // When getting the task input with the given workflow model, task definition, input map, and null task id
        Map<String, Object> taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInputV2", inputParams, workflow, null, taskDef);
        Map<String, Object> expectedTaskInput = Map.of("testKey", "testValue");
        assertEquals(expectedTaskInput, taskInput);

        // Given a workflow model, task definition, input map, and task id
        String taskId = "testTaskId";
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInputV2", inputParams, workflow, taskDef, taskId);
        expectedTaskInput = Map.of("testKey", "testValue", "taskId", taskId);
        assertEquals(expectedTaskInput, taskInput);

        // Given a workflow model, task definition, input map, and null task id with empty workflow input
        workflow.setInput(Collections.emptyMap());
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInputV2", inputParams, workflow, taskDef, null);
        expectedTaskInput = Map.of();
        assertEquals(expectedTaskInput, taskInput);

        // Given a workflow model, task definition, input map, and task id with empty workflow input
        workflowInput = Collections.emptyMap();
        workflow.setInput(workflowInput);
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInputV2", inputParams, workflow, taskDef, taskId);
        expectedTaskInput = Map.of();
        assertEquals(expectedTaskInput, taskInput);

        // Given a workflow model, task definition, input map, and task id with workflow input
        expectedTaskInput = Map.of("testKey", "testValue", "testVariable", "testValue", "taskId", taskId);
        inputParams.put("testVariable", Map.of("testKey", "testValue"));
        taskInput = invokeMethod(new ParametersUtils(objectMapper), "getTaskInputV2", inputParams, workflow, taskDef, taskId);
        assertEquals(expectedTaskInput, taskInput);
    }

    @Test
    public void testReplace() {
        // Given an input map and JSON document context
        Object json = Map.of("testKey", "{$'testVariable'}");
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testVariable", "testValue");

        // When replacing variables in a string with the given input map and JSON document context
        String paramString = "${testVariable}";
        Object replaced = invokeMethod(new ParametersUtils(objectMapper), "replace", paramString, json);
        assertEquals("testValue", replaced);

        // Given an input map and JSON document context with a task id
        String taskId = "testTaskId";
        Object documentContext = Map.of("testVariable", "testValue");
        Map<String, Object> replacedTaskInput = new HashMap<>();
        replacedTaskInput.put("testKey", "testValue");
        replacedTaskInput.put("testVariable", documentContext);

        // When replacing variables in a JSON document with the given input map, JSON document context, and task id
        Object replacedJson = Map.of("testKey", "${'testVariable'}");
        replaced = invokeMethod(new ParametersUtils(objectMapper), "replace", replacedJson, documentContext, taskId);
        assertEquals(replacedTaskInput, replaced);

        // Given an empty input map and JSON document context
        inputParams = new HashMap<>();
        json = Map.of();

        // When replacing variables in an empty JSON document with the given input map and JSON document context
        Object emptyReplaced = invokeMethod(new ParametersUtils(objectMapper), "replace", json, documentContext, null);
        assertEquals(json, emptyReplaced);

        // Given an input map and null JSON document context
        json = null;

        // When replacing variables in a null JSON document with the given input map and null JSON document context
        Object nullReplaced = invokeMethod(new ParametersUtils(objectMapper), "replace", json, inputParams, null);
        assertNull(nullReplaced);
    }

    @Test
    public void testReplaceList() {
        // Given a list and task id
        List<String> values = List.of("{$'testVariable'}", "testValue");
        String taskId = "testTaskId";

        // When replacing variables in a list with the given task id
        List<Object> replacedList = invokeMethod(new ParametersUtils(objectMapper), "replaceList", values, taskId, null);
        List<Object> expectedReplacedList = List.of("testValue", "testValue");
        assertEquals(expectedReplacedList, replacedList);

        // Given a list and JSON document context
        DocumentContext documentContext = Map.of("testVariable", "testValue");

        // When replacing variables in a list with the given JSON document context
        replacedList = invokeMethod(new ParametersUtils(objectMapper), "replaceList", values, null, documentContext);
        expectedReplacedList = List.of(Map.of("testKey", "testValue"), "testValue");
        assertEquals(expectedReplacedList, replacedList);

        // Given a list and JSON document context with a task id
        replacedList = invokeMethod(new ParametersUtils(objectMapper), "replaceList", values, taskId, documentContext);
        expectedReplacedList = List.of(Map.of("testKey", "testValue"), Map.of("testKey", "testValue"), "testValue");
        assertEquals(expectedReplacedList, replacedList);
    }

    @Test
    public void testReplaceVariables() {
        // Given a string with a JSON document context and task id
        String paramString = "{$'testVariable'}";
        DocumentContext documentContext = Map.of("testVariable", "testValue");
        String taskId = "testTaskId";

        // When replacing variables in a string with the given JSON document context and task id
        Object replaced = invokeMethod(new ParametersUtils(objectMapper), "replaceVariables", paramString, documentContext, taskId);
        assertEquals("testValue", replaced);

        // Given a string with a null JSON document context and null task id
        paramString = "{$'testVariable'}";
        documentContext = null;
        taskId = null;

        // When replacing variables in a string with the given null JSON document context and null task id
        Object nullReplaced = invokeMethod(new ParametersUtils(objectMapper), "replaceVariables", paramString, documentContext, taskId);
        assertNull(nullReplaced);
    }

    @Test
    public void testReplaceVariablesWithEmptyJson() {
        // Given a string with an empty JSON document context and null task id
        String paramString = "{$'testVariable'}";
        DocumentContext documentContext = Collections.emptyMap();
        String taskId = null;

        // When replacing variables in a string with the given empty JSON document context and null task id
        Object emptyJsonReplaced = invokeMethod(new ParametersUtils(objectMapper), "replaceVariables", paramString, documentContext, taskId);
        assertEquals("", emptyJsonReplaced);
    }
}