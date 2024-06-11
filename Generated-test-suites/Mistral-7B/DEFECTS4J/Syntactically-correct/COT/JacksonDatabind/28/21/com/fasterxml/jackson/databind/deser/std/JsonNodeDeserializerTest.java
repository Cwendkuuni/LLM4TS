package com.fasterxml.jackson.databind.deser.std; // Adjust the package name if necessary

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Test;

public class JsonNodeDeserializerTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    

    @Test
    public void testGetDeserializer() throws Exception {
        JsonNode node = MAPPER.readTree("{ \"name\": \"John\", \"age\": 30 }");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(node.getClass());
        assertEquals(JsonNodeDeserializer.class, deserializer.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDeserializerWithInvalidClass() throws Exception {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
    }

    @Test
    public void testGetNullValue() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode nullNode = deserializer.getNullValue(null);
        assertNull(nullNode);
    }

    @Test
    public void testDeserializeNullValue() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode nullNode = deserializer.getNullValue();
        assertNull(nullNode);
    }

    @Test
    public void testDeserializeObject() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, null);
        ObjectNode objectNode = (ObjectNode) result;
        assertEquals("John", objectNode.get("name").asText());
        assertEquals(30, objectNode.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[[1,2],[3,4]]");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, null);
        ArrayNode arrayNode = (ArrayNode) result;
        assertEquals(2, arrayNode.size());
        assertEquals(1, arrayNode.get(0).get(0).asInt());
        assertEquals(2, arrayNode.get(0).get(1).asInt());
        assertEquals(3, arrayNode.get(1).get(0).asInt());
        assertEquals(4, arrayNode.get(1).get(1).asInt());
    }

    @Test
    public void testDeserializeAny() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{\"name\": \"John\"}");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, null);
        ObjectNode objectNode = (ObjectNode) result;
        assertEquals("John", objectNode.get("name").asText());

        parser.nextToken(); // Move to the next token to test deserializing an empty JSON Object
        result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{ \"name\": \"John\" }"); // Missing comma between properties
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        deserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{\"name\": \"John\", 123}");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeEmptyJsonObject() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{ }");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeEmptyJsonArray() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[]");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertTrue(result instanceof ArrayNode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeEmptyJsonObjectWithFeatures() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithEmptyObjectFeature = new ObjectMapper();
        mapperWithEmptyObjectFeature.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonParser parser = mapperWithEmptyObjectFeature.getFactory().createParser("{ }");
        JsonDeserializer<JsonNode> deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
        assertTrue(result.isEmpty());

        parser = mapper.getFactory().createParser("{ }");
        result = deserializer.deserialize(parser, null);
        assertEquals(JsonMappingException.class, result.getClass());
    }

    
}