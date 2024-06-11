package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(null, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        
        assertEquals(null, deserializer.deserializeWithType(jp, ctxt, typeDeserializer));
    }
    
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(null, deserializer.handleNonArray(jp, ctxt));
    }
    
    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        
        assertNull(deserializer.createContextual(ctxt, property));
    }
}