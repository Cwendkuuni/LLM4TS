package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ContextualDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    private final JsonParser jp = mock(JsonParser.class);
    private final DeserializationContext ctxt = mock(DeserializationContext.class);
    private final ObjectBuffer buffer = mock(ObjectBuffer.class);
    private final JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
    private final JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(deserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(elementDeserializer.getNullValue()).thenReturn(null);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(buffer);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(deser);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(buffer);
        when(deser.deserialize(jp, ctxt)).thenReturn(null);

        String[] result = deserializer._deserializeCustom(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn(null);

        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jp.getText()).thenReturn("");
        when(ctxt.mappingException(String[].class)).thenReturn(new RuntimeException());

        String[] result = deserializer.handleNonArray(jp, ctxt);

        assertEquals(null, result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.constructType(String.class)).thenReturn(Mockito.mock(JavaType.class));
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), null)).thenReturn(null);
        when(ctxt.handleSecondaryContextualization(deser, null)).thenReturn(null);
        when(deserializer.isDefaultDeserializer(deser)).thenReturn(true);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);

        assertEquals(deserializer, result);
    }
}