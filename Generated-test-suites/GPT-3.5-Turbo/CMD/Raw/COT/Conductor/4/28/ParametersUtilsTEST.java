
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "12345";
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
        // Add specific assertions based on expected behavior
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "12345";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        assertNotNull(result);
        // Add specific assertions based on expected behavior
    }

    // Additional test cases for other methods in ParametersUtils can be added here

}

