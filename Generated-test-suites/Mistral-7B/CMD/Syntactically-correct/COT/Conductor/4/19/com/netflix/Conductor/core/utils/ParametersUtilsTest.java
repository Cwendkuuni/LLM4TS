package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
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
import com.netflix.conductor.model.WorkflowModel.Input;
import com.netflix.conductor.model.WorkflowModel.Task;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TaskDef taskDefinition;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private WorkflowDef workflowDef;

    @Mock
    private EnvUtils envUtils;

    @Mock
    private TaskUtils taskUtils;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(objectMapper.readValue(Any.byteArray(), Map.class)).thenReturn(Collections.emptyMap());
        when(objectMapper.writeValueAsBytes(Any.map())).thenReturn(new byte[0]);
        when(envUtils.getSystemParametersValue(AnyString(), AnyString())).thenReturn("env_value");
    }

    @Test
    public void testGetTaskInput() {
        Input input = new Input();
        input.put("input_key", "input_value");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(input);
        workflowModel.setTasks(Collections.singletonList(createTaskModel()));

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("input_key", "input_value");
        expected.put("task_key", "task_value");

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDefinition, "task_id");

        assertEquals(expected, result);
    }

    @Test
    public void testGetTaskInputWithNullWorkflowModel() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, null, taskDefinition, "task_id");
        assertNotNull(result);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInputWithoutTaskDefinitionInputTemplate() {
        Input input = new Input();
        input.put("input_key", "input_value");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(input);
        workflowModel.setTasks(Collections.singletonList(createTaskModel()));

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, null, "task_id");

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("input_key", "input_value");
        assertEquals(expected, result);
    }

    @Test
    public void testGetTaskInputWithTaskDefinitionInputTemplate() {
        Input input = new Input();
        input.put("input_key", "input_value");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(input);
        workflowModel.setTasks(Collections.singletonList(createTaskModel()));

        TaskDef taskDefinition = mock(TaskDef.class);
        when(taskDefinition.getInputTemplate()).thenReturn(createMap());

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDefinition, "task_id");

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("input_key", "task_input_value");
        expected.put("input_key", "input_value");
        assertEquals(expected, result);
    }

    // Add more test methods for other methods in the class

    private TaskModel createTaskModel() {
        Task task = new Task();
        task.setTaskType("task_type");
        task.setInputData(createMap());
        task.setOutputData(createMap());
        return new TaskModel("task_id", "task_name", task);
    }

    private Map<String, Object> createMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("task_key", "task_value");
        return map;
    }

    private static Object Any = Objects.requireNonNull((Object) null);
    private static String AnyString() {
        return (String) Any;
    }
}