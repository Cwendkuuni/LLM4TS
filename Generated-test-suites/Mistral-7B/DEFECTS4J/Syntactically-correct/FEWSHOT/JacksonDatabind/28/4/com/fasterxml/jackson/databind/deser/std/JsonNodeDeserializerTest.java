package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class JsonNodeDeserializerTest extends TestBase {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeObject() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNodeType.OBJECT, node.getNodeType());
        ObjectNode objNode = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().getParser(json), null);
        assertEquals(JsonNodeType.OBJECT, objNode.getNodeType());
        assertEquals("John", objNode.get("name").asText());
        assertEquals(30, objNode.get("age").asInt());
        ArrayNode hobbiesNode = (ArrayNode) objNode.get("hobbies");
        assertEquals(2, hobbiesNode.size());
        assertEquals("reading", hobbiesNode.get(0).asText());
        assertEquals("swimming", hobbiesNode.get(1).asText());
    }

    @Test
    public void testDeserializeArray() throws Exception {
        String json = "[1,2,3,4]";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNodeType.ARRAY, node.getNodeType());
        ArrayNode arrayNode = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().getParser(json), null);
        assertEquals(JsonNodeType.ARRAY, arrayNode.getNodeType());
        assertEquals(1, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
    }

    @Test
    public void testDeserializeAny() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        JsonParser p = mapper.getFactory().getParser(json);
        JsonNode node = JsonNodeDeserializer.instance.deserialize(p, null);
        assertEquals(JsonNodeType.OBJECT, node.getNodeType());

        json = "[1,2,3,4]";
        p = mapper.getFactory().getParser(json);
        node = JsonNodeDeserializer.instance.deserialize(p, null);
        assertEquals(JsonNodeType.ARRAY, node.getNodeType());

        json = "\"hello world\"";
        p = mapper.getFactory().getParser(json);
        node = JsonNodeDeserializer.instance.deserialize(p, null);
        assertEquals(JsonNodeType.TEXT, node.getNodeType());
        assertEquals("hello world", node.asText());

        json = "true";
        p = mapper.getFactory().getParser(json);
        node = JsonNodeDeserializer.instance.deserialize(p, null);
        assertEquals(JsonNodeType.BOOLEAN, node.getNodeType());
        assertEquals(true, node.asBoolean());

        json = "false";
        p = mapper.getFactory().getParser(json);
        node = JsonNodeDeserializer.instance.deserialize(p, null);
        assertEquals(JsonNodeType.BOOLEAN, node.getNodeType());
        assertEquals(false, node.asBoolean());

        json = "null";
        p = mapper.getFactory().getParser(json);
        node = JsonNodeDeserializer.instance.deserialize(p, null);
        assertEquals(JsonNodeType.NULL, node.getNodeType());
    }

    @Test
    public void testDeserializeObjectWithDuplicateKey() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"name\":\"Jane\"}";
        JsonParser p = mapper.getFactory().getParser(json);
        try {
            JsonNodeDeserializer.instance.deserialize(p, null);
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
        }
    }
}