import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    // Test case for deserialize method
    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Typical use case
        String json1 = "[\"apple\", \"banana\", \"orange\"]";
        String[] expected1 = {"apple", "banana", "orange"};
        assertArrayEquals(expected1, deserializer.deserialize(json1));
        
        // Edge case: empty array
        String json2 = "[]";
        String[] expected2 = {};
        assertArrayEquals(expected2, deserializer.deserialize(json2));
        
        // Edge case: array with null value
        String json3 = "[\"apple\", null, \"orange\"]";
        String[] expected3 = {"apple", null, "orange"};
        assertArrayEquals(expected3, deserializer.deserialize(json3));
        
        // Error scenario: invalid JSON array
        String json4 = "\"apple\", \"banana\", \"orange\"";
        assertThrows(IOException.class, () -> {
            deserializer.deserialize(json4);
        });
    }
    
    // Test case for deserializeWithType method
    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Typical use case
        String json1 = "[\"apple\", \"banana\", \"orange\"]";
        String[] expected1 = {"apple", "banana", "orange"};
        assertArrayEquals(expected1, deserializer.deserializeWithType(json1));
        
        // Edge case: empty array
        String json2 = "[]";
        String[] expected2 = {};
        assertArrayEquals(expected2, deserializer.deserializeWithType(json2));
        
        // Edge case: array with null value
        String json3 = "[\"apple\", null, \"orange\"]";
        String[] expected3 = {"apple", null, "orange"};
        assertArrayEquals(expected3, deserializer.deserializeWithType(json3));
        
        // Error scenario: invalid JSON array
        String json4 = "\"apple\", \"banana\", \"orange\"";
        assertThrows(IOException.class, () -> {
            deserializer.deserializeWithType(json4);
        });
    }
    
    // Test case for handleNonArray method
    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Typical use case: array with single value
        String json1 = "\"apple\"";
        String[] expected1 = {"apple"};
        assertArrayEquals(expected1, deserializer.handleNonArray(json1));
        
        // Edge case: null value
        String json2 = "null";
        String[] expected2 = null;
        assertArrayEquals(expected2, deserializer.handleNonArray(json2));
        
        // Error scenario: JSON object instead of array
        String json3 = "{\"fruit\":\"apple\"}";
        assertThrows(IOException.class, () -> {
            deserializer.handleNonArray(json3);
        });
        
        // Error scenario: JSON array with multiple values
        String json4 = "[\"apple\", \"banana\"]";
        assertThrows(IOException.class, () -> {
            deserializer.handleNonArray(json4);
        });
    }

    // Test case for createContextual method
    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Typical use case: value deserializer is null
        JsonDeserializer<?> deser1 = null;
        assertNull(deserializer.createContextual(deser1));
        
        // Typical use case: value deserializer is not null
        JsonDeserializer<?> deser2 = new CustomStringDeserializer();
        assertNotNull(deserializer.createContextual(deser2));
    }
    
    // Custom String deserializer for testing createContextual method
    private static class CustomStringDeserializer extends JsonDeserializer<String> {
        
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            // Custom deserialization logic
            return "custom string";
        }
    }
}