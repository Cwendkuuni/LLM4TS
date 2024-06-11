
import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.empty(), TestEnum.class), false);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        SerializerProvider serializerProvider = new DefaultSerializerProvider.Impl();
        
        try {
            enumSerializer.serialize(TestEnum.FIRST, jsonGenerator, serializerProvider);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
        
        Assert.assertEquals("Expected json string does not match", "\"FIRST\"", jsonGenerator.toString());
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.empty(), TestEnum.class), false);
        SerializerProvider serializerProvider = new DefaultSerializerProvider.Impl();
        
        JsonNode schema = enumSerializer.getSchema(serializerProvider, null);
        
        Assert.assertTrue("Expected enum node not found", schema.has("enum"));
        Assert.assertEquals("Expected enum value does not match", "FIRST", schema.get("enum").get(0).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.empty(), TestEnum.class), false);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = null;
        
        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }
}
