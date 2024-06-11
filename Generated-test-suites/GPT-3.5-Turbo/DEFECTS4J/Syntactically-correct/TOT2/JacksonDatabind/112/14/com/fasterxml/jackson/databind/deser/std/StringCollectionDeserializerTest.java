import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

public class StringCollectionDeserializerTest {
    
    // Test public method deserialize(JsonParser p, DeserializationContext ctxt)
    @Test
    public void testDeserialize() throws IOException {
        // Test empty JSON array
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(createParser("[]"), null);
        assertTrue(result.isEmpty());
        
        // Test JSON array with multiple string values
        result = deserializer.deserialize(createParser("[\"value1\", \"value2\"]"), null);
        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        
        // Test JSON array with null value
        result = deserializer.deserialize(createParser("[null]"), null);
        assertEquals(1, result.size());
        assertNull(result.iterator().next());
        
        // Test JSON array with mix of string values and null value
        result = deserializer.deserialize(createParser("[\"value1\", null, \"value2\"]"), null);
        assertEquals(3, result.size());
        assertTrue(result.contains("value1"));
        assertNull(result.toArray()[1]);
        assertTrue(result.contains("value2"));
    }
    
    // Test helper method handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<String> result)
    @Test
    public void testHandleNonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        
        // Test when unwrapSingle is true
        Collection<String> result = deserializer.deserialize(createParser("\"singleValue\""), null);
        assertEquals(1, result.size());
        assertTrue(result.contains("singleValue"));
        
        // Test when unwrapSingle is false
        result = deserializer.deserialize(createParser("\"singleValue\""), null);
        assertTrue(result.isEmpty());
    }
    
    // Helper method to create JsonParser for testing
    private JsonParser createParser(String json) throws IOException {
        return new ObjectMapper().getFactory().createParser(json);
    }

}