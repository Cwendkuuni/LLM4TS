
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        taskDef.setName("testTask");
        
        Map<String, Object> inputParams = Map.of("param1", "value1", "param2", "value2");
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");
        
        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("value2", taskInput.get("param2"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        
        Map<String, Object> input = Map.of("testInput1", "input1", "testInput2", "input2");
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, "taskId", null);
        
        assertNotNull(taskInput);
        assertEquals("input1", taskInput.get("testInput1"));
        assertEquals("input2", taskInput.get("testInput2"));
    }

    @Test
    public void testReplace() {
        String jsonString = "{\"key\": \"value\"}";
        Object replaced = parametersUtils.replace(jsonString);
        
        assertNotNull(replaced);
        assertEquals("{\"key\":\"value\"}", replaced.toString());
    }

    // Add more test cases to cover edge cases and exceptions

}

