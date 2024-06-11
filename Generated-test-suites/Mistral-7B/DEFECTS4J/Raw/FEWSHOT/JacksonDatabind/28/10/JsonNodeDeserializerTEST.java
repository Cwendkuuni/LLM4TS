package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        assertEquals(JsonNodeDeserializer.class, deserializer.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDeserializerNullClass() {
        JsonNodeDeserializer.getDeserializer(null);
    }

    @Test
    public void testGetDeserializerObjectNode() {
        JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertEquals(ObjectDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testGetDeserializerArrayNode() {
        JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertEquals(ArrayDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonParser parser = createParser("");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(parser, null);
        assertNull(node);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonParser parser = createParser("\"\"");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(parser, null);
        assertEquals(JsonNode.class, node.getClass());
        assertTrue(node.isNull());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser parser = createParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(parser, null);
        assertEquals(ObjectNode.class, node.getClass());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals("John", objNode.get("name").asText());
        assertEquals(30, objNode.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String json = "[1,2,3]";
        JsonParser parser = createParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(parser, null);
        assertEquals(ArrayNode.class, node.getClass());
        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(3, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializeObjectWithEmbeddedArray() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        JsonParser parser = createParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(parser, null);
        assertEquals(ObjectNode.class, node.getClass());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals("John", objNode.get("name").asText());
        assertEquals(30, objNode.get("age").asInt());
        ArrayNode hobbiesNode = (ArrayNode) objNode.get("hobbies");
        assertEquals(2, hobbiesNode.size());
        assertEquals("reading", hobbiesNode.get(0).asText());
        assertEquals("swimming", hobbiesNode.get(1).asText());
    }

    @Test
    public void testDeserializeArrayWithEmbeddedObject() throws IOException {
        String json = "[{\"name\":\"John\",\"age\":30},{" +
                "\"name\":\"Jane\",\"age\":25}]";
        JsonParser parser = createParser(json);
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(parser, null);
        assertEquals(ArrayNode.class, node.getClass());
        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(2, arrayNode.size());
        ObjectNode firstObjectNode = (ObjectNode) arrayNode.get(0);
        assertEquals("John", firstObjectNode.get("name").asText());
        assertEquals(30, firstObjectNode.get("age").asInt());
        ObjectNode secondObjectNode = (ObjectNode) arrayNode.get(1);
        assertEquals("Jane", secondObjectNode.get("name").asText());
        assertEquals(25, secondObjectNode.get("age").asInt());
    }

    @Test
    public void testDeserializeObjectWithDuplicateField() throws IOException {
        String json = "{\"name\":\"John\",\"name\":\"Jane\"}";
        JsonParser parser = createParser(json);
        BaseNodeDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        deserializer.getClass().getDeclaredField("_handleDuplicateField");
        deserializer.setAccessible(true);
        JsonNode node = deserializer.deserialize(parser, null);
        assertEquals(ObjectNode.class, node.getClass());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals("Jane", objNode.get("name").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectWithDuplicateFieldWithException() throws IOException {
        String json = "{\"name\":\"John\",\"name\":\"Jane\"}";
        JsonParser parser = createParser(json);
        BaseNodeDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        deserializer.getClass().getDeclaredField("_reportProblem");
        deserializer.setAccessible(true);
        deserializer.deserialize(parser, null);
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }
}