package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        // Typical use case
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        // Typical use case
        assertNull(deserializer.deserializeWithType(null, null, null));
    }

    @Test
    public void testDeserialize_CheckNullForInvalidTokenId() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonToken invalidToken = new JsonToken(JsonTokenId.ID_NOT_AVAILABLE, "<invalid>");
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType_CheckTypeDeserializationForObjectToken() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        p.setCurrentTokenId(JsonTokenId.ID_START_OBJECT);
        assertNotNull(deserializer.deserializeWithType(p, ctxt, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_CheckNullForUnsupportedToken() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        p.setCurrentTokenId(JsonTokenId.ID_END_ARRAY);
        assertNull(deserializer.deserializeWithType(p, ctxt, typeDeserializer));
    }
}