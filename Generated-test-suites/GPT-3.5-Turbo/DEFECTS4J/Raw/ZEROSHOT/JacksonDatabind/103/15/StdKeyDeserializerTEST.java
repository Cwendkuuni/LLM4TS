package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import junit.framework.TestCase;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class StdKeyDeserializerTest extends TestCase {

    public void testDeserializeKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class, null);
        DeserializationContext context = new DeserializationContext(null, null);
        TokenBuffer tokenBuffer = new TokenBuffer(null);
        String key = "testKey";
        assertEquals(key, keyDeserializer.deserializeKey(key, context));
    }

    public void testDeserializeKeyWithNull() throws IOException, JsonProcessingException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class, null);
        DeserializationContext context = new DeserializationContext(null, null);
        String key = null;
        assertEquals(null, keyDeserializer.deserializeKey(key, context));
    }

    public void testDeserializeKeyWithInvalidValue() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Integer.class, null);
        DeserializationContext context = new DeserializationContext(null, null);
        TokenBuffer tokenBuffer = new TokenBuffer(null);
        String key = "testKey";
        assertEquals(null, keyDeserializer.deserializeKey(key, context));
    }
}