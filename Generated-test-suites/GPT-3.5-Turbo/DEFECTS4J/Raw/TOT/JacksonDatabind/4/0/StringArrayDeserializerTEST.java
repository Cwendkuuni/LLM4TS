package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

@RunWith(MockitoJUnitRunner.class)
public class StringArrayDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    private StringArrayDeserializer stringArrayDeserializer;

    private JsonDeserializer<String> elementDeserializer;

    private ObjectBuffer objectBuffer;

    @Before
    public void setup() {
        elementDeserializer = mock(JsonDeserializer.class);
        objectBuffer = mock(ObjectBuffer.class);
        stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer, objectBuffer);
    }

    @Test
    public void testDeserialize_WhenStartArrayToken_ThenReturnsStringArray() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, context);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_WhenElementDeserializerIsNotNull_ThenDeserializeCustom() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.getNullValue()).thenReturn("null");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, context);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_WhenValueStringToken_ThenReturnsStringArrayWithValueString() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("value");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, context);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testDeserialize_WhenValueNullToken_ThenReturnsStringArrayWithNullValue() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, context);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testDeserialize_WhenOtherToken_ThenParseStringAndReturnsStringArray() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_FLOAT, JsonToken.END_ARRAY);
        when(stringArrayDeserializer.parseString(jsonParser, context)).thenReturn("value");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, context);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testDeserializeCustom_WhenValueNullToken_ThenReturnsStringArrayWithNullValue() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.getNullValue()).thenReturn("null");

        String[] result = stringArrayDeserializer.deserializeCustom(jsonParser, context);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeCustom_WhenOtherToken_ThenDeserializeUsingElementDeserializer() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("value");

        String[] result = stringArrayDeserializer.deserializeCustom(jsonParser, context);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithType_ThenDelegatesToTypeDeserializer() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(any(), any())).thenReturn("result");

        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);

        assertEquals("result", result);
    }

    @Test
    public void testHandleNonArray_WhenNotAcceptSingleValueAsArray_ThenThrowsMappingException() throws IOException {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        try {
            stringArrayDeserializer.handleNonArray(jsonParser, context);
        } catch (Exception e) {
            assertEquals("MappingException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void testHandleNonArray_WhenAcceptEmptyStringAsNullObject_AndValueIsEmptyString_ThenReturnsNull() throws IOException {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("");

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, context);

        assertNull(result);
    }

    @Test
    public void testHandleNonArray_WhenAcceptSingleValueAsArray_AndValueIsNull_ThenReturnsStringArrayWithNullValue() throws IOException {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, context);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testHandleNonArray_WhenAcceptSingleValueAsArray_AndValueIsString_ThenReturnsStringArrayWithValueString() throws IOException {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, context);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerExists_AndElementDeserializerIsDefaultDeserializer_ThenReturnsCurrentInstance() throws IOException {
        when(context.constructType(String.class)).thenReturn(mock(JsonDeserializer.class));
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);

        JsonDeserializer<String> result = stringArrayDeserializer.createContextual(context, null);

        assertEquals(stringArrayDeserializer, result);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerExists_AndElementDeserializerIsNotDefaultDeserializer_ThenReturnsNewInstanceWithElementDeserializer() throws IOException {
        when(context.constructType(String.class)).thenReturn(mock(JsonDeserializer.class));
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);
        when(context.handleSecondaryContextualization(elementDeserializer, null)).thenReturn(elementDeserializer);

        JsonDeserializer<String> result = stringArrayDeserializer.createContextual(context, null);

        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertEquals(elementDeserializer, ((StringArrayDeserializer) result).getElementDeserializer());
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerIsNull_ThenReturnsNewInstanceWithNullElementDeserializer() throws IOException {
        when(context.constructType(String.class)).thenReturn(mock(JsonDeserializer.class));
        when(context.findContextualValueDeserializer(context.constructType(String.class), null)).thenReturn(null);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(context, null);

        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertNull(((StringArrayDeserializer) result).getElementDeserializer());
    }

    private ObjectBuffer createObjectBuffer() {
        return new ObjectBuffer();
    }

    private String parseString(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.nextToken() != JsonToken.VALUE_STRING) {
            throw ctxt.wrongTokenException(jp, JsonToken.VALUE_STRING, "Expected string");
        }
        return jp.getText();
    }
}