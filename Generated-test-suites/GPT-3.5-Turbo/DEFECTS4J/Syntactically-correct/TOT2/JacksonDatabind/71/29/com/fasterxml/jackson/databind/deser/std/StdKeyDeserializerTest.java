import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class); // TYPE_BOOLEAN
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_BooleanKey_ReturnsBoolean() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class); // TYPE_BOOLEAN
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_InvalidBooleanKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class); // TYPE_BOOLEAN
        try {
            deserializer.deserializeKey("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_IntKey_ReturnsInteger() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(5, Integer.class); // TYPE_INT
        assertEquals(Integer.valueOf(42), deserializer.deserializeKey("42", null));
    }

    @Test
    public void testDeserializeKey_InvalidIntKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(5, Integer.class); // TYPE_INT
        try {
            deserializer.deserializeKey("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    // Add more test cases for other methods and edge cases

}