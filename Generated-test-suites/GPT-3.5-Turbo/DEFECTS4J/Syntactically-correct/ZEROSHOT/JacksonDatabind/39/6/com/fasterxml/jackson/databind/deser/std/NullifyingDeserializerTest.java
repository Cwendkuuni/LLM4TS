package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; //initialize with dummy value
        DeserializationContext ctxt = null; //initialize with dummy value
        assertNull(nullifyingDeserializer.deserialize(jsonParser, ctxt));
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; //initialize with dummy value
        DeserializationContext ctxt = null; //initialize with dummy value
        TypeDeserializer typeDeserializer = null; //initialize with dummy value
        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, ctxt, typeDeserializer));
    }
}