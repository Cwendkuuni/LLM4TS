package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;
import org.mockito.Mockito;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        // Setup
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ObjectBuffer buffer = Mockito.mock(ObjectBuffer.class);
        JsonToken t = JsonToken.VALUE_STRING;

        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(t, t, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("foo");

        assertNull(StringArrayDeserializer.instance._elementDeserializer);
        assertEquals("foo", StringArrayDeserializer.instance.deserialize(jp, ctxt));

        verify(ctxt, times(1)).leaseObjectBuffer();
        verify(ctxt, times(1)).returnObjectBuffer(buffer);
        verify(buffer, times(2)).resetAndStart();
        verify(buffer, times(1)).appendCompletedChunk(Mockito.any(Object[].class));
        verify(buffer, times(1)).completeAndClearBuffer(Mockito.any(Object[].class), Mockito.anyInt(), Mockito.any(Class.class));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        // Setup
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        StringArrayDeserializer instance = mock(StringArrayDeserializer.class);
        when(instance.deserializeWithType(jp, ctxt, typeDeserializer)).thenCallRealMethod();
        assertEquals(typeDeserializer.deserializeTypedFromArray(jp, ctxt), instance.deserializeWithType(jp, ctxt, typeDeserializer));

        verify(typeDeserializer, times(1)).deserializeTypedFromArray(jp, ctxt);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        // Setup
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        // testThrowMappingException
        when(jp.getCurrentToken()).thenReturn(JsonToken.NOT_AVAILABLE);
        try {
            StringArrayDeserializer.instance.handleNonArray(jp, ctxt);
        } catch (Exception e) {
            assertEquals(ctxt.mappingException(String[].class), e);
        }

        // testEmptyStringAsNull
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        assertNull(StringArrayDeserializer.instance.handleNonArray(jp, ctxt));
    }

    @Test
    public void testCreateContextual() throws IOException {
        // Setup
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        JsonDeserializer<?> defaultDeser = Mockito.mock(JsonDeserializer.class);
        JsonDeserializer<?> result;

        // testNoDeserAndDefaultDeser
        StringArrayDeserializer instance1 = new StringArrayDeserializer();
        instance1._elementDeserializer = null;
        when(ctxt.findContextualValueDeserializer(Mockito.any(Class.class), Mockito.any(BeanProperty.class))).thenReturn((JsonDeserializer<?>) null);
        when(instance1.isDefaultDeserializer(deser)).thenReturn(true);
        result = instance1.createContextual(ctxt, property);
        assertNull(result);

        // testNoDeserAndNonDefaultDeser
        instance1._elementDeserializer = null;
        when(ctxt.findContextualValueDeserializer(Mockito.any(Class.class), Mockito.any(BeanProperty.class))).thenReturn(deser);
        when(instance1.isDefaultDeserializer(deser)).thenReturn(false);
        when(ctxt.handleSecondaryContextualization(deser, property)).thenReturn(deser);
        result = instance1.createContextual(ctxt, property);
        assertEquals(instance1, result);

        // testDeserAndDefaultDeser
        StringArrayDeserializer instance2 = spy(new StringArrayDeserializer(deser));
        when(instance2.isDefaultDeserializer(deser)).thenReturn(true);
        result = instance2.createContextual(ctxt, property);
        assertNull(result);

        // testDeserAndNonDefaultDeser
        instance2._elementDeserializer = deser;
        when(instance2.isDefaultDeserializer(deser)).thenReturn(false);
        when(ctxt.handleSecondaryContextualization(deser, property)).thenReturn(deser);
        result = instance2.createContextual(ctxt, property);
        assertEquals(instance2, result);

        verify(ctxt, times(2)).findContextualValueDeserializer(Mockito.any(Class.class), Mockito.any(BeanProperty.class));
        verify(ctxt, times(1)).handleSecondaryContextualization(deser, property);
    }
}