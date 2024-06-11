package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

public class JsonNodeDeserializerTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testGetDeserializer_ObjectNode() throws IOException {
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonDeserializer<? extends JsonNode> deserializer = jsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode objectNode = (ObjectNode) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        Assert.assertEquals("John", objectNode.get("name").asText());
        Assert.assertEquals(30, objectNode.get("age").asInt());
        Assert.assertEquals("New York", objectNode.get("city").asText());
    }

    @Test
    public void testGetDeserializer_ArrayNode() throws IOException {
        String json = "[{\"name\":\"John\"}, {\"name\":\"Doe\"}, {\"name\":\"Smith\"}]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonDeserializer<? extends JsonNode> deserializer = jsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode arrayNode = (ArrayNode) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        Assert.assertEquals(3, arrayNode.size());
        Iterator<JsonNode> iterator = arrayNode.elements();
        Assert.assertEquals("John", iterator.next().get("name").asText());
        Assert.assertEquals("Doe", iterator.next().get("name").asText());
        Assert.assertEquals("Smith", iterator.next().get("name").asText());
    }

    @Test
    public void testGetDeserializer_GenericNode() throws IOException {
        String json = "{\"name\":\"John\", \"age\":30, \"address\":{\"city\":\"New York\"}}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonDeserializer<? extends JsonNode> deserializer = jsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode jsonNode = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        Assert.assertTrue(jsonNode.isObject());
        Assert.assertEquals("John", jsonNode.get("name").asText());
        Assert.assertEquals(30, jsonNode.get("age").asInt());
        Assert.assertEquals("New York", jsonNode.get("address").get("city").asText());
    }
}