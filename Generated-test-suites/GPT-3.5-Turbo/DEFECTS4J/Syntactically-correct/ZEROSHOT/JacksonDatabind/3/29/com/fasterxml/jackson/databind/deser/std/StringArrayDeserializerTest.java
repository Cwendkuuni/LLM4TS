package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // fill in with a valid JsonParser instance
        DeserializationContext ctxt = null; // fill in with a valid DeserializationContext instance
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // assert statements
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeCustom() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // fill in with a valid JsonParser instance
        DeserializationContext ctxt = null; // fill in with a valid DeserializationContext instance
        
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        // assert statements
        assertNotNull(result);
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // fill in with a valid JsonParser instance
        DeserializationContext ctxt = null; // fill in with a valid DeserializationContext instance
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        // assert statements
        assertNotNull(result);
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = null; // fill in with a valid DeserializationContext instance
        BeanProperty property = null; // fill in with a valid BeanProperty instance
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        // assert statements
        assertNotNull(result);
    }
}