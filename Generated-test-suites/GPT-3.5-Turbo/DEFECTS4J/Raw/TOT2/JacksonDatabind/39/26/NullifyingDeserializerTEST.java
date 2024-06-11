package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonToken token = JsonToken.START_ARRAY;
        assertEquals(null, deserializer.deserializeWithType(null, mapper.getDeserializationContext(), null));
    }

    // Edge case - testing for non-null field name
    @Test
    public void testDeserializeWithNonFieldName() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonToken token = JsonToken.FIELD_NAME;
        assertEquals(null, deserializer.deserializeWithType(null, mapper.getDeserializationContext(), null));
    }

    // Testing for IOException in deserialize method
    @Test
    public void testDeserializeIOException() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(IOException.class, () -> {
            deserializer.deserialize(null, mapper.getDeserializationContext());
        });
    }

    // Testing for IOException in deserializeWithType method
    @Test
    public void testDeserializeWithTypeIOException() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(IOException.class, () -> {
            deserializer.deserializeWithType(null, mapper.getDeserializationContext(), null);
        });
    }
}