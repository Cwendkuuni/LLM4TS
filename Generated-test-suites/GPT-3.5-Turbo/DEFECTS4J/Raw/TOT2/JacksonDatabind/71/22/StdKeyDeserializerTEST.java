import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsParsedValue() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(42), keyDeserializer.deserializeKey("42", null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsIOException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertThrows(IOException.class, () -> keyDeserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testDeserializeKey_NullKeyEnableUnknownEnumValue_ReturnsNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.getConfig().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        assertNull(keyDeserializer.deserializeKey(null, ctxt));
    }

    // Add more test cases for other methods as needed

}