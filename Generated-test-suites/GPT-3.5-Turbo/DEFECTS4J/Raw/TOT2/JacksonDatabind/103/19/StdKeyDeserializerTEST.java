import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ShouldReturnNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ShouldReturnStringValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test
    public void testDeserializeKey_EmptyStringKey_ShouldReturnEmptyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("", deserializer.deserializeKey("", null));
    }

    @Test
    public void testDeserializeKey_IntKey_ShouldReturnIntValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(123, deserializer.deserializeKey("123", null));
    }

    @Test
    public void testDeserializeKey_InvalidIntKey_ShouldThrowException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertThrows(NumberFormatException.class, () -> deserializer.deserializeKey("abc", null));
    }

    @Test
    public void testDeserializeKey_BooleanKey_ShouldReturnBooleanValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_InvalidBooleanKey_ShouldThrowException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("abc", null));
    }

}