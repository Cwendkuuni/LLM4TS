import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task_123";

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDef);

        // Test the functionality of getTaskInputV2 method
        // Add more specific test cases if needed
        assertEquals(0, result.size());
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${workflow.id}");

        Object json = "{\"workflow\": {\"id\": 123}}";

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Test the functionality of replace method
        // Add more specific test cases if needed
        assertEquals(1, result.size());
        assertEquals(123, result.get("key"));
    }

    // Add more test cases for other public methods in ParametersUtils class

}