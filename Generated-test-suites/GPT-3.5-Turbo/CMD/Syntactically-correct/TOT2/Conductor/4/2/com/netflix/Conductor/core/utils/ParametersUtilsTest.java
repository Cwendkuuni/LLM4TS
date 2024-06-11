import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputV2() {
        // Write test case for getTaskInputV2
    }

    @Test
    public void testClone() {
        // Write test case for clone method
    }

    @Test
    public void testReplace() {
        // Write test case for replace method
    }

    @Test
    public void testReplaceList() {
        // Write test case for replaceList method
    }

    @Test
    public void testReplaceVariables() {
        // Write test case for replaceVariables method
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}