import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_BooleanType_TrueValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_ByteType_NegativeValue_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", null));
    }

    @Test
    public void testDeserializeKey_CharsType_MultipleCharacterString_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertThrows(IOException.class, () -> deserializer.deserializeKey("ab", null));
    }

    @Test
    public void testParse_NullKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertThrows(IOException.class, () -> deserializer._parse(null, null));
    }

    @Test
    public void testWeirdKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertThrows(IOException.class, () -> deserializer._weirdKey(null, "test", new Exception("Test Exception")));
    }
}