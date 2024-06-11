package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        // Setup
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        // Test
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        // Setup
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        // Test
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void test_deserializeCustom() throws IOException {
        // Setup
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        // Test
        String[] result = stringArrayDeserializer._deserializeCustom(jp, ctxt);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        // Setup
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        // Test
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        // Setup
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        // Test
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(ctxt, property);
        
        // Verify
        assertEquals(stringArrayDeserializer, result);
    }
}