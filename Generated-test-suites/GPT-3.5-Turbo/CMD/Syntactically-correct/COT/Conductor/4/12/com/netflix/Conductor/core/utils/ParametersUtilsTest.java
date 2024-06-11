
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

        assertNotNull(taskInput);
        // Add specific assertions based on the expected behavior of getTaskInput() method
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";
        TaskDef taskDef = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

        assertNotNull(taskInputV2);
        // Add specific assertions based on the expected behavior of getTaskInputV2() method
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String paramString = "testString";
        Object json = new Object(); // Provide a valid JSON object here

        Map<String, Object> replacedParams = parametersUtils.replace(paramString, json);

        assertNotNull(replacedParams);
        // Add specific assertions based on the expected behavior of replace() method
    }

    // Add more test cases for other methods here

}
