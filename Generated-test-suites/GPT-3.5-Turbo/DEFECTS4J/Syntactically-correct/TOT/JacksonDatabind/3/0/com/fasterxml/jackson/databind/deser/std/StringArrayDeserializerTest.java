import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WhenStartTokenIsNotArray_ThrowsIOException() {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.VALUE_STRING, "value");
        DeserializationContext ctxt = createMockDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Act and Assert
        assertThrows(IOException.class, () -> {
            deserializer.deserialize(jp, ctxt);
        });
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerIsNull_DeserializesStringsFromTokens() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.START_ARRAY, null, JsonToken.VALUE_STRING, "value1", JsonToken.VALUE_STRING, "value2", JsonToken.END_ARRAY);
        DeserializationContext ctxt = createMockDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = { "value1", "value2" };
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerIsNotNull_DeserializesStringsUsingElementDeserializer() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.START_ARRAY, null, JsonToken.VALUE_STRING, "value1", JsonToken.VALUE_STRING, "value2", JsonToken.END_ARRAY);
        DeserializationContext ctxt = createMockDeserializationContext();
        JsonDeserializer<String> elementDeserializer = createMockElementDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] expected = { "value1", "value2" };
        
        // Act
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // Assert
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithType_DelegatesToTypeDeserializer() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext ctxt = createMockDeserializationContext();
        TypeDeserializer typeDeserializer = createMockTypeDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Act
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray_WhenSingleValueNotAllowed_ThrowsJsonMappingException() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.VALUE_NUMBER_INT, 1);
        DeserializationContext ctxt = createMockDeserializationContext(false, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Act and Assert
        assertThrows(JsonMappingException.class, () -> {
            deserializer.handleNonArray(jp, ctxt);
        });
    }
    
    @Test
    public void testHandleNonArray_WhenEmptyStringAsNullNotAllowed_ThrowsJsonMappingException() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.VALUE_STRING, "");
        DeserializationContext ctxt = createMockDeserializationContext(false, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Act and Assert
        assertThrows(JsonMappingException.class, () -> {
            deserializer.handleNonArray(jp, ctxt);
        });
    }
    
    @Test
    public void testHandleNonArray_WhenSingleValueIsNull_ReturnsStringArrayWithNullElement() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext ctxt = createMockDeserializationContext(true, true);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = { null };
        
        // Act
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        // Assert
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testHandleNonArray_WhenSingleValueIsNotNull_ReturnsStringArrayWithSingleElement() throws IOException {
        // Arrange
        JsonParser jp = createMockJsonParser(JsonToken.VALUE_STRING, "value");
        DeserializationContext ctxt = createMockDeserializationContext(true, true);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = { "value" };
        
        // Act
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        // Assert
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsNull_ReturnsSelf() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = createMockDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        BeanProperty property = createMockBeanProperty();
        
        // Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        // Assert
        assertEquals(deserializer, result);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsDefault_ReturnsSelf() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = createMockDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(createDefaultElementDeserializer());
        BeanProperty property = createMockBeanProperty();
        
        // Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        // Assert
        assertEquals(deserializer, result);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsNotNullAndNonDefault_ReturnsNewInstanceWithElementDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = createMockDeserializationContext();
        JsonDeserializer<String> elementDeserializer = createMockElementDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer._elementDeserializer = createDefaultElementDeserializer();
        BeanProperty property = createMockBeanProperty();
        
        // Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        // Assert
        assertNotEquals(deserializer, result);
        assertEquals(elementDeserializer, deserializer._elementDeserializer);
    }
    
    private JsonParser createMockJsonParser(JsonToken... tokens) {
        JsonParser parser = createMock(JsonParser.class);
        when(parser.nextToken()).thenReturn(tokens);
        when(parser.isExpectedStartArrayToken()).thenReturn(tokens[0] == JsonToken.START_ARRAY);
        if (tokens.length > 1) {
            when(parser.getText()).thenReturn("value1", "value2");
        }
        return parser;
    }
    
    private DeserializationContext createMockDeserializationContext() {
        return createMock(DeserializationContext.class);
    }
    
    private DeserializationContext createMockDeserializationContext(boolean acceptSingleValueAsArray, boolean acceptEmptyStringAsNull) {
        DeserializationContext ctxt = createMock(DeserializationContext.class);
        DeserializationConfig config = createMock(DeserializationConfig.class);
        when(ctxt.getConfig()).thenReturn(config);
        when(config.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(acceptSingleValueAsArray);
        when(config.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(acceptEmptyStringAsNull);
        return ctxt;
    }
    
    private JsonDeserializer<String> createMockElementDeserializer() {
        return createMock(JsonDeserializer.class);
    }
    
    private JsonDeserializer<String> createDefaultElementDeserializer() {
        return createMockElementDeserializer();
    }
    
    private TypeDeserializer createMockTypeDeserializer() {
        return createMock(TypeDeserializer.class);
    }
    
    private BeanProperty createMockBeanProperty() {
        return createMock(BeanProperty.class);
    }
    
    // Helper method to create mock objects using mockito
    private <T> T createMock(Class<T> mockClass) {
        return Mockito.mock(mockClass);
    }
}