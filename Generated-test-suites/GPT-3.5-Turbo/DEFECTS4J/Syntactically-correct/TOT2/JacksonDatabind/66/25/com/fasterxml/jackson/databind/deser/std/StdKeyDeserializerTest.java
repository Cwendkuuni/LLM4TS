import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyWithValidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "test";
        assertEquals(key, keyDeserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKeyWithNullString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKeyWithEmptyString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "";
        assertEquals("", keyDeserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKeyWithInvalidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = null;
        assertNull(keyDeserializer.deserializeKey(key, null));
    }

    @Test
    public void testParseIntegerWithValidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        assertEquals(123, keyDeserializer._parseInt(key));
    }

    @Test
    public void testParseIntegerWithInvalidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "abc";
        try {
            keyDeserializer._parseInt(key);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongWithValidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "1234567890";
        assertEquals(1234567890L, keyDeserializer._parseLong(key));
    }

    @Test
    public void testParseLongWithInvalidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "abc";
        try {
            keyDeserializer._parseLong(key);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseDoubleWithValidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key = "12.34";
        assertEquals(12.34, keyDeserializer._parseDouble(key), 0.001);
    }

    @Test
    public void testParseDoubleWithInvalidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key = "abc";
        try {
            keyDeserializer._parseDouble(key);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }
}