import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        // Test normal input
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "12345";
        TaskDef taskDef = new TaskDef();

        Map<String, Object> taskInput = parametersUtils.getTaskInput(input, workflow, taskDef, taskId);

        assertEquals("Task input should be empty", 0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV1() {
        // Test when workflow input is null
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(null);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV1(workflow, inputParams);

        assertEquals("Task input should be empty", 0, taskInput.size());
    }

    @Test
    public void testGetWorkflowInput() {
        // Test when workflow definition input template is null
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(null);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("Workflow input should be the same as input params", inputParams, workflowInput);
    }
}