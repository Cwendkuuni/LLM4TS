import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "123";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", 123);

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);

        assertEquals("value1", taskInput.get("param1"));
        assertEquals(123, taskInput.get("param2"));
        assertNotEquals("value2", taskInput.get("param1"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDef = new TaskDef();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "param1_value");
        inputParams.put("param2", "param2_value");

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDef);

        assertEquals("param1_value", taskInput.get("param1"));
        assertEquals("param2_value", taskInput.get("param2"));
        assertNotEquals("param3_value", taskInput.get("param3"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "Hello, ${name}");
        input.put("key2", "${value}");
        input.put("key3", "${value1}.${value2}");

        Map<String, Object> json = new HashMap<>();
        json.put("name", "World");
        json.put("value", 123);
        json.put("value1", "ABC");
        json.put("value2", "DEF");

        Map<String, Object> replaced = parametersUtils.replace(input, json);

        assertEquals("Hello, World", replaced.get("key1"));
        assertEquals(123, replaced.get("key2"));
        assertEquals("ABC.DEF", replaced.get("key3"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "Hello, ${name}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "123";

        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("Hello, World", replaced);
        assertNotEquals("Hello, ${name}", replaced);
    }

    // Additional test cases can be added to cover more scenarios

}