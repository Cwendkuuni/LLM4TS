import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_ValidString_ReturnsString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer.deserializeKey("test", ctxt);
        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidKey_ThrowsIOException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        deserializer.deserializeKey(null, ctxt);
    }

    @Test
    public void testDeserializeKey_EmptyString_ReturnsEmptyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer.deserializeKey("", ctxt);
        assertEquals("", result);
    }
}