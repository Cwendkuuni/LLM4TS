import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        // Typical use case
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
        
        // Edge case
        inputParams = null;
        result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
        
        // Error scenario
        assertNull(parametersUtils.getTaskInput(null, null, null, taskId));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        // Typical use case
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);

        // Edge case
        workflowDef.setInputTemplate(new HashMap<>());
        result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);

        // Error scenario
        assertNull(parametersUtils.getWorkflowInput(null, null));
    }

    // Add more test cases for other methods in ParametersUtils class

}