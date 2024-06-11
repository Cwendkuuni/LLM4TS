package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        NullifyingDeserializer instance = new NullifyingDeserializer();
        assertNull(instance.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeWithType() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        NullifyingDeserializer instance = new NullifyingDeserializer();
        assertNull(instance.deserializeWithType(p, ctxt, typeDeserializer));
    }
    
}