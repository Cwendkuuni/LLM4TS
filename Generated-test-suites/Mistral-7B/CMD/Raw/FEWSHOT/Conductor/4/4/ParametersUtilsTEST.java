package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParametersUtilsTest {
    private static final String WORKFLOW_NAME = "MyWorkflow";
    private static final String TASK_NAME = "MyTask";
    private static final String TASK_INPUT_FIELD = "taskInput";
    private static final String WORKFLOW_INPUT_FIELD = "workflowInput";
    private static final String TASK_OUTPUT_FIELD = "taskOutput";
    private static final String WORKFLOW_OUTPUT_FIELD = "workflowOutput";
    private static final String TASK_REFERENCE_FIELD = "taskReference";

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private TaskDef taskDef;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private TaskModel taskModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = Mockito.spy(new ObjectMapper());
        parametersUtils = new ParametersUtils(objectMapper);

        taskDef = Mockito.mock(TaskDef.class);
        workflowDef = Mockito.mock(WorkflowDef.class);
        workflowModel = Mockito.mock(WorkflowModel.class);
        taskModel = Mockito.mock(TaskModel.class);

        Mockito.doAnswer(
                new Answer<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> answer(InvocationOnMock invocation) throws IOException {
                        Map<String, Object> inputTemplate = new HashMap<>();
                        inputTemplate.put("someKey", "someValue");
                        return inputTemplate;
                    }
                }).when(taskDef).getInputTemplate();

        Mockito.doAnswer(
                new Answer<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> answer(InvocationOnMock invocation) throws IOException {
                        Map<String, Object> input = new HashMap<>();
                        input.put("someInputKey", "someInputValue");
                        return input;
                    }
                }).when(workflowModel).getInput();

        Mockito.doAnswer(
                new Answer<TaskModel>() {
                    @Override
                    public TaskModel answer(InvocationOnMock invocation) {
                        return taskModel;
                    }
                }).when(workflowModel).getTaskByRefName(TASK_REFERENCE_FIELD);

        Mockito.doAnswer(
                new Answer<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> answer(InvocationOnMock invocation) throws IOException {
                        Map<String, Object> output = new HashMap<>();
                        output.put("someOutputKey", "someOutputValue");
                        return output;
                    }
                }).when(taskModel).getOutputData();

        Mockito.doAnswer(
                new Answer<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> answer(InvocationOnMock invocation) throws IOException {
                        Map<String, Object> input = new HashMap<>();
                        input.put("someInputKey", "someInputValue");
                        return input;
                    }
                }).when(taskModel).getInputData();

        Mockito.doReturn(true).when(taskModel).isLoopOverTask();
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("someKey", "someValue");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowVersion("1.0.0");
        workflowModel.setInput(Collections.emptyMap());
        workflowModel.setTasks(
                Collections.singletonList(
                        new TaskModel()
                                .setReferenceTaskName(TASK_NAME)
                                .setTaskDefName(TASK_NAME)
                                .setInputData(Collections.singletonMap("someInputKey", "someInputValue"))
                                .setOutputData(Collections.singletonMap("someOutputKey", "someOutputValue"))));

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, UUID.randomUUID().toString());
        assertEquals(inputParams.size() + 1, result.size());
        assertEquals("someInputValue", result.get("someInputKey"));
        assertEquals("someOutputValue", result.get("someKey.someOutputKey"));
        assertNull(result.get("someKey.someInputKey"));
        assertNull(result.get("someKey.someOtherKey"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("someKey", "someValue");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowVersion("2.0.0");
        workflowModel.setInput(Collections.singletonMap("workflowInput", Collections.singletonMap("someInputKey", "someInputValue")));
        workflowModel.setTasks(
                Collections.singletonList(
                        new TaskModel()
                                .setReferenceTaskName(TASK_NAME)
                                .setTaskDefName(TASK_NAME)
                                .setInputData(Collections.singletonMap("taskInput.someInputKey", "someTaskInputValue"))
                                .setOutputData(Collections.singletonMap("taskOutput.someOutputKey", "someTaskOutputValue"))));

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, UUID.randomUUID().toString());
        assertEquals(inputParams.size() + 2, result.size());
        assertEquals("someInputValue", result.get("workflowInput.someInputKey"));
        assertEquals("someTaskInputValue", result.get("someKey.taskInput.someInputKey"));
        assertEquals("someOutputValue", result.get("workflowInput.someOutputKey"));
        assertEquals("someTaskOutputValue", result.get("someKey.taskOutput.someOutputKey"));
        assertNull(result.get("someKey.someInputKey"));
        assertNull(result.get("someKey.someOtherKey"));
    }

    @Test
    public void testReplace() {
        String paramString = "$${workflowInput.someInputKey}.$${taskOutput.someOutputKey}";
        Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);

        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(paramString, replacement.toString());

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowVersion("2.0.0");
        workflowModel.setInput(Collections.singletonMap("workflowInput", Collections.singletonMap("someInputKey", "someInputValue")));
        workflowModel.setTasks(
                Collections.singletonList(
                        new TaskModel()
                                .setReferenceTaskName(TASK_NAME)
                                .setTaskDefName(TASK_NAME)
                                .setInputData(Collections.singletonMap("taskInput.someInputKey", "someTaskInputValue"))
                                .setOutputData(Collections.singletonMap("taskOutput.someOutputKey", "someTaskOutputValue"))));

        documentContext = JsonPath.parse(workflowModel, configuration);
        replacement = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("someInputValue.someTaskOutputValue", replacement.toString());

        replacement = parametersUtils.replaceVariables(paramString, documentContext, TASK_NAME);
        assertEquals("taskModel.outputData.someOutputKey", replacement.toString());

        replacement = parametersUtils.replaceVariables(paramString, documentContext, TASK_REFERENCE_FIELD);
        assertEquals("taskModelList[0].outputData.someOutputKey", replacement.toString());
    }

    @Test
    public void testReplaceList() {
        String paramString = "[${workflowInput.someInputKey}]";
        Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);

        List<Object> replacement = (List<Object>) parametersUtils.replaceList(Collections.singletonList("someValue"), null, documentContext);
        assertEquals(Collections.singletonList("someValue"), replacement);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowVersion("2.0.0");
        workflowModel.setInput(Collections.singletonMap("workflowInput", Collections.singletonMap("someInputKey", "someInputValue")));
        workflowModel.setTasks(
                Collections.singletonList(
                        new TaskModel()
                                .setReferenceTaskName(TASK_NAME)
                                .setTaskDefName(TASK_NAME)
                                .setInputData(Collections.singletonMap("taskInput.someInputKey", "someTaskInputValue"))
                                .setOutputData(Collections.singletonMap("taskOutput.someOutputKey", "someTaskOutputValue"))));

        documentContext = JsonPath.parse(workflowModel, configuration);
        replacement = parametersUtils.replaceList(Collections.singletonList("someValue"), documentContext, null);
        assertEquals(Collections.singletonList("someInputValue"), replacement);

        replacement = parametersUtils.replaceList(
                Collections.singletonList("${workflowInput.someInputKey}"), documentContext, null);
        assertEquals(Collections.singletonList("someInputValue"), replacement);

        replacement = parametersUtils.replaceList(
                Collections.singletonList("${workflowInput.someInputKey}"), documentContext, TASK_NAME);
        assertEquals(Collections.singletonList("taskModel.outputData.someOutputKey"), replacement);

        replacement = parametersUtils.replaceList(
                Collections.singletonList("${workflowInput.someInputKey}"), documentContext, TASK_REFERENCE_FIELD);
        assertEquals(Collections.singletonList("taskModelList[0].outputData.someOutputKey"), replacement);

        replacement = parametersUtils.replaceList(
                Collections.singletonList("[${workflowInput.someInputKey}]"), documentContext, TASK_NAME);
        assertEquals(
                Collections.singletonList("[taskModel.outputData.someOutputKey]"), replacement);

        replacement = parametersUtils.replaceList(
                Collections.singletonList("[${workflowInput.someInputKey}]"), documentContext, TASK_REFERENCE_FIELD);
        assertEquals(
                Collections.singletonList("[taskModelList[0].outputData.someOutputKey]"), replacement);
    }
}