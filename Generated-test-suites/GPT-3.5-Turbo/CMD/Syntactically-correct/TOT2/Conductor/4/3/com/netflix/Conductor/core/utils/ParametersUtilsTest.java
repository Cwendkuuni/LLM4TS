import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        // Test for null inputParams
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "12345";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);
        assertNotNull(taskInput);
        assertTrue(taskInput.isEmpty());

        // Test for empty inputParams
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = new HashMap<>();
        workflow.setInput(workflowInput);
        taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(taskInput);
        assertTrue(taskInput.isEmpty());

        // Add more test cases as needed
    }

    // Add more test cases for each public method in ParametersUtils class

}