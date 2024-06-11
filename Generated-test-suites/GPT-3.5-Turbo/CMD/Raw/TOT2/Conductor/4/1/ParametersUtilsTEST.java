import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.getWorkflowDefinition().setSchemaVersion(2);
        workflow.setWorkflowId("workflowId");

        TaskDef taskDef = new TaskDef();
        taskDef.setName("taskName");
        taskDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDef);

        assertNotNull(taskInput);
        assertEquals("workflowId", taskInput.get("workflowId"));
        // Add more assertions here for edge cases and error scenarios
    }

    // Add more test cases for other public methods in ParametersUtils class

}