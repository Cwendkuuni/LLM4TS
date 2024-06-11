import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        // Arrange
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "testTaskId";
        Map<String, Object> inputParams = new HashMap<>();

        // Act
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert
        // Add assert statements as per the expected behavior
    }

    @Test
    public void testGetTaskInputV2() {
        // Arrange
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "testTaskId";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();

        // Act
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Assert
        // Add assert statements as per the expected behavior
    }

    @Test
    public void testReplace() {
        // Arrange
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String jsonString = "{\"key\":\"value\"}";

        // Act
        Object result = parametersUtils.replace(jsonString);

        // Assert
        // Add assert statements as per the expected behavior
    }

    @Test
    public void testReplaceVariables() {
        // Arrange
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String paramString = "${key}";
        DocumentContext documentContext = JsonPath.parse("{\"key\":\"value\"}");
        String taskId = "testTaskId";

        // Act
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Assert
        // Add assert statements as per the expected behavior
    }

    // Additional test methods for other public methods in ParametersUtils class

}