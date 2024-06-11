import com.netflix.conductor.core.utils.ParametersUtils;
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
        String taskId = "123";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Perform assertions on taskInput
        // Test the functionality of getTaskInput method
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Perform assertions on taskInput
        // Test the functionality of getTaskInputV2 method
    }

    // Add more test methods for other public methods in ParametersUtils class

    // Edge case and exception handling scenarios
    // Test edge cases and exception handling scenarios for each method

}