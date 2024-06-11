import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        
        String key = "test";
        DeserializationContext context = new DeserializationContext(null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Object() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        
        String key = "test";
        DeserializationContext context = new DeserializationContext(null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_UUID() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        
        String key = "550e8400-e29b-41d4-a716-446655440000";
        DeserializationContext context = new DeserializationContext(null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        UUID expected = UUID.fromString(key);
        
        assertEquals(expected, result);
    }
    
    // Add more test cases for each method in StdKeyDeserializer class
        
}