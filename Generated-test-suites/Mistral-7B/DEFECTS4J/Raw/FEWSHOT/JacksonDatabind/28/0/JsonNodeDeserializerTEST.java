package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testNullValue() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonParser p = createParser("null");
        JsonNode node = deserializer.deserialize(p, new DeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertNull(node);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ObjectNode node = null;
        String json = "{\"foo\":1,\"bar\":[\"hello\",\"world\"]}";
        JsonParser p = createParser(json);

        node = (ObjectNode) deserializer.deserialize(p, new DeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals(2, node.size());
        assertEquals("foo", node.fieldNames().nextValue());
        JsonNode foo = node.get("foo");
        assertEquals(JsonNodeType.NUMBER, foo.getNodeType());
        assertEquals(1, foo.asInt());

        JsonNode bar = node.get("bar");
        assertEquals(JsonNodeType.ARRAY, bar.getNodeType());
        assertEquals(2, bar.size());
        JsonNode first = bar.elements().nextValue();
        assertEquals("hello", first.asText());
        JsonNode second = bar.elements().nextValue();
        assertEquals("world", second.asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        ArrayNode node = null;
        String json = "[1,2.5,\"three\",null,true]";
        JsonParser p = createParser(json);

        node = (ArrayNode) deserializer.deserialize(p, new DeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals(6, node.size());
        JsonNode first = node.get(0);
        assertEquals(JsonNodeType.NUMBER, first.getNodeType());
        assertEquals(1, first.asInt());

        JsonNode second = node.get(1);
        assertEquals(JsonNodeType.NUMBER, second.getNodeType());
        assertEquals(2.5, second.asDouble());

        JsonNode third = node.get(2);
        assertEquals(JsonNodeType.TEXT, third.getNodeType());
        assertEquals("three", third.asText());

        JsonNode nullNode = node.get(4);
        assertNull(nullNode);

        JsonNode fifth = node.get(5);
        assertEquals(JsonNodeType.BOOLEAN, fifth.getNodeType());
        assertTrue(fifth.asBoolean());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        String json = "{\"foo\":1,\"bar\":[\"hello\",\"world\"]}";
        JsonParser p = createParser(json);

        JsonNode node = deserializer.deserialize(p, new DeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals(JsonNodeType.OBJECT, node.getNodeType());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals(2, objNode.size());
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonFactory factory = mapper.getFactory();
        return factory.createParser(new StringReader(json));
    }

}