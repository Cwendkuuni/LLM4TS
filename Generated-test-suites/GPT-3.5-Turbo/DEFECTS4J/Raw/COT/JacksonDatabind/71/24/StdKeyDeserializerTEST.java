
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Boolean.FALSE, result);

        key = "notABoolean";
        try {
            result = keyDeserializer.deserializeKey(key, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("not a valid representation: ", e.getMessage());
        }
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        Class<?> keyClass = keyDeserializer.getKeyClass();
        assertEquals(Object.class, keyClass);
    }
}
