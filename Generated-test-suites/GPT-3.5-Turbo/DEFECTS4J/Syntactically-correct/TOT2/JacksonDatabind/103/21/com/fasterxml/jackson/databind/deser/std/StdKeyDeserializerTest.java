import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_ValidKey_ReturnsObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = deserializer.deserializeKey("test", ctxt);
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = deserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        try {
            deserializer.deserializeKey("invalid key", ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParse_EmptyStringForByte_ReturnsZero() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = deserializer._parse("", ctxt);
        assertEquals((byte) 0, result);
    }

    @Test
    public void testParse_OverflowForByte_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        try {
            deserializer._parse("256", ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }
}