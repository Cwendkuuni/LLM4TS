import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

    @Test
    public void serialize_DateValue_DefaultSerializeDateKeyCalled() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date(1234567890L);
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        
        // Act
        serializer.serialize(date, generator, provider);
        
        // Assert
        assertTrue(provider.defaultSerializeDateKeyCalled);
    }

    @Test
    public void serialize_NonDateValue_ValueToStringCalled() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Object object = new Object();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        
        // Act
        serializer.serialize(object, generator, provider);
        
        // Assert
        assertTrue(generator.writeFieldNameCalled);
        assertEquals(object.toString(), generator.fieldName);
    }

    @Test
    public void getSchema_ReturnsStringSchemaNode() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProviderStub provider = new SerializerProviderStub();
        
        // Act
        JsonNode schemaNode = serializer.getSchema(provider, null);
        
        // Assert
        assertNotNull(schemaNode);
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void acceptJsonFormatVisitor_ExpectStringFormatCalled() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapperStub visitor = new JsonFormatVisitorWrapperStub();
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, null);
        
        // Assert
        assertTrue(visitor.expectStringFormatCalled);
    }

    // Helper stub classes for testing
        
    class JsonGeneratorStub extends JsonGenerator {
        boolean writeFieldNameCalled;
        String fieldName;

        @Override
        public void writeFieldName(String name) throws IOException {
            writeFieldNameCalled = true;
            fieldName = name;
        }

        // Other overridden methods
    }

    class SerializerProviderStub extends SerializerProvider {
        boolean defaultSerializeDateKeyCalled;

        @Override
        public void defaultSerializeDateKey(Date date, JsonGenerator jgen) throws IOException {
            defaultSerializeDateKeyCalled = true;
        }

        // Other overridden methods
    }

    class JsonFormatVisitorWrapperStub extends JsonFormatVisitorWrapper {
        boolean expectStringFormatCalled;

        @Override
        public void expectStringFormat(JavaType convertedType) throws JsonMappingException {
            expectStringFormatCalled = true;
        }

        // Other overridden methods
    }
}