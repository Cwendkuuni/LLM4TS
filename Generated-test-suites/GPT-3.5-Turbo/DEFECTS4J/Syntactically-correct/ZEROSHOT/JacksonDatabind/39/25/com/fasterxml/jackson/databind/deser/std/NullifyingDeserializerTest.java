package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserializeWithType(null, null, null));
    }

}