import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnNull() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNull(keyDeserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKey_EmptyStringKey_ReturnEmptyString() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertEquals("", keyDeserializer.deserializeKey("", ctxt));
    }

    @Test
    public void testDeserializeKey_IntegerKey_ReturnInteger() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertEquals(123, keyDeserializer.deserializeKey("123", ctxt));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertThrows(IOException.class, () -> {
            keyDeserializer.deserializeKey("invalid", ctxt);
        });
    }

}