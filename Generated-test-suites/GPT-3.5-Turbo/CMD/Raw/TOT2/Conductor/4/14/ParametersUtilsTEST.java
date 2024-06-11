import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        
        TaskDef taskDefinition = new TaskDef();
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.taskId");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task1");
        
        assertNotNull(taskInput);
        assertEquals(taskInput.size(), 1);
        assertTrue(taskInput.containsKey("param1"));
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.taskId");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "task1", null);
        
        assertNotNull(taskInput);
        assertEquals(taskInput.size(), 1);
        assertTrue(taskInput.containsKey("param1"));
    }

    // Add more test cases for other methods in ParametersUtils class

}