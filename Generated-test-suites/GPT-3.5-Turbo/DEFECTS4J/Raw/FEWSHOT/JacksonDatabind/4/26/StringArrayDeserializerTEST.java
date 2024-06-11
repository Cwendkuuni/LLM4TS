package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringArrayDeserializerTest {
    private StringArrayDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;

    @Before
    public void setup() {
        deserializer = new StringArrayDeserializer();
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserialize() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(parser.getText()).thenReturn("foo");

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(1, result.length);
        assertEquals("foo", result[0]);
        verify(context).returnObjectBuffer(any());
    }

    @Test
    public void testDeserializeWithNull() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(1, result.length);
        assertNull(result[0]);
        verify(context).returnObjectBuffer(any());
    }

    @Test
    public void testDeserializeNonArray() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("foo");

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(1, result.length);
        assertEquals("foo", result[0]);
        verify(context, never()).returnObjectBuffer(any());
    }

    @Test(expected = Exception.class)
    public void testDeserializeNonArrayWithoutAcceptSingleValueAsArray() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("foo");

        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeNonArrayWithEmptyStringAsNullObject() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("");

        String[] result = deserializer.deserialize(parser, context);

        assertNull(result);
        verify(context, never()).returnObjectBuffer(any());
    }

    @Test
    public void testDeserializeNonArrayWithEmptyStringAsNonNullObject() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("");

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(1, result.length);
        assertEquals("", result[0]);
        verify(context, never()).returnObjectBuffer(any());
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        deserializer.deserializeWithType(parser, context, typeDeserializer);

        verify(typeDeserializer).deserializeTypedFromArray(parser, context);
    }

    @Test
    public void testCreateContextualWhenElementDeserializerIsNull() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(String.class), eq(property))).thenReturn(valueDeserializer);
        when(deserializer.isDefaultDeserializer(valueDeserializer)).thenReturn(true);

        deserializer.createContextual(context, property);

        verify(property, never()).getContextualType();
        verify(deserializer, never()).findConvertingContentDeserializer(eq(context), eq(property), eq(valueDeserializer));
        verify(context, never()).handleSecondaryContextualization(eq(valueDeserializer), eq(property));
    }

    @Test
    public void testCreateContextualWhenElementDeserializerIsNotNull() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(String.class), eq(property))).thenReturn(valueDeserializer);
        when(deserializer.isDefaultDeserializer(valueDeserializer)).thenReturn(false);

        JsonDeserializer<?> result = deserializer.createContextual(context, property);

        assertEquals(deserializer, result);
        verify(property, never()).getContextualType();
        verify(deserializer, never()).findConvertingContentDeserializer(eq(context), eq(property), eq(valueDeserializer));
        verify(context, never()).handleSecondaryContextualization(eq(valueDeserializer), eq(property));
    }
}