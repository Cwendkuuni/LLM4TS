import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        
        Map<String, Object> inputParams = new HashMap<>();
        
        // Typical use case
        Map<String, Object> output = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        assertNotNull(output);
        
        // Edge case
        assertNull(parametersUtils.getTaskInput(null, workflow, taskDefinition, "taskId"));
        
        // Error scenario
        assertThrows(NullPointerException.class, () -> {
            parametersUtils.getTaskInput(inputParams, null, taskDefinition, "taskId");
        });
    }

    @Test
    public void testGetTaskInputV2() {
        // To be implemented
    }

    @Test
    public void testReplace() {
        // To be implemented
    }

    // Add more test cases for other methods as needed

}