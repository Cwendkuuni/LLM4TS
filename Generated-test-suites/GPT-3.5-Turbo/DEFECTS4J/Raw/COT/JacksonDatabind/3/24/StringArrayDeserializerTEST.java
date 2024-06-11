import org.junit.Test;

public class StringArrayDeserializerTest {

    // Test deserialize method
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        // Test with empty array
        String[] result = deserializer.deserialize(null, null);
        assertArrayEquals(new String[]{}, result);
        
        // Test with array containing single string value
        result = deserializer.deserialize(null, new DeserializationContext(null));
        assertArrayEquals(new String[]{null}, result);
        
        // Test with array containing multiple string values
        result = deserializer.deserialize(null, new DeserializationContext(null));
        assertArrayEquals(new String[]{null, "test", "test2"}, result);
    }
    
    // Test deserializeWithType method
    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(null, null, null);
        assertNull(result);
    }
    
    // Test handleNonArray method
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test with non-array input and ACCEPT_SINGLE_VALUE_AS_ARRAY disabled
        DeserializationContext ctxt = new DeserializationContext(null);
        Exception exception = assertThrows(ctxt.mappingException, () -> {
            deserializer.handleNonArray(null, ctxt);
        });
        
        String expectedMessage = "Exception message";
        assertEquals(expectedMessage, exception.getMessage());

        // Test with non-array input and ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
        ctxt = new DeserializationContext(null);
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        String[] result = deserializer.handleNonArray(null, ctxt);
        assertArrayEquals(new String[]{null}, result);
        
        // Test with empty string and ACCEPT_EMPTY_STRING_AS_NULL_OBJECT enabled
        jp = new JsonParser();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setText("");
        ctxt = new DeserializationContext(null);
        ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        result = deserializer.handleNonArray(jp, ctxt);
        assertNull(result);
    }
    
    // Test createContextual method
    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null);
        BeanProperty property = new BeanProperty();
        
        // Test with null value deserializer
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertNull(result);
        
        // Test with non-null value deserializer
        JsonDeserializer<?> elementDeserializer = new StringDeserializer();
        deserializer._elementDeserializer = elementDeserializer;
        result = deserializer.createContextual(ctxt, property);
        assertNotNull(result);
    }
}