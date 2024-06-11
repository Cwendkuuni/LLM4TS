import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        
        // Test positive integer key
        String key = "10";
        Object result = deserializer.deserializeKey(key, ctxt);
        assertEquals(Integer.valueOf(10), result);
        
        // Test negative integer key
        key = "-5";
        result = deserializer.deserializeKey(key, ctxt);
        assertEquals(Integer.valueOf(-5), result);
        
        // Test non-integer key
        key = "abc";
        result = deserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        // Test positive integer value
        String key = "10";
        int result = deserializer._parseInt(key);
        assertEquals(10, result);
        
        // Test negative integer value
        key = "-5";
        result = deserializer._parseInt(key);
        assertEquals(-5, result);
        
        // Test non-integer value
        key = "abc";
        try {
            result = deserializer._parseInt(key);
            fail("NumberFormatException should be thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    // Add more test cases here...

}