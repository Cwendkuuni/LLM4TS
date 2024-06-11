
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
  
    @Before
    public void setUp() {
        objectMapper=new ObjectMapper();
        parametersUtils=new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "testTaskId";
  
        parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "testTaskId";
  
        parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
  
        parametersUtils.getWorkflowInput(workflowDef, inputParams);
    }
  
    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("test_key", "${test_value}");
  
        parametersUtils.replace(input, new HashMap<>());
    }

    @Test
    public void testReplaceString() {
        String paramString = "${test_value}";
        parametersUtils.replace(paramString);
    }

}
