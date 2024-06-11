
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest  {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ParametersUtils parametersUtils;

    private WorkflowModel workflowModel;
    private TaskDef taskDef;
    private WorkflowDef workflowDef;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        workflowModel = new WorkflowModel();
        taskDef = new TaskDef();
        workflowDef = new WorkflowDef();
        // Add setup data for models if required
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        // Add data to inputParams if required
        String taskId = "1";
        assertEquals(new HashMap<>(), parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId));
        // Add assertions to check the task input
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        // Add data to input if required
        String taskId = "1";
        assertEquals(new HashMap<>(), parametersUtils.getTaskInputV2(input, workflowModel, taskId, taskDef));
        // Add assertions to check the task input v2
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        // Add data to inputParams if required
        assertEquals(new HashMap<>(), parametersUtils.getWorkflowInput(workflowDef, inputParams));
        // Add assertions to check the workflow input
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        // Add data to input if required
        Object json = new Object();
        // Initialize json object
        assertEquals(new HashMap<>(), parametersUtils.replace(input, json));
        // Add assertions to check the replaced output
    }

    @Test
    public void testReplaceString() {
        String paramString = "test";
        assertEquals("test", parametersUtils.replace(paramString));
        // Add assertions to check the replaced string
    }
}
