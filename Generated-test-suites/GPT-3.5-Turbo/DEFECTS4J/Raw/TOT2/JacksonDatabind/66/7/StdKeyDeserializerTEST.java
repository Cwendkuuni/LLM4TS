import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_EmptyStringKey_ReturnsEmptyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("", deserializer.deserializeKey("", null));
    }

    @Test
    public void testDeserializeKey_ValidKeyString_ReturnsKeyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test
    public void testDeserializeKey_ExceptionThrown_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testParse_NullKey_ThrowsIOException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertThrows(IOException.class, () -> {
            deserializer._parse(null, null);
        });
    }

    @Test
    public void testParse_InvalidKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            deserializer._parse("invalid", null);
        });
    }

    @Test
    public void testParse_IntKey_ReturnsIntegerValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(123, deserializer._parse("123", null));
    }

    @Test
    public void testParse_DoubleKey_ReturnsDoubleValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(3.14, deserializer._parse("3.14", null));
    }

    @Test
    public void testParse_BooleanKey_ReturnsBooleanValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(true, deserializer._parse("true", null));
    }

}