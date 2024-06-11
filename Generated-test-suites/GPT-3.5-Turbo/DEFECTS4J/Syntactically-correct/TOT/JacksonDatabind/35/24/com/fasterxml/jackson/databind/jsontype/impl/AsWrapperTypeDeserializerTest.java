import static org.junit.Assert.*;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_typical() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromObject(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_edge() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromObject(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_error() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromObject(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray_typical() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromArray(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray_edge() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromArray(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray_error() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromArray(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar_typical() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromScalar(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar_edge() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromScalar(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar_error() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromScalar(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny_typical() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromAny(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny_edge() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromAny(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny_error() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserializeTypedFromAny(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void test_deserialize_typical() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserialize(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void test_deserialize_edge() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserialize(null, null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void test_deserialize_error() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        
        // Act
        Object result = deserializer.deserialize(null, null);
        
        // Assert
        assertNull(result);
    }

}