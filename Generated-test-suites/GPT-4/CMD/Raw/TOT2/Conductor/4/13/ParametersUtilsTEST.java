import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.netflix.conductor.core.utils.ParamtersUtils;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testClone() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");
        Map<String, Object> copy = parametersUtils.clone(inputTemplate);

        assertEquals(inputTemplate.get("key"), copy.get("key"));
        inputTemplate.put("key", "newValue");
        assertNotEquals(inputTemplate.get("key"), copy.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value ${replaceThis}");
        input = parametersUtils.replace(input, "{\"replaceThis\": \"newValue\"}");

        assertEquals("value newValue", input.get("key"));
    }

    // More test cases should be produced similar to these to test all edge cases and error scenarios.
    // Examples of other things to test could include handing of null values, types that aren't String's being used in the replace and clone methods, invalid json being passed in, etc.
}