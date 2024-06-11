import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionDeserializerTest {
    
    @Test
    public void testDeserializeEmptyArray() {
        // Test deserializing an empty array
        // Arrange
        CollectionDeserializer deserializer = new CollectionDeserializer(JavaType.SimpleType.constructUnsafe(Object.class),
            null, null, null);
        JsonParser parser = new JsonFactory().createParser("[]");
        DeserializationContext context = new DeserializationContext(null, null, null);
        // Act
        Collection<Object> result = deserializer.deserialize(parser, context);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testHandleNonArraySingleValue() {
        // Test handling a single value when expecting an array
        // Arrange
        CollectionDeserializer deserializer = new CollectionDeserializer(JavaType.SimpleType.constructUnsafe(Object.class),
            null, null, null);
        JsonParser parser = new JsonFactory().createParser("\"singleValue\"");
        DeserializationContext context = new DeserializationContext(null, null, null);
        Collection<Object> result = new ArrayList<>();
        // Act
        Collection<Object> updatedResult = deserializer.handleNonArray(parser, context, result);
        // Assert
        assertNotNull(updatedResult);
        assertEquals(1, updatedResult.size());
        assertEquals("singleValue", updatedResult.get(0));
    }
    
    @Test
    public void testDeserializeWithType() {
        // Test deserializing with type information
        // Arrange
        CollectionDeserializer deserializer = new CollectionDeserializer(JavaType.SimpleType.constructUnsafe(Object.class),
            null, null, null);
        JsonParser parser = new JsonFactory().createParser("[1, 2]");
        DeserializationContext context = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = new TypeDeserializer(null, null, null);
        // Act
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Collection);
        assertEquals(2, ((Collection<?>) result).size());
    }
}