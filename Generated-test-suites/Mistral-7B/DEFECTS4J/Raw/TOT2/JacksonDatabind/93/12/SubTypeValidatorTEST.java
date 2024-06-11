import static org.junit.Assert.assertEquals;
import static com.fasterxml.jackson.databind.JavaType.construct;
import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static com.fasterxml.jackson.databind.json.JsonMapper.getDefaultMapper;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testGetDefaultNoDeserClassNames() {
        Set<String> defaultNoDeserClassNames = SubTypeValidator.getDefaultNoDeserClassNames();
        assertEquals(DEFAULT_NO_DESER_CLASS_NAMES, defaultNoDeserClassNames);
    }
}