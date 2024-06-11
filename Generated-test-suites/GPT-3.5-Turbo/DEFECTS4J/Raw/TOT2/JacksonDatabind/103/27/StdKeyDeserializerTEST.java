import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);
        assertNull(deserializer.deserializeKey(null, ctxt));
    }
    
    @Test
    public void testDeserializeKey_InvalidKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);
        try {
            deserializer.deserializeKey("invalid", ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test
    public void testKeyClassGetter() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals(String.class, deserializer.getKeyClass());
    }
}