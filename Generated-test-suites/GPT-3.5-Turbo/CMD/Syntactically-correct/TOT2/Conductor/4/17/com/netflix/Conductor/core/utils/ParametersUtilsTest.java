import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "12345";
        
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "Hello ${name}");
        
        Object json = "{\"name\": \"World\"}";

        Map<String, Object> result = parametersUtils.replace(input, json);
        
        assertNotNull(result);
        assertEquals("Hello World", result.get("testKey"));
    }

    // Add more test cases for other methods in ParametersUtils class

}