package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() {
        assertEquals(JsonNodeDeserializer.class, JsonNodeDeserializer.getDeserializer(JsonNode.class));
        assertEquals(ObjectNodeDeserializer.class, JsonNodeDeserializer.getDeserializer(ObjectNode.class));
        assertEquals(ArrayNodeDeserializer.class, JsonNodeDeserializer.getDeserializer(ArrayNode.class));
        assertNull(JsonNodeDeserializer.getDeserializer(String.class));
    }

    // Test null value deserialization

    @Test
    public void testGetNullValue() throws IOException {
        JsonNode node = JsonNodeDeserializer.instance.getNullValue(null);
        assertNull(node);

        JsonNodeFactory factory = new JsonFactory();
        JsonNode node2 = JsonNodeDeserializer.instance.getNullValue(new JsonFactory());
        assertEquals(NullNode.getInstance(), node2);
    }

    // Test deserialization of JSON Objects

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        ObjectNode obj = (ObjectNode) node;
        assertEquals("John", obj.get("name").asText());
        assertEquals(30, obj.get("age").asInt());
        assertEquals("New York", obj.get("city").asText());
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        ObjectNode obj = (ObjectNode) node;
        assertNotNull(obj);
        assertEquals(0, obj.size());
    }

    @Test
    public void testDeserializeObjectWithFieldOrder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapOrder.FIELD_ORDER);
        String json = "{\"city\": \"New York\", \"name\": \"John\", \"age\": 30}";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        ObjectNode obj = (ObjectNode) node;
        assertEquals("John", obj.get("name").asText());
        assertEquals(30, obj.get("age").asInt());
        assertEquals("New York", obj.get("city").asText());
    }

    // Test deserialization of JSON Arrays

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3, 4]";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        ArrayNode arr = (ArrayNode) node;
        assertEquals(4, arr.size());
        assertEquals(1, arr.get(0).asInt());
        assertEquals(2, arr.get(1).asInt());
        assertEquals(3, arr.get(2).asInt());
        assertEquals(4, arr.get(3).asInt());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        ArrayNode arr = (ArrayNode) node;
        assertEquals(0, arr.size());
    }

    // Test deserialization of other JSON types

    @Test
    public void testDeserializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"Hello World\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        TextNode str = (TextNode) node;
        assertEquals("Hello World", str.asText());
    }

    @Test
    public void testDeserializeNumberInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "123";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        NumberNode intNode = (NumberNode) node;
        assertEquals(123, intNode.asInt());
    }

    @Test
    public void testDeserializeNumberFloat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "123.45";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        NumberNode floatNode = (NumberNode) node;
        assertEquals(123.45, floatNode.asDouble(), 0.001);
    }

    @Test
    public void testDeserializeBooleanTrue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "true";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        BooleanNode boolNode = (BooleanNode) node;
        assertTrue(boolNode.asBoolean());
    }

    @Test
    public void testDeserializeBooleanFalse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "false";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        BooleanNode boolNode = (BooleanNode) node;
        assertFalse(boolNode.asBoolean());
    }

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.instance;
        JsonNode node = deserializer.deserialize(parser, null, null);

        NullNode nullNode = (NullNode) node;
        assertSame(nullNode, node);
    }

    // Test handling of duplicate fields

    @Test
    public void testHandleDuplicateField() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"name\": \"Jane\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = new ObjectDeserializer();
        JsonNode node = deserializer.deserialize(parser, null, null);

        ObjectNode obj = (ObjectNode) node;
        assertEquals(1, obj.size());
        assertEquals("John", obj.get("name").asText());
    }

    // Test handling of duplicate fields with custom error message

    @Test(expected = JsonMappingException.class)
    public void testHandleDuplicateFieldWithCustomErrorMessage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodeFactory = mapper.getFactory().getNodeFactory();
        JsonNodeDeserializer deserializer = new ObjectDeserializer();

        String json = "{\"name\": \"John\", \"name\": \"Jane\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        deserializer.setContext(new DeserializationContext(mapper.getConfiguration(), parser, null, null, null, null));
        deserializer.setContext(deserializer.getContext().withFeature(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true));

        JsonNode node = deserializer.deserialize(parser, null, nodeFactory);
        ObjectNode obj = (ObjectNode) node;
        assertEquals(1, obj.size());
        assertEquals("John", obj.get("name").asText());

        String fieldName = obj.get("name").asText();
        deserializer._handleDuplicateField(parser, deserializer.getContext(), nodeFactory, fieldName, obj, null, null);
    }
}