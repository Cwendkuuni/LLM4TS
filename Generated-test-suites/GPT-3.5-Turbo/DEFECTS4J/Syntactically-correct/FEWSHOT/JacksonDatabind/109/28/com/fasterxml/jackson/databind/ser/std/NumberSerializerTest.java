import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        
        // Test BigDecimal
        BigDecimal bd = new BigDecimal("123.45");
        JsonGenerator g = null; // instantiate a JsonGenerator object
        SerializerProvider provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(bd, g, provider);
        // Add assertions here
        
        // Test BigInteger
        BigInteger bi = new BigInteger("12345");
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(bi, g, provider);
        // Add assertions here
        
        // Test Long
        Long l = 123L;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(l, g, provider);
        // Add assertions here
        
        // Test Double
        Double d = 123.45;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(d, g, provider);
        // Add assertions here
        
        // Test Float
        Float f = 123.45f;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(f, g, provider);
        // Add assertions here
        
        // Test Integer
        Integer i = 123;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(i, g, provider);
        // Add assertions here
        
        // Test Byte
        Byte b = 123;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(b, g, provider);
        // Add assertions here
        
        // Test Short
        Short s = 123;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(s, g, provider);
        // Add assertions here
        
        // Test other Numbers
        Number n = 123.45;
        g = null; // instantiate a JsonGenerator object
        provider = null; // instantiate a SerializerProvider object
        // Call the serialize() method
        serializer.serialize(n, g, provider);
        // Add assertions here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        
        // Test for _isInt as false
        SerializerProvider provider = null; // instantiate a SerializerProvider object
        Type typeHint = null; // instantiate a Type object
        // Call the getSchema() method
        JsonNode schema = serializer.getSchema(provider, typeHint);
        // Add assertions here
        
        // Test for _isInt as true
        serializer = new NumberSerializer(BigInteger.class);
        // Call the getSchema() method
        schema = serializer.getSchema(provider, typeHint);
        // Add assertions here
    }
}