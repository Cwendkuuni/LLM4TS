import org.junit.*;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {
    
    @Test
    public void testSerialize_NullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object value = null;
        
        // Act
        String result = serializer.serialize(value);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testSerialize_NonNullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object value = "Test";
        
        // Act
        String result = serializer.serialize(value);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test", result);
    }
    
    @Test
    public void testSerializeWithType_NullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object value = null;
        
        // Act
        String result = serializer.serializeWithType(value);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testSerializeWithType_NonNullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object value = "Test";
        
        // Act
        String result = serializer.serializeWithType(value);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test", result);
    }
    
    @Test
    public void testIsEmpty_NullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object value = null;
        
        // Act
        boolean result = serializer.isEmpty(value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsEmpty_NonNullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object value = "Test";
        
        // Act
        boolean result = serializer.isEmpty(value);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsEmpty_ProviderAndNullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = null;
        Object value = null;
        
        // Act
        boolean result = serializer.isEmpty(provider, value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsEmpty_ProviderAndNonNullValue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = null;
        Object value = "Test";
        
        // Act
        boolean result = serializer.isEmpty(provider, value);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testGetSchema_WithoutDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = null;
        Type typeHint = null;
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetSchema_WithDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonSerializer<?> delegateSerializer = new JsonSerializer<JsonNode>() {
            @Override
            public void serialize(JsonNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // do nothing
            }
        };
        serializer.setDelegateSerializer(delegateSerializer);
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testGetSchema_WithDelegateSerializerAndIsOptional() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonSerializer<?> delegateSerializer = new JsonSerializer<JsonNode>() {
            @Override
            public void serialize(JsonNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // do nothing
            }
        };
        serializer.setDelegateSerializer(delegateSerializer);
        boolean isOptional = true;
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WithoutDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        // No exceptions should be thrown
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WithDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        JsonSerializer<?> delegateSerializer = new JsonSerializer<JsonNode>() {
            @Override
            public void serialize(JsonNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // do nothing
            }
        };
        serializer.setDelegateSerializer(delegateSerializer);
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        // No exceptions should be thrown
    }
    
}