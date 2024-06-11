import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        // Typical Use Case
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "task1";
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        assertNotNull(taskInput);

        // Edge Case: Null inputParams
        Map<String, Object> emptyInputParams = null;
        Map<String, Object> taskInputEmptyParams = parametersUtils.getTaskInput(emptyInputParams, workflowModel, taskDef, taskId);
        assertNotNull(taskInputEmptyParams);

        // Exception Handling: Invalid task definition
        TaskDef invalidTaskDef = null;
        Map<String, Object> taskInputInvalidTaskDef = parametersUtils.getTaskInput(inputParams, workflowModel, invalidTaskDef, taskId);
        assertNotNull(taskInputInvalidTaskDef);
    }

    @Test
    public void testGetWorkflowInput() {
        // Typical Use Case
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(workflowInput);

        // Edge Case: Workflow without input template
        WorkflowDef workflowDefNoTemplate = new WorkflowDef();
        Map<String, Object> workflowInputNoTemplate = parametersUtils.getWorkflowInput(workflowDefNoTemplate, inputParams);
        assertNotNull(workflowInputNoTemplate);
    }
}