package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    
    private NullifyingDeserializer nullifyingDeserializer;
    
    @Before
    public void setUp() {
        nullifyingDeserializer = NullifyingDeserializer.instance;
    }
    
    @Test
    public void testDeserialize() throws IOException {
        DeserializationContext ctxt = null;
        JsonParser p = null; // Replace with valid JsonParser object
        
        Object result = nullifyingDeserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        DeserializationContext ctxt = null;
        JsonParser p = null; // Replace with valid JsonParser object
        TypeDeserializer typeDeserializer = null; // Replace with valid TypeDeserializer object
        
        Object result = nullifyingDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }
}