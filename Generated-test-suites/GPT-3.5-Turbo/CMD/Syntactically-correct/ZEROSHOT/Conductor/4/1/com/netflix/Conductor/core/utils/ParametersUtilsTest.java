import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;



public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "workflow.input.param2");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertEquals("value1", result.get("param1"));
        assertEquals(null, result.get("param2"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value1", result.get("param1"));
    }

    // Add more test methods for other functionalities of ParametersUtils class

}

