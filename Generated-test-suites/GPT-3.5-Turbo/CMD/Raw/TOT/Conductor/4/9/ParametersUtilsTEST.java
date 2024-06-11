import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput_ValidInputParams_ReturnTaskInputMap() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        // Add assertions here for validating the taskInput
    }

    @Test
    public void testGetTaskInputV2_ValidInput_ReturnTaskInputMap() {
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task1";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        // Add assertions here for validating the taskInput
    }

    @Test
    public void testReplace_ValidInput_ReturnReplacedValue() {
        String paramString = "${test}";
        DocumentContext documentContext = JsonPath.parse("{}");
        Object replacedValue = parametersUtils.replace(paramString);
        assertEquals("Expected replaced value", "test", replacedValue);
    }

    // Add more tests for other methods in ParametersUtils class

}