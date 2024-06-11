import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        // Test case for deserializeKey method with a valid key
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "123";
        assertEquals(123, keyDeserializer.deserializeKey(key, ctxt));

        // Test case for deserializeKey method with a null key
        assertNull(keyDeserializer.deserializeKey(null, ctxt));

        // Test case for deserializeKey method with an invalid key
        key = "abc";
        try {
            keyDeserializer.deserializeKey(key, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testForKeyClass() {
        // Test case for getKeyClass method
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testParse() {
        // Test case for _parse method with TYPE_INT
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "123";
        assertEquals(123, keyDeserializer._parse(key, ctxt));

        // Test case for _parse method with TYPE_URI
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        key = "http://www.example.com";
        assertEquals(URI.create("http://www.example.com"), keyDeserializer._parse(key, ctxt));

        // Test case for _parse method with invalid key
        key = "invalid";
        try {
            keyDeserializer._parse(key, ctxt);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testParseInt() {
        // Test case for _parseInt method with a valid integer key
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        assertEquals(123, keyDeserializer._parseInt(key));

        // Test case for _parseInt method with an invalid integer key
        key = "abc";
        try {
            keyDeserializer._parseInt(key);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testParseLong() {
        // Test case for _parseLong method with a valid long key
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "1234567890";
        assertEquals(1234567890L, keyDeserializer._parseLong(key));

        // Test case for _parseLong method with an invalid long key
        key = "abc";
        try {
            keyDeserializer._parseLong(key);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testParseDouble() {
        // Test case for _parseDouble method with a valid double key
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key = "3.14";
        assertEquals(3.14, keyDeserializer._parseDouble(key), 0.001);

        // Test case for _parseDouble method with an invalid double key
        key = "abc";
        try {
            keyDeserializer._parseDouble(key);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }
}