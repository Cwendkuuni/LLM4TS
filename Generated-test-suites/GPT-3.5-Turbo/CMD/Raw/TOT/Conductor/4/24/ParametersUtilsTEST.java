import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper()); // Assuming ObjectMapper is properly initialized here
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "testTaskId";
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "${workflow.input}");
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDef);
        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("key1"));
        // Add more test cases for expected behavior
    }

    @Test
    public void testReplace() {
        String jsonString = "{\"key\": \"${workflow.input}\", \"nested\": {\"nestedKey\": \"${workflow.output}\"}}";
        Map<String, Object> input = Collections.singletonMap("workflow.input", "inputValue");
        Object replaced = parametersUtils.replace(jsonString);
        assertNotNull(replaced);
        // Add assertions for expected behavior
    }

    // Add more test methods for complete test coverage

}