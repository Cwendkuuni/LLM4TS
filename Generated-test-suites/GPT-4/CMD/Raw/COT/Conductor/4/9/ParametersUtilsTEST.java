
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.hamcrest.Matchers.*;

import java.util.*;

public class ParametersUtilsTest {
    
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        String taskId = "taskId";
        
        assertThatthrownBy(() -> parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        String taskId = "taskId";
        TaskDef taskDefinition = mock(TaskDef.class);

        assertThatthrownBy(() -> parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();

        assertThatthrownBy(() -> parametersUtils.getTaskInputV1(workflow, inputParams))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        
        assertThatThrownBy(() -> parametersUtils.getWorkflowInput(workflowDef, inputParams))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    public void testCloneSystemException() {
        Map<String, Object> inputParams = new HashMap<>();

        assertThatThrownBy(() -> parametersUtils.clone(inputParams))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new HashMap<>();

        assertThatthrownBy(() -> parametersUtils.replace(input, json))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    public void testReplaceString() {
        String paramString = "";

        assertThatThrownBy(() -> parametersUtils.replace(paramString))
            .isInstanceOf(RuntimeException.class)
            .hasCauseExactlyInstanceOf(IOException.class);
    }
}
