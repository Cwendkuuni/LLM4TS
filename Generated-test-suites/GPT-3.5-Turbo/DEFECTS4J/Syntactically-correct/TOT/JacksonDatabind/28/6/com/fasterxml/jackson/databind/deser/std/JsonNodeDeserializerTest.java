package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserialize_ObjectNode() throws IOException {
        // Arrange
        String json = "{\"key\":\"value\"}";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act
        ObjectNode result = (ObjectNode) JsonNodeDeserializer.instance.deserialize(parser, context);

        // Assert
        assertEquals("value", result.get("key").asText());
    }

    @Test
    public void testDeserialize_ArrayNode() throws IOException {
        // Arrange
        String json = "[1,2,3]";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act
        ArrayNode result = (ArrayNode) JsonNodeDeserializer.instance.deserialize(parser, context);

        // Assert
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test
    public void testDeserialize_NullNode() throws IOException {
        // Arrange
        String json = "null";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act
        JsonNode result = JsonNodeDeserializer.instance.deserialize(parser, context);

        // Assert
        assertEquals(NullNode.instance, result);
    }

    @Test
    public void testDeserialize_EmptyJsonObjectNode() throws IOException {
        // Arrange
        String json = "{}";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act
        ObjectNode result = (ObjectNode) JsonNodeDeserializer.instance.deserialize(parser, context);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_EmptyJsonArrayNode() throws IOException {
        // Arrange
        String json = "[]";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act
        ArrayNode result = (ArrayNode) JsonNodeDeserializer.instance.deserialize(parser, context);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        // Arrange
        String json = "{key\":\"value\"}";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act & Assert
        JsonNodeDeserializer.instance.deserialize(parser, context);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidJsonArray() throws IOException {
        // Arrange
        String json = "[1,2";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act & Assert
        JsonNodeDeserializer.instance.deserialize(parser, context);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidJsonNode() throws IOException {
        // Arrange
        String json = "\"text\"";
        ObjectMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();

        // Act & Assert
        JsonNodeDeserializer.instance.deserialize(parser, context);
    }
}