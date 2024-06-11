package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);

        Boolean result = (Boolean) deserializer.deserializeKey("true", new DeserializationContext(null));

        assertEquals(true, result.booleanValue());
    }

    @Test
    public void testDeserializeKeyWithNullKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);

        Object result = deserializer.deserializeKey(null, new DeserializationContext(null));

        assertNull(result);
    }

    @Test
    public void testDeserializeKeyWithInvalidKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);

        Object result = deserializer.deserializeKey("abc", new DeserializationContext(null));

        assertNull(result);
    }
}