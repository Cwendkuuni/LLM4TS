
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_WhenKeyIsNull_ExpectNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        String key = null;
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeKey_WhenKeyIsTrue_ExpectTrue() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        String key = "true";
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKey_WhenKeyIsFalse_ExpectFalse() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        String key = "false";
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        assertEquals(Boolean.FALSE, result);
    }

    // More test cases for other methods
    // ...

}