import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize_WhenStartArrayToken_ReturnsStringArray() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[2]);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("value1", "value2");
        when(buffer.appendCompletedChunk(any())).thenReturn(new Object[2]);
        when(buffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[] {"value1", "value2"});

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser, times(3)).nextToken();
        verify(jsonParser).getText();
        verify(buffer).completeAndClearBuffer(any(), anyInt(), any());
        verify(deserializationContext).returnObjectBuffer(buffer);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }

    @Test
    public void testDeserialize_WhenElementDeserializerNotNull_ReturnsStringArray() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(stringArrayDeserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[2]);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("value1", "value2");
        when(elementDeserializer.getNullValue()).thenReturn(null);
        when(elementDeserializer.deserialize(any(), any())).thenReturn("value1", "value2");
        when(buffer.appendCompletedChunk(any())).thenReturn(new Object[2]);
        when(buffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[] {"value1", "value2"});

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        verify(stringArrayDeserializer, times(1))._deserializeCustom(jsonParser, deserializationContext);
        verify(jsonParser, times(3)).nextToken();
        verify(jsonParser).getText();
        verify(elementDeserializer).deserialize(any(), any());
        verify(buffer).completeAndClearBuffer(any(), anyInt(), any());
        verify(deserializationContext).returnObjectBuffer(buffer);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }

    @Test
    public void testDeserializeWithType_ReturnsObject() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn("result");

        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer).deserializeTypedFromArray(jsonParser, deserializationContext);
        assertEquals("result", result);
    }

    @Test
    public void testHandleNonArray_WhenSingleValueAsArrayEnabled_ReturnsStringArray() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("value");

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test (expected = JsonMappingException.class)
    public void testHandleNonArray_WhenSingleValueAsArrayDisabled_ThrowsMappingException() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
    }

    @Test
    public void testHandleNonArray_WhenEmptyStringAsNullObjectEnabledAndEmptyString_ReturnsNull() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("");

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test (expected = JsonMappingException.class)
    public void testHandleNonArray_WhenEmptyStringAsNullObjectEnabledAndNotEmptyString_ThrowsMappingException() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("value");

        stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
    }

    @Test
    public void testHandleNonArray_WhenEmptyStringAsNullObjectDisabledAndEmptyString_ReturnsStringArrayWithNullValue() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(jsonParser.getText()).thenReturn("");

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerNotNullAndIsDefaultDeserializer_ReturnsStringArrayDeserializer() throws JsonMappingException {
        JsonDeserializer<?> elementDeserializer = mock(JsonDeserializer.class);
        BeanProperty property = mock(BeanProperty.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(stringArrayDeserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertEquals(stringArrayDeserializer, result);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerNotNullAndNotDefaultDeserializer_ReturnsNewStringArrayDeserializer() throws JsonMappingException {
        JsonDeserializer<?> elementDeserializer = mock(JsonDeserializer.class);
        BeanProperty property = mock(BeanProperty.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(stringArrayDeserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);
        when(stringArrayDeserializer.findConvertingContentDeserializer(deserializationContext, property, elementDeserializer)).thenReturn(elementDeserializer);
        when(deserializationContext.constructType(String.class)).thenReturn(mock(JavaType.class));
        when(deserializationContext.findContextualValueDeserializer(any(), any())).thenReturn(elementDeserializer);
        when(deserializationContext.handleSecondaryContextualization(elementDeserializer, property)).thenReturn(elementDeserializer);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertEquals(stringArrayDeserializer.getClass(), result.getClass());
        assertNotEquals(stringArrayDeserializer, result);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerNull_ReturnsStringArrayDeserializer() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(stringArrayDeserializer._elementDeserializer).thenReturn(null);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertEquals(stringArrayDeserializer, result);
    }

    private boolean isDefaultDeserializer(JsonDeserializer<?> elementDeserializer) {
        return elementDeserializer == null;
    }
    
}