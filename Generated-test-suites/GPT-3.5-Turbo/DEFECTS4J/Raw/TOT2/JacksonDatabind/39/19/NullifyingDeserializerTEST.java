package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        
        JsonParser p = null; // mock JsonParser
        
        assertNull(deserializer.deserialize(p, null));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        
        JsonParser p = null; // mock JsonParser
        DeserializationContext ctxt = null; // mock DeserializationContext
        TypeDeserializer typeDeserializer = null; // mock TypeDeserializer
        
        assertEquals(null, deserializer.deserializeWithType(p, ctxt, typeDeserializer));
    }
}