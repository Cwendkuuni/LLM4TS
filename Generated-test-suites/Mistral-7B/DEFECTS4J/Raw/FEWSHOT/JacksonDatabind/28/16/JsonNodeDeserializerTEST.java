package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser mockParser = createMockParser(JsonToken.NULL);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mockParser, new TestContext(), null);
        assertEquals(JsonNode.Value.nullValue(), result);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser mockParser = createMockParser(json);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mockParser, new TestContext(), new JsonNodeFactory());

        ObjectNode expectedObject = new ObjectNode(new JsonFactory());
        expectedObject.put("name", TextNode.valueOf("John"));
        expectedObject.put("age", NumberNode.value(30));

        assertEquals(JsonNodeType.OBJECT, result.getNodeType());
        assertEquals(expectedObject, result);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser mockParser = createMockParser(json);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mockParser, new TestContext(), new JsonNodeFactory());

        ArrayNode expectedArray = new ArrayNode(new JsonFactory());
        expectedArray.add(NumberNode.value(1));
        expectedArray.add(NumberNode.value(2));
        expectedArray.add(NumberNode.value(3));

        assertEquals(JsonNodeType.ARRAY, result.getNodeType());
        assertEquals(expectedArray, result);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        String json = "true";
        JsonParser mockParser = createMockParser(json);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mockParser, new TestContext(), new JsonNodeFactory());

        assertEquals(JsonNodeType.BOOLEAN, result.getNodeType());
        assertTrue(result.asBoolean());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String json = "Invalid JSON";
        JsonParser mockParser = createMockParser(JsonToken.VALUE_STRING, json);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mockParser, new TestContext(), new JsonNodeFactory());
    }

    @Test
    public void testGetDeserializerByNodeClass() {
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertSame(ObjectDeserializer.class, deserializer.getClass());

        deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertSame(ArrayDeserializer.class, deserializer.getClass());

        deserializer = JsonNodeDeserializer.getDeserializer(NumberNode.class);
        assertSame(JsonNodeDeserializer.class, deserializer.getClass());
    }

    private JsonParser createMockParser(JsonToken... tokens) {
        MockJsonParser mockParser = new MockJsonParser();
        for (JsonToken token : tokens) {
            mockParser.nextToken(token);
        }
        return mockParser;
    }

    private static class TestContext extends DeserializationContext {
        JsonNodeFactory nodeFactory = new JsonNodeFactory();
    }
}