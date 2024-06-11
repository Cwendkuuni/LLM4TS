import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        // Test scenario when inputParams and taskDefinition are null
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = null;
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = null;
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        assertTrue(taskInput.isEmpty());
    }

    @Test
    public void testGetTaskInputV2() {
        // Test scenario when inputParams is not null
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        assertNotNull(taskInput);
        assertTrue(taskInput.containsKey("key1"));
        assertEquals("value1", taskInput.get("key1"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input}");
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(Collections.singletonMap("input", "workflowInput"));
        
        Object replacedInput = parametersUtils.replace(input, workflow);

        assertNotNull(replacedInput);
        assertTrue(replacedInput instanceof Map);
        assertTrue(((Map<String, Object>) replacedInput).containsKey("key1"));
        assertEquals("workflowInput", ((Map<String, Object>) replacedInput).get("key1"));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        DocumentContext documentContext = JsonPath.parse("{\"workflow\": {\"input\": \"workflowInput\"}}");
        String paramString = "${workflow.input}";

        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, "123");

        assertNotNull(replacement);
        assertTrue(replacement instanceof String);
        assertEquals("workflowInput", replacement);
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("key1", "value1"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key2", "value2");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertTrue(workflowInput.containsKey("key1"));
        assertTrue(workflowInput.containsKey("key2"));
        assertEquals("value1", workflowInput.get("key1"));
        assertEquals("value2", workflowInput.get("key2"));
    }

    // Add more test cases for other methods in the ParametersUtils class

}