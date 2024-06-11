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
        // Typical use case
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(taskInput);
        
        // Edge case
        assertEquals(0, taskInput.size());

        // Error scenario
        assertNull(taskInput.get("key"));
    }

    @Test
    public void testGetTaskInputV2() {
        // Typical use case
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();
        String taskId = "task123";
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, "task123", taskDefinition);
        assertNotNull(taskInputV2);

        // Edge case
        assertEquals(0, taskInputV2.size());

        // Error scenario
        assertNull(taskInputV2.get("key"));
    }

    @Test
    public void testReplace() {
        // Typical use case
        Map<String, Object> input = new HashMap<>();
        Object json = "json";
        Map<String, Object> replaced = parametersUtils.replace(input, json);
        assertNotNull(replaced);

        // Edge case
        assertEquals(0, replaced.size());

        // Error scenario
        assertNull(replaced.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        // Typical use case
        String paramString = "${variable}";
        Object replaced = parametersUtils.replaceVariables(paramString, JsonPath.parse(""), "task123");
        assertNotNull(replaced);
        assertEquals("value", replaced.toString());

        // Edge case
        paramString = "${}";
        replaced = parametersUtils.replaceVariables(paramString, JsonPath.parse(""), "task123");
        assertNotNull(replaced);
        assertEquals("", replaced.toString());

        // Error scenario
        paramString = "${invalidExpression}";
        replaced = parametersUtils.replaceVariables(paramString, JsonPath.parse(""), "task123");
        assertNull(replaced);
    }

    @Test
    public void testGetWorkflowInput() {
        // Typical use case
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(workflowInput);

        // Edge case
        assertEquals(0, workflowInput.size());

        // Error scenario
        assertNull(workflowInput.get("key"));
    }
}