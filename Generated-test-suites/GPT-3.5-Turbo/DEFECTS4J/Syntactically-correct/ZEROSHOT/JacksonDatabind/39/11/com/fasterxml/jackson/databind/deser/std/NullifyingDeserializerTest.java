package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        assertNull(deserializer.deserializeWithType(null, null, null));
    }
}