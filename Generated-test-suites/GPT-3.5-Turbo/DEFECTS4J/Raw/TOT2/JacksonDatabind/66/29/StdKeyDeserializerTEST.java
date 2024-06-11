import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsParsedValue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testParseKey_OverflowByteValue_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        
        Exception exception = assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("256", null);
        });
        
        assertTrue(exception.getMessage().contains("overflow"));
    }

    @Test
    public void testParseKey_InvalidLocaleKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        
        Exception exception = assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("invalid", null);
        });
        
        assertTrue(exception.getMessage().contains("unable to parse key as locale"));
    }

    @Test
    public void testParseKey_InvalidClassKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        
        Exception exception = assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("InvalidClass", null);
        });
        
        assertTrue(exception.getMessage().contains("unable to parse key as Class"));
    }
}