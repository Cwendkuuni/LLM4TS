import static org.junit.Assert.*;
import org.junit.*;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTaskInputNullParams() {
        parametersUtils.getTaskInput(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTaskInputV2NullParams() {
        parametersUtils.getTaskInputV2(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceNullParams() {
        parametersUtils.replace(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceStringNullParams() {
        parametersUtils.replace(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWorkflowInputNullParams() {
        parametersUtils.getWorkflowInput(null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneIOException() {
        // Mocking objectMapper for IOException testing
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValueAsBytes(anyMap())).thenThrow(IOException.class);
        parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> mockMap = new HashMap<>();
        mockMap.put("key", "value");

        parametersUtils.clone(mockMap);
    }

    // Tests for successful method calls will be similar and require proper instance of WorkflowModel, TaskDef, etc.
    // Testing them requires deeper knowledge of these classes and their behavior, 
    // hence they are omitted in this context. 
}