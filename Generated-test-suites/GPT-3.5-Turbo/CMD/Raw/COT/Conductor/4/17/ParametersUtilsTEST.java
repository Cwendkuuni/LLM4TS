
import org.junit.Test;
import static org.junit.Assert.*;

public class ParametersUtilsTest {

    // Test getTaskInput method
    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "taskId";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        
        // Assert the taskInput is not null
        assertNotNull(taskInput);
        // Add more assertions as needed
    }

    // Test getTaskInputV2 method
    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        
        // Assert the taskInputV2 is not null
        assertNotNull(taskInputV2);
        // Add more assertions as needed
    }

    // Test replace method
    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        Object json = "{\"key\": \"value\"}";
        Map<String, Object> replacedParams = parametersUtils.replace(input, json);
        
        // Assert the replacedParams is not null
        assertNotNull(replacedParams);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in ParametersUtils class

}

