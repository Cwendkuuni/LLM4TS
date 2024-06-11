package com.netflix.conductor.core.utils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    @Test
    public void testParametersUtilsConstructor() {
        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        assertNotNull(parametersUtils);
    }

    @Test
    public void testGetTaskInput() {
        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        Map<String, Object> taskInput = parametersUtils.getTaskInput(new HashMap<>(), workflowModel, taskDef, "testTaskId");
        assertNotNull(taskInput);
    }

    @Test
    public void testReplace() {
        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        Map<String, Object> replaceMap = parametersUtils.replace(new HashMap<>(), new HashMap<>());
        assertNotNull(replaceMap);
    }

    //... Add additional test methods here
}