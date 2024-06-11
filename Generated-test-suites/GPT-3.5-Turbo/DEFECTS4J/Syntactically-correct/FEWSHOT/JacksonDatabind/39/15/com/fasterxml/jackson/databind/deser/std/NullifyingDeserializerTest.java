package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        // Test with different tokens
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        // Test with different tokens
        assertNull(deserializer.deserializeWithType(null, null, null));
    }
}