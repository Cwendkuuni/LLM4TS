import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsParsedValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(10), deserializer.deserializeKey("10", null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnsWeirdKeyException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        try {
            deserializer.deserializeKey("invalid", null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Could not parse key as boolean", e.getMessage());
        }
    }

    @Test
    public void testDeserializeKey_HandleUnknownEnumValuesAsNull_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        assertNull(deserializer.deserializeKey("unknown", null));
    }

    @Test
    public void testDeserializeKey_InvalidURIKey_ReturnsWeirdKeyException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        try {
            deserializer.deserializeKey("invalid-uri", null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Problem creating URI from key: invalid-uri", e.getMessage());
        }
    }

    @Test
    public void testDeserializeKey_InvalidURLKey_ReturnsWeirdKeyException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        try {
            deserializer.deserializeKey("invalid-url", null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Malformed URL: invalid-url", e.getMessage());
        }
    }
}