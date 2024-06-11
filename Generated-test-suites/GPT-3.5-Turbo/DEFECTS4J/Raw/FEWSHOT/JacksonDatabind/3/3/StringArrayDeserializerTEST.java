package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("foo", "bar");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("foo", "bar");
        
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        
        try {
            deserializer.handleNonArray(jp, ctxt);
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize value of type [Ljava.lang.String; from Token.START_OBJECT", e.getMessage());
        }
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(elementDeserializer);
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertEquals(deserializer, result);
    }
}