import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object result = deserializer.deserializeKey("test", null);
        assertEquals("test", result);
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        int result = deserializer._parseInt("123");
        assertEquals(123, result);
    }
    
    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        long result = deserializer._parseLong("123456789");
        assertEquals(123456789, result);
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        double result = deserializer._parseDouble("3.14");
        assertEquals(3.14, result, 0.0001);
    }
  
    @Test
    public void testWeirdKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object result = deserializer._weirdKey(null, "error", new Exception("Test exception"));
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyWithNullKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object result = deserializer.deserializeKey(null, null);
        assertNull(result);
    }

    // Add more test cases here for edge cases and error scenarios

}