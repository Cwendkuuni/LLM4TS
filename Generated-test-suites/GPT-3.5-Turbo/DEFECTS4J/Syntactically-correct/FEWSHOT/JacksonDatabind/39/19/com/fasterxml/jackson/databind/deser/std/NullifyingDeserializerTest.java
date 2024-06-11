package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // your implementation here
        DeserializationContext deserializationContext = null; // your implementation here
        assertEquals(null, nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // your implementation here
        DeserializationContext deserializationContext = null; // your implementation here
        TypeDeserializer typeDeserializer = null; // your implementation here
        assertEquals(null, nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }
}