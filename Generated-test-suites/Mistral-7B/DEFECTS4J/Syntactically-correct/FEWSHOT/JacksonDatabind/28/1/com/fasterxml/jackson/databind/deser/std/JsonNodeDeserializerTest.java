package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class JsonNodeDeserializerTest extends TestBase {

    @Test
    public void testJsonNodeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test null value
        JsonNode deserialized = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0])), null);
        assertEquals(JsonNode.Value.nullValue(), deserialized);

        // Test empty JSON object
        String jsonObject = "{}";
        JsonNode deserializedObject = JsonNodeDeserializer.getInstance().deserialize(mapper.readTree(jsonObject), ObjectNode.class);
        assertTrue(deserializedObject instanceof ObjectNode);
        assertEquals(0, deserializedObject.size());

        // Test empty JSON array
        String jsonArray = "[]";
        JsonNode deserializedArray = JsonNodeDeserializer.getInstance().deserialize(mapper.readTree(jsonArray), ArrayNode.class);
        assertTrue(deserializedArray instanceof ArrayNode);
        assertEquals(0, deserializedArray.size());

        // Test JSON object with fields
        String jsonObjectWithFields = "{\"field1\": 1, \"field2\": \"hello\"}";
        JsonNode deserializedObjectWithFields = JsonNodeDeserializer.getInstance().deserialize(mapper.readTree(jsonObjectWithFields), ObjectNode.class);
        assertTrue(deserializedObjectWithFields instanceof ObjectNode);
        assertEquals(2, deserializedObjectWithFields.size());
        assertEquals(1, deserializedObjectWithFields.get("field1").asInt());
        assertEquals("hello", deserializedObjectWithFields.get("field2").asText());

        // Test JSON array with elements
        String jsonArrayWithElements = "[1, 2, 3]";
        JsonNode deserializedArrayWithElements = JsonNodeDeserializer.getInstance().deserialize(mapper.readTree(jsonArrayWithElements), ArrayNode.class);
        assertTrue(deserializedArrayWithElements instanceof ArrayNode);
        assertEquals(3, deserializedArrayWithElements.size());
        assertEquals(1, deserializedArrayWithElements.get(0).asInt());
        assertEquals(2, deserializedArrayWithElements.get(1).asInt());
        assertEquals(3, deserializedArrayWithElements.get(2).asInt());
    }

    @Test
    public void testObjectDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNodeDeserializer.ObjectDeserializer objectDeserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
        JsonNode deserialized = objectDeserializer.deserialize(mapper.getFactory().createParser("{}"), null);
        assertTrue(deserialized instanceof ObjectNode);
        assertEquals(0, deserialized.size());

        deserialized = objectDeserializer.deserialize(mapper.getFactory().createParser("{\"field1\": 1, \"field2\": \"hello\"}"), null);
        assertTrue(deserialized instanceof ObjectNode);
        assertEquals(2, deserialized.size());
        assertEquals(1, deserialized.get("field1").asInt());
        assertEquals("hello", deserialized.get("field2").asText());
    }

    @Test
    public void testArrayDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNodeDeserializer.ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
        JsonNode deserialized = arrayDeserializer.deserialize(mapper.getFactory().createParser("[]"), null);
        assertTrue(deserialized instanceof ArrayNode);
        assertEquals(0, deserialized.size());

        deserialized = arrayDeserializer.deserialize(mapper.getFactory().createParser("[1, 2, 3]"), null);
        assertTrue(deserialized instanceof ArrayNode);
        assertEquals(3, deserialized.size());
        assertEquals(1, deserialized.get(0).asInt());
        assertEquals(2, deserialized.get(1).asInt());
        assertEquals(3, deserialized.get(2).asInt());
    }
}