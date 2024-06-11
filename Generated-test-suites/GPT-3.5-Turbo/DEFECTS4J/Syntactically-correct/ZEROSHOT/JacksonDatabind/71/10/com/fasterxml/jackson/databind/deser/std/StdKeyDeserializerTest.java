import org.junit.*;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        try {
            Object result = deserializer.deserializeKey("test", ctxt);
            assertEquals("test", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Class<?> keyClass = deserializer.getKeyClass();
        assertEquals(String.class, keyClass);
    }

    @Test
    public void testParse() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        try {
            Object result = deserializer._parse("123", ctxt);
            assertEquals(123, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        try {
            int result = deserializer._parseInt("123");
            assertEquals(123, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        try {
            long result = deserializer._parseLong("123");
            assertEquals(123, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        try {
            double result = deserializer._parseDouble("123.45");
            assertEquals(123.45, result, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}