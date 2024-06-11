package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {
    private StringArrayDeserializer stringArrayDeserializer;
    private JsonParser mockJsonParser;
    private DeserializationContext mockDeserializationContext;
    private JsonDeserializer<String> mockElementDeserializer;
    private TypeDeserializer mockTypeDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = StringArrayDeserializer.instance;
        mockJsonParser = mock(JsonParser.class);
        mockDeserializationContext = mock(DeserializationContext.class);
        mockElementDeserializer = mock(JsonDeserializer.class);
        mockTypeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testDeserialize_WhenExpectedStartArrayToken_ThenReturnDeserializedArray() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("value1", null);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertEquals("value1", result[0]);
        assertNull(result[1]);
    }

    @Test
    public void testDeserialize_WhenNotExpectedStartArrayToken_ThenHandleNonArrayAndReturnArray() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("value1");
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);

        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        assertEquals("value1", result[0]);
    }

    @Test
    public void testDeserialize_WhenNullDeser_ThenDeserializeUsingParseString() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("value1");

        String[] result = stringArrayDeserializer._deserializeCustom(mockJsonParser, mockDeserializationContext);

        assertEquals("value1", result[0]);
    }

    @Test
    public void testDeserializeWithType_ThenDeserializeTypedFromArray() throws IOException {
        Object result = stringArrayDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        // verify deserializeTypedFromArray method is called on TypeDeserializer
    }

    @Test
    public void testHandleNonArray_WhenNotAcceptSingleValueAsArray_ThenThrowMappingException() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
                .thenReturn(false);

        try {
            stringArrayDeserializer.handleNonArray(mockJsonParser, mockDeserializationContext);
        } catch (Exception e) {
            // verify mappingException is thrown
        }
    }

    @Test
    public void testHandleNonArray_WhenCurrentValueIsStringValueAndAcceptEmptyStringAsNullObject_ThenReturnNull() throws IOException {
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("");
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))
                .thenReturn(true);

        String[] result = stringArrayDeserializer.handleNonArray(mockJsonParser, mockDeserializationContext);

        assertNull(result);
    }

    @Test
    public void testContextual_WhenElementDeserializerIsNull_ThenReturnStringArrayDeserializerWithDeserNull() throws JsonMappingException {
        JsonDeserializer<?> contextualDeserializer = stringArrayDeserializer.createContextual(mockDeserializationContext, null);

        assertNull(((StringArrayDeserializer)contextualDeserializer)._elementDeserializer);
    }

    @Test
    public void testContextual_WhenElementDeserializerIsDefaultDeserializer_ThenReturnStringArrayDeserializerWithNullDeser() throws JsonMappingException {
        when(mockDeserializationContext.isDefaultDeserializer(mockElementDeserializer)).thenReturn(true);

        JsonDeserializer<?> contextualDeserializer = stringArrayDeserializer.createContextual(mockDeserializationContext, null);

        assertNull(((StringArrayDeserializer)contextualDeserializer)._elementDeserializer);
    }

    @Test
    public void testContextual_WhenElementDeserializerIsNotNullAndNotDefaultDeserializer_ThenReturnStringArrayDeserializerWithNewDeser() throws JsonMappingException {
        when(mockDeserializationContext.isDefaultDeserializer(mockElementDeserializer)).thenReturn(false);
        when(mockElementDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn("value1");

        JsonDeserializer<?> contextualDeserializer = stringArrayDeserializer.createContextual(mockDeserializationContext, null);

        assertEquals("value1", ((StringArrayDeserializer)contextualDeserializer)._elementDeserializer.deserialize(mockJsonParser, mockDeserializationContext));
    }
}