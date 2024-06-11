
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils();
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        
        assertNotNull(taskInput);
        // Add more assertions based on the expected output
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        Object json = "{\"key1\": \"value1\"}";

        Map<String, Object> replacedMap = parametersUtils.replace(input, json);

        assertNotNull(replacedMap);
        // Add more assertions based on the expected output
    }

    // Add more test cases for other public methods in ParametersUtils class

}
