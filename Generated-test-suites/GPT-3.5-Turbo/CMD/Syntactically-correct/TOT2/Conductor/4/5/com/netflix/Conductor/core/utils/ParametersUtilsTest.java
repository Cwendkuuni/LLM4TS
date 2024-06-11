import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        assertNotNull(taskInputV2);
        assertTrue(taskInputV2.isEmpty());
    }

    @Test
    public void testReplaceVariables() {
        String input = "${workflow.input.foo}";
        DocumentContext documentContext = JsonPath.parse("{}");

        Object replacedValue = parametersUtils.replaceVariables(input, documentContext, null);

        assertNull(replacedValue);
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("foo", "${workflow.input.foo}");

        Object replacedValue = parametersUtils.replace(inputMap, "{}");

        assertNotNull(replacedValue);
        assertTrue(replacedValue instanceof Map);
        Map<String, Object> resultMap = (Map<String, Object>) replacedValue;
        assertNull(resultMap.get("foo"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("bar", "baz");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("baz", workflowInput.get("bar"));
    }
}