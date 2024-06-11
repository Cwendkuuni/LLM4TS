import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput_NullInputParams() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        assertEquals(1, taskInput.size());
    }

    @Test
    public void testReplace_NullInput() {
        Object result = parametersUtils.replace(null);
        assertNull(result);
    }

    @Test
    public void testReplace() {
        String jsonString = "{\"key\": \"value\"}";
        Object result = parametersUtils.replace(jsonString);
        assertNotNull(result);
        assertEquals("value", ((Map)result).get("key"));
    }

    // Add more test cases for other methods in the ParametersUtils class

}