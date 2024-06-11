import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        // Typical use case: test serialization of BigDecimal
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal input = new BigDecimal("123.45");
        MockJsonGenerator generator = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        
        serializer.serialize(input, generator, provider);
        
        assertEquals("Serialized value should match input", "123.45", generator.getOutput());
    }

    @Test
    public void testGetSchema() {
        // Edge case: test schema generation for BigInteger
        NumberSerializer serializer = new NumberSerializer(Number.class);
        MockSerializerProvider provider = new MockSerializerProvider();
        
        JsonNode schema = serializer.getSchema(provider, BigInteger.class);
        
        assertEquals("Schema type should be integer for BigInteger", "integer", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // Error scenario: test acceptJsonFormatVisitor with unsupported type
        NumberSerializer serializer = NumberSerializer.instance;
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        JavaType typeHint = TypeFactory.defaultInstance().constructType(String.class);
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            fail("Should throw JsonMappingException for unsupported type");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }
}