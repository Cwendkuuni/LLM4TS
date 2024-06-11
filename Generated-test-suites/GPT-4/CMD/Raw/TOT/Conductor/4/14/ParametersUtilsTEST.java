import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowModel workflowModel;
    private TaskDef taskDefinition;
    private String taskId;
    private Map<String, Object> inputParams;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        workflowModel = mock(WorkflowModel.class);
        taskDefinition = mock(TaskDef.class);
        taskId = "TestTaskId";
        inputParams = new HashMap<>();
    }

    @Test
    public void getTaskInputTest_v2Schema() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);
        assertNotNull(parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, taskId));
    }

    @Test
    public void getTaskInputTest_v1Schema() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);
        assertNotNull(parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, taskId));
    }

    // Write more tests for each method and edge cases in the class
}