import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils utils = new ParametersUtils(new ObjectMapper());
        
        // Create some mock input parameters
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        // Create a mock workflow model
        WorkflowModel mockWorkflow = new WorkflowModel();
        mockWorkflow.setWorkflowId("1234");
        mockWorkflow.setWorkflowType("TestWorkflow");

        // Create a mock task definition
        TaskDef mockTaskDef = new TaskDef();
        mockTaskDef.setName("Test Task");
        mockTaskDef.setRetryCount(0);

        // Necessary id for the method to work
        String taskId = "1234";

        Map<String, Object> result = utils.getTaskInput(inputParams, mockWorkflow, mockTaskDef, taskId);
        
        // Assert that the result from the method is not null.
        assertNotNull(result);
        // Assert that the result contains the key-value pair from the input
        assertTrue(result.containsKey("testKey"));
        assertEquals("testValue", result.get("testKey"));

        // Additional asserts would be done based on different edge and error scenarios.
        // For complete test coverage, more tests covering all possible branches in 
        // your method would be needed, including edge cases and error scenarios.
    }

    // More test methods for other methods in ParametersUtils class...
}