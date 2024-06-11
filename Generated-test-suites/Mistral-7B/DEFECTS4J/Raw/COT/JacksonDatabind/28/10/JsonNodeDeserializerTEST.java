package com.example.jsonnodedeserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

/**
 * Test class for JsonNodeDeserializer.
 */
public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("null");
        JsonNode result = deserializer.deserialize(parser, null);
        assertEquals(JsonNode.Value.NULL, result.getValueAsToken());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1}");
        JsonNode result = deserializer.deserialize(parser, null);
        ObjectNode obj = (ObjectNode) result;
        assertEquals("foo", obj.fieldNames().nextValue());
        assertEquals(JsonNode.Value.NUMBER, obj.get("foo").valueId());
        assertEquals(1, obj.get("foo").asInt());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("[1,2]");
        JsonNode result = deserializer.deserialize(parser, null);
        ArrayNode arr = (ArrayNode) result;
        assertEquals(2, arr.size());
        assertEquals(1, arr.get(0).asInt());
        assertEquals(2, arr.get(1).asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("{foo: 1, bar: 3}"); // missing '}'
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("{}");
        JsonNode result = deserializer.deserialize(parser, null);
        ObjectNode obj = (ObjectNode) result;
        assertTrue(obj.isEmpty());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonNode result = deserializer.deserialize(parser, null);
        ArrayNode arr = (ArrayNode) result;
        assertTrue(arr.isEmpty());
    }

    @Test
    public void testDeserializeObjectWithArrayProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("{" +
                "\"foo\": {" +
                "\"bar\": [1,2]" +
                "}" +
                "}");
        JsonNode result = deserializer.deserialize(parser, null);
        ObjectNode obj = (ObjectNode) result;
        ObjectNode foo = (ObjectNode) obj.get("foo");
        ArrayNode arr = (ArrayNode) foo.get("bar");
        assertEquals(2, arr.size());
        assertEquals(1, arr.get(0).asInt());
        assertEquals(2, arr.get(1).asInt());
    }

    @Test
    public void testDeserializeObjectWithNestedObjectProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("{" +
                "\"foo\": {" +
                "\"bar\": {" +
                "\"baz\": 1" +
                "}" +
                "}" +
                "}");
        JsonNode result = deserializer.deserialize(parser, null);
        ObjectNode obj = (ObjectNode) result;
        ObjectNode foo = (ObjectNode) obj.get("foo");
        ObjectNode bar = (ObjectNode) foo.get("bar");
        assertEquals("baz", bar.fieldNames().nextValue());
        assertEquals(JsonNode.Value.NUMBER, bar.get("baz").valueId());
        assertEquals(1, bar.get("baz").asInt());
    }

    @Test
    public void testDeserializeArrayWithArrayElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("[1,[2,3]]");
        JsonNode result = deserializer.deserialize(parser, null);
        ArrayNode arr = (ArrayNode) result;
        assertEquals(2, arr.size());
        JsonNode firstElement = arr.get(0);
        assertEquals(JsonNode.Value.NUMBER, firstElement.valueId());
        assertEquals(1, firstElement.asInt());
        JsonNode secondElement = arr.get(1);
        assertTrue(secondElement instanceof ArrayNode);
        ArrayNode arr2 = (ArrayNode) secondElement;
        assertEquals(2, arr2.size());
        assertEquals(2, arr2.get(0).asInt());
        assertEquals(3, arr2.get(1).asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser parser = mapper.getFactory().createParser("[1,]"); // missing ']'
        deserializer.deserialize(parser, null);
    }
}