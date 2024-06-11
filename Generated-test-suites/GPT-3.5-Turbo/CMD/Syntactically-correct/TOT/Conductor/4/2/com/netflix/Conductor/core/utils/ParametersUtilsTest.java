import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("123");
        TaskDef taskDef = new TaskDef();
        taskDef.setName("task1");
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

        assertNotNull(taskInput);
        assertEquals("123", taskInput.get("workflow.workflowId"));
        // Add more test cases for different scenarios

    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setName("workflow1");
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("param1"));
        // Add more test cases for different scenarios

    }

    // Add more test cases for other methods

}