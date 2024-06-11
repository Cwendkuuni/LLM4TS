// First the import section
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.Map;

// Then the class definition and member attributess
@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    private WorkflowModel mockWorkflowModel;
    private TaskDef mockTaskDef;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
        mockWorkflowModel = mock(WorkflowModel.class);
        mockTaskDef = mock(TaskDef.class);
    }

    // Every method should have it's own corresponding test method
    @Test
    public void testGetTaskInput_Normal() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");

        when(mockWorkflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, mockWorkflowModel, mockTaskDef, "testTaskId");

        assertEquals("value1", result.get("key1"));
    }

    @Test
    public void testGetTaskInputV2_NormalWithNullInput() {
        Map<String, Object> result = parametersUtils.getTaskInputV2(null, mockWorkflowModel, "testTaskId", mockTaskDef);

        assertNotNull(result);
    }

    @Test
    public void testReplaceWithMap_Normal() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "${workflow.input.testKey}");

        Map<String, Object> json = new HashMap<>();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("testKey", "testValue");
        json.put("workflow", Collections.singletonMap("input", workflowInput));

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertEquals(1, result.size());
        assertEquals("testValue", result.get("testKey"));
    }

    @Test
    public void testReplaceWithJsonString_Normal() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "${workflow.input.testKey}");

        String json = "{ \"workflow\": { \"input\": { \"testKey\": \"testValue\" } } }";

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertEquals(1, result.size());
        assertEquals("testValue", result.get("testKey"));
    }

    // ... additional tests for remaining public methods...
}