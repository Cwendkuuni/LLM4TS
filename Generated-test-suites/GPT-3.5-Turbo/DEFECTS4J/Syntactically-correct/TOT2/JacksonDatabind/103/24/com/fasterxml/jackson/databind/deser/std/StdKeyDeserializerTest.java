import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_EmptyKey_ReturnsEmptyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("", deserializer.deserializeKey("", null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test
    public void testDeserializeKey_IntKey_ReturnsIntValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(10, deserializer.deserializeKey("10", null));
    }

    @Test
    public void testDeserializeKey_BooleanKey_ReturnsBooleanValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(true, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnsError() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null);
        try {
            deserializer.deserializeKey("invalid", context);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Invalid key", e.getMessage());
        }
    }
}