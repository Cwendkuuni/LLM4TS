import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructorWithEnumValues() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        
        assertNotNull(enumSerializer);
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testConstructorWithEnumValuesAndSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        assertNotNull(enumSerializer);
        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertFalse(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testConstructWithValidParameters() {
        EnumSerializer enumSerializer = EnumSerializer.construct(EnumType.class, new SerializationConfig(), new BeanDescription(), new JsonFormat.Value());
        
        assertNotNull(enumSerializer);
        assertNotNull(enumSerializer.getEnumValues());
    }

    @Test
    public void testCreateContextualWithIndexSerialization() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        EnumSerializer contextualSerializer = (EnumSerializer) enumSerializer.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertTrue(contextualSerializer._serializeAsIndex);
    }

    @Test
    public void testCreateContextualWithTextualSerialization() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        EnumSerializer contextualSerializer = (EnumSerializer) enumSerializer.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertFalse(contextualSerializer._serializeAsIndex);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        
        assertDoesNotThrow(() -> enumSerializer.acceptJsonFormatVisitor(visitor, typeHint));
    }

    @Test
    public void testGetSchemaWithIndexSerialization() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        JsonNode schema = enumSerializer.getSchema(new SerializerProvider(), EnumType.class);
        assertNotNull(schema);
        assertEquals("integer", schema.textValue());
    }

    @Test
    public void testGetSchemaWithTextualSerialization() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        JsonNode schema = enumSerializer.getSchema(new SerializerProvider(), EnumType.class);
        assertNotNull(schema);
        assertEquals("string", schema.textValue());
    }

    @Test
    public void testSerializeWithIndexSerialization() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        assertDoesNotThrow(() -> enumSerializer.serialize(EnumType.VALUE, gen, serializers));
    }

    @Test
    public void testSerializeWithToString() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        assertDoesNotThrow(() -> enumSerializer.serialize(EnumType.VALUE, gen, serializers));
    }

    @Test
    public void testSerializeWithSerializedValue() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        assertDoesNotThrow(() -> enumSerializer.serialize(EnumType.VALUE, gen, serializers));
    }

    @Test
    public void testIsShapeWrittenUsingIndexForValidEnumClass() {
        Class<EnumType> enumClass = EnumType.class;
        JsonFormat.Value format = new JsonFormat.Value();
        
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertNotNull(result);
    }

    @Test
    public void testIsShapeWrittenUsingIndexForInvalidEnumClass() {
        Class<InvalidEnumType> invalidEnumClass = InvalidEnumType.class;
        JsonFormat.Value format = new JsonFormat.Value();
        
        assertThrows(IllegalArgumentException.class, () -> EnumSerializer._isShapeWrittenUsingIndex(invalidEnumClass, format, false));
    }

}