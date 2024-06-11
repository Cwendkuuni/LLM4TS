import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        assertNotNull(enumSerializer);
        assertEquals(EnumClass.class, enumSerializer.handledType());
        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertTrue(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testConstructorWithNullSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);

        assertNotNull(enumSerializer);
        assertEquals(EnumClass.class, enumSerializer.handledType());
        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertNull(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testConstruct() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        JsonFormat.Value format = new JsonFormat.Value();
        EnumSerializer enumSerializer = EnumSerializer.construct(EnumClass.class, config, beanDesc, format);

        assertNotNull(enumSerializer);
        assertEquals(EnumClass.class, enumSerializer.handledType());
        assertNotNull(enumSerializer.getEnumValues());
    }

    @Test
    public void testCreateContextual() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        try {
            EnumSerializer contextualSerializer = (EnumSerializer) enumSerializer.createContextual(serializers, property);
            assertEquals(enumSerializer, contextualSerializer);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }

    @Test
    public void testSerialize() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        EnumClass enumInstance = EnumClass.ENUM1;

        assertEquals("Value1", enumSerializer._values.serializedValueFor(enumInstance));
    }

    @Test
    public void testGetSchema() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = null;

        assertNotNull(enumSerializer.getSchema(provider, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }

    @Test
    public void testSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
        SerializerProvider serializers = new SerializerProvider();

        assertEquals(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX), enumSerializer._serializeAsIndex(serializers));
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<EnumClass> enumClass = EnumClass.class;
        JsonFormat.Value format = new JsonFormat.Value();
        boolean fromClass = true;

        assertNull(EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass));
    }
}