import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Test case for getTaskInput method
        Map<String, Object> inputParams = null; // Define input parameters
        WorkflowModel workflow = new WorkflowModel(); // Create a WorkflowModel object
        TaskDef taskDefinition = new TaskDef(); // Create a TaskDef object
        String taskId = "12345"; // Task ID
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert statements to validate the result
        // Add your assertions here
    }

    @Test
    public void testGetWorkflowInput() {
        // Test case for getWorkflowInput method
        WorkflowDef workflowDef = new WorkflowDef(); // Create a WorkflowDef object
        Map<String, Object> inputParams = null; // Define input parameters
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Assert statements to validate the result
        // Add your assertions here
    }

    // Add more test cases to cover other methods in ParametersUtils class

}