import org.junit.Test;

public class StringCollectionDeserializerTest {
    
    @Test
    public void testDeserialize_StandardCase() {
        // Arrange
        JsonParser parser = MockJsonParser.withTokens(JsonToken.START_ARRAY, 
                                                    JsonToken.VALUE_STRING, 
                                                    JsonToken.VALUE_STRING,
                                                    JsonToken.END_ARRAY);
        DeserializationContext ctxt = MockDeserializationContext.getContext();
        Collection<String> result = new ArrayList<>();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        // Act
        Collection<String> deserialized = deserializer.deserialize(parser, ctxt, result);
        
        // Assert
        assertEquals(2, deserialized.size());
        assertTrue(deserialized.contains("value1"));
        assertTrue(deserialized.contains("value2"));
    }
    
    @Test
    public void testDeserialize_NullValue() {
        // Arrange
        JsonParser parser = MockJsonParser.withTokens(JsonToken.START_ARRAY, 
                                                    JsonToken.VALUE_NULL,
                                                    JsonToken.END_ARRAY);
        DeserializationContext ctxt = MockDeserializationContext.getContext();
        Collection<String> result = new ArrayList<>();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        // Act
        Collection<String> deserialized = deserializer.deserialize(parser, ctxt, result);
        
        // Assert
        assertEquals(1, deserialized.size());
        assertNull(deserialized.get(0));
    }
    
    @Test
    public void testDeserialize_EmptyArray() {
        // Arrange
        JsonParser parser = MockJsonParser.withTokens(JsonToken.START_ARRAY, 
                                                    JsonToken.END_ARRAY);
        DeserializationContext ctxt = MockDeserializationContext.getContext();
        Collection<String> result = new ArrayList<>();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        // Act
        Collection<String> deserialized = deserializer.deserialize(parser, ctxt, result);
        
        // Assert
        assertTrue(deserialized.isEmpty());
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidToken() {
        // Arrange
        JsonParser parser = MockJsonParser.withTokens(JsonToken.VALUE_STRING);
        DeserializationContext ctxt = MockDeserializationContext.getContext();
        Collection<String> result = new ArrayList<>();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        // Act & Assert
        deserializer.deserialize(parser, ctxt, result);
    }
    
}