
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", null));
    }

    @Test
    public void testDeserializeKeyWithInvalidValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        try {
            deserializer.deserializeKey("invalid", null);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Class: java.lang.Boolean, Key: invalid, Message: not a valid representation", e.getMessage());
        }
    }

    // Test cases for other methods can be added in a similar manner

}
