package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    @Test
    public void testObjectDeserializer() throws IOException, JsonProcessingException {
        JsonParser p = createParser("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode objectNode = (ObjectNode) objectDeserializer.deserialize(p, new TestDeserializationContext());
        Assert.assertEquals("John", objectNode.get("name").asText());
        Assert.assertEquals(30, objectNode.get("age").asInt());
        Assert.assertEquals("New York", objectNode.get("city").asText());
    }

    @Test
    public void testArrayDeserializer() throws IOException, JsonProcessingException {
        JsonParser p = createParser("[1, 2, 3]");
        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode arrayNode = (ArrayNode) arrayDeserializer.deserialize(p, new TestDeserializationContext());
        Assert.assertEquals(3, arrayNode.size());
        Assert.assertEquals(1, arrayNode.get(0).asInt());
        Assert.assertEquals(2, arrayNode.get(1).asInt());
        Assert.assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testGenericDeserializer() throws IOException, JsonProcessingException {
        JsonParser p = createParser("{ \"foo\": 1, \"bar\": [1, 2, 3] }");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.instance;
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(p, new TestDeserializationContext());
        Assert.assertEquals(JsonType.OBJECT, jsonNode.getType());
        ObjectNode objectNode = (ObjectNode) jsonNode;
        Assert.assertEquals("1", objectNode.get("foo").asText());
        ArrayNode arrayNode = (ArrayNode) objectNode.get("bar");
        Assert.assertEquals(3, arrayNode.size());
        Assert.assertEquals(1, arrayNode.get(0).asInt());
        Assert.assertEquals(2, arrayNode.get(1).asInt());
        Assert.assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializeNullValue() throws IOException, JsonProcessingException {
        JsonParser p = createParser("null");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.instance;
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(p, new TestDeserializationContext());
        Assert.assertEquals(JsonType.NULL, jsonNode.getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException, JsonProcessingException {
        JsonParser p = createParser("{ \"name\": \"John\" }");
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.instance;
        jsonNodeDeserializer.deserialize(p, new TestDeserializationContext());
    }

    @Test
    public void testHandleDuplicateField() throws IOException, JsonProcessingException {
        JsonParser p = createParser("{\"name\": \"John\", \"name\": 30}");
        JsonDeserializationContext context = new TestDeserializationContext();
        JsonNodeFactory nodeFactory = new TestJsonNodeFactory();
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.instance;
        ObjectNode objectNode = (ObjectNode) jsonNodeDeserializer.deserialize(p, context);
        Assert.assertEquals(2, objectNode.size());
        String firstName = objectNode.get("name").asText();
        Assert.assertEquals("John", firstName);
        JsonNode secondName = objectNode.get("name");
        Assert.assertNotNull(secondName);
        Assert.assertEquals(JsonType.INT, secondName.getType());
        Assert.assertEquals(30, secondName.asInt());
    }

    private JsonParser createParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
    }

    static class TestDeserializationContext extends DeserializationContext {
        TestDeserializationContext() {
            super(new TestAnnotationIntrospector(), new TestObjectMapper.DefaultAnnotationIntrospector());
        }
    }

    static class TestJsonNodeFactory extends JsonNodeFactory {
        @Override
        public RawValue newRawValueNode(Object o) {
            return super.newRawValueNode(o);
        }

        @Override
        public BinaryNode newBinaryNode(byte[] v) {
            return super.newBinaryNode(v);
        }

        @Override
        public JsonNode newValueNode(String textValue) {
            return super.newValueNode(textValue);
        }

        @Override
        public ObjectNode newObjectNode() {
            return (ObjectNode) super.newObjectNode();
        }

        @Override
        public ArrayNode newArrayNode() {
            return (ArrayNode) super.newArrayNode();
        }

        @Override
        public NumberNode newNumberNode(Number n) {
            return super.newNumberNode(n);
        }

        @Override
        public TextNode newTextNode(CharSequence textValue) {
            return super.newTextNode(textValue);
        }

        @Override
        public Boolean newBooleanNode(boolean b) {
            return super.newBooleanNode(b);
        }

        @Override
        public NullNode newNullNode() {
            return super.newNullNode();
        }
    }
}