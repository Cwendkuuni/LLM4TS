import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, null, null, null);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> result =
                    parametersUtils.getTaskInputV2(null, null, null, null);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testReplaceWithMap() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("testKey", "testValue");
        Object result = parametersUtils.replace(inputMap,null);
        assertEquals(inputMap, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWithString() {
        parametersUtils.replace("Invalid input");
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("testKey", "testValue");
        assertEquals(inputMap, parametersUtils.getWorkflowInput(null, inputMap));
    }
}