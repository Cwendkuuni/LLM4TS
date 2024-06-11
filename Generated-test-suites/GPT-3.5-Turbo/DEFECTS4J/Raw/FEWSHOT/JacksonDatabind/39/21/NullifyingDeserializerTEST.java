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
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = null; // replace with valid JsonParser object
        DeserializationContext context = null; // replace with valid DeserializationContext object

        Object result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = null; // replace with valid JsonParser object
        DeserializationContext context = null; // replace with valid DeserializationContext object
        TypeDeserializer typeDeserializer = null; // replace with valid TypeDeserializer object

        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        assertNull(result);
    }
}