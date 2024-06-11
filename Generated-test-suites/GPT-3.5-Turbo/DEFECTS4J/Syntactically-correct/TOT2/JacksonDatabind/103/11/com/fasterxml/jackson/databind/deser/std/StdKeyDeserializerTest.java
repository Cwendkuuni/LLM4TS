import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_ValidInput() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        DeserializationContext ctxt = new DeserializationContext(null);

        Object result = deserializer.deserializeKey("123", ctxt);
        
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeKey_NullInput() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        
        DeserializationContext ctxt = new DeserializationContext(null);

        Object result = deserializer.deserializeKey(null, ctxt);
        
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidInput() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        
        DeserializationContext ctxt = new DeserializationContext(null);

        Object result = deserializer.deserializeKey("invalid_url", ctxt);
        
        assertNull(result);
    }

    @Test
    public void testParse_InvalidType() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(99, String.class);
        
        DeserializationContext ctxt = new DeserializationContext(null);

        try {
            Object result = deserializer._parse("test", ctxt);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Internal error: unknown key type class java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        
        DeserializationContext ctxt = new DeserializationContext(null);

        try {
            Object result = deserializer._parse("invalid_value", ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("java.lang.Boolean", e.getMessage());
        }
    }
}