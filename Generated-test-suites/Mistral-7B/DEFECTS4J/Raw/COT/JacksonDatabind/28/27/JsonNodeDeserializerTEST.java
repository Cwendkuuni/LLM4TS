package com.example.jsonnodedeserializertest;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    @Test
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        JsonParser parser = mapper.getFactory().createParser("null");
        JsonNode result = deserializer.deserialize(parser, new TestContext(), null);
        Assert.assertEquals(JsonNode.Value.NULL, result.getValueAsToken());
    }

    @Test
    public void testObjectDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        ObjectNodeDeserializer objectDeserializer = ObjectDeserializer.getInstance();
        JsonNode result = objectDeserializer.deserialize(parser, new TestContext(), null);
        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.get("name").asText());
        Assert.assertEquals(30, result.get("age").asInt());
    }

    @Test
    public void testArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,\"three\",null]");
        ArrayNodeDeserializer arrayDeserializer = ArrayDeserializer.getInstance();
        JsonNode result = arrayDeserializer.deserialize(parser, new TestContext(), null);
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(1, result.get(0).asInt());
        Assert.assertEquals(2, result.get(1).asInt());
        Assert.assertEquals("three", result.get(2).asText());
        Assert.assertEquals(JsonNode.Value.NULL, result.get(3).getValueAsToken());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{invalid:\"JSON\"}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        deserializer.deserialize(parser, new TestContext(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[invalid]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        deserializer.deserialize(parser, new TestContext(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonObjectWithDuplicateField() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"name\":\"Doe\"}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        deserializer.deserialize(parser, new TestContext(), null);
    }

    @Test
    public void testEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        JsonNode result = deserializer.deserialize(parser, new TestContext(), null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        JsonNode result = deserializer.deserialize(parser, new TestContext(), null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDeserializationWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,\"three\",null]");
        TypeDeserializer typeDeserializer = new JsonNodeTypeDeserializer();
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        JsonNode result = deserializer.deserializeWithType(parser, new TestContext(), typeDeserializer);
        Assert.assertNotNull(result);
        Assert.assertEquals(JsonNodeType.ARRAY, result.getNodeType());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonNodeFactory getNodeFactory() {
            return new StandardNodeFactory();
        }

        @Override
        public DeserializationFeature[] getActiveFeatures() {
            return new DeserializationFeature[0];
        }
    }

    private static class JsonNodeTypeDeserializer extends StdDeserializer<JsonNodeType> {
        public JsonNodeTypeDeserializer() {
            this(JsonNodeType.class);
        }

        private JsonNodeTypeDeserializer(Class<JsonNodeType> t) {
            super(t);
        }

        @Override
        public JsonNodeType deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            switch (p.getCurrentTokenId()) {
            case JsonTokenId.ID_START_OBJECT:
                return JsonNodeType.OBJECT;
            case JsonTokenId.ID_START_ARRAY:
                return JsonNodeType.ARRAY;
            default:
                return JsonNodeType.ANY;
            }
        }
    }
}

enum JsonNodeType {
    OBJECT, ARRAY, ANY
}