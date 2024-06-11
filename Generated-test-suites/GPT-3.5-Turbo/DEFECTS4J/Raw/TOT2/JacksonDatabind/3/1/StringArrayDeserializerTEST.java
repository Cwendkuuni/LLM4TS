import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WhenArrayWithStrings_ShouldReturnStringArray() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("[\"hello\", \"world\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertEquals(2, result.length);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    @Test
    public void testDeserialize_WhenNullValue_ShouldReturnNull() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserialize_WhenEmptyArray_ShouldReturnEmptyStringArray() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenInvalidJson_ShouldThrowIOException() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("{invalid}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            deserializer.deserialize(jp, ctxt);
        });
    }
    
    // Add more test cases for handleNonArray, _deserializeCustom, etc.

}