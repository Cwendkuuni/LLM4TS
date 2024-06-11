package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ObjectMapper mapper = new ObjectMapper();
    private ParametersUtils parametersUtils = new ParametersUtils(mapper);

    @Test
    public void getTaskInputTest() {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        String taskID = "task1";

        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskID);
        assertNotNull(result);
    }
}
