import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";
        
        // Typical use case
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertTrue(taskInput.isEmpty());
        
        // Edge case
        workflow.getWorkflowDefinition().setSchemaVersion(1);
        taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertTrue(taskInput.isEmpty());
        
        // Error scenario
        workflow.getWorkflowDefinition().setSchemaVersion(2);
        taskDefinition.setInputTemplate(null);
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");
        taskInput = parametersUtils.getTaskInput(input, workflow, taskDefinition, taskId);
        assertEquals(1, taskInput.size());
        assertEquals("value", taskInput.get("key"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils();
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        
        // Typical use case
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertTrue(workflowInput.isEmpty());
        
        // Edge case
        workflowDef.setInputTemplate(new HashMap<>());
        workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertTrue(workflowInput.isEmpty());
    }
}