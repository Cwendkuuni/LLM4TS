
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = Map.of("param1", "value1", "param2", "value2");
        String taskId = "12345";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        // Perform assertions on taskInput to validate functionality
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> inputParams = Map.of("param1", "value1", "param2", "value2");
        String taskId = "12345";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        // Perform assertions on taskInputV2 to validate functionality
    }

    // Add more test cases for other public methods in ParametersUtils class

}
