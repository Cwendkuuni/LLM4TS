package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "testTaskId";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertEquals(0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> input = new HashMap<>();
        String taskId = "testTaskId";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        assertEquals(0, taskInput.size());
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = "";
        Map<String, Object> replaced = parametersUtils.replace(input, json);

        assertEquals(0, replaced.size());
    }

    @Test
    public void testReplaceString() {
        String paramString = "";
        Object replaced = parametersUtils.replace(paramString);

        assertEquals("", replaced);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "";
        DocumentContext documentContext = JsonPath.parse("");
        String taskId = "testTaskId";
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("", replaced);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(0, workflowInput.size());
    }
}