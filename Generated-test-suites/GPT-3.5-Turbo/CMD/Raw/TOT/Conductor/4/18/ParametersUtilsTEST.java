import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput_Success() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        // Initialize workflow and taskDef with required data

        Map<String, Object> inputParams = new HashMap<>();
        // Add input parameters

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");

        assertNotNull(taskInput);
        // Add assertions based on the expected output
    }

    @Test
    public void testReplace_Success() {
        Map<String, Object> input = new HashMap<>();
        // Add input data

        Object json = null;
        // Add JSON data

        Map<String, Object> replacedMap = parametersUtils.replace(input, json);

        assertNotNull(replacedMap);
        // Add assertions based on the expected output
    }

    @Test
    public void testReplaceVariables_Success() {
        DocumentContext documentContext = null;
        String paramString = "TestString";
        String taskId = "taskId";
        int depth = 0;

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId, depth);

        assertNotNull(result);
        // Add assertions based on the expected output
    }

    // Add more test methods covering edge cases and error scenarios

}