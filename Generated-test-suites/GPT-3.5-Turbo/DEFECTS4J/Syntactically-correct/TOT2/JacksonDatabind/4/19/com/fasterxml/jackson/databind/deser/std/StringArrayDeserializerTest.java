import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    // Test deserialize method with normal array input
    @Test
    public void testDeserializeNormalArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"one\", \"two\", \"three\"]";
        String[] expected = {"one", "two", "three"};
        
        // Test
        String[] result = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        
        assertArrayEquals(expected, result);
    }
    
    // Test deserialize method with null values
    @Test
    public void testDeserializeNullValues() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[null, null, null]";
        String[] expected = {null, null, null};
        
        // Test
        String[] result = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        
        assertArrayEquals(expected, result);
    }
    
    // Test deserialize method with empty array
    @Test
    public void testDeserializeEmptyArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[]";
        String[] expected = {};
        
        // Test
        String[] result = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        
        assertArrayEquals(expected, result);
    }
    
    // Test deserialize method with invalid JSON format
    @Test
    public void testDeserializeInvalidFormat() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "invalid";
        
        // Test
        try {
            String[] result = deserializer.deserialize(new JsonParser(), new DeserializationContext());
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected
        }
    }
    
    // Test handleNonArray method with single value array
    @Test
    public void testHandleNonArraySingleValue() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"single\"";
        String[] expected = {"single"};
        
        // Test
        String[] result = deserializer.handleNonArray(new JsonParser(), new DeserializationContext());
        
        assertArrayEquals(expected, result);
    }
    
    // Test handleNonArray method with empty string
    @Test
    public void testHandleNonArrayEmptyString() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"\"";
        String[] expected = {null};
        
        // Test
        String[] result = deserializer.handleNonArray(new JsonParser(), new DeserializationContext());
        
        assertArrayEquals(expected, result);
    }
    
    // Test handleNonArray method with single value array when ACCEPT_SINGLE_VALUE_AS_ARRAY is disabled
    @Test
    public void testHandleNonArraySingleValueDisabled() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"single\"";
        
        // Test
        try {
            String[] result = deserializer.handleNonArray(new JsonParser(), new DeserializationContext());
            fail("Expected JsonMappingException not thrown");
        } catch (IOException e) {
            // Expected
        }
    }
    
    // Test _deserializeCustom method with custom deserializer
    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"1\", \"2\", \"3\"]";
        String[] expected = {"1", "2", "3"};
        JsonDeserializer<String> customDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) {
                try {
                    return jp.getText();
                } catch (IOException e) {
                    return null;
                }
            }
        };
        
        deserializer._elementDeserializer = customDeserializer;
        
        // Test
        String[] result = deserializer._deserializeCustom(new JsonParser(), new DeserializationContext());
        
        assertArrayEquals(expected, result);
    }
    
    // Test createContextual method with default deserializer
    @Test
    public void testCreateContextualDefaultDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonDeserializer<?> result = deserializer.createContextual(new DeserializationContext(), null);
        
        assertNull(result);
    }

}