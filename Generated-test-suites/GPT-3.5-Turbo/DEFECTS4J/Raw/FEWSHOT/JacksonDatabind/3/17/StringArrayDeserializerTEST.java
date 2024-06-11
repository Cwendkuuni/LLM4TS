package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"foo", "bar"};
        assertEquals(expected, deserializer.deserialize(jsonParser, deserializationContext));

        expected = null;
        assertEquals(expected, deserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"foo"};
        assertEquals(expected, deserializer.handleNonArray(jsonParser, deserializationContext));

        expected = null;
        assertEquals(expected, deserializer.handleNonArray(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object expected = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(expected);
    }

    @Test
    public void testContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> expected = deserializer.createContextual(deserializationContext, property);
        assertNull(expected);
    }
}