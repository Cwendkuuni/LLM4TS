package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testObjectDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonNode deserializedNode = jsonNodeDeserializer.deserialize(parser, new ObjectMapper.DefaultDeserializationContext());
        ObjectNode objectNode = (ObjectNode) deserializedNode;
        assertEquals("John", objectNode.get("name").asText());
        assertEquals(30, objectNode.get("age").asInt());
    }

    @Test
    public void testArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(" [1, 2, 3]");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonNode deserializedNode = jsonNodeDeserializer.deserialize(parser, new ObjectMapper.DefaultDeserializationContext());
        ArrayNode arrayNode = (ArrayNode) deserializedNode;
        assertEquals(3, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializationOfUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"type\": \"Object\", \"value\": { \"name\": \"John\" } }");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode deserializedNode = jsonNodeDeserializer.deserialize(parser, new ObjectMapper.DefaultDeserializationContext());
        ObjectNode objectNode = (ObjectNode) deserializedNode;
        assertEquals("John", objectNode.get("value").get("name").asText());
    }

    @Test
    public void testNullDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode deserializedNode = jsonNodeDeserializer.deserialize(parser, new ObjectMapper.DefaultDeserializationContext());
        assertEquals(JsonNode.Value.nullValue(), deserializedNode);
    }
}