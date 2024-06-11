import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_String() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        String key = "key";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_BooleanTrue() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "true";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testDeserializeKey_BooleanFalse() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "false";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testDeserializeKey_InvalidBoolean() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "invalid";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_Integer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        String key = "12345";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(12345, result);
    }
    
    @Test
    public void testDeserializeKey_InvalidInteger() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        String key = "invalid";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_Long() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        String key = "123456789";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(123456789L, result);
    }
    
    @Test
    public void testDeserializeKey_InvalidLong() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        String key = "invalid";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_Character() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        String key = "a";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals('a', result);
    }
    
    @Test
    public void testDeserializeKey_InvalidCharacter() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        String key = "ab";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertNull(result);
    }
    
    // TODO: Add test cases for other methods
    
}