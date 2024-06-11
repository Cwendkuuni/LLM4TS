
package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() { //only a simple test, we should add more for proper testing
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getWorkflowDefinition()).thenReturn(new WorkflowDef());

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(inputParams);

        assertEquals(inputParams, parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId"));
    }

    //Similar tests should also be written for all the' public methods in the ParametersUtils class.
}
