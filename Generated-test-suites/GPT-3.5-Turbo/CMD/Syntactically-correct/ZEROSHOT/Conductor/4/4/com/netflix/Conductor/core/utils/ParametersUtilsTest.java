import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    private WorkflowModel mockWorkflow;
    private TaskDef mockTaskDefinition;
    private String mockTaskId;
    private Map<String, Object> mockInputParams;

    @Before
    public void setUp() {
        mockWorkflow = mock(WorkflowModel.class);
        mockTaskDefinition = mock(TaskDef.class);
        mockTaskId = "mockTaskId";
        mockInputParams = new HashMap<>();
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(mockInputParams, mockWorkflow, mockTaskId, mockTaskDefinition);
        // Add your assertions here
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = ""; // Add sample JSON here
        Map<String, Object> replaced = parametersUtils.replace(input, json);
        // Add your assertions here
    }

    @Test
    public void testReplaceVariables() {
        String paramString = ""; // Add sample parameter string here
        Object replaced = parametersUtils.replaceVariables(paramString);
        // Add your assertions here
    }

    // Add more test methods for other functionality

}