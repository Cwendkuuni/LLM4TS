package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {

    private JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.getInstance();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = null;
        JsonNode nullValue = jsonNodeDeserializer.getNullValue(ctxt);
        assertTrue(nullValue instanceof NullNode);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = objectMapper.getFactory().createParser("{}");
        DeserializationContext ctxt = null;
        JsonDeserializer<?> typeDeserializer = null;
        JsonNode node = jsonNodeDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertTrue(node instanceof ObjectNode);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser p = objectMapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNode node = jsonNodeDeserializer.deserializeObject(p, ctxt, nodeFactory);
        assertTrue(node instanceof ObjectNode);
        assertEquals("value", node.get("key").asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser p = objectMapper.getFactory().createParser("[1,2,3]");
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNode node = jsonNodeDeserializer.deserializeArray(p, ctxt, nodeFactory);
        assertTrue(node instanceof ArrayNode);
        assertEquals(3, node.size());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonParser p = objectMapper.getFactory().createParser("null");
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNode node = jsonNodeDeserializer.deserializeAny(p, ctxt, nodeFactory);
        assertTrue(node instanceof NullNode);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeAnyInvalidToken() throws IOException {
        JsonParser p = objectMapper.getFactory().createParser("{");
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        jsonNodeDeserializer.deserializeAny(p, ctxt, nodeFactory);
    }

    @Test
    public void testDeserializeObjectWithDuplicateFields() throws IOException {
        JsonParser p = objectMapper.getFactory().createParser("{\"key\":\"value\", \"key\":\"value2\"}");
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNode node = jsonNodeDeserializer.deserializeObject(p, ctxt, nodeFactory);
        assertTrue(node instanceof ObjectNode);
        assertEquals("value2", node.get("key").asText());
    }
}