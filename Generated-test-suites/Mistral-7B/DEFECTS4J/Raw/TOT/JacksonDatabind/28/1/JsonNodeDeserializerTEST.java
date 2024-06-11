package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class JsonNodeDeserializerTest {

    @Test
    void testDeserializeNullValue() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("null"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
        assertEquals(JsonNode.Value.NULL, result.getNodeType());
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    void testDeserializeEmptyObject() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{}"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
        assertTrue(result instanceof ObjectNode);
        assertEquals(0, result.size());
    }

    @Test
    void testDeserializeEmptyArray() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("[{}]"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
        assertTrue(result instanceof ArrayNode);
        assertEquals(0, result.size());
    }

    @Test
    void testDeserializeSimpleObject() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{\"field1\":1,\"field2\":\"hello\"}"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ObjectNode obj = (ObjectNode) result;
        assertEquals("field1", obj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.NUMBER, obj.get(obj.fieldNames().nextValue()).getNodeType());
        assertEquals(1, obj.get(obj.fieldNames().nextValue()).asInt());

        assertEquals("field2", obj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.TEXT, obj.get(obj.fieldNames().nextValue()).getNodeType());
        assertEquals("hello", obj.get(obj.fieldNames().nextValue()).asText());
    }

    @Test
    void testDeserializeSimpleArray() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("[1,2,\"hello\",null,true]"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ArrayNode arr = (ArrayNode) result;
        assertEquals(6, arr.size());

        assertEquals(JsonNode.Type.NUMBER, arr.get(0).getNodeType());
        assertEquals(1, arr.get(0).asInt());

        assertEquals(JsonNode.Type.NUMBER, arr.get(1).getNodeType());
        assertEquals(2, arr.get(1).asInt());

        assertEquals(JsonNode.Type.TEXT, arr.get(2).getNodeType());
        assertEquals("hello", arr.get(2).asText());

        assertEquals(JsonNode.Type.NULL, arr.get(3).getNodeType());

        assertEquals(JsonNode.Type.BOOLEAN, arr.get(4).getNodeType());
        assertTrue(arr.get(4).asBoolean());

        assertEquals(JsonNode.Type.NUMBER, arr.get(5).getNodeType());
        assertFalse(arr.get(5).asLong() > 0);
    }

    @Test
    void testDeserializeNestedObjectsAndArrays() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{\"obj1\":{\"field1\":1,\"field2\":[\"hello\",null]},\"obj2\":[{\"field1\":1,\"field2\":\"world\"}]}"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ObjectNode root = (ObjectNode) result;

        ObjectNode obj1 = root.get("obj1");
        assertEquals(2, obj1.size());

        assertEquals("field1", obj1.fieldNames().nextValue());
        assertEquals(JsonNode.Type.NUMBER, obj1.get(obj1.fieldNames().nextValue()).getNodeType());
        assertEquals(1, obj1.get(obj1.fieldNames().nextValue()).asInt());

        assertEquals("field2", obj1.fieldNames().nextValue());
        ArrayNode arr = (ArrayNode) obj1.get(obj1.fieldNames().nextValue());

        assertEquals(2, arr.size());

        assertEquals(JsonNode.Type.TEXT, arr.get(0).getNodeType());
        assertEquals("hello", arr.get(0).asText());

        assertEquals(JsonNode.Type.NULL, arr.get(1).getNodeType());

        ArrayNode arr2 = (ArrayNode) root.get("obj2");
        assertEquals(1, arr2.size());

        ObjectNode nestedObj = (ObjectNode) arr2.get(0);
        assertEquals(2, nestedObj.size());

        assertEquals("field1", nestedObj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.NUMBER, nestedObj.get(nestedObj.fieldNames().nextValue()).getNodeType());
        assertEquals(1, nestedObj.get(nestedObj.fieldNames().nextValue()).asInt());

        assertEquals("field2", nestedObj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.TEXT, nestedObj.get(nestedObj.fieldNames().nextValue()).getNodeType());
        assertEquals("world", nestedObj.get(nestedObj.fieldNames().nextValue()).asText());
    }

    @Test
    void testDeserializeInvalidJson() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{abc"));
        DeserializationContext ctxt = new TestContext();
        assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(parser, ctxt));
    }

    @Test
    void testDeserializeUnrecognizedToken() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{1:\"abc\""));
        DeserializationContext ctxt = new TestContext();
        assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(parser, ctxt));
    }

    @Test
    void testDeserializeObjectWithDuplicateField() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{\"field1\":1,\"field1\":2}"));
        DeserializationContext ctxt = new TestContext();
        ctxt.setConfig(new ObjectMapper.DefaultConfig().disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY));
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ObjectNode obj = (ObjectNode) result;
        assertEquals(1, obj.size());
        assertEquals("field1", obj.fieldNames().nextValue());
        JsonNode value1 = obj.get(obj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.NUMBER, value1.getNodeType());
        assertEquals(1, value1.asInt());

        JsonNode value2 = obj.get(obj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.NUMBER, value2.getNodeType());
        assertEquals(2, value2.asInt());
    }

    @Test
    void testDeserializeObjectWithDuplicateField_ExceptionThrown() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{\"field1\":1,\"field1\":2}"));
        DeserializationContext ctxt = new TestContext();
        ctxt.setConfig(new ObjectMapper.DefaultConfig().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY));
        assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(parser, ctxt));
    }

    @Test
    void testDeserializeObjectWithEmptyStringField() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{\"field\":\"\"}"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ObjectNode obj = (ObjectNode) result;
        assertEquals(1, obj.size());
        assertEquals("field", obj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.TEXT, obj.get(obj.fieldNames().nextValue()).getNodeType());
        assertEquals("", obj.get(obj.fieldNames().nextValue()).asText());
    }

    @Test
    void testDeserializeObjectWithNullField() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("{\"field\":null}"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ObjectNode obj = (ObjectNode) result;
        assertEquals(1, obj.size());
        assertEquals("field", obj.fieldNames().nextValue());
        assertEquals(JsonNode.Type.NULL, obj.get(obj.fieldNames().nextValue()).getNodeType());
    }

    @Test
    void testDeserializeArrayWithEmptyArray() throws IOException {
        JsonParser parser = new JsonParser(new StringReader("[ ]"));
        DeserializationContext ctxt = new TestContext();
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);

        ArrayNode arr = (ArrayNode) result;
        assertEquals(0, arr.size());
    }

    private static class TestContext extends DeserializationContextBase {
        public TestContext() {
            super(null, null, new ObjectMapper());
        }
    }
}