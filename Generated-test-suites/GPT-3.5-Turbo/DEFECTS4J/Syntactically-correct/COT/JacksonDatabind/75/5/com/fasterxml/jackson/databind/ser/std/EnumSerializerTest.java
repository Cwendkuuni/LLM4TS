import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testEnumSerializerConstructor() {
        EnumValues values = new EnumValues();
        Boolean serializeAsIndex = Boolean.TRUE;
        EnumSerializer serializer = new EnumSerializer(values, serializeAsIndex);
        assertNotNull(serializer);
    }

    @Test
    public void testConstructMethod() {
        Class<?> enumClass = Enum.class;
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        JsonFormat.Value format = new JsonFormat.Value();
        EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
        assertNotNull(serializer);
    }

    @Test
    public void testCreateContextualMethod() {
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), Boolean.TRUE);
        JsonSerializer<?> jsonSerializer = serializer.createContextual(serializers, property);
        assertEquals(serializer, jsonSerializer);
    }

    @Test
    public void testGetEnumValuesMethod() {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), Boolean.TRUE);
        EnumValues values = serializer.getEnumValues();
        assertEquals(new EnumValues(), values);
    }

    @Test
    public void testSerializeMethod() throws IOException {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), Boolean.TRUE);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        Enum<?> en = TestEnum.VALUE1;
        serializer.serialize(en, gen, serializers);
        assertEquals("0", gen.getOutput());
    }

    @Test
    public void testGetSchemaMethod() {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), Boolean.TRUE);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();
        JsonNode node = serializer.getSchema(provider, typeHint);
        assertEquals(new ObjectNode(), node);
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), Boolean.TRUE);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // No assertion since this tests for successful completion
    }

    @Test
    public void testSerializeAsIndexMethod() {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), null);
        SerializerProvider serializers = new SerializerProvider();
        boolean result = serializer._serializeAsIndex(serializers);
        assertEquals(true, result);
    }

    @Test
    public void testIsShapeWrittenUsingIndexMethod() {
        Class<?> enumClass = Enum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        boolean fromClass = true;
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);
        assertEquals(Boolean.FALSE, result);
    }
}

