import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StringArrayDeserializerTest {

    @Mock
    private JsonParser mockJsonParser;
    
    @Mock
    private DeserializationContext mockDeserializationContext;
    
    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stringArrayDeserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize_WhenExpectedStartArrayToken_ReturnsDeserializedStringArray() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("value1");

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value1", result[0]);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser).getText();
    }
    
    @Test
    public void testDeserialize_WhenNotExpectedStartArrayToken_CallsHandleNonArray() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNull(result);
        verify(mockJsonParser).isExpectedStartArrayToken();
        verify(mockJsonParser, never()).nextToken();
        verify(mockJsonParser, never()).getText();
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNotNull_CallsDeserializeCustom() throws IOException {
        stringArrayDeserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("value1");

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value1", result[0]);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser).getText();
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNullAndValueTokenIsNull_ReturnsNull() throws IOException {
        stringArrayDeserializer = new StringArrayDeserializer();
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNull(result);
        verify(mockJsonParser, times(2)).nextToken();
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNullAndValueTokenIsString_ReturnsNull() throws IOException {
        stringArrayDeserializer = new StringArrayDeserializer();
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("");

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNull(result);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser).getText();
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNullAndValueTokenIsOther_ReturnsNull() throws IOException {
        stringArrayDeserializer = new StringArrayDeserializer();
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNull(result);
        verify(mockJsonParser, times(2)).nextToken();
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNotNullAndValueTokenIsNull_ReturnsNullValueFromDeserializer() throws IOException {
        JsonDeserializer<String> mockElementDeserializer = mock(JsonDeserializer.class);
        when(mockElementDeserializer.getNullValue()).thenReturn("Null");
        stringArrayDeserializer = new StringArrayDeserializer(mockElementDeserializer);

        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("Null", result[0]);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockElementDeserializer).getNullValue();
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNotNullAndValueTokenIsString_ReturnsDeserializedStringFromDeserializer() throws IOException {
        JsonDeserializer<String> mockElementDeserializer = mock(JsonDeserializer.class);
        when(mockElementDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn("value1");
        stringArrayDeserializer = new StringArrayDeserializer(mockElementDeserializer);

        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true, false);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value1", result[0]);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockElementDeserializer).deserialize(mockJsonParser, mockDeserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_Always_CallsDeserializeTypedFromArray() throws IOException {
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);

        Object result = stringArrayDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        assertNull(result);
        verify(mockTypeDeserializer).deserializeTypedFromArray(mockJsonParser, mockDeserializationContext);
    }
    
    @Test
    public void testHandleNonArray_WhenAcceptSingleValueAsArrayIsEnabledAndValueTokenIsNull_ReturnsNull() throws IOException {
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        
        String[] result = stringArrayDeserializer.handleNonArray(mockJsonParser, mockDeserializationContext);
        
        assertNull(result);
        verify(mockDeserializationContext).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(mockJsonParser).getCurrentToken();
    }
    
    @Test
    public void testHandleNonArray_WhenAcceptSingleValueAsArrayIsEnabledAndValueTokenIsEmptyString_ReturnsNull() throws IOException {
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(mockJsonParser.getText()).thenReturn("");
        
        String[] result = stringArrayDeserializer.handleNonArray(mockJsonParser, mockDeserializationContext);

        assertNull(result);
        verify(mockDeserializationContext).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(mockJsonParser).getCurrentToken();
        verify(mockDeserializationContext).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(mockJsonParser).getText();
    }
    
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_WhenAcceptSingleValueAsArrayIsNotEnabled_ThrowsJsonMappingException() throws IOException {
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        
        stringArrayDeserializer.handleNonArray(mockJsonParser, mockDeserializationContext);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsNull_ReturnsStringArrayDeserializer() throws JsonMappingException {
        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(mockDeserializationContext, mock(BeanProperty.class));

        assertNotNull(deserializer);
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsDefaultDeserializer_ReturnsStringArrayDeserializerWithoutElementDeserializer() throws JsonMappingException {
        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(mockDeserializationContext, mock(BeanProperty.class));

        assertNotNull(deserializer);
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
        assertNull(((StringArrayDeserializer) deserializer)._elementDeserializer);
    }
    
    @Test
    public void testCreateContextual_WhenElementDeserializerIsNotDefaultDeserializer_ReturnsStringArrayDeserializerWithElementDeserializer() throws JsonMappingException {
        JsonDeserializer<?> mockElementDeserializer = mock(JsonDeserializer.class);
        when(mockDeserializationContext.findContextualValueDeserializer(mockDeserializationContext.constructType(String.class), mock(BeanProperty.class))).thenReturn(mockElementDeserializer);

        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(mockDeserializationContext, mock(BeanProperty.class));

        assertNotNull(deserializer);
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
        assertEquals(mockElementDeserializer, ((StringArrayDeserializer) deserializer)._elementDeserializer);
    }
    
    private boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
        return String.class.equals(((StringArrayDeserializer) deserializer)._valueClass);
    }
}