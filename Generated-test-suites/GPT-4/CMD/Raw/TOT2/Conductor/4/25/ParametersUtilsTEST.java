import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        String taskId = "TestTaskID";
        Map<String, Object> output;

        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        output = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(output);
        assertTrue("Output should not be empty.", output.size() > 0);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "testValue");
        String json = "{\"jsonKey\":\"jsonValue\"}";
        Map<String, Object> output;

        output = parametersUtils.replace(input, json);

        assertNotNull(output);
        assertEquals("Output should have same size as input.", input.size(), output.size());
    }
}