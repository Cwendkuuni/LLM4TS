import static org.junit.Assert.*;
import org.junit.*;



public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    
    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }
    
    @Test
    public void testReplace() {
       Map<String, Object> inputMap = new HashMap<>(); 
       inputMap.put("key", "${value}");
       Map<String, Object> result = parametersUtils.replace(inputMap, "{\"value\": \"test\"}");
       assertEquals("test", result.get("key"));
    }
    
    @Test(expected = RuntimeException.class)
    public void cloneIOException() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put(new Object(), new Object());
        parametersUtils.getTaskInputV2(inputMap, new WorkflowModel(), "123", new TaskDef());
    }

    // Further tests would go here

}

