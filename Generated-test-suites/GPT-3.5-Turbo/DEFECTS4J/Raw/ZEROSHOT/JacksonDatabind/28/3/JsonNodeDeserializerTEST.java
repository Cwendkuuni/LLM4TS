package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import java.io.IOException;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode result = deserializer.getNullValue(ctxt);
        assertNotNull(result);
        assertEquals(NullNode.class, result.getClass());
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode rootNode = new TextNode("RootNode");
        JsonParser p = null;
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertNotNull(result);
        assertEquals(JsonNode.class, result.getClass());
    }

    @Test
    public void testObjectDeserializerDeserialize() throws IOException {
        JsonNodeDeserializer.ObjectDeserializer deserializer = new JsonNodeDeserializer.ObjectDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertNotNull(result);
        assertEquals(ObjectNode.class, result.getClass());
    }

    @Test
    public void testArrayDeserializerDeserialize() throws IOException {
        JsonNodeDeserializer.ArrayDeserializer deserializer = new JsonNodeDeserializer.ArrayDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertNotNull(result);
        assertEquals(ArrayNode.class, result.getClass());
    }

}