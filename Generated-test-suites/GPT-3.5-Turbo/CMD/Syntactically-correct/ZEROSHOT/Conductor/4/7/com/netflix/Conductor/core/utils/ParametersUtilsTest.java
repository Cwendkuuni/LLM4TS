import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";
        Map<String, Object> inputParams = new HashMap<>();
        
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add assertions here for the expected result based on the input
        assertEquals(expectedValue, result.get("key"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Add assertions here for the expected result based on the input
        assertEquals(expectedValue, result.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputParams = new HashMap<>();
        Object json = "sample_json";

        Map<String, Object> result = parametersUtils.replace(inputParams, json);

        // Add assertions here for the expected result based on the input
        assertEquals(expectedValue, result.get("key"));
    }

    // Add more test methods for other functionality in ParametersUtils class

}