import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Typical use case
        String key = "test";
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);

        // Edge cases
        key = null;
        result = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);

        // Error scenario
        key = "invalid";
        try {
            stdKeyDeserializer.deserializeKey(key, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void test_parse() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Typical use case
        String key = "42";
        Object result = stdKeyDeserializer._parse(key, ctxt);
        assertEquals(42, result);

        // Edge cases
        key = "-1";
        result = stdKeyDeserializer._parse(key, ctxt);
        assertEquals(-1, result);

        // Error scenario
        key = "abc";
        try {
            stdKeyDeserializer._parse(key, ctxt);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}