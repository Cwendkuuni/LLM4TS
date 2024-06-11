import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StringArrayDeserializerTest {

    @Mock
    private JsonParser mockParser;

    @Mock
    private DeserializationContext mockCtxt;

    @Mock
    private ObjectBuffer mockBuffer;

    @Mock
    private String mockValue;

    private StringArrayDeserializer deserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        deserializer = StringArrayDeserializer.instance;
    }

    @Test
    public void testDeserialize_withStartArrayToken_shouldDeserializeArray() throws IOException {
        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);

        String[] result = deserializer.deserialize(mockParser, mockCtxt);

        assertNull(result);
        verify(mockParser).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserialize_withCustomDeserialize_shouldDeserializeArray() throws IOException {
        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockParser.getText()).thenReturn("value");
        when(mockCtxt.leaseObjectBuffer()).thenReturn(mockBuffer);
        when(mockBuffer.resetAndStart()).thenReturn(new String[1]);
        when(mockBuffer.appendCompletedChunk(any())).thenReturn(new String[2]);
        when(mockBuffer.completeAndClearBuffer(any(), anyInt(), any(Class.class))).thenReturn(new String[1]);
        when(mockCtxt.returnObjectBuffer(any())).thenReturn(true);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockParser.readValueAs(eq(String.class))).thenReturn(mockValue);

        String[] result = deserializer.deserialize(mockParser, mockCtxt);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
        verify(mockParser).isExpectedStartArrayToken();
        verify(mockParser, times(2)).nextToken();
        verify(mockParser).getText();
        verify(mockCtxt).leaseObjectBuffer();
        verify(mockBuffer).resetAndStart();
        verify(mockBuffer).appendCompletedChunk(any());
        verify(mockBuffer).completeAndClearBuffer(any(), anyInt(), any(Class.class));
        verify(mockCtxt).returnObjectBuffer(any());
        verify(mockParser).getCurrentToken();
        verify(mockParser).readValueAs(eq(String.class));
    }

    @Test
    public void testDeserializeCustom_withCustomDeserialize_shouldDeserializeArray() throws IOException {
        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockParser.getText()).thenReturn("value");
        when(mockCtxt.leaseObjectBuffer()).thenReturn(mockBuffer);
        when(mockBuffer.resetAndStart()).thenReturn(new String[1]);
        when(mockBuffer.appendCompletedChunk(any())).thenReturn(new String[2]);
        when(mockBuffer.completeAndClearBuffer(any(), anyInt(), any(Class.class))).thenReturn(new String[1]);
        when(mockCtxt.returnObjectBuffer(any())).thenReturn(true);
        when(mockParser.readValueAs(eq(String.class))).thenReturn(mockValue);

        String[] result = deserializer._deserializeCustom(mockParser, mockCtxt);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
        verify(mockParser, times(2)).nextToken();
        verify(mockParser).getText();
        verify(mockCtxt).leaseObjectBuffer();
        verify(mockBuffer).resetAndStart();
        verify(mockBuffer).appendCompletedChunk(any());
        verify(mockBuffer).completeAndClearBuffer(any(), anyInt(), any(Class.class));
        verify(mockCtxt).returnObjectBuffer(any());
        verify(mockParser).readValueAs(eq(String.class));
    }

    @Test
    public void testDeserializeWithType_shouldDeserializeTypedArray() throws IOException {
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);

        Object result = deserializer.deserializeWithType(mockParser, mockCtxt, mockTypeDeserializer);

        assertNull(result);
        verify(mockTypeDeserializer).deserializeTypedFromArray(mockParser, mockCtxt);
    }

    @Test
    public void testHandleNonArray_withNonArrayTokenAndAcceptSingleValueAsArrayDisabled_shouldThrowMappingException() throws IOException {
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        try {
            deserializer.handleNonArray(mockParser, mockCtxt);
        } catch (JsonMappingException e) {
            // Expected
        }

        verify(mockCtxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(mockParser, never()).getCurrentToken();
        verify(mockParser, never()).getText();
        verify(mockCtxt, never()).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(mockParser, never()).getValueAsString();
        verify(mockParser, never()).nextToken();
    }

    @Test
    public void testHandleNonArray_withNonArrayTokenAndAcceptSingleValueAsArrayEnabledAndValueIsNull_shouldReturnNull() throws IOException {
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);

        String[] result = deserializer.handleNonArray(mockParser, mockCtxt);

        assertNull(result);
        verify(mockCtxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(mockParser).getCurrentToken();
        verify(mockCtxt, never()).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(mockParser, never()).getValueAsString();
        verify(mockParser, never()).getText();
    }

    @Test
    public void testHandleNonArray_withNonArrayTokenAndAcceptSingleValueAsArrayEnabledAndValueIsEmptyString_shouldReturnNull() throws IOException {
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(mockParser.getValueAsString()).thenReturn("");

        String[] result = deserializer.handleNonArray(mockParser, mockCtxt);

        assertNull(result);
        verify(mockCtxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(mockParser).getCurrentToken();
        verify(mockCtxt).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(mockParser).getValueAsString();
        verify(mockParser, never()).getText();
    }

    @Test
    public void testHandleNonArray_withNonArrayTokenAndAcceptSingleValueAsArrayEnabledAndValueIsNotEmptyString_shouldReturnArrayWithSingleValue() throws IOException {
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockCtxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(mockParser.getValueAsString()).thenReturn("value");

        String[] result = deserializer.handleNonArray(mockParser, mockCtxt);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
        verify(mockCtxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(mockParser).getCurrentToken();
        verify(mockCtxt).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(mockParser).getValueAsString();
        verify(mockParser, never()).getText();
    }

    @Test
    public void testCreateContextual_withNullDeserializer_shouldReturnSameDeserializer() throws JsonMappingException {
        when(mockCtxt.findContextualValueDeserializer(any(), any())).thenReturn(null);

        JsonDeserializer<?> result = deserializer.createContextual(mockCtxt, null);

        assertEquals(deserializer, result);
        verify(mockCtxt).findContextualValueDeserializer(any(), any());
    }

    @Test
    public void testCreateContextual_withDefaultDeserializer_shouldReturnSameDeserializer() throws JsonMappingException {
        JsonDeserializer<?> mockDeser = mock(JsonDeserializer.class);
        when(mockCtxt.findContextualValueDeserializer(any(), any())).thenReturn(mockDeser);
        when(mockCtxt.handleSecondaryContextualization(any(), any())).thenReturn(mockDeser);
        when(deserializer.isDefaultDeserializer(eq(mockDeser))).thenReturn(true);

        JsonDeserializer<?> result = deserializer.createContextual(mockCtxt, null);

        assertEquals(deserializer, result);
        verify(mockCtxt).findContextualValueDeserializer(any(), any());
        verify(mockCtxt, never()).handleSecondaryContextualization(any(), any());
        verify(deserializer).isDefaultDeserializer(eq(mockDeser));
    }

    @Test
    public void testCreateContextual_withCustomDeserializer_shouldReturnNewDeserializer() throws JsonMappingException {
        JsonDeserializer<?> mockDeser = mock(JsonDeserializer.class);
        when(mockCtxt.findContextualValueDeserializer(any(), any())).thenReturn(mockDeser);
        when(mockCtxt.handleSecondaryContextualization(any(), any())).thenReturn(mockDeser);
        when(deserializer.isDefaultDeserializer(eq(mockDeser))).thenReturn(false);

        JsonDeserializer<?> result = deserializer.createContextual(mockCtxt, null);

        assertEquals(StringArrayDeserializer.class, result.getClass());
        verify(mockCtxt).findContextualValueDeserializer(any(), any());
        verify(mockCtxt).handleSecondaryContextualization(any(), any());
        verify(deserializer).isDefaultDeserializer(eq(mockDeser));
    }
}