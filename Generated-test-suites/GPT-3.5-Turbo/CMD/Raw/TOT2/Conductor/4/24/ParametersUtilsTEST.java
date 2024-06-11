import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.someParam");

        Map<String, Object> taskInput = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertNotNull(taskInput);
        assertEquals("expectedValue", taskInput.get("param1"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.someParam");

        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDefinition);

        assertNotNull(taskInput);
        assertEquals("expectedValue", taskInput.get("param1"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "value1");
        input.put("param2", "${workflow.input.param2}");

        Map<String, Object> json = new HashMap<>();
        json.put("workflow", Map.of("input", Map.of("param2", "test")));

        Map<String, Object> replaced = parametersUtils.replace(input, json);

        assertNotNull(replaced);
        assertEquals("value1", replaced.get("param1"));
        assertEquals("test", replaced.get("param2"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value2", workflowInput.get("param2"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "Hello ${workflow.input.name}, it is ${workflow.input.time}";

        Map<String, Object> json = new HashMap<>();
        json.put("workflow", Map.of("input", Map.of("name", "John", "time", "morning")));

        String replacedString = (String) parametersUtils.replaceVariables(paramString, json, null);

        assertEquals("Hello John, it is morning", replacedString);
    }
}