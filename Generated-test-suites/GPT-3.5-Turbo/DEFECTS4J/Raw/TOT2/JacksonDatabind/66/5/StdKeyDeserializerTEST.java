import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        
        try {
            Object result = deserializer.deserializeKey("testKey", DeserializationContext.fakeContext());
            assertEquals("testKey", result);
        } catch (IOException e) {
            fail("Exception thrown during deserialization");
        }
    }

    @Test
    public void testInvalidKeyHandling() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        
        try {
            Object result = deserializer.deserializeKey(null, DeserializationContext.fakeContext());
            assertNull(result);
        } catch (IOException e) {
            fail("Exception thrown during deserialization");
        }
    }

    @Test
    public void testParseBooleanKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        try {
            Object result = deserializer._parse("true", DeserializationContext.fakeContext());
            assertEquals(Boolean.TRUE, result);
        } catch (Exception e) {
            fail("Exception thrown during boolean key parsing");
        }
    }

    @Test
    public void testInvalidBooleanKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        try {
            Object result = deserializer._parse("invalid", DeserializationContext.fakeContext());
            fail("Invalid boolean key accepted");
        } catch (Exception e) {
            // expected behavior
        }
    }
    
    // Add more test cases for the other methods of StdKeyDeserializer

}