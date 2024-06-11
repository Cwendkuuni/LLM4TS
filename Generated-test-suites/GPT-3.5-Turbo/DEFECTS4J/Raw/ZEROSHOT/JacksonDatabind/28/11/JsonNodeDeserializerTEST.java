package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {

    private final JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();

    @Test
    public void testGetNullValue() {
        JsonNode jsonNode = jsonNodeDeserializer.getNullValue(null);
        assertNull(jsonNode);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser jsonParser = jsonFactory.createParser("{ \"name\" : \"John\", \"age\" : 30 }");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

        ObjectNode objectNode = jsonNodeDeserializer.deserializeObject(jsonParser, ctxt, nodeFactory);

        assertNotNull(objectNode);
        assertEquals("John", objectNode.get("name").asText());
        assertEquals(30, objectNode.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser jsonParser = jsonFactory.createParser("[ \"apple\", \"banana\", \"cherry\" ]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

        ArrayNode arrayNode = jsonNodeDeserializer.deserializeArray(jsonParser, ctxt, nodeFactory);

        assertNotNull(arrayNode);
        assertEquals("apple", arrayNode.get(0).asText());
        assertEquals("banana", arrayNode.get(1).asText());
        assertEquals("cherry", arrayNode.get(2).asText());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser jsonParser = jsonFactory.createParser("{ \"name\" : \"John\", \"age\" : 30 }");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

        JsonNode jsonNode = jsonNodeDeserializer.deserializeAny(jsonParser, ctxt, nodeFactory);

        assertNotNull(jsonNode);
        assertTrue(jsonNode.isObject());
        assertEquals("John", jsonNode.get("name").asText());
        assertEquals(30, jsonNode.get("age").asInt());
    }
}