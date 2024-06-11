import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        Object result = keyDeserializer.deserializeKey("test", context);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testDeserializeKey_Integer() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(5, Integer.class);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        Object result = keyDeserializer.deserializeKey("123", context);
        
        assertEquals(123, result);
    }
    
    @Test
    public void testDeserializeKey_Double() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(8, Double.class);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        Object result = keyDeserializer.deserializeKey("3.14", context);
        
        assertEquals(3.14, result);
    }
    
    @Test
    public void testDeserializeKey_InvalidInput() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        Object result = keyDeserializer.deserializeKey(null, context);
        
        assertNull(result);
    }
}