import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private WorkflowModel workflowModel;
    private TaskDef taskDefinition;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
        workflowModel = new WorkflowModel();
        taskDefinition = new TaskDef();
    }

    @Test
    public void testGetTaskInput_WithNullInputParams() {
        Map<String, Object> inputParams = null;
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "task_id");
        assertNotNull(taskInput);
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2_WithNullInput() {
        Map<String, Object> input = null;
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflowModel, "task_id", taskDefinition);
        assertNotNull(taskInput);
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testReplace_WithValidInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        Object json = "{ \"key2\": \"value2\" }";
        Map<String, Object> replaced = parametersUtils.replace(input, json);
        assertNotNull(replaced);
        assertEquals(2, replaced.size());
        assertEquals("value1", replaced.get("key1"));
        assertEquals("value2", replaced.get("key2"));
    }

    @Test
    public void testReplaceVariables_WithValidParamString() {
        String paramString = "${workflow.input.test}";
        DocumentContext documentContext = JsonPath.parse("{ \"test\": \"test_value\" }");
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, "task_id");
        assertNotNull(replaced);
        assertEquals("test_value", replaced.toString());
    }

    @Test
    public void testReplaceVariables_WithInvalidParamString() {
        String paramString = "${workflow.invalid}";
        DocumentContext documentContext = JsonPath.parse("{}");
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, "task_id");
        assertNull(replaced);
    }

    @Test
    public void testGetWorkflowInput_WithNullInputParams() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = null;
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(workflowInput);
        assertEquals(0, workflowInput.size());
    }

    @Test
    public void testReplaceVariables_WithEmptyParamString() {
        String paramString = "${}";
        DocumentContext documentContext = JsonPath.parse("{}");
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, "task_id");
        assertEquals("", replaced.toString());
    }

}