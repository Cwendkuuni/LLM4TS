import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_BooleanKey_True() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_IntegerKey_ReturnsParsedInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(10), deserializer.deserializeKey("10", null));
    }

    @Test
    public void testDeserializeKey_DoubleKey_ReturnsParsedDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(Double.valueOf(10.5), deserializer.deserializeKey("10.5", null));
    }

    @Test
    public void testDeserializeKey_StringKey_ReturnsString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidKey_ThrowsIOException() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        deserializer.deserializeKey("invalid", null);
    }
}