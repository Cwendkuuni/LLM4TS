import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "123";
        Map<String, Object> inputParams = new HashMap<>();
        
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);
        
        assertNotNull(taskInput);
        // Add more assertions as needed for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.inputParam}");
        DocumentContext documentContext = JsonPath.parse("{\"workflow\": {\"inputParam\": 123}}");
        
        Map<String, Object> replacedMap = parametersUtils.replace(input, documentContext);
        
        assertNotNull(replacedMap);
        assertEquals(123, replacedMap.get("key1"));
        // Add more assertions as needed for typical use cases, edge cases, and error scenarios
    }

    // Add more test cases for other methods in ParametersUtils class

}