package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.*;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }

}