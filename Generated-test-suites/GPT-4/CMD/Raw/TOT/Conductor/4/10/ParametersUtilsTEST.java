import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;
    
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }
    
    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel mockWorkflow = Mockito.mock(WorkflowModel.class);
        TaskDef mockTaskDef = Mockito.mock(TaskDef.class);
        String taskId = "1";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, mockWorkflow, mockTaskDef, taskId);
        
        assertNotNull(result);
        // Add assertions relevant to your specific test case.
    }

    @Test
    public void testGetTaskInputV2() {
        // Similar test case as above, but for the getTaskInputV2 method.
    }

    @Test
    public void testReplace() {
        // Use a similar format as the getTaskInput test method, but for replace 
    }

    @Test
    public void testReplaceString() {
        // Similar test case as above, but for the replaceString method.
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef mockWorkflowDef = Mockito.mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getWorkflowInput(mockWorkflowDef, inputParams);
        
        assertNotNull(result);
        // Add assertions relevant to your specific test case
    }
}