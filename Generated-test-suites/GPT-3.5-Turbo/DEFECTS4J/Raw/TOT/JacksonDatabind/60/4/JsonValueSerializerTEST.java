import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    @Test
    public void testSerialize_NullValue_ShouldSerializeNull() {
        // Arrange
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        TestJsonGenerator generator = new TestJsonGenerator();
        
        // Act
        serializer.serialize(null, generator, null);

        // Assert
        assertTrue(generator.isDefaultSerializeNullCalled());
    }

    @Test
    public void testSerialize_NonNullValue_ShouldSerializeValue() {
        // Arrange
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        TestJsonGenerator generator = new TestJsonGenerator();
        Object value = new Object();
        
        // Act
        serializer.serialize(value, generator, null);

        // Assert
        assertTrue(generator.isSerializeCalled());
    }

    @Test
    public void testSerializeWithType_NullValue_ShouldSerializeNull() {
        // Arrange
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        TestJsonGenerator generator = new TestJsonGenerator();
        
        // Act
        serializer.serializeWithType(null, generator, null, null);

        // Assert
        assertTrue(generator.isDefaultSerializeNullCalled());
    }

    @Test
    public void testSerializeWithType_NonNullValue_ShouldSerializeValueWithType() {
        // Arrange
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        TestJsonGenerator generator = new TestJsonGenerator();
        Object value = new Object();
        
        // Act
        serializer.serializeWithType(value, generator, null, null);

        // Assert
        assertTrue(generator.isSerializeWithTypeCalled());
    }

    @Test
    public void testGetSchema_ValueSerializerImplementsSchemaAware_ShouldCallValueSerializerGetSchema() {
        // Arrange
        TestSerializer valueSerializer = new TestSerializer();
        JsonValueSerializer serializer = new JsonValueSerializer(null, valueSerializer);
        
        // Act
        serializer.getSchema(null, null);

        // Assert
        assertTrue(valueSerializer.isGetSchemaCalled());
    }

    @Test
    public void testGetSchema_ValueSerializerDoesNotImplementSchemaAware_ShouldReturnDefaultSchemaNode() {
        // Arrange
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        
        // Act
        JsonNode schema = serializer.getSchema(null, null);

        // Assert
        assertNotNull(schema);
        assertEquals(JsonSchema.getDefaultSchemaNode(), schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor_ValueSerializerImplementsJsonFormatVisitable_ShouldCallValueSerializerAcceptJsonFormatVisitor() {
        // Arrange
        TestJsonFormatVisitorWrapper visitor = new TestJsonFormatVisitorWrapper();
        TestJsonFormatVisitor valueVisitor = new TestJsonFormatVisitor();
        TestSerializer valueSerializer = new TestSerializer(valueVisitor);
        JsonValueSerializer serializer = new JsonValueSerializer(null, valueSerializer);
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, null);

        // Assert
        assertTrue(valueVisitor.isAcceptJsonFormatVisitorCalled());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ValueSerializerDoesNotImplementJsonFormatVisitable_ShouldCallExpectAnyFormat() {
        // Arrange
        TestJsonFormatVisitorWrapper visitor = new TestJsonFormatVisitorWrapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, null);

        // Assert
        assertTrue(visitor.isExpectAnyFormatCalled());
    }

    @Test
    public void testToString_ShouldReturnCorrectStringRepresentation() {
        // Arrange
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        
        // Act
        String result = serializer.toString();

        // Assert
        assertEquals("(@JsonValue serializer for method null#null)", result);
    }
}