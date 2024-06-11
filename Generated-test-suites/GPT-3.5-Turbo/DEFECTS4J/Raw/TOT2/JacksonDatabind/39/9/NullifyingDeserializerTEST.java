package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        // Typical use case - Verify that deserialize method returns null
        assertNull(deserializer.deserialize(null, null));
        // Test with null JsonParser - Should return null
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        // Typical use case - Verify that deserializeWithType method returns null
        assertNull(deserializer.deserializeWithType(null, null, null));
        // Edge case - Test with different token ids
        assertEquals(null, deserializer.deserializeWithType(new JsonFactory().createParser("null"), null, null));
    }
}