package com.example.jsonnodedeserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.BeforeClass;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
    }

    // Test method for JsonNodeDeserializer constructor
    @Test
    public void testJsonNodeDeserializerConstructor() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        assertNotNull(deserializer);
    }

    // Test method for getDeserializer method
    @Test
    public void testGetDeserializerObjectNode() {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertInstanceOf(ObjectNode.class, deserializer.deserialize(createParser("{}"), new TestJsonFactory()));
    }

    @Test
    public void testGetDeserializerArrayNode() {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertInstanceOf(ArrayNode.class, deserializer.deserialize(createParser("[]"), new TestJsonFactory()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDeserializerInvalidClass() {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
    }

    // Test method for deserialize method with null context
    @Test(expected = NullPointerException.class)
    public void testDeserializeNullContext() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.deserialize(createParser("{}"), null);
    }

    // Test method for deserialize method with JsonParser containing null value
    @Test
    public void testDeserializeNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("null"), new TestJsonFactory());
        assertEquals(JsonNode.Value.nullValue, result.getNodeType());
    }

    // Test method for deserialize method with JsonParser containing empty Object
    @Test
    public void testDeserializeEmptyObject() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("{}"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(ObjectNode.class, result.getClass());
        assertTrue(result.isObject());
        assertTrue(result.isEmpty());
    }

    // Test method for deserialize method with JsonParser containing empty Array
    @Test
    public void testDeserializeEmptyArray() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("[]"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(ArrayNode.class, result.getClass());
        assertTrue(result.isArray());
        assertTrue(result.isEmpty());
    }

    // Test method for deserialize method with JsonParser containing string value
    @Test
    public void testDeserializeStringValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("\"hello world\""), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(TextNode.class, result.getClass());
        assertTrue(result.isTextual());
        assertEquals("hello world", result.textValue());
    }

    // Test method for deserialize method with JsonParser containing number value
    @Test
    public void testDeserializeNumberValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("123"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(NumberNode.class, result.getClass());
        assertTrue(result.isNumber());
        assertEquals(123, result.numberValue().intValue());
    }

    // Test method for deserialize method with JsonParser containing boolean value
    @Test
    public void testDeserializeBooleanValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("true"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(BooleanNode.class, result.getClass());
        assertTrue(result.isBoolean());
        assertTrue(result.booleanValue());
    }

    // Test method for deserialize method with JsonParser containing array value
    @Test
    public void testDeserializeArrayValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("[]"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(ArrayNode.class, result.getClass());
        assertTrue(result.isArray());
        assertTrue(result.isEmpty());

        result = deserializer.deserialize(createParser("[\n1,\"two\",3.0,\"four\"]"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(ArrayNode.class, result.getClass());
        assertTrue(result.isArray());
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals("two", result.get(1).textValue());
        assertEquals(3.0, result.get(2).numberValue().doubleValue(), 0.001);
        assertEquals("four", result.get(3).textValue());
    }

    // Test method for deserialize method with JsonParser containing object value
    @Test
    public void testDeserializeObjectValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(ObjectNode.class, result.getClass());
        assertTrue(result.isObject());
        assertTrue(result.has("name"));
        assertEquals("John", result.get("name").textValue());
        assertTrue(result.has("age"));
        assertEquals(30, result.get("age").intValue());
        assertTrue(result.has("city"));
        assertEquals("New York", result.get("city").textValue());
    }

    // Test method for deserialize method with JsonParser containing embedded object
    @Test
    public void testDeserializeEmbeddedObject() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(createParser("{ \"name\": { \"first\": \"John\", \"last\": \"Doe\" } }"), new TestJsonFactory());
        assertNotNull(result);
        assertEquals(ObjectNode.class, result.getClass());
        assertTrue(result.isObject());
        assertTrue(result.has("name"));
        ObjectNode nameNode = (ObjectNode) result.get("name");
        assertTrue(nameNode.isObject());
        assertTrue(nameNode.has("first"));
        assertEquals("John", nameNode.get("first").textValue());
        assertTrue(nameNode.has("last"));
        assertEquals("Doe", nameNode.get("last").textValue());
    }

    // Test method for deserialize method with JsonParser containing null key in object
    @Test
    public void testDeserializeNullKeyObject() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "{\"null\":null}";

        try {
            JsonParser parser = mapper.getFactory().createParser(json);
            JsonNode node = deserializer.deserialize(parser, new TestJsonFactory());
            fail("Expected JsonMappingException due to null key in Object");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Test method for deserialize method with JsonParser containing duplicate keys in object
    @Test
    public void testDeserializeDuplicateKeysObject() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "{\"name\":\"John\",\"name\":\"Jane\"}";

        try {
            JsonParser parser = mapper.getFactory().createParser(json);
            JsonNode node = deserializer.deserialize(parser, new TestJsonFactory());
            fail("Expected JsonMappingException due to duplicate keys in Object");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private JsonParser createParser(String jsonString) {
        return mapper.getFactory().createParser(jsonString);
    }

    private static class TestJsonFactory extends JsonFactory {
        @Override
        public JsonParser createParser(String charArray) {
            return createParser(new CharArrayInput(charArray));
        }
    }
}