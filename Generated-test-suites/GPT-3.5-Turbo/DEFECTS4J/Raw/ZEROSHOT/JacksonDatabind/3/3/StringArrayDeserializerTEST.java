import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import static org.mockito.Mockito.*;

public class StringArrayDeserializerTest {
    private StringArrayDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext context;
    private JsonDeserializer<String> elementDeserializer;
    
    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        elementDeserializer = mock(JsonDeserializer.class);
        deserializer._elementDeserializer = elementDeserializer;
    }
    
    @Test
    public void testDeserialize_WhenExpectedStartArrayToken_ThenReturnStringArray() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("value");
        
        String[] result = deserializer.deserialize(jsonParser, context);
        
        assertArrayEquals(new String[] {"value"}, result);
    }
    
    @Test
    public void testDeserialize_WhenNonExpectedStartArrayToken_ThenCallHandleNonArray() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        
        deserializer.deserialize(jsonParser, context);
        
        verify(deserializer).handleNonArray(jsonParser, context);
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNotNull_ThenCallDeserializeCustom() throws Exception {
        deserializer._elementDeserializer = elementDeserializer;
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(elementDeserializer.getNullValue()).thenReturn("null");
        
        deserializer.deserialize(jsonParser, context);
        
        verify(deserializer).deserializeCustom(jsonParser, context);
    }
    
    @Test
    public void testHandleNonArray_WhenAcceptSingleValueAsArray_ThenReturnStringArrayWithSingleValue() throws Exception {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");
        
        String[] result = deserializer.handleNonArray(jsonParser, context);
        
        assertArrayEquals(new String[] {"value"}, result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_WhenNotAcceptSingleValueAsArray_ThenThrowJsonMappingException() throws Exception {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");
        
        deserializer.handleNonArray(jsonParser, context);
    }
    
    @Test
    public void testHandleNonArray_WhenAcceptEmptyStringAsNullObjectAndEmptyString_ThenReturnNull() throws Exception {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");
        
        String[] result = deserializer.handleNonArray(jsonParser, context);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray_WhenAcceptEmptyStringAsNullObjectAndNotEmptyString_ThenReturnStringArrayWithSingleValue() throws Exception {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");
        
        String[] result = deserializer.handleNonArray(jsonParser, context);
        
        assertArrayEquals(new String[] {"value"}, result);
    }
    
    @Test
    public void testDeserializeCustom_WhenExpectedStartArrayToken_ThenReturnStringArray() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("null");
        
        String[] result = deserializer.deserializeCustom(jsonParser, context);
        
        assertArrayEquals(new String[] {"null"}, result);
    }
    
    @Test
    public void testDeserializeWithType_ThenCallDeserializeTypedFromArray() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn("result");
        
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        
        assertEquals("result", result);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsDefault_ThenReturnThis() throws Exception {
        JsonDeserializer<?> result = deserializer.createContextual(context, null);
        
        assertSame(deserializer, result);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsNotDefault_ThenReturnNewStringArrayDeserializer() throws Exception {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(newDeserializer);
        
        JsonDeserializer<?> result = deserializer.createContextual(context, null);
        
        assertNotSame(deserializer, result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertSame(newDeserializer, ((StringArrayDeserializer) result)._elementDeserializer);
    }
    
    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}