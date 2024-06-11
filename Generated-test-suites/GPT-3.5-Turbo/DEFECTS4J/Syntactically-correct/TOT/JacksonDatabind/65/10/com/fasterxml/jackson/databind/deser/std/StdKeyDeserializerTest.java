
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        String key = "test";

        Object result = deserializer.deserializeKey(key, ctxt);

        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Boolean_True() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        String key = "true";

        Object result = deserializer.deserializeKey(key, ctxt);

        assertTrue((Boolean)result);
    }

    @Test
    public void testDeserializeKey_Boolean_False() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        String key = "false";

        Object result = deserializer.deserializeKey(key, ctxt);

        assertFalse((Boolean)result);
    }

    @Test
    public void testDeserializeKey_Boolean_InvalidValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        String key = "invalid";

        Object result = deserializer.deserializeKey(key, ctxt);

        assertNotNull(result);
        assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(2, Byte.class, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        String key = "100";

        Object result = deserializer.deserializeKey(key, ctxt);

        assertEquals(Byte.valueOf((byte)100), result);
    }

    // TODO: Add tests for other methods

}

