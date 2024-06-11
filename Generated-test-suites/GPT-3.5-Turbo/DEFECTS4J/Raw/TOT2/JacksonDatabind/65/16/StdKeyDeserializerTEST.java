import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null);
        
        // Typical use case
        Object result = keyDeserializer.deserializeKey("123", context);
        assertEquals(123, result);
        
        // Edge case: negative number
        result = keyDeserializer.deserializeKey("-456", context);
        assertEquals(-456, result);
        
        // Error scenario: invalid number format
        try {
            keyDeserializer.deserializeKey("abc", context);
            fail("Expected IOException");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("java.lang.NumberFormatException: For input string: \"abc\"", e.getCause().getMessage());
        }
    }

    @Test
    public void testDeserializeKeyWithNullInput() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext context = new DeserializationContext(null, null);

        // Typical use case
        Object result = keyDeserializer.deserializeKey(null, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyForClass() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Class.class);
        DeserializationContext context = new DeserializationContext(null, null);

        // Edge case: Class not found
        try {
            keyDeserializer.deserializeKey("com.example.NonExistentClass", context);
            fail("Expected IOException");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Can not find class: com.example.NonExistentClass", e.getMessage());
        }
    }
}