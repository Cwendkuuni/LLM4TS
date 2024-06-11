import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "12345";
        
        // Typical use case
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertNotNull(result);
        
        // Edge case
        inputParams.put("testParam", "${workflow.input.test}");
        workflow.setInput(Collections.singletonMap("test", "testValue"));
        result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertEquals("testValue", result.get("testParam"));
        
        // Error scenario
        workflow.setInput(null);
        result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertNull(result.get("testParam"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${workflow.input.value}");

        Object json = "{\"workflow\": {\"input\": {\"value\": \"replacedValue\"}}}";

        // Typical use case
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertEquals("replacedValue", result.get("key"));

        // Error scenario
        input.clear();
        input.put("key", "${workflow.input.nonExistent}");
        result = parametersUtils.replace(input, json);
        assertNull(result.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        DocumentContext documentContext = JsonPath.parse("{\"workflow\": {\"input\": {\"value\": \"replacedValue\"}}}");
        String paramString = "${workflow.input.value}";

        // Typical use case
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "12345");
        assertEquals("replacedValue", result);

        // Error scenario
        paramString = "${workflow.input.nonExistent}";
        result = parametersUtils.replaceVariables(paramString, documentContext, "12345");
        assertTrue(result.toString().contains("Unable to clone input params"));
    }

    // Additional test cases can be added for other methods as needed

}