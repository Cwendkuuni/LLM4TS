import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("key1"));
        assertEquals("value2", taskInput.get("key2"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String input = "Hello, ${name}!";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "Alice");

        Object replaced = parametersUtils.replaceVariables(input, variables, "123");

        assertNotNull(replaced);
        assertEquals("Hello, Alice!", replaced.toString());
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("key1"));
        assertEquals("value2", workflowInput.get("key2"));
    }
}