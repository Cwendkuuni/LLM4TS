import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplace() {
        // Prepare input parameters
        Map<String, Object> input = new HashMap<>();
        input.put("variable1", "${workflow.input.variable1}");
        input.put("variable2", "${workflow.input.variable2}");
        
        // Prepare expected output
        Map<String, Object> expected = new HashMap<>();
        expected.put("variable1", "value1");
        expected.put("variable2", "value2");

        // Prepare workflow object
        // Assuming a mock WorkflowModel class
        WorkflowModel workflow = new WorkflowModel();
        
        // Replace variables in the workflow
        Map<String, Object> actual = parametersUtils.replace(input, workflow);

        // Verify that replaced values are correct
        assertEquals(expected, actual);
    }

    // You can add many more test methods here for other functions, edge cases and error scenarios

    @Test(expected=NullPointerException.class)
    public void testReplaceNullInput() {
        // Assuming that the replace method does not accept null input

        Map<String, Object> actual = parametersUtils.replace(null, "[null]");
        // Expect a NullPointerException
    }
}