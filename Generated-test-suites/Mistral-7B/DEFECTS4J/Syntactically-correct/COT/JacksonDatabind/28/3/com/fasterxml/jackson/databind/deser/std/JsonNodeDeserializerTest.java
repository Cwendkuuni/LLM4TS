package com.example; // replace with your test package name

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() throws JsonProcessingException {
        JsonNode node = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonParser, context, nodeFactory);
        assertInstanceOf(ObjectNode.class, node);

        node = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(jsonParser, context, nodeFactory);
        assertInstanceOf(ArrayNode.class, node);

        node = JsonNodeDeserializer.getDeserializer(String.class).deserialize(jsonParser, context, nodeFactory);
        assertInstanceOf(TextNode.class, node);

        node = JsonNodeDeserializer.getDeserializer(Integer.class).deserialize(jsonParser, context, nodeFactory);
        assertInstanceOf(NumberNode.class, node);

        node = JsonNodeDeserializer.getDeserializer(Boolean.class).deserialize(jsonParser, context, nodeFactory);
        assertInstanceOf(BooleanNode.class, node);

        node = JsonNodeDeserializer.getDeserializer(NullNode.class).deserialize(jsonParser, context, nodeFactory);
        assertInstanceOf(NullNode.class, node);
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);
        assertNull(node);
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);

        ObjectNode objectNode = (ObjectNode) node;
        assertEquals("John", objectNode.get("name").asText());
        assertEquals(30, objectNode.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        String json = "[1, 2, 3]";
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);

        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(3, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws JsonProcessingException {
        String json = "invalid json";
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeEmptyObject() throws JsonProcessingException {
        String json = "{}";
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);

        ObjectNode objectNode = (ObjectNode) node;
        assertTrue(objectNode.isEmpty());
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        String json = "[]";
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);

        ArrayNode arrayNode = (ArrayNode) node;
        assertTrue(arrayNode.isEmpty());
    }

    @Test
    public void testDeserializeObjectWithDuplicateFields() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"name\": \"Doe\"}";
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);

        try {
            JsonNodeDeserializer.getInstance().deserialize(jsonParser, context);
            fail("Expected JsonMappingException due to duplicate field 'name'");
        } catch (JsonMappingException e) {
            assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
        }
    }
}