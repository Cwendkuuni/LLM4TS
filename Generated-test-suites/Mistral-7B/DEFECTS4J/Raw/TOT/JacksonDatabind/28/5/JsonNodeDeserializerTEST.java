package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonNodeDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeNull() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("null");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(p, null);
        Assertions.assertEquals(JsonNode.Value.NULL, node.getValueAsToken());
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(p, null);
        ObjectNode objNode = (ObjectNode) node;
        Assertions.assertEquals("John", objNode.get("name").asText());
        Assertions.assertEquals(30, objNode.get("age").asInt());
    }

    @Test
    public void testDeserializeObjectWithDuplicateField() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", \"age\":30, \"name\":\"Jane\"}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(p, null);
        ObjectNode objNode = (ObjectNode) node;
        Assertions.assertEquals("John", objNode.get("name").asText());
        Assertions.assertEquals(30, objNode.get("age").asInt());
        // No exception should be thrown due to the default handling of duplicate fields
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("[1, 2, 3]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(p, null);
        ArrayNode arrayNode = (ArrayNode) node;
        Assertions.assertEquals(3, arrayNode.size());
        Assertions.assertEquals(1, arrayNode.get(0).asInt());
        Assertions.assertEquals(2, arrayNode.get(1).asInt());
        Assertions.assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializeInvalidJson() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("{invalid_json");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ObjectNode objNode = MAPPER.readValue(p, ObjectNode.class);
        Assertions.assertNull(objNode);
    }

    @Test
    public void testDeserializeUnexpectedToken() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("{\"name\":\"John\", 30}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        Assertions.assertThrows(JsonMappingException.class, () -> deserializer.deserialize(p, null));
    }

    @Test
    public void testDeserializeArrayWithObjects() throws JsonProcessingException {
        JsonParser p = MAPPER.getFactory().createParser("[{\"name\":\"John\"}, {\"name\":\"Jane\"}]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(p, null);
        ArrayNode arrayNode = (ArrayNode) node;
        Assertions.assertEquals(2, arrayNode.size());
        ObjectNode firstObject = (ObjectNode) arrayNode.get(0);
        Assertions.assertEquals("John", firstObject.get("name").asText());
        ObjectNode secondObject = (ObjectNode) arrayNode.get(1);
        Assertions.assertEquals("Jane", secondObject.get("name").asText());
    }
}