package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    private DeserializationContext ctxt;
    private JsonParser jp;
    private ObjectBuffer buffer;
    private StringArrayDeserializer deserializer;
    private JsonDeserializer<String> elementDeserializer;

    @Before
    public void setUp() {
        ctxt = mock(DeserializationContext.class);
        jp = mock(JsonParser.class);
        buffer = mock(ObjectBuffer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        deserializer = new StringArrayDeserializer();
        elementDeserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testEmptyArray() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        String[] result = deserializer.deserialize(jp, ctxt, null);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeWithSingleValue() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("foo");
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testDeserializeWithNull() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("foo");
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("bar");
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("baz");

        deserializer = new StringArrayDeserializer(elementDeserializer);

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("{");
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object[] result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(new Object[]{new String[0]}, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("foo");

        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        when(ctxt.findValueDeserializer(String[].class)).thenReturn(deserializer);
        JsonDeserializer<String> contextualDeserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(String.class, null)).thenReturn(contextualDeserializer);

        JsonDeserializer<String[]> result = deserializer.createContextual(ctxt, null);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        JsonDeserializer<String[]> deserializerWithContextual = new StringArrayDeserializer(contextualDeserializer);
        assertEquals(deserializerWithContextual, result);
    }
}