package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expectedResult = {"value1", "value2", "value3"};
        String[] result = deserializer.deserialize(null, null);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(null, null, null);
        assertNull(result);
    }

    @Test
    public void testContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(null, null);
        assertEquals(deserializer, result);
    }

}