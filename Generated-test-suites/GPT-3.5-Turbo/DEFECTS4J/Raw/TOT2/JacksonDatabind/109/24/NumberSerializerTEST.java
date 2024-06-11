import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {
    
    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator g = new JsonGenerator(); // mock object
        SerializerProvider provider = new SerializerProvider(); // mock object
        
        BigDecimal value = new BigDecimal("123.456");
        
        try {
            serializer.serialize(value, g, provider);
            
            // Assert that the correct value is written to JsonGenerator
            assertEquals("123.456", g.getOutput());
            
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator g = new JsonGenerator(); // mock object
        SerializerProvider provider = new SerializerProvider(); // mock object
        
        BigInteger value = new BigInteger("123456");
        
        try {
            serializer.serialize(value, g, provider);
            
            // Assert that the correct value is written to JsonGenerator
            assertEquals("123456", g.getOutput());
            
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = new SerializerProvider(); // mock object
        Type typeHint = BigDecimal.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert that the correct schema node is created
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }
    
    // Add more test cases for other methods
    
}