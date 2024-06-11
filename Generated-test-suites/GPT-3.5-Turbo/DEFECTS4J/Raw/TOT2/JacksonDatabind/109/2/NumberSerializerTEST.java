import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {
    
    @Test
    public void testSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        // Typical use case with BigDecimal
        BigDecimal valueBigDecimal = new BigDecimal("10.5");
        numberSerializer.serialize(valueBigDecimal, generator, provider);
        assertEquals("10.5", generator.getOutput());

        // Edge case with BigInteger
        BigInteger valueBigInteger = new BigInteger("10000000000000000");
        numberSerializer.serialize(valueBigInteger, generator, provider);
        assertEquals("10000000000000000", generator.getOutput());

        // Error scenario with invalid type
        String valueInvalid = "invalid";
        numberSerializer.serialize(valueInvalid, generator, provider);
        assertEquals("invalid", generator.getOutput());
    
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        
        // Typical use case with integer type
        JsonNode schema = numberSerializer.getSchema(provider, Integer.class);
        assertEquals("integer", schema.get("type").asText());

        // Edge case with BigDecimal type
        JsonNode schemaBigDecimal = numberSerializer.getSchema(provider, BigDecimal.class);
        assertEquals("number", schemaBigDecimal.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapperMock visitor = new JsonFormatVisitorWrapperMock();
        JavaType typeHint = new JavaType();

        // Typical use case with BigInteger expected format
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getVisitedType());
        
        // Edge case with BigDecimal expected format
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(JsonParser.NumberType.BIG_DECIMAL, visitor.getVisitedType());
    }
}