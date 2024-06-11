package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser("null"), null);
        assertNotNull(node);
        assertTrue(node instanceof JsonNull);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertEquals("John", obj.get("name").asText());
        assertEquals(30, obj.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(3, array.size());
        assertEquals(1, array.get(0).asInt());
        assertEquals(2, array.get(1).asInt());
        assertEquals(3, array.get(2).asInt());
    }

    @Test
    public void testDeserializeObjectWithDuplicateFields() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"name\": \"Jane\"}";
        try {
            JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
        }
    }

    @Test
    public void testDeserializeArrayWithDuplicateValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 1, 2]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(3, array.size());
        assertEquals(1, array.get(0).asInt());
        assertEquals(1, array.get(1).asInt());
        assertEquals(2, array.get(2).asInt());
    }

    @Test
    public void testDeserializeObjectWithEmptyName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertTrue(obj.isEmpty());
    }

    @Test
    public void testDeserializeArrayWithEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertTrue(array.isEmpty());
    }

    @Test
    public void testDeserializeObjectWithFieldName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertEquals("John", obj.get("name").asText());
        assertTrue(obj.isEmpty());
    }

    @Test
    public void testDeserializeArrayWithStartArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[";
        try {
            JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected end-of-input when binding data into ArrayNode", e.getMessage());
        }
    }

    @Test
    public void testDeserializeObjectWithEndObjectToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        try {
            JsonParser p = mapper.createParser(json);
            p.nextToken(); // consume START_OBJECT token
            JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, null);
            p.nextToken(); // consume END_OBJECT token
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected end-of-input when binding data into ObjectNode", e.getMessage());
        }
    }

    @Test
    public void testDeserializeObjectWithStringValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertEquals("John", obj.get("name").asText());
    }

    @Test
    public void testDeserializeObjectWithIntValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"age\": 30}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertEquals(30, obj.get("age").asInt());
    }

    @Test
    public void testDeserializeObjectWithBooleanValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"isStudent\": true}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertTrue(obj.get("isStudent").asBoolean());
    }

    @Test
    public void testDeserializeObjectWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"isStudent\": null}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertNull(obj.get("isStudent"));
    }

    @Test
    public void testDeserializeObjectWithEmptyStringValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"\"}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        assertEquals("", obj.get("name").asText());
    }

    @Test
    public void testDeserializeArrayWithStringValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"apple\", \"banana\"]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(2, array.size());
        assertEquals("apple", array.get(0).asText());
        assertEquals("banana", array.get(1).asText());
    }

    @Test
    public void testDeserializeArrayWithIntValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(3, array.size());
        assertEquals(1, array.get(0).asInt());
        assertEquals(2, array.get(1).asInt());
        assertEquals(3, array.get(2).asInt());
    }

    @Test
    public void testDeserializeArrayWithBooleanValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[true, false]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(2, array.size());
        assertTrue(array.get(0).asBoolean());
        assertFalse(array.get(1).asBoolean());
    }

    @Test
    public void testDeserializeArrayWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null, null]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(2, array.size());
        assertNull(array.get(0));
        assertNull(array.get(1));
    }

    @Test
    public void testDeserializeArrayWithEmptyStringValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"\", \"\"]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(2, array.size());
        assertEquals("", array.get(0).asText());
        assertEquals("", array.get(1).asText());
    }

    @Test
    public void testDeserializeObjectWithEmptyObjectToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        try {
            JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected end-of-input when binding data into ObjectNode", e.getMessage());
        }
    }

    @Test
    public void testDeserializeArrayWithEmptyArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        try {
            JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected end-of-input when binding data into ArrayNode", e.getMessage());
        }
    }

    @Test
    public void testDeserializeObjectWithEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonObject obj = (JsonObject) node;
        JsonObject embeddedObj = (JsonObject) obj.get("name");
        assertEquals("John", embeddedObj.get("first").asText());
        assertEquals("Doe", embeddedObj.get("last").asText());
    }

    @Test
    public void testDeserializeArrayWithEmbeddedArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, [2, 3], 4]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.createParser(json), null);
        JsonArray array = (JsonArray) node;
        assertEquals(3, array.size());
        assertEquals(1, array.get(0).asInt());
        JsonArray embeddedArray = (JsonArray) array.get(1);
        assertEquals(2, embeddedArray.size());
        assertEquals(2, embeddedArray.get(0).asInt());
        assertEquals(3, embeddedArray.get(1).asInt());
        assertEquals(4, array.get(2).asInt());
    }
}