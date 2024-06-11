import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WhenValidJsonArray_ReturnsStringArray() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = getJsonParser("[\"test1\", \"test2\", \"test3\"]");
        DeserializationContext ctxt = getDeserializationContext();
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("test1", result[0]);
        assertEquals("test2", result[1]);
        assertEquals("test3", result[2]);
    }
    
    @Test
    public void testDeserialize_WhenEmptyJsonArray_ReturnsEmptyStringArray() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = getJsonParser("[]");
        DeserializationContext ctxt = getDeserializationContext();
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenNullValue_ReturnsNullStringArray() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = getJsonParser("null");
        DeserializationContext ctxt = getDeserializationContext();
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertNull(result);
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_WhenInvalidJsonArray_ThrowsIOException() throws IOException {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = getJsonParser("[\"test1\", \"test2\", \"test3\"");
        DeserializationContext ctxt = getDeserializationContext();
        
        // Act
        deserializer.deserialize(jp, ctxt);
    }
    
    private JsonParser getJsonParser(String json) {
        // return a mock json parser for testing
    }
    
    private DeserializationContext getDeserializationContext() {
        // return a mock deserialization context for testing
    }
}