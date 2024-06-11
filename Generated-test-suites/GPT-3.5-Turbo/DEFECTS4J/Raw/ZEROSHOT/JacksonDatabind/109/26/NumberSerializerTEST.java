import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        try {
            serializer.serialize(10, generator, provider);
            assertEquals("10", generator.toString());
            
            serializer.serialize(10L, generator, provider);
            assertEquals("10", generator.toString());
            
            serializer.serialize(10.5, generator, provider);
            assertEquals("10.5", generator.toString());
            
            serializer.serialize(10.5f, generator, provider);
            assertEquals("10.5", generator.toString());
            
            serializer.serialize(new BigInteger("100"), generator, provider);
            assertEquals("100", generator.toString());
            
            serializer.serialize(new BigDecimal("100.5"), generator, provider);
            assertEquals("100.5", generator.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = BigDecimal.class;
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertEquals("number", schema.getString("type"));
        assertTrue(schema.getBoolean("required"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = BigDecimal.class;
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(visitor.formatVisited(JsonParser.NumberType.BIG_DECIMAL));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }
}