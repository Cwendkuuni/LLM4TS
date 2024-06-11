import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_EmptyKey_ReturnsEmptyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("", deserializer.deserializeKey("", null));
    }

    @Test
    public void testDeserializeKey_IntegerKey_ReturnsInteger() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(10, deserializer.deserializeKey("10", null));
    }

    @Test
    public void testDeserializeKey_InvalidBooleanKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testDeserializeKey_InvalidDateFormatKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testDeserializeKey_NullKeyForEnum_ThrowsException() {
        EnumResolver resolver = new EnumResolver(Enum.class);
        StdKeyDeserializer deserializer = new EnumKD(resolver, null);
        assertThrows(IOException.class, () -> deserializer.deserializeKey(null, null));
    }
}