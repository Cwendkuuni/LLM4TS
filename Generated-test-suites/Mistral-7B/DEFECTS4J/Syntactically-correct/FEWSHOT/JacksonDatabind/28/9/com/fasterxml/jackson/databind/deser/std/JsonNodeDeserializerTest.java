package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testJsonNodeDeserializer() throws IOException {
        JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParser(), new ObjectMapper().getDeserializationContext());
        assertNotNull(node);
    }

    @Test
    public void testObjectDeserializer() throws IOException {
        ObjectNode objectNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mockJsonParser(), new ObjectMapper().getDeserializationContext());
        assertNotNull(objectNode);
        assertEquals("{}", objectNode.toString());

        ObjectNode objectNodeWithValue = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mockJsonParserWithObject("{\"foo\":\"bar\"}"), new ObjectMapper().getDeserializationContext());
        assertNotNull(objectNodeWithValue);
        assertEquals("{\"foo\":\"bar\"}", objectNodeWithValue.toString());
    }

    @Test
    public void testArrayDeserializer() throws IOException {
        ArrayNode arrayNode = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mockJsonParserWithArray("[1, 2, 3]"), new ObjectMapper().getDeserializationContext());
        assertNotNull(arrayNode);
        assertEquals("[1, 2, 3]", arrayNode.toString());
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonNode nullNode = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithNullValue(), new ObjectMapper().getDeserializationContext());
        assertNull(nullNode);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectNode objectNode = (ObjectNode) JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithObject("{\"foo\":1,\"bar\":[1,2]}"), new ObjectMapper().getDeserializationContext());
        assertNotNull(objectNode);

        assertEquals("1", objectNode.get("foo").asText());
        JsonNode arrayNode = objectNode.get("bar");
        assertInstanceOf(ArrayNode.class, arrayNode);
        assertEquals("[1, 2]", arrayNode.toString());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ArrayNode arrayNode = (ArrayNode) JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithArray("[1, 2, 3]"), new ObjectMapper().getDeserializationContext());
        assertNotNull(arrayNode);

        JsonNode node1 = arrayNode.get(0);
        assertEquals("1", node1.asText());

        JsonNode node2 = arrayNode.get(1);
        assertEquals("2", node2.asText());

        JsonNode node3 = arrayNode.get(2);
        assertEquals("3", node3.asText());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        ObjectNode objectNode = (ObjectNode) JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithObject("{\"foo\":\"bar\",\"baz\":[1,2]}"), new ObjectMapper().getDeserializationContext());
        assertNotNull(objectNode);

        JsonNode node1 = objectNode.get("foo");
        assertEquals("bar", node1.asText());

        ArrayNode node2 = (ArrayNode) objectNode.get("baz");
        assertNotNull(node2);
        assertEquals("[1, 2]", node2.toString());

        JsonNode node3 = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithObject("{\"foo\":1}"), new ObjectMapper().getDeserializationContext());
        assertEquals("1", node3.asText());
    }

    @Test
    public void testDeserializeInt() throws IOException {
        JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithInt(1), new ObjectMapper().getDeserializationContext());
        assertEquals("1", node.asText());

        JsonNode node2 = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithInt(123), new ObjectMapper().getDeserializationContext());
        assertEquals("123", node2.asText());

        JsonNode node3 = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithInt(-1), new ObjectMapper().getDeserializationContext());
        assertEquals("-1", node3.asText());
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithFloat(1.0f), new ObjectMapper().getDeserializationContext());
        assertEquals("1.0", node.asText());

        JsonNode node2 = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithFloat(1.23f), new ObjectMapper().getDeserializationContext());
        assertEquals("1.23", node2.asText());

        JsonNode node3 = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithFloat(-1.5f), new ObjectMapper().getDeserializationContext());
        assertEquals("-1.5", node3.asText());
    }

    @Test
    public void testDeserializeBoolean() throws IOException {
        JsonNode nodeTrue = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithBoolean(true), new ObjectMapper().getDeserializationContext());
        assertTrue(nodeTrue.asBoolean(true));

        JsonNode nodeFalse = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithBoolean(false), new ObjectMapper().getDeserializationContext());
        assertFalse(nodeFalse.asBoolean(false));
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mockJsonParserWithNullValue(), new ObjectMapper().getDeserializationContext());
        assertNull(node);
    }

    private JsonParser mockJsonParser() {
        return mock(JsonParser.class);
    }

    private JsonParser mockJsonParserWithObject(String jsonString) throws IOException {
        ObjectReader objectReader = new ObjectMapper().readerFor(JsonNode.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.readValueAsTree(eq(JsonFactory.instance), any()))
                .thenReturn(new ObjectMapper().readTree(jsonString));
        when(parser.getParserConfig())
                .thenReturn(new JsonParser.DefaultConfig());
        return parser;
    }

    private JsonParser mockJsonParserWithArray(String jsonArray) throws IOException {
        ObjectReader objectReader = new ObjectMapper().readerFor(JsonNode.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.readValueAsTree(eq(JsonFactory.instance), any()))
                .thenReturn(new ObjectMapper().readTree(jsonArray));
        when(parser.getParserConfig())
                .thenReturn(new JsonParser.DefaultConfig());
        return parser;
    }

    private JsonParser mockJsonParserWithNullValue() throws IOException {
        ObjectReader objectReader = new ObjectMapper().readerFor(JsonNode.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextValue())
                .thenReturn(null);
        when(parser.getParserConfig())
                .thenReturn(new JsonParser.DefaultConfig());
        return parser;
    }

    private JsonParser mockJsonParserWithInt(int value) throws IOException {
        ObjectReader objectReader = new ObjectMapper().readerFor(JsonNode.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextValue())
                .thenReturn(JsonNodeFactory.instance.numberNode(value));
        when(parser.getParserConfig())
                .thenReturn(new JsonParser.DefaultConfig());
        return parser;
    }

    private JsonParser mockJsonParserWithFloat(float value) throws IOException {
        ObjectReader objectReader = new ObjectMapper().readerFor(JsonNode.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextValue())
                .thenReturn(JsonNodeFactory.instance.numberNode(value));
        when(parser.getParserConfig())
                .thenReturn(new JsonParser.DefaultConfig());
        return parser;
    }

    private JsonParser mockJsonParserWithBoolean(boolean value) throws IOException {
        ObjectReader objectReader = new ObjectMapper().readerFor(JsonNode.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextValue())
                .thenReturn(JsonNodeFactory.instance.booleanNode(value));
        when(parser.getParserConfig())
                .thenReturn(new JsonParser.DefaultConfig());
        return parser;
    }
}