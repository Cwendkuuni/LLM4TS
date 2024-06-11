import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        
        assertNotNull(enumSerializer);
        assertNotNull(enumSerializer.getEnumValues());
    }
    
    @Test
    public void testSerialization() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        EnumType enumValue = EnumType.VALUE1;
        
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();

        try {
            enumSerializer.serialize(enumValue, gen, serializers);
            String expected = "\"Value1\"";
            assertEquals(expected, gen);
        } catch (IOException e) {
            fail("Serialization failed with an exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSchema() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);

        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Type typeHint = EnumType.class;

        JsonNode schema = enumSerializer.getSchema(provider, typeHint);

        assertNotNull(schema);
    }
    
    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<?> enumClass = EnumType.class;
        JsonFormat.Value format = new JsonFormat.Value();
        
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);
        
        assertNotNull(result);
    }
    
    @Test
    public void testContextual() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer baseSerializer = new EnumSerializer(enumValues);

        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        BeanProperty property = null;

        try {
            EnumSerializer result = (EnumSerializer) baseSerializer.createContextual(serializers, property);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail("Contextual creation failed with an exception: " + e.getMessage());
        }
    }

    enum EnumType {
        VALUE1, VALUE2, VALUE3
    }
}