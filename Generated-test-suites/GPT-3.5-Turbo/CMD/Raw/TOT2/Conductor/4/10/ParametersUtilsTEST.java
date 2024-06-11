import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        // Typical Use Case
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        assertEquals("value1", taskInput.get("key1"));
        assertEquals("value2", taskInput.get("key2"));
        
        // Edge Case
        inputParams.put("key1", null);
        taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        assertNull(taskInput.get("key1"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        // Typical Use Case
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.key}");
        workflow.setInput(new HashMap<>());
        workflow.getInput().put("key", "value");
        
        Object result = parametersUtils.replace(input, workflow);
        assertEquals("value", ((Map<String, Object>) result).get("key1"));

        // Edge Case
        input.put("key1", "${workflow.input.invalidKey}");
        result = parametersUtils.replace(input, workflow);
        assertNull(((Map<String, Object>) result).get("key1"));
        
        // Exception Handling
        input.put("key1", "${workflow.input.key:invalidPath}");
        result = parametersUtils.replace(input, workflow);
        assertNull(((Map<String, Object>) result).get("key1"));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        // Typical Use Case
        DocumentContext documentContext = JsonPath.parse("{\"workflow\": {\"input\": {\"key\": \"value\"}}}");
        String paramString = "${workflow.input.key}";
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("value", result);
        
        // Edge Case
        paramString = "${workflow.input.invalidKey}";
        result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("", result);
        
        // Exception Handling
        paramString = "${workflow.input.key:invalidPath}";
        result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("", result);
    }

    // Add more test cases for other methods as per edge cases and error scenarios

}