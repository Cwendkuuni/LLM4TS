import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeEmptyJson() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "{}";
        
        // Act
        Object result = deserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeSingleFieldJson() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "{\"field\": \"value\"}";
        
        // Act
        Object result = deserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeMultipleFieldsJson() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "{\"field1\": \"value1\", \"field2\": \"value2\"}";
        
        // Act
        Object result = deserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeArray() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "[]";
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeObject() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "{}";
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeFieldName() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "\"field\"";
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeOtherToken() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        String json = "123";
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        assertNull(result);
    }
}