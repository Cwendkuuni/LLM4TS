import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String_Success() {
        StdKeyDeserializer.StringKD stringDeserializer = new StdKeyDeserializer.StringKD(String.class);
        String key = "testKey";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = stringDeserializer.deserializeKey(key, ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_String_NullKey() {
        StdKeyDeserializer.StringKD stringDeserializer = new StdKeyDeserializer.StringKD(Object.class);
        String key = null;
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = stringDeserializer.deserializeKey(key, ctxt);
        
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Boolean_Success() {
        StdKeyDeserializer booleanDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = booleanDeserializer.deserializeKey(key, ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }

    @Test
    public void testDeserializeKey_Boolean_InvalidValue() {
        StdKeyDeserializer booleanDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "invalid";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = booleanDeserializer.deserializeKey(key, ctxt);
        
        assertNull(result);
    }

    // Additional test cases for other methods in StdKeyDeserializer can be added here

}