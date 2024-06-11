package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringArrayDeserializerTest {
    
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    public void testDeserialize_withEmptyArray_returnsEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_withCustomDeserialization_methodCalled() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(deserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value");
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("value");
        when(ctxt.leaseObjectBuffer()).thenReturn(mock(ObjectBuffer.class));
        
        String[] result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }
    
    @Test(expected = com.fasterxml.jackson.databind.exc.MismatchedInputException.class)
    public void testDeserialize_withNonArray_whenFeatureDisabled_throwsException() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testDeserialize_withNonArray_whenFeatureEnabled_returnsArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
    }
    
    @Test
    public void testDeserializeWithType_withCustomDeserializer_methodCalled() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(deserializer._elementDeserializer).thenReturn(elementDeserializer);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value");
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("value");
        when(ctxt.leaseObjectBuffer()).thenReturn(mock(ObjectBuffer.class));
        
        String[] result = (String[]) deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }
    
    @Test
    public void testDeserialize_withNonArray_whenAcceptEmptyStringAsNullEnabled_returnsNull() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jp.getText()).thenReturn("");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNull(result);
    }
}