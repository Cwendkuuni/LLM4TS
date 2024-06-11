import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsParsedObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testParse_InvalidKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertThrows(IOException.class, () -> deserializer._parse("256", null));
    }

    @Test
    public void testParseDate_ValidDate_ReturnsDateObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertEquals(new Date(0), deserializer._parse("1970-01-01T00:00:00Z", null));
    }

    @Test
    public void testParseLong_InvalidLong_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertThrows(NumberFormatException.class, () -> deserializer._parse("notValidLong", null));
    }
    
    // Add more test cases for other methods
    
}