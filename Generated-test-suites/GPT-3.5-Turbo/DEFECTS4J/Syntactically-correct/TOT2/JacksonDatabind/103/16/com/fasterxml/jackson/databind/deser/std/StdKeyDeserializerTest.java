import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    // Test deserializeKey() with a valid key
    @Test
    public void testDeserializeKeyValid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);

        DeserializationContext context = new DeserializationContext();

        Object key = deserializer.deserializeKey("test_key", context);

        assertEquals("test_key", key);
    }

    // Test deserializeKey() with null key
    @Test
    public void testDeserializeKeyNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);

        DeserializationContext context = new DeserializationContext();

        Object key = deserializer.deserializeKey(null, context);

        assertNull(key);
    }

    // Test _parseInt() with a valid integer string
    @Test
    public void testParseIntValid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        int value = deserializer._parseInt("123");

        assertEquals(123, value);
    }

    // Test _parseInt() with an invalid integer string
    @Test(expected = IllegalArgumentException.class)
    public void testParseIntInvalid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        deserializer._parseInt("abc");
    }

    // Test _parseLong() with a valid long string
    @Test
    public void testParseLongValid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        long value = deserializer._parseLong("1234567890");

        assertEquals(1234567890, value);
    }

    // Test _parseLong() with an invalid long string
    @Test(expected = IllegalArgumentException.class)
    public void testParseLongInvalid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        deserializer._parseLong("abc");
    }

    // Test _parseDouble() with a valid double string
    @Test
    public void testParseDoubleValid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        double value = deserializer._parseDouble("12.34");

        assertEquals(12.34, value, 0.001);
    }

    // Test _parseDouble() with an invalid double string
    @Test(expected = IllegalArgumentException.class)
    public void testParseDoubleInvalid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        deserializer._parseDouble("abc");
    }

}