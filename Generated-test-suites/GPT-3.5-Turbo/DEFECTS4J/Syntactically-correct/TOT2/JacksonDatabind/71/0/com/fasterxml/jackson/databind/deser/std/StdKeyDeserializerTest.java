import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testStdKeyDeserializerForType() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        assertNotNull(keyDeserializer);
        assertEquals(StdKeyDeserializer.TYPE_STRING, keyDeserializer._kind);
    }

    @Test
    public void testDeserializeKeyWithString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "Test";
        assertEquals(key, keyDeserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKeyWithNullString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKeyWithInvalidString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        try {
            keyDeserializer.deserializeKey("", null);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // success
        }
    }

    @Test
    public void testParseBooleanKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(true, keyDeserializer._parse("true", null));
        assertEquals(false, keyDeserializer._parse("false", null));
    }

    @Test
    public void testParseByteKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf((byte) 127), keyDeserializer._parse("127", null));
    }

    @Test
    public void testParseShortKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertEquals(Short.valueOf((short) 32767), keyDeserializer._parse("32767", null));
    }

    @Test
    public void testParseCharKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertEquals(Character.valueOf('A'), keyDeserializer._parse("A", null));
    }

    @Test
    public void testparseIntKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(42), keyDeserializer._parse("42", null));
    }

    @Test
    public void testParseLongKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(Long.valueOf(1234567890), keyDeserializer._parse("1234567890", null));
    }

    @Test
    public void testParseDoubleKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(Double.valueOf(3.14), keyDeserializer._parse("3.14", null));
    }

    // Add more comprehensive test cases for other methods and edge cases

}