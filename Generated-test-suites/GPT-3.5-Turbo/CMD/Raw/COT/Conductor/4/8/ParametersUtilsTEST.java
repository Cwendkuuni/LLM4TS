import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        
        // Add assertions here to test the functionality of getTaskInput method
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> input = new HashMap<>();
        String taskId = "task1";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        
        // Add assertions here to test the functionality of getTaskInputV2 method
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = "{}";
        Map<String, Object> replaced = parametersUtils.replace(input, json);
        
        // Add assertions here to test the functionality of replace method
    }

    // Add more test cases for other methods as needed

}