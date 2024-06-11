import static org.junit.Assert.*;

import com.netflix.conductor.core.utils.ParametersUtils;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplaceWithEmptyJson() {
        Map<String, Object> input = Collections.singletonMap("key", "${jsonPath}");
        String json = "{}";

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertNotNull(result);
        assertEquals("${jsonPath}", result.get("key"));
    }

    @Test
    public void testReplaceWithJsonValue() {
        Map<String, Object> input = Collections.singletonMap("key", "${jsonPath}");
        String json = "{ \"jsonPath\" : \"value\" }";

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertNotNull(result);
        assertEquals("value", result.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWithNullInput() {
        Map<String, Object> input = null;
        String json = "{}";

        parametersUtils.replace(input, json);
    }

    @Test
    public void testReplaceWithNestedJson() {
        Map<String, Object> input = Collections.singletonMap("key", "${jsonPath.subPath}");
        String json = "{ \"jsonPath\" : { \"subPath\" : \"value\" } }";

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertNotNull(result);
        assertEquals("value", result.get("key"));
    }

    // TODO: add tests for other methods and edge/error cases
}