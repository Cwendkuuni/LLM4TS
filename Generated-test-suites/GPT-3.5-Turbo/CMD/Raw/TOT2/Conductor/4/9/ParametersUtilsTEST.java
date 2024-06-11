import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        when(workflow.getWorkflowDefinition()).thenReturn(new WorkflowDef());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);

        // Add your assertions here
        // Example:
        // assertEquals(expectedValue, result.get("key"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        Map<String, Object> input = new HashMap<>();
        String taskId = "task1";
        TaskDef taskDef = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDef);

        // Add your assertions here
        // Example:
        // assertEquals(expectedValue, result.get("key"));
    }

    @Test
    public void testReplace() {
        String input = "test";
        Object json = "{\"key\": \"value\"}";

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Add your assertions here
        // Example:
        // assertEquals(expectedValue, result.get("key"));
    }

    // Add more test cases for all methods in ParametersUtils class

}