package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("null");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        JsonNode result = deserializer.deserialize(parser, new DeserializationContext(), null);

        assertNotNull(result);
        assertEquals(JsonNodeType.NULL, result.getNodeType());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"foo\":1,\"bar\":[1,2,3]}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        ObjectNode result = (ObjectNode) deserializer.deserialize(parser, new DeserializationContext(), ObjectMap.STANDARD_NODE_FACTORY);

        assertEquals(JsonNodeType.OBJECT, result.getNodeType());
        assertEquals("foo", result.get("foo").asText());
        assertEquals(JsonNodeType.ARRAY, result.get("bar").getNodeType());
        assertEquals(3, result.get("bar").size());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1,2,\"foo\",null]");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        ArrayNode result = (ArrayNode) deserializer.deserialize(parser, new DeserializationContext(), ObjectMap.STANDARD_NODE_FACTORY);

        assertEquals(JsonNodeType.ARRAY, result.getNodeType());
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals("foo", result.get(2).asText());
        assertEquals(JsonNodeType.NULL, result.get(3).getNodeType());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"foo\":1,\"bar\":[1,2,3],\"baz\":\"quux\"}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        JsonNode result = deserializer.deserialize(parser, new DeserializationContext(), ObjectMap.STANDARD_NODE_FACTORY);

        assertEquals(JsonNodeType.OBJECT, result.getNodeType());
        assertEquals("foo", result.get("foo").asText());
        assertEquals(JsonNodeType.ARRAY, result.get("bar").getNodeType());
        assertEquals(3, result.get("bar").size());
        assertEquals("quux", result.get("baz").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("123");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        deserializer.deserialize(parser, new DeserializationContext(), ObjectMap.STANDARD_NODE_FACTORY);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidObject() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{1:2}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        deserializer.deserialize(parser, new DeserializationContext(), ObjectMap.STANDARD_NODE_FACTORY);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1:2]");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        deserializer.deserialize(parser, new DeserializationContext(), ObjectMap.STANDARD_NODE_FACTORY);
    }
}