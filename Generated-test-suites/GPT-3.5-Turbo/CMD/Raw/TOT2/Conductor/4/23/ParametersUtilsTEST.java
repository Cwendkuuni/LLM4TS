import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils();
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        // Set up WorkflowModel with required attributes

        TaskDef taskDefinition = new TaskDef();
        // Set up TaskDef with required attributes

        Map<String, Object> inputParams = new HashMap<>();
        // Set up inputParams with required attributes

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        
        // Asserts for typical use case
        assertNotNull(taskInput);
        // Add more asserts for edge cases and error scenarios
    }

    // Add more test methods for other public methods in ParametersUtils class

}