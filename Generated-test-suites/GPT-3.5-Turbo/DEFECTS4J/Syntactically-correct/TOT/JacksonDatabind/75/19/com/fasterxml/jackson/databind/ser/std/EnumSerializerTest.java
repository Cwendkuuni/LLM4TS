
import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        TestEnum enumValue = TestEnum.FIRST;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        
        try {
            serializer.serialize(enumValue, generator, serializers);
            verify(generator).writeNumber(enumValue.ordinal());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeAsString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        TestEnum enumValue = TestEnum.FIRST;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        
        try {
            serializer.serialize(enumValue, generator, serializers);
            verify(generator).writeString(enumValue.toString());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeUsingToString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        TestEnum enumValue = TestEnum.FIRST;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        
        try {
            serializer.serialize(enumValue, generator, serializers);
            verify(generator).writeString(enumValue.toString());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetSchemaWithIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonNode expectedSchema = createSchemaNode("integer", true);
        
        try {
            JsonNode schema = serializer.getSchema(serializers, TestEnum.class);
            assertEquals(expectedSchema, schema);
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetSchemaWithString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonNode expectedSchema = createSchemaNode("string", true);
        
        try {
            JsonNode schema = serializer.getSchema(serializers, TestEnum.class);
            assertEquals(expectedSchema, schema);
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitorWithString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);
        Set<String> expectedEnums = new LinkedHashSet<String>();
        expectedEnums.add("FIRST");
        expectedEnums.add("SECOND");
        expectedEnums.add("THIRD");
        expectedEnums.add("FOURTH");

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            verify(stringVisitor).enumTypes(expectedEnums);
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}

enum TestEnum {
    FIRST,
    SECOND,
    THIRD,
    FOURTH;
}
