import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task1";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void testReplace() {
        String json = "{\"key\": \"value\"}";
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${key}";
        DocumentContext documentContext = JsonPath.parse("{}");
        String taskId = "task1";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertNotNull(result);
        // Add more assertions here
    }

    // Add more test methods for other methods in ParametersUtils class

}