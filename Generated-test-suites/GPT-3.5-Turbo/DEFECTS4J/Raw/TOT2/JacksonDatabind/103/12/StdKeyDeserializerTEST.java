import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_StringKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("testKey", deserializer.deserializeKey("testKey", null));
    }

    @Test
    public void testParseInt_ValidInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(123, deserializer._parseInt("123"));
    }

    @Test
    public void testParseInt_InvalidInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        try {
            deserializer._parseInt("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseDouble_ValidDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(12.34, deserializer._parseDouble("12.34"), 0.001);
    }

    @Test
    public void testParseDouble_InvalidDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        try {
            deserializer._parseDouble("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeserializeKey_EnumKey() {
        EnumResolver resolver = EnumResolver.constructUnsafeUsingToString(TestEnum.class);
        StdKeyDeserializer deserializer = new EnumKD(resolver, null);
        assertEquals(TestEnum.VALUE1, deserializer.deserializeKey("VALUE1", null));
    }

    // Add more test cases as needed

}