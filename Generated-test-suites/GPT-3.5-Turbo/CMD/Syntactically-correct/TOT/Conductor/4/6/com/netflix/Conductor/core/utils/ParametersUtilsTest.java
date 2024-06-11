import org.junit.Test;
import static org.junit.Assert.*;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";
        Map<String, Object> inputParams = new HashMap<>();
        
        // Typical use case
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(taskInput);
        
        // Edge case
        assertEquals(0, taskInput.size());
        
        // Error scenario
        // Add test case for error scenario here
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();
        
        // Typical use case
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        assertNotNull(taskInputV2);
        
        // Edge case
        // Add test case for edge case here
        
        // Error scenario
        // Add test case for error scenario here
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();
        
        // Typical use case
        Map<String, Object> replacedMap = parametersUtils.replace(input, json);
        assertNotNull(replacedMap);
        
        // Edge case
        // Add test case for edge case here
        
        // Error scenario
        // Add test case for error scenario here
    }

    // More test methods for other methods in ParametersUtils class

}