package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("");
        DeserializationContext context = new DeserializationContext(null, null);
        Object result = deserializer.deserialize(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("");
        DeserializationContext context = new DeserializationContext(null, null);
        TypeDeserializer typeDeserializer = null;
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        assertEquals(null, result);
    }
}