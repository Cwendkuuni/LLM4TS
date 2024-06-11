import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Object result = deserializer.deserializeKey("123", context);
            assertEquals(123, result);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKeyNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Object result = deserializer.deserializeKey(null, context);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKeyInvalidBoolean() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Object result = deserializer.deserializeKey("invalid", context);
            fail("No exception thrown");
        } catch (IOException e) {
            assertEquals("not a valid representation, problem: value not 'true' or 'false'", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKeyInvalidByte() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Object result = deserializer.deserializeKey("256", context);
            fail("No exception thrown");
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKeyInvalidShort() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Object result = deserializer.deserializeKey("32768", context);
            fail("No exception thrown");
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }
    
    // Test other methods in the StdKeyDeserializer class
    
    // ...
}