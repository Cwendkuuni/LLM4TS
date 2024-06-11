package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() throws JsonProcessingException {
        JsonNode expectedObjectNode = ObjectMapper.getDefaultInstance().readValue("{\"name\": \"John Doe\"}", ObjectNode.class);
        JsonNode expectedArrayNode = ObjectMapper.getDefaultInstance().readValue("[1, 2, 3]", ArrayNode.class);

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode actualObjectNode = deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()));
        JsonNode actualArrayNode = deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ArrayNode(ObjectMapper.getDefaultInstance()));

        assertEquals(expectedObjectNode, actualObjectNode);
        assertEquals(expectedArrayNode, actualArrayNode);

        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actualObjectDeserializerObjectNode = objectDeserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ObjectNode(ObjectMapper.getDefaultInstance()));
        assertEquals(expectedObjectNode, actualObjectDeserializerObjectNode);

        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode actualArrayDeserializerArrayNode = arrayDeserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ArrayNode(ObjectMapper.getDefaultInstance()));
        assertEquals(expectedArrayNode, actualArrayDeserializerArrayNode);
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode actualNullNode = deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()));
        assertNull(actualNullNode);
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        JsonNode expectedObjectNode = ObjectMapper.getDefaultInstance().readValue("{\"name\": \"John Doe\"}", ObjectNode.class);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ObjectNode actualObjectNode = (ObjectNode) deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ObjectNode(ObjectMapper.getDefaultInstance()));

        assertEquals(expectedObjectNode, actualObjectNode);
        assertEquals(expectedObjectNode.get("name").asText(), actualObjectNode.get("name").asText());
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        JsonNode expectedArrayNode = ObjectMapper.getDefaultInstance().readValue("[1, 2, 3]", ArrayNode.class);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ArrayNode actualArrayNode = (ArrayNode) deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ArrayNode(ObjectMapper.getDefaultInstance()));

        assertEquals(expectedArrayNode, actualArrayNode);
        assertEquals(expectedArrayNode.size(), actualArrayNode.size());
        assertEquals(expectedArrayNode.get(0).asInt(), actualArrayNode.get(0).asInt());
        assertEquals(expectedArrayNode.get(1).asInt(), actualArrayNode.get(1).asInt());
        assertEquals(expectedArrayNode.get(2).asInt(), actualArrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializeEmptyObject() throws JsonProcessingException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ObjectNode actualEmptyObjectNode = (ObjectNode) deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ObjectNode(ObjectMapper.getDefaultInstance()));
        assertNotNull(actualEmptyObjectNode);
        assertEquals(0, actualEmptyObjectNode.size());
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ArrayNode actualEmptyArrayNode = (ArrayNode) deserializer.deserialize(JsonParserFactory.defaultParser(), new DeserializationContext(new ObjectMapper()), new ArrayNode(ObjectMapper.getDefaultInstance()));
        assertNotNull(actualEmptyArrayNode);
        assertEquals(0, actualEmptyArrayNode.size());
    }
}