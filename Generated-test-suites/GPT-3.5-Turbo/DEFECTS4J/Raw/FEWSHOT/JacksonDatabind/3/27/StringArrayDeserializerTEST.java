package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer deserializer;
    private ObjectBuffer buffer;
    
    @Mock
    private JsonParser jp;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private JsonDeserializer<String> elementDeserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deserializer = new StringArrayDeserializer(elementDeserializer);
        buffer = mock(ObjectBuffer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        Object[] chunk = new Object[]{"foo", "bar"};
        when(buffer.resetAndStart()).thenReturn(chunk);
        when(buffer.appendCompletedChunk(chunk)).thenReturn(chunk);
        when(buffer.completeAndClearBuffer(chunk, 2, String.class)).thenReturn(new String[]{"foo", "bar"});
        
        when(jp.nextToken())
            .thenReturn(JsonToken.VALUE_STRING)
            .thenReturn(JsonToken.VALUE_STRING)
            .thenReturn(JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("foo").thenReturn("bar");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
    
        verify(ctxt).leaseObjectBuffer();
        verify(jp, times(3)).nextToken();
        verify(jp, times(2)).getText();
        verify(buffer).resetAndStart();
        verify(buffer).appendCompletedChunk(chunk);
        verify(buffer).completeAndClearBuffer(chunk, 2, String.class);
        verify(ctxt).returnObjectBuffer(buffer);
        
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }
    
    @Test
    public void testDeserialize_CustomDeserializer() throws IOException {
        Object[] chunk = new Object[]{"foo", "bar"};
        when(buffer.resetAndStart()).thenReturn(chunk);
        when(buffer.appendCompletedChunk(chunk)).thenReturn(chunk);
        when(buffer.completeAndClearBuffer(chunk, 2, String.class)).thenReturn(new String[]{"foo", "bar"});
        
        when(jp.nextToken())
            .thenReturn(JsonToken.VALUE_STRING)
            .thenReturn(JsonToken.VALUE_STRING)
            .thenReturn(JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("foo").thenReturn("bar");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        verify(ctxt).leaseObjectBuffer();
        verify(jp, times(3)).nextToken();
        verify(elementDeserializer, times(2)).deserialize(jp, ctxt);
        verify(buffer).resetAndStart();
        verify(buffer).appendCompletedChunk(chunk);
        verify(buffer).completeAndClearBuffer(chunk, 2, String.class);
        verify(ctxt).returnObjectBuffer(buffer);
        
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }
    
    @Test
    public void testDeserialize_NullToken() throws IOException {
        Object[] chunk = new Object[]{"foo", "bar"};
        when(buffer.resetAndStart()).thenReturn(chunk);
        when(buffer.appendCompletedChunk(chunk)).thenReturn(chunk);
        when(buffer.completeAndClearBuffer(chunk, 2, String.class)).thenReturn(new String[]{"foo", "bar"});
        
        when(jp.nextToken())
            .thenReturn(JsonToken.VALUE_STRING)
            .thenReturn(JsonToken.VALUE_NULL)
            .thenReturn(JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("foo");
        when(elementDeserializer.getNullValue()).thenReturn(null);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        verify(ctxt).leaseObjectBuffer();
        verify(jp, times(3)).nextToken();
        verify(jp).getText();
        verify(elementDeserializer).getNullValue();
        verify(buffer).resetAndStart();
        verify(buffer).appendCompletedChunk(chunk);
        verify(buffer).completeAndClearBuffer(chunk, 2, String.class);
        verify(ctxt).returnObjectBuffer(buffer);
        
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertNull(result[1]);
    }
    
    @Test
    public void testDeserialize_NonArray() throws IOException {
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        verify(jp).isExpectedStartArrayToken();
        verify(ctxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(jp).getCurrentToken();
        verify(ctxt).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(jp).getText();
        verify(ctxt).mappingException(String[].class);
        
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test
    public void testDeserialize_NonArray_ZeroLengthString() throws IOException {
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        
        when(jp.getText()).thenReturn("");

        String[] result = deserializer.deserialize(jp, ctxt);
    
        verify(jp).isExpectedStartArrayToken();
        verify(ctxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(jp).getCurrentToken();
        verify(ctxt).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(jp).getText();
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        verify(typeDeserializer).deserializeTypedFromArray(jp, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = deserializer.deserialize(jp, ctxt);
        verify(ctxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(jp).getCurrentToken();
        assertNull(result[0]);
    }
    
    @Test
    public void testCreateContextual_DefaultDeserializer() throws JsonMappingException {
        JsonDeserializer<?> deser = deserializer.createContextual(ctxt, null);
        assertNull(deser);
    }
    
    @Test
    public void testCreateContextual_DifferentDeserializer() throws JsonMappingException {
        JsonDeserializer<?> otherDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<?> deser = deserializer.createContextual(ctxt, null);
        assertNotNull(deser);
        assertNotSame(deserializer, deser);
    }
    
    @Test
    public void testCreateContextual_SameDeserializer() throws JsonMappingException {
        JsonDeserializer<?> deser = deserializer.createContextual(ctxt, null);
        assertSame(deserializer, deser);
    }
}