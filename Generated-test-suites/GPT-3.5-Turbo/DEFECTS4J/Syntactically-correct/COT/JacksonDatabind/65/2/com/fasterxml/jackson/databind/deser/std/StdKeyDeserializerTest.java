import org.junit.Test;
import org.junit.Assert;

public class StdKeyDeserializerTest {
    
    private StdKeyDeserializer deserializer;
    
    @Test
    public void testDeserializeKeyWithNullKey() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        
        Object result = deserializer.deserializeKey(null, ctxt);
        
        Assert.assertNull(result);
    }
    
    // Test other scenarios for deserializeKey method
    
    @Test
    public void testParseIntWithValidKey() throws Exception {
        deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
        
        int result = deserializer._parseInt("123");
        
        Assert.assertEquals(123, result);
    }
    
    // Test other scenarios for _parseInt method
    
    @Test
    public void testParseLongWithValidKey() throws Exception {
        deserializer = new StdKeyDeserializer(TYPE_LONG, Long.class);
        
        long result = deserializer._parseLong("1234567890123456789");
        
        Assert.assertEquals(1234567890123456789L, result);
    }
    
    // Test other scenarios for _parseLong method
    
    @Test
    public void testParseDoubleWithValidKey() throws Exception {
        deserializer = new StdKeyDeserializer(TYPE_DOUBLE, Double.class);
        
        double result = deserializer._parseDouble("3.14");
        
        Assert.assertEquals(3.14, result, 0.0001);
    }
    
    // Test other scenarios for _parseDouble method
    
}