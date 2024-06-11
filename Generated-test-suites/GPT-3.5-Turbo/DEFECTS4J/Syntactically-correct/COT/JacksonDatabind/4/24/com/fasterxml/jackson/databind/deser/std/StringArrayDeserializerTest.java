import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("test");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
    }
    
    @Test
    public void testDeserializeWithNullValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        
        try {
            deserializer.handleNonArray(jp, ctxt);
            fail("Expected exception not thrown");
        } catch (JsonMappingException e) {
            // exception expected
        }
    }
    
    @Test
    public void testHandleNonArrayWithEmptyStringAsNull() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jp.getText()).thenReturn("");
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArrayWithSingleValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jp.getText()).thenReturn("test");
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
    }
    
    @Test
    public void testDeserializeCustom() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        deserializer._elementDeserializer = mock(JsonDeserializer.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("test");
        when(deserializer._elementDeserializer.deserialize(jp, ctxt)).thenReturn("test");
        
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
    }
    
    @Test
    public void testDeserializeCustomWithNullValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        deserializer._elementDeserializer = mock(JsonDeserializer.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(deserializer._elementDeserializer.getNullValue()).thenReturn(null);
        
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test
    public void testCreateContextualWithValueDeserializer() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(valueDeserializer);
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNotNull(result);
        assertNotEquals(deserializer, result);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertEquals(valueDeserializer, ((StringArrayDeserializer) result)._elementDeserializer);
    }
    
    @Test
    public void testCreateContextualWithDefaultDeserializer() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(null);
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

}