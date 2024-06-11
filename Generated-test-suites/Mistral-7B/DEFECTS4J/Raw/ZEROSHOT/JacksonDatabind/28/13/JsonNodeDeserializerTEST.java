package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseTest;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

@Test
public class JsonNodeDeserializerTest extends BaseTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @BeforeMethod
    public void setup() throws JsonMappingException {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
    }

    @Test
    public void testNullNodeDeserialization() throws IOException {
        JsonParser jsonParser = jsonFactory.getParser("");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getClass(), JsonNode.class);
        Assert.assertNull(node);
    }

    @Test
    public void testEmptyObjectNodeDeserialization() throws IOException {
        JsonParser jsonParser = jsonFactory.getParser("{}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(node.getClass(), ObjectNode.class);
        Assert.assertNotNull(node.getFieldNames());
        Assert.assertTrue(node.getFieldNames().isEmpty());
    }

    @Test
    public void testEmptyArrayNodeDeserialization() throws IOException {
        JsonParser jsonParser = jsonFactory.getParser("[ ]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(node.getClass(), ArrayNode.class);
        Assert.assertFalse(node.isEmpty());
        Assert.assertTrue(node.size() == 0);
    }

    @Test
    public void testDeserializationOfObjectNode() throws IOException {
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        JsonParser jsonParser = jsonFactory.getParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(node.getClass(), ObjectNode.class);

        Assert.assertEquals(node.get("name").asText(), "John");
        Assert.assertEquals(node.get("age").asInt(), 30);
        Assert.assertEquals(node.get("city").asText(), "New York");
    }

    @Test
    public void testDeserializationOfArrayNode() throws IOException {
        String json = "[1, 2, 3, 4, 5]";
        JsonParser jsonParser = jsonFactory.getParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(node.getClass(), ArrayNode.class);

        Assert.assertEquals(node.get(0).asInt(), 1);
        Assert.assertEquals(node.get(1).asInt(), 2);
        Assert.assertEquals(node.get(2).asInt(), 3);
        Assert.assertEquals(node.get(3).asInt(), 4);
        Assert.assertEquals(node.get(4).asInt(), 5);
        Assert.assertEquals(node.size(), 5);
    }

    @Test
    public void testDeserializationOfAnyNode() throws IOException {
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        JsonParser jsonParser = jsonFactory.getParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(node.getClass(), ObjectNode.class);

        JsonParser newParser = jsonFactory.getParser(json);
        JsonDeserializer<ObjectNode> objectNodeDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode newNode = (ObjectNode) objectNodeDeserializer.deserialize(newParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(node, newNode);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        JsonParser jsonParser = jsonFactory.getParser("{name: John}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode node = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }
}