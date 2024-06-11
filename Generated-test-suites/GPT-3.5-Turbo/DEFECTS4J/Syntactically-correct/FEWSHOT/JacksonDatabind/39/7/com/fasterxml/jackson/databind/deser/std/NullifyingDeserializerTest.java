package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        try {
            deserializer.deserialize(null, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        try {
            deserializer.deserializeWithType(null, null, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }
}