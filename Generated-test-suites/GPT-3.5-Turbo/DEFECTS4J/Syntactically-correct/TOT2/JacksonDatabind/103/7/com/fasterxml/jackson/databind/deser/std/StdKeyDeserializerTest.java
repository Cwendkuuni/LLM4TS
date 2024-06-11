import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    // Test deserializeKey method for null key
    @Test
    public void testDeserializeKey_NullKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        Object result = stdKeyDeserializer.deserializeKey(null, ctxt);
        
        assertNull(result);
    }

    // Test deserializeKey method for invalid key with exception
    @Test
    public void testDeserializeKey_InvalidKeyWithException() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey("invalid_date", ctxt));
    }

    // Test getKeyClass method
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        
        Class<?> keyClass = stdKeyDeserializer.getKeyClass();
        
        assertEquals(String.class, keyClass);
    }

    // Test _parseInt helper method
    @Test
    public void testParseInt_ValidInput() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        int result = stdKeyDeserializer._parseInt("123");
        
        assertEquals(123, result);
    }

    // Test _parseInt helper method for invalid input
    @Test
    public void testParseInt_InvalidInput() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parseInt("invalid_input"));
    }

    // Test _weirdKey method
    @Test
    public void testWeirdKey() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        assertThrows(IOException.class, () -> stdKeyDeserializer._weirdKey(ctxt, "1", new Exception("Test")));
    }
}