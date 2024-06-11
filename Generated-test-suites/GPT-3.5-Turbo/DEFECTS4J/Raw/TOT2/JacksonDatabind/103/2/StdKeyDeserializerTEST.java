import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_WithValidString_ShouldReturnString() {
        // Arrange
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext context = new DeserializationContext(null);
        String key = "test";

        // Act
        Object result = deserializer.deserializeKey(key, context);

        // Assert
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_WithNullKey_ShouldReturnNull() {
        // Arrange
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext context = new DeserializationContext(null);
        String key = null;

        // Act
        Object result = deserializer.deserializeKey(key, context);

        // Assert
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_WithInvalidKey_ShouldThrowIOException() throws IOException {
        // Arrange
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext context = new DeserializationContext(null);
        String key = "\uFFFF"; // Invalid unicode character

        // Act
        deserializer.deserializeKey(key, context);
    }
}