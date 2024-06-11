import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer._deserializeCustom(parser, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(parser, ctxt);
        assertNotNull(result);
        assertEquals(1, result.length);
    }

    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertNull(result);
    }
}