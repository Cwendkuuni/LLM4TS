import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParametersUtilsTest {
    
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "1234";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert statements for typical use cases
        // ...

        // Assert statements for edge cases
        // ...

        // Assert statements for error scenarios
        // ...
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Assert statements for typical use cases
        // ...

        // Assert statements for edge cases
        // ...

        // Assert statements for error scenarios
        // ...
    }

    // More test cases for other methods in ParametersUtils class

}