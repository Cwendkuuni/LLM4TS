import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);

        Object result = deserializer.deserializeKey(null, Mockito.mock(DeserializationContext.class));

        assertNull(result);
    }

    @Test
    public void testDeserializeKey_BooleanKeyType_ReturnsBooleanValue() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        Object result = deserializer.deserializeKey("true", Mockito.mock(DeserializationContext.class));

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKey_EmptyStringForCharKeyType_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("", ctxt);
        });
    }

    // Add more test methods for other scenarios

}