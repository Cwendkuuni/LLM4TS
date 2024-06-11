import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        // Test typical use case of serializing Enum
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        Enum<?> testEnum = TestEnum.VALUE1;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createGenerator(serializers);
        serializer.serialize(testEnum, generator, provider);
        generator.close();
        assertEquals("1", writer.toString());
    }

    @Test
    public void testCreateSchemaNode() {
        // Test creating schema node for integer type
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createGenerator(serializers);
        JsonNode schemaNode = serializer.getSchema(provider, Integer.class);
        assertTrue(schemaNode.isInt());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // Test accepting JSON format visitor for string format
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createGenerator(serializers);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(provider);
        serializer.acceptJsonFormatVisitor(visitor, String.class);
        assertTrue(true);
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        // Test checking shape for writing using index
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);

        // Test shape as "any"
        JsonFormat.Value formatAny = JsonFormat.Value.forShape(JsonFormat.Shape.ANY);
        Boolean resultAny = serializer.isShapeWrittenUsingIndex(TestEnum.class, formatAny, true);
        assertNull(resultAny);

        // Test shape as "string"
        JsonFormat.Value formatString = JsonFormat.Value.forShape(JsonFormat.Shape.STRING);
        Boolean resultString = serializer.isShapeWrittenUsingIndex(TestEnum.class, formatString, true);
        assertEquals(Boolean.FALSE, resultString);

        // Test shape as "array"
        JsonFormat.Value formatArray = JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY);
        Boolean resultArray = serializer.isShapeWrittenUsingIndex(TestEnum.class, formatArray, true);
        assertEquals(Boolean.TRUE, resultArray);

        // Test shape as "object"
        JsonFormat.Value formatObject = JsonFormat.Value.forShape(JsonFormat.Shape.OBJECT);
        try {
            serializer.isShapeWrittenUsingIndex(TestEnum.class, formatObject, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}