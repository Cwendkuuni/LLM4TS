import org.junit.Test;

import com.netflix.conductor.core.utils.ParametersUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = new WorkflowModel();
        // Initialize workflowModel with necessary data for testing

        TaskDef taskDef = new TaskDef();
        // Initialize taskDef with necessary data for testing

        Map<String, Object> inputParams = new HashMap<>();
        // Initialize inputParams with necessary data for testing

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

        // Add assertions to validate the result based on typical use cases, edge cases, and error scenarios
        assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        // Initialize workflow with necessary data for testing

        String taskId = "task2";

        TaskDef taskDefinition = new TaskDef();
        // Initialize taskDefinition with necessary data for testing

        Map<String, Object> input = new HashMap<>();
        // Initialize input with necessary data for testing

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add assertions to validate the result based on typical use cases, edge cases, and error scenarios
        assertNotNull(result);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in ParametersUtils class

}