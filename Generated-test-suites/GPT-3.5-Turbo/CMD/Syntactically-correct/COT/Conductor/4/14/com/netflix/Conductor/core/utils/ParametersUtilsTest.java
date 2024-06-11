import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;

public class ParametersUtilsTest {

    ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

    @Test
    public void testGetTaskInput() {
        WorkflowDef workflow = new WorkflowDef();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "123";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(taskInput);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowDef workflow = new WorkflowDef();
        Map<String, Object> input = new HashMap<>();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        assertNotNull(taskInputV2);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        String jsonString = "{\"key1\":\"${workflow.input.key1}\",\"key2\":\"${workflow.output.key2}\"}";
        Object replaced = parametersUtils.replace(jsonString);
        assertNotNull(replaced);
    }

    // Additional tests for edge cases and exception handling scenarios can be added here

}