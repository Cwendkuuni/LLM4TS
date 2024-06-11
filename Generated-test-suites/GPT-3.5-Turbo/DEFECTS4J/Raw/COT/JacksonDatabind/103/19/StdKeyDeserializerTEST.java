import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializingKeyNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertNull(deserializer.deserializeKey(null, null));
    }
    
    @Test
    public void testDeserializingKeyTrue() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }
    
    @Test
    public void testDeserializingKeyFalse() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", null));
    }
    
    @Test
    public void testDeserializingKeyOutOfRangeByte() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertNull(deserializer.deserializeKey("256", null));
    }
    
    @Test
    public void testDeserializingKeyByte() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf((byte) 100), deserializer.deserializeKey("100", null));
    }
    
    @Test
    public void testDeserializingKeyOutOfRangeShort() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertNull(deserializer.deserializeKey("32768", null));
    }
    
    @Test
    public void testDeserializingKeyShort() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertEquals(Short.valueOf((short) 1000), deserializer.deserializeKey("1000", null));
    }
    
    @Test
    public void testDeserializingKeyChar() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertEquals(Character.valueOf('a'), deserializer.deserializeKey("a", null));
    }
    
    // Add more test cases for other methods in StdKeyDeserializer class
    
}