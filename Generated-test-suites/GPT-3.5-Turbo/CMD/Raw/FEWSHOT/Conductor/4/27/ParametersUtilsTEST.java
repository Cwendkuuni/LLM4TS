package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInputV2() {
        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow_id");
        workflow.setWorkflowName("test_workflow");
        workflow.setWorkflowVersion(1);

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setName("test_workflow_def");
        workflowDef.setInputTemplate(new HashMap<>() {{
            put("param1", "value1");
            put("param2", "value2");
        }});

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("test_task_def");
        taskDefinition.setInputTemplate(new HashMap<>() {{
            put("param3", "value3");
        }});

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input1", "workflow.input.param1");
        inputParams.put("input2", "workflow.input.param2");
        inputParams.put("input3", "task.input.param3");

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "task_id", taskDefinition);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("input1"));
        assertEquals("value2", taskInput.get("input2"));
        assertEquals("value3", taskInput.get("input3"));
    }

    @Test
    public void testReplace() {
        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> input = new HashMap<>() {{
            put("key1", "${workflow.input.param}");
        }};
        
        Map<String, Object> json = new HashMap<>() {{
            put("workflow", new HashMap<>() {{
                put("input", new HashMap<>() {{
                    put("param", "value");
                }});
            }});
        }};
        
        Map<String, Object> replaced = parametersUtils.replace(input, json);

        assertNotNull(replaced);
        assertEquals("value", replaced.get("key1"));
    }
}