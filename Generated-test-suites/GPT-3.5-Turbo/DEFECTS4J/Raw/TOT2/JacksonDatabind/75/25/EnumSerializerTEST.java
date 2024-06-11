import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testConstruct() {
        EnumSerializer enumSerializer = EnumSerializer.construct(SomeEnum.class, new SerializationConfig(), new BeanDescription(), new JsonFormat.Value());
        assertNotNull(enumSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        EnumValues enumValues = new EnumValues(SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(new SerializerProvider(), new BeanProperty());
        assertNotNull(contextualSerializer);
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = new EnumValues(SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testSerialize() throws IOException {
        EnumValues enumValues = new EnumValues(SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        enumSerializer.serialize(SomeEnum.VALUE, gen, serializers);
        assertEquals("\"VALUE\"", writer.toString());
    }

    @Test
    public void testGetSchema() {
        EnumValues enumValues = new EnumValues(SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonNode schema = enumSerializer.getSchema(new SerializerProvider(), SomeEnum.class);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumValues enumValues = new EnumValues(SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = new TypeReference<SomeEnum>() {}.getType();
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertTrue(true); // No exception is thrown
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<?> enumClass = SomeEnum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        Boolean shapeWrittenUsingIndex = EnumSerializer.isShapeWrittenUsingIndex(enumClass, format, true);
        assertFalse(shapeWrittenUsingIndex);
    }
}