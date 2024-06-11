import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String_null_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_String_validString_ReturnsString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "test";
        assertEquals(key, deserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKey_String_unrecognizedString_ThrowsWeirdKeyException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "unrecognized";
        try {
            deserializer.deserializeKey(key, null);
            fail("Expected a WeirdKeyException to be thrown");
        } catch (WeirdKeyException e) {
            assertEquals(String.format("not a valid representation: %s", key), e.getMessage());
        }
    }

    // Repeat the above three test cases for each supported key type, ensuring they're handled correctly.

    @Test
    public void testDeserializeKey_Byte_validString_ReturnsByte() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        Byte key = 10;
        assertEquals(key, deserializer.deserializeKey(key.toString(), null));
    }

    @Test
    public void testDeserializeKey_Byte_unrecognizedString_ThrowsWeirdKeyException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        String key = "unrecognized";
        try {
            deserializer.deserializeKey(key, null);
            fail("Expected a WeirdKeyException to be thrown");
        } catch (WeirdKeyException e) {
            assertEquals(String.format("not a valid representation: %s", key), e.getMessage());
        }
    }

    // Repeat the above two test cases for each supported key type, ensuring they're handled correctly.

}