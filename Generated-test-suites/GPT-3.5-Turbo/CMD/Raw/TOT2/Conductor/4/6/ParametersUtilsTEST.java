import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Check if the task input is as expected
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.param1}";
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("param1", "value"));
        String replacement = (String) parametersUtils.replaceVariables(paramString, documentContext, null);

        // Check if the replacement is as expected
        assertEquals("value", replacement);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Check if the workflow input is as expected
        assertEquals(0, workflowInput.size());
    }

    // Add more test cases for other public methods in ParametersUtils class

}