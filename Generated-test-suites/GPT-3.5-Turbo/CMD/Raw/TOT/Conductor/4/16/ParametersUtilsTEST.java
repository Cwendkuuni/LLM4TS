import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput_NullInputV1() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "123";
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflow, taskDef, taskId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputV2_EmptyInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "123";
        Map<String, Object> emptyInput = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInputV2(emptyInput, workflow, taskId, taskDef);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReplaceVariables_EmptyStringInput() {
        String input = "";
        Object result = parametersUtils.replaceVariables(input);
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_ValidInput() {
        String input = "${workflow.input.param1}";
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("param1", "value1");
        workflow.setInput(workflowInput);
        Object result = parametersUtils.replaceVariables(input);
        assertEquals("value1", result);
    }

    @Test
    public void testGetWorkflowInput_WithInputTemplate() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "value1");
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, input);
        assertNotNull(result);
        assertTrue(result.containsKey("param1"));
        assertEquals("value1", result.get("param1"));
    }
}