import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        // Test case for getTaskInput method
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        // Add assertions based on expected behavior
    }

    @Test
    public void testReplace() {
        // Test case for replace method
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String paramString = "${input.workflowId}";
        Object json = "{\"input\": {\"workflowId\": \"123\"}}";

        Object replacedValue = parametersUtils.replace(paramString, json);

        assertNotNull(replacedValue);
        assertEquals("123", replacedValue);
        // Add assertions based on expected behavior
    }

    @Test
    public void testReplaceVariables() {
        // Test case for replaceVariables method
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String paramString = "${workflowId}";
        DocumentContext documentContext = JsonPath.parse("{\"workflowId\": \"123\"}");
        String taskId = "task1";

        Object replacedValue = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertNotNull(replacedValue);
        assertEquals("123", replacedValue);
        // Add assertions based on expected behavior
    }

    @Test
    public void testGetWorkflowInput() {
        // Test case for getWorkflowInput method
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        // Add assertions based on expected behavior
    }

    // Add more test cases for other methods if needed

}