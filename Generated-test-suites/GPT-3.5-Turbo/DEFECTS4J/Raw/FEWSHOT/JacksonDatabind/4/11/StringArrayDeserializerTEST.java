package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    @Test
    public void testStringArrayDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertEquals(String[].class, deserializer.handledType());
        assertNull(deserializer.getNullValue());
    }

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);

        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(deserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(buffer.appendCompletedChunk(new Object[1])).thenReturn(new Object[1]);
        when(buffer.completeAndClearBuffer(new Object[1], 1, String.class)).thenReturn(new String[1]);
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(null);

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[1], result);
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
        } catch (Exception e) {
            assertEquals("com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer", e.getStackTrace()[0].getClassName());
        }
    }

    @Test
    public void testCreateContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);
    }
}