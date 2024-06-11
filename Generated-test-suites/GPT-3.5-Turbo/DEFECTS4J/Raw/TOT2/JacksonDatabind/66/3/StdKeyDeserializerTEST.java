import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;

public class StdKeyDeserializerTest {

    StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "testKey";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testParseException() {
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalidKey";
        try {
            keyDeserializer.deserializeKey(key, ctxt);
            fail("Expected exception to be thrown");
        } catch (JsonProcessingException e) {
            // Exception expected
        }
    }

    @Test
    public void testNullKey() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = keyDeserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }
}