import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    public ParametersUtilsTest() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Mock data
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();

        // Test the method
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");

        // Verify the result
        assertEquals(new HashMap<>(), taskInput);
    }

    @Test
    public void testGetTaskInputV2() {
        // Mock data
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDef = new TaskDef();

        // Test the method
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDef);

        // Verify the result
        assertEquals(new HashMap<>(), taskInput);
    }

    @Test
    public void testReplace() {
        // Mock data
        Map<String, Object> input = new HashMap<>();
        Object json = "{\"key\": \"value\"}";

        // Test the method
        Map<String, Object> replacedMap = parametersUtils.replace(input, json);

        // Verify the result
        assertEquals(new HashMap<>(), replacedMap);
    }

    @Test
    public void testReplaceString() {
        // Mock data
        String paramString = "${test}";

        // Test the method
        Object replacedValue = parametersUtils.replace(paramString);

        // Verify the result
        assertEquals("", replacedValue);
    }

    // Additional test methods can be added for thorough testing

}