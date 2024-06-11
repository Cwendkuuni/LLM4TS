import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_NullKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        assertNull(deserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKey_InvalidKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "invalidKey";

        try {
            deserializer.deserializeKey(key, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid key exception message", e.getMessage(), "not a valid representation: invalidKey");
        }
    }

    @Test
    public void testParse_Int() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "42";

        assertEquals("Integer value should be 42", deserializer._parse(key, ctxt), 42);
    }

    @Test
    public void testParse_Long() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "1234567890";

        assertEquals("Long value should be 1234567890", deserializer._parse(key, ctxt), 1234567890L);
    }

    @Test
    public void testParse_Boolean() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "true";

        assertTrue("Boolean value should be true", (boolean) deserializer._parse(key, ctxt));
    }

    // Add more test cases for other methods and scenarios
    
}