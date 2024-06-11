package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class JsonNodeDeserializerTest extends TestBase {

    private JsonNode deserializeAnyJson(String jsonText) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonText);
        return node;
    }

    @Test
    public void testGetDeserializer() throws IOException {
        ObjectNode objNode = new ObjectMapper().readValue("{ \"foo\": 1 }", ObjectNode.class);
        ArrayNode arrNode = new ObjectMapper().readValue("[1,2,3]", ArrayNode.class);
        JsonNode node = new ObjectMapper().readValue("{}", JsonNode.class);

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonDeserializer<ObjectNode> objDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonDeserializer<ArrayNode> arrDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);

        assertEquals(deserializer.getClass(), instance.getClass());
        assertEquals(objDeserializer.getClass(), ObjectDeserializer.class);
        assertEquals(arrDeserializer.getClass(), ArrayDeserializer.class);

        JsonNode deserialized = deserializer.deserialize(null, new TestContext(getMapper()), null);
        assertEquals(deserialized.getClass(), JsonNode.class);

        ObjectNode objDeserialized = (ObjectNode) objDeserializer.deserialize(null, new TestContext(getMapper()), null);
        assertEquals(objDeserialized.getClass(), ObjectNode.class);

        ArrayNode arrDeserialized = (ArrayNode) arrDeserializer.deserialize(null, new TestContext(getMapper()), null);
        assertEquals(arrDeserialized.getClass(), ArrayNode.class);
    }

    @Test
    public void testNullValue() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode node = deserializer.getNullValue(new TestContext(getMapper()));
        assertEquals(node.getClass(), NullNode.class);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "{\"foo\": 1, \"bar\": [1, 2, 3], \"baz\": null, \"qux\": { \"quux\": 42 }}";

        JsonNode node = deserializer.deserialize(new JsonParser(getMapper().readTree(json)), new TestContext(getMapper()), getMapper().getNodeFactory());

        ObjectNode objNode = (ObjectNode) node;
        assertEquals(objNode.get("foo").asInt(), 1);
        assertEquals(objNode.get("bar").size(), 3);
        assertNull(objNode.get("baz"));

        ObjectNode nestedObjNode = (ObjectNode) objNode.get("qux");
        assertEquals(nestedObjNode.get("quux").asInt(), 42);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "[1, 2, 3, null, { \"quux\": 42 }]";

        JsonNode node = deserializer.deserialize(new JsonParser(getMapper().readTree(json)), new TestContext(getMapper()), getMapper().getNodeFactory());

        ArrayNode arrNode = (ArrayNode) node;
        assertEquals(arrNode.size(), 5);
        assertEquals(arrNode.get(0).asInt(), 1);
        assertNull(arrNode.get(3));
        ObjectNode nestedObjNode = (ObjectNode) arrNode.get(4);
        assertEquals(nestedObjNode.get("quux").asInt(), 42);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "{\"foo\": 1, \"bar\": [1, 2, 3], \"baz\": null, \"qux\": { \"quux\": 42 }, \"quux\": [1, 2, 3]}";

        JsonNode node = deserializer.deserialize(new JsonParser(getMapper().readTree(json)), new TestContext(getMapper()), getMapper().getNodeFactory());

        ObjectNode objNode = (ObjectNode) node.path("foo");
        assertEquals(objNode.asText(), "1");

        ArrayNode arrNode = (ArrayNode) node.path("qux");
        assertEquals(arrNode.size(), 1);
        ObjectNode nestedObjNode = (ObjectNode) arrNode.get(0);
        assertEquals(nestedObjNode.get("quux").asInt(), 42);

        ArrayNode nestedArrNode = (ArrayNode) node.path("quux");
        assertEquals(nestedArrNode.size(), 3);
        assertEquals(nestedArrNode.get(0).asInt(), 1);
        assertEquals(nestedArrNode.get(1).asInt(), 2);
        assertEquals(nestedArrNode.get(2).asInt(), 3);
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "{}";

        JsonNode node = deserializer.deserialize(new JsonParser(getMapper().readTree(json)), new TestContext(getMapper()), getMapper().getNodeFactory());

        ObjectNode objNode = (ObjectNode) node;
        assertTrue(objNode.size() == 0);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        String json = "[]";

        JsonNode node = deserializer.deserialize(new JsonParser(getMapper().readTree(json)), new TestContext(getMapper()), getMapper().getNodeFactory());

        ArrayNode arrNode = (ArrayNode) node;
        assertTrue(arrNode.size() == 0);
    }

    @Test
    public void testDeserializeInvalidJson() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        try {
            deserializer.deserialize(new JsonParser(new CharArrayInput(new Character[]{'{'})), new TestContext(getMapper()), getMapper().getNodeFactory());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            deserializer.deserialize(new JsonParser(new CharArrayInput(new Character[]{'{'})), new TestContext(getMapper()), getMapper().getNodeFactory());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            deserializer.deserialize(new JsonParser(new CharArrayInput(new Character[]{'{'})), new TestContext(getMapper()), getMapper().getNodeFactory());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            deserializer.deserialize(new JsonParser(new CharArrayInput(new Character[]{'{'})), new TestContext(getMapper()), getMapper().getNodeFactory());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testHandleDuplicateField() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode objectNode = getMapper().getNodeFactory().objectNode();
        objectNode.set("foo", new IntNode(1));

        try {
            deserializer.deserialize(new JsonParser(getMapper().readTree("{\"foo\": 2}")), new TestContext(getMapper()), objectNode);
            fail();
        } catch (JsonMappingException e) {
            // Expected, since FAIL_ON_READING_DUP_TREE_KEY is enabled by default
        }
    }

    @Test
    public void testHandleDuplicateFieldDisabled() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode objectNode = getMapper().getNodeFactory().objectNode();
        objectNode.set("foo", new IntNode(1));

        JsonNode node = deserializer.deserialize(new JsonParser(mapper.readTree("{\"foo\": 2}")), new TestContext(mapper), objectNode);
        ObjectNode result = (ObjectNode) node;
        assertEquals(result.get("foo").asInt(), 2);
    }
}