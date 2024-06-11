import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        // Mock input parameters
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        // Mock behavior
        when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());

        // Test method
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Verify result
        Assert.assertNotNull(result);
        // Add more assertions as needed based on the implementation
    }

    @Test
    public void testGetTaskInput() {
        // Mock input parameters
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        // Mock behavior
        when(workflow.getWorkflowDefinition()).thenReturn(new WorkflowDef(1));

        // Test method
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Verify result
        Assert.assertNotNull(result);
        // Add more assertions as needed based on the implementation
    }

    // Add more test methods to cover all scenarios and edge cases for ParametersUtils class

}