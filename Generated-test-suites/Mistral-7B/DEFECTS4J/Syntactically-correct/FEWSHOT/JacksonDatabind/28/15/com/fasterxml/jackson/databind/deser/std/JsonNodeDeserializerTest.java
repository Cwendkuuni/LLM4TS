package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.databind.JsonMapping.Builder.jsonDeserializer;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Test;

public class JsonNodeDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeJsonObject() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNode.Class.OBJECT, node.getClass());
        ObjectNode objNode = (ObjectNode) node;
        assertNotNull(objNode.get("name"));
        assertEquals("John", objNode.get("name").asText());
        assertNotNull(objNode.get("age"));
        assertEquals(30, objNode.get("age").asInt());
        assertNotNull(objNode.get("city"));
        assertEquals("New York", objNode.get("city").asText());
    }

    @Test
    public void testDeserializeJsonArray() throws Exception {
        String json = "[1, 2, 3]";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNode.Class.ARRAY, node.getClass());
        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(3, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializeJsonObjectWithDuplicateFields() throws Exception {
        String json = "{\"name\": \"John\", \"name\": \"Doe\"}";
        try {
            mapper.readTree(json);
            // This test case should not pass, as FAIL_ON_READING_DUP_TREE_KEY is enabled by default
            fail("Expected JsonMappingException: Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled");
        } catch (JsonMappingException e) {
            assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
        }
    }

    @Test
    public void testDeserializeEmptyJsonObject() throws Exception {
        String json = "{}";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNode.Class.OBJECT, node.getClass());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals(0, objNode.size());
    }

    @Test
    public void testDeserializeEmptyJsonArray() throws Exception {
        String json = "[]";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNode.Class.ARRAY, node.getClass());
        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(0, arrayNode.size());
    }

    @Test
    public void testDeserializeNullJson() throws Exception {
        String json = "null";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNode.Class.NULL, node.getClass());
        assertNull(node.asText());
        assertNull(node.asInt());
        assertNull(node.asLong());
        assertNull(node.asDouble());
    }

    @Test
    public void testDeserializeJsonWithNestedObjectsAndArrays() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": {\"name\": \"New York\", \"population\": [100000, 200000]}}";
        JsonNode node = mapper.readTree(json);
        assertEquals(JsonNode.Class.OBJECT, node.getClass());
        ObjectNode objNode = (ObjectNode) node;

        assertNotNull(objNode.get("name"));
        assertEquals("John", objNode.get("name").asText());
        assertNotNull(objNode.get("age"));
        assertEquals(30, objNode.get("age").asInt());
        assertNotNull(objNode.get("city"));
        ObjectNode cityNode = (ObjectNode) objNode.get("city");

        assertNotNull(cityNode.get("name"));
        assertEquals("New York", cityNode.get("name").asText());
        assertNotNull(cityNode.get("population"));
        ArrayNode populationNode = (ArrayNode) cityNode.get("population");

        assertEquals(2, populationNode.size());
        assertEquals(100000, populationNode.get(0).asInt());
        assertEquals(200000, populationNode.get(1).asInt());
    }
}