package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue(null);
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        try {
            JsonNode result = deserializer.deserialize(jsonParser, deserializationContext);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testObjectDeserializerDeserialize() {
        JsonNodeDeserializer.ObjectDeserializer deserializer = new JsonNodeDeserializer.ObjectDeserializer();
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        try {
            ObjectNode result = deserializer.deserialize(jsonParser, deserializationContext);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testArrayDeserializerDeserialize() {
        JsonNodeDeserializer.ArrayDeserializer deserializer = new JsonNodeDeserializer.ArrayDeserializer();
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        try {
            ArrayNode result = deserializer.deserialize(jsonParser, deserializationContext);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}