import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        // Typical Use Case
        // Prepare test data
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        // Call the method
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Validate the result
        assertEquals("Result should be as expected", expectedValue, result);
        
        // Edge Case
        // Add edge test case if needed
        
        // Error Scenario
        // Add error test case if needed
    }

    @Test
    public void testGetTaskInputV2() {
        // Prepare test data
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();

        // Call the method
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Validate the result
        assertEquals("Result should be as expected", expectedValue, result);
    }

    // Add tests for other methods in ParametersUtils class

}