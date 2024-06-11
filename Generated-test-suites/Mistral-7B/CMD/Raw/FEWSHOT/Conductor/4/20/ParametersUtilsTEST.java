package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Unit tests for the {@link ParametersUtils} class.
 */
public class ParametersUtilsTest {

    private static final Logger LOGGER = LogFactory.getLogger(ParametersUtils.class);

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private Configuration jsonPathConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.objectMapper = mock(ObjectMapper.class);
        this.parametersUtils = new ParametersUtils(this.objectMapper);
        this.jsonPathConfiguration = Configuration.defaultConfiguration();
    }

    @Test
    public void testGetTaskInput_NullInputParams_ReturnsEmptyMap() {
        WorkflowModel workflow = createMockWorkflowModel();
        TaskDef taskDefinition = createMockTaskDef();
        Map<String, Object> input = null;

        Map<String, Object> result = this.parametersUtils.getTaskInput(input, workflow, taskDefinition, "taskId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_NullWorkflow_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = createMockTaskDef();
        String taskId = "taskId";

        Map<String, Object> result = this.parametersUtils.getTaskInput(inputParams, null, taskDefinition, taskId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_NullTaskDefinition_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = createMockWorkflowModel();
        String taskId = "taskId";

        Map<String, Object> result = this.parametersUtils.getTaskInput(inputParams, workflow, null, taskId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_EmptyTaskDefInputTemplate_ReturnsInputParams() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("someKey", "someValue");
        WorkflowModel workflow = createMockWorkflowModel();
        TaskDef taskDefinition = createMockTaskDef();
        String taskId = "taskId";

        when(taskDefinition.getInputTemplate()).thenReturn(Collections.emptyMap());

        Map<String, Object> result = this.parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(result);
        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInput_NonEmptyTaskDefInputTemplate_MergesWithInputParams() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("someKey1", "someValue1");
        WorkflowModel workflow = createMockWorkflowModel();
        TaskDef taskDefinition = createMockTaskDef();
        String taskId = "taskId";

        Map<String, Object> taskDefInputTemplate = new HashMap<>();
        taskDefInputTemplate.put("someKey2", "someValue2");

        when(taskDefinition.getInputTemplate()).thenReturn(taskDefInputTemplate);

        Map<String, Object> result = this.parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.containsKey("someKey1"));
        assertTrue(result.containsKey("someKey2"));
    }

    @Test
    public void testGetTaskInput_WorkflowWithTasks_ReturnsTaskInputs() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("someKey1", "someValue1");
        WorkflowModel workflow = createMockWorkflowModel();
        TaskDef taskDefinition1 = createMockTaskDef("task1");
        TaskDef taskDefinition2 = createMockTaskDef("task2");
        String taskId1 = "task1Id";
        String taskId2 = "task2Id";

        when(taskDefinition1.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(taskDefinition2.getInputTemplate()).thenReturn(Collections.emptyMap());

        workflow.getTasks().add(createTaskModel("task1", taskDefinition1));
        workflow.getTasks().add(createTaskModel("task2", taskDefinition2));

        Map<String, Object> result = this.parametersUtils.getTaskInput(inputParams, workflow, null, taskId1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("task1.input.someKey1"));
        assertTrue(result.containsKey("task1.input.someKey2"));

        result = this.parametersUtils.getTaskInput(inputParams, workflow, null, taskId2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("task2.input.someKey1"));
        assertTrue(result.containsKey("task2.input.someKey2"));
    }

    // Add more test methods here for other methods in the ParametersUtils class

    private WorkflowModel createMockWorkflowModel() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getInput()).thenReturn(Collections.emptyMap());
        when(workflow.getOutput()).thenReturn(Collections.emptyMap());
        when(workflow.getStatus()).thenReturn(null);
        when(workflow.getWorkflowId()).thenReturn("workflowId");
        when(workflow.getParentWorkflowId()).thenReturn(null);
        when(workflow.getParentWorkflowTaskId()).thenReturn(null);
        when(workflow.getWorkflowName()).thenReturn("workflowName");
        when(workflow.getWorkflowVersion()).thenReturn(1);
        when(workflow.getCorrelationId()).thenReturn("correlationId");
        when(workflow.getReasonForIncompletion()).thenReturn(null);
        when(workflow.getSchemaVersion()).thenReturn(1);
        when(workflow.getVariables()).thenReturn(Collections.emptyMap());
        return workflow;
    }

    private TaskDef createMockTaskDef() {
        TaskDef taskDefinition = mock(TaskDef.class);
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(taskDefinition.getOutputTemplate()).thenReturn(Collections.emptyMap());
        when(taskDefinition.getTaskType()).thenReturn("taskType");
        when(taskDefinition.getTaskDefName()).thenReturn("taskDefName");
        when(taskDefinition.getInputData()).thenReturn(Collections.emptyMap());
        when(taskDefinition.getOutputData()).thenReturn(Collections.emptyMap());
        return taskDefinition;
    }

    private TaskDef createMockTaskDef(String taskRefName) {
        TaskDef taskDefinition = createMockTaskDef();
        when(taskDefinition.getReferenceTaskName()).thenReturn(taskRefName);
        return taskDefinition;
    }

    private TaskModel createTaskModel(String taskRefName, TaskDef taskDefinition) {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getReferenceTaskName()).thenReturn(taskRefName);
        when(taskModel.getTaskDefName()).thenReturn("taskDefName");
        when(taskModel.getTaskType()).thenReturn("taskType");
        when(taskModel.getIteration()).thenReturn(1);
        when(taskModel.getTaskId()).thenReturn("taskId");
        when(taskModel.getWorkerId()).thenReturn("workerId");
        when(taskModel.getCorrelationId()).thenReturn("correlationId");
        when(taskModel.getScheduledTime()).thenReturn(System.currentTimeMillis());
        when(taskModel.getStartTime()).thenReturn(System.currentTimeMillis());
        when(taskModel.getEndTime()).thenReturn(System.currentTimeMillis());
        when(taskModel.getStatus()).thenReturn(null);
        when(taskModel.getReasonForIncompletion()).thenReturn(null);
        when(taskModel.getRetryCount()).thenReturn(0);
        when(taskModel.getPollCount()).thenReturn(0);
        when(taskModel.getInputData()).thenReturn(Collections.emptyMap());
        when(taskModel.getOutputData()).thenReturn(Collections.emptyMap());
        return taskModel;
    }
}