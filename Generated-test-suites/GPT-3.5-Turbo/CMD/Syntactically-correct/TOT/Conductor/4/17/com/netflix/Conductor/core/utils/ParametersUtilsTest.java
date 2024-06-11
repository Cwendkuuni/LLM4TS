import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput_NullInputParams_ReturnsEmptyMap() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2_NullInputParams_ReturnsInputParams() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
        assertEquals(1, taskInput.size());
        assertEquals("value", taskInput.get("key"));
    }

    @Test
    public void testReplace_NullInput_ReturnsEmptyMap() {
        Map<String, Object> replacedMap = parametersUtils.replace(Collections.emptyMap(), Collections.emptyMap());
        assertEquals(0, replacedMap.size());
    }

    @Test
    public void testReplaceVariables_EmptyParamString_ReturnsParamString() {
        String replacedString = (String) parametersUtils.replaceVariables("", null, "taskId");
        assertEquals("", replacedString);
    }

    @Test
    public void testReplaceVariables_EnvironmentVariable_ReturnsSystemParameter() {
        String replacedVariable = (String) parametersUtils.replaceVariables("${SYS_ENV_VAR}", null, "taskId");
        assertEquals(System.getenv("SYS_ENV_VAR"), replacedVariable);
    }

    @Test
    public void testGetWorkflowInput_WithInputTemplate_ReturnsMergedInputParams() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("key", "value"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key2", "value2");
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(2, result.size());
        assertEquals("value", result.get("key"));
        assertEquals("value2", result.get("key2"));
    }

    // Add more test cases to cover edge cases and error scenarios

}