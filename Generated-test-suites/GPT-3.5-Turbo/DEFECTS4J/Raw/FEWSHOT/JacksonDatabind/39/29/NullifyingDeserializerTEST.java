package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        JsonParser p = null; // Create a JsonParser object
        DeserializationContext ctxt = null; // Create a DeserializationContext object
        
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        assertEquals(null, deserializer.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeWithType() {
        JsonParser p = null; // Create a JsonParser object
        DeserializationContext ctxt = null; // Create a DeserializationContext object
        TypeDeserializer typeDeserializer = null; // Create a TypeDeserializer object
        
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        assertNull(deserializer.deserializeWithType(p, ctxt, typeDeserializer));
    }
}