package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() {
        JsonNode node = JsonNodeFactory.instance.nullNode();
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(node.getClass());
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue(null);
        assertEquals(JsonNode.Null.class, result.getClass());
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser("{\"key\":\"value\"}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode node = (ObjectNode) deserializer.deserialize(parser, null, JsonNodeFactory.instance);
        assertEquals("value", node.get("key").asText());
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser("[1, 2, 3]");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ArrayNode node = (ArrayNode) deserializer.deserialize(parser, null, JsonNodeFactory.instance);
        assertEquals(3, node.size());
        assertEquals(1, node.get(0).asInt());
        assertEquals(2, node.get(1).asInt());
        assertEquals(3, node.get(2).asInt());
    }

    @Test
    public void testDeserializeAny() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser("true");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode node = deserializer.deserialize(parser, null, JsonNodeFactory.instance);
        assertTrue(node.booleanValue());

        parser.nextToken(); // Move to next token to test for null
        node = deserializer.deserialize(parser, null, JsonNodeFactory.instance);
        assertNull(node);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser("invalid_json");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.deserialize(parser, null, JsonNodeFactory.instance);
    }

    @Test
    public void testDeserializeObjectWithDuplicateFields() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser("{\"field1\":1, \"field1\":2}");
        BaseNodeDeserializer.JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode node = (ObjectNode) deserializer.deserialize(parser, null, nodeFactory);
        assertEquals(2, node.size());
        assertEquals(1, node.get("field1").asInt());
        assertEquals(2, node.get("field1").asInt());
    }

    @Test
    public void testDeserializeObjectWithEmptyObject() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser("{}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode node = (ObjectNode) deserializer.deserialize(parser, null, JsonNodeFactory.instance);
        assertTrue(node.isEmpty());
    }
}