package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializerObjectNode() {
        JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetDeserializerArrayNode() {
        JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        ObjectNode objectNode = new ObjectMapper().readValue(new StringReader("null"), ObjectNode.class);
        JsonDeserializer<JsonNode> jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode deserializedNullValue = jsonNodeDeserializer.deserialize(new JsonFactory(), new DeserializationContext(new ObjectMapper()), null);
        assertEquals(JsonNode.Value.nullValue(), deserializedNullValue);
    }

    @Test
    public void testDeserializeStartObject() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"address\": {\"city\": \"New York\", \"state\": \"NY\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.readValue(new StringReader(json), ObjectNode.class);
        JsonDeserializer<JsonNode> jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode deserializedObjectNode = jsonNodeDeserializer.deserialize(new JsonFactory(), new DeserializationContext(new ObjectMapper()), jsonNode);
        assertEquals(JsonNodeType.OBJECT, deserializedObjectNode.getType());
        ObjectNode deserializedObject = (ObjectNode) deserializedObjectNode;
        assertEquals("John", deserializedObject.get("name").asText());
        assertEquals(30, deserializedObject.get("age").asInt());
        ObjectNode addressNode = (ObjectNode) deserializedObject.get("address");
        assertEquals("New York", addressNode.get("city").asText());
        assertEquals("NY", addressNode.get("state").asText());
    }

    @Test
    public void testDeserializeStartArray() throws IOException {
        String json = "[1, 2, 3, 4]";
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonNode = objectMapper.readValue(new StringReader(json), ArrayNode.class);
        JsonDeserializer<JsonNode> jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode deserializedArrayNode = jsonNodeDeserializer.deserialize(new JsonFactory(), new DeserializationContext(new ObjectMapper()), jsonNode);
        assertEquals(JsonNodeType.ARRAY, deserializedArrayNode.getType());
        ArrayNode deserializedArray = (ArrayNode) deserializedArrayNode;
        assertEquals(4, deserializedArray.size());
        assertEquals(1, deserializedArray.get(0).asInt());
        assertEquals(2, deserializedArray.get(1).asInt());
        assertEquals(3, deserializedArray.get(2).asInt());
        assertEquals(4, deserializedArray.get(3).asInt());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"address\": {\"city\": \"New York\", \"state\": \"NY\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.readValue(new StringReader(json), ObjectNode.class);
        JsonDeserializer<JsonNode> jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode deserializedAny = jsonNodeDeserializer.deserialize(new JsonFactory(), new DeserializationContext(new ObjectMapper()), jsonNode);
        assertEquals(JsonNodeType.OBJECT, deserializedAny.getType());
        ObjectNode deserializedObject = (ObjectNode) deserializedAny;
        assertEquals("John", deserializedObject.get("name").asText());
        assertEquals(30, deserializedObject.get("age").asInt());
        ObjectNode addressNode = (ObjectNode) deserializedObject.get("address");
        assertEquals("New York", addressNode.get("city").asText());
        assertEquals("NY", addressNode.get("state").asText());
    }
}