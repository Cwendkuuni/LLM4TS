import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = createDummyWorkflowModel();
        TaskDef taskDefinition = createDummyTaskDef();
        String taskId = "dummyTaskId";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        // Add specific assertions for the task input here
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = createDummyInput();
        String json = "{ \"key\": \"value\" }";

        Object replaced = parametersUtils.replace(input, json);

        assertNotNull(replaced);
        // Add specific assertions for the replaced object here
    }

    // Add more test methods for other methods in ParametersUtils class

    private WorkflowModel createDummyWorkflowModel() {
        WorkflowModel workflow = new WorkflowModel();
        // Set dummy values for the WorkflowModel
        return workflow;
    }

    private TaskDef createDummyTaskDef() {
        TaskDef taskDef = new TaskDef();
        // Set dummy values for the TaskDef
        return taskDef;
    }

    private Map<String, Object> createDummyInput() {
        Map<String, Object> input = new HashMap<>();
        // Add dummy key-value pairs to the input map
        return input;
    }
}