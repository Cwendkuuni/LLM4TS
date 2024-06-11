
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "12345";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, taskId);

        // Add assertions here to validate the taskInput
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        String taskId = "12345";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDefinition);

        // Add assertions here to validate the taskInputV2
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        DocumentContext documentContext = JsonPath.parse("{}");

        Map<String, Object> replacedMap = parametersUtils.replace(input, documentContext);

        // Add assertions here to validate the replaced map
    }

    // Add more test cases for other methods in ParametersUtils class

}
