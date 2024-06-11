package com.example.jsonnodedeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        Assert.assertSame(objectDeserializer, jsonNodeDeserializer);

        JsonNodeDeserializer jsonNodeDeserializer1 = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        Assert.assertSame(arrayDeserializer, jsonNodeDeserializer1);

        JsonNodeDeserializer jsonNodeDeserializer2 = JsonNodeDeserializer.getDeserializer(String.class.getName().equals("com.example.MyJsonNode") ? MyJsonNode.class : JsonNode.class);
        BaseNodeDeserializer baseNodeDeserializer = (BaseNodeDeserializer) jsonNodeDeserializer2;
        Assert.assertNotNull(baseNodeDeserializer);
    }

    @Test
    public void testGetNullValue() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode nullNode = jsonNodeDeserializer.getNullValue(new TestContext());
        Assert.assertNotNull(nullNode);
        Assert.assertEquals(JsonNodeType.NULL, nullNode.getNodeType());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser parser = createParser("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode objectNode = objectDeserializer.deserialize(parser, new TestContext());
        Assert.assertEquals("John", objectNode.get("name").textValue());
        Assert.assertEquals(30, objectNode.get("age").numberValue().intValue());
        Assert.assertEquals("New York", objectNode.get("city").textValue());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser parser = createParser("[1,2,3,4,5]");
        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode arrayNode = arrayDeserializer.deserialize(parser, new TestContext());
        Assert.assertEquals(5, arrayNode.size());
        Assert.assertEquals(1, arrayNode.get(0).numberValue().intValue());
        Assert.assertEquals(2, arrayNode.get(1).numberValue().intValue());
        Assert.assertEquals(3, arrayNode.get(2).numberValue().intValue());
        Assert.assertEquals(4, arrayNode.get(3).numberValue().intValue());
        Assert.assertEquals(5, arrayNode.get(4).numberValue().intValue());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonParser parser = createParser("\"Hello World!\"");
        JsonDeserializer<JsonNode> jsonDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode jsonNode = jsonDeserializer.deserialize(parser, new TestContext());
        Assert.assertEquals("Hello World!", jsonNode.textValue());

        parser.nextToken(); // consume "{"
        JsonParser.Token currentToken = parser.getCurrentToken();
        JsonNode deserializedNode = jsonDeserializer.deserialize(parser, new TestContext());
        Assert.assertEquals(JsonNodeType.OBJECT, deserializedNode.getNodeType());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser parser = createParser("{\"name\":\"John\"age\":30}");
        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        objectDeserializer.deserialize(parser, new TestContext());
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(json));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonNodeFactory nodeFactory() {
            return new StandardJsonNodeFactory();
        }

        @Override
        public JsonDeserializerProvider deserializerProvider() {
            return new StandardDeserializerProvider();
        }

        @Override
        public ObjectReader reader() {
            return null;
        }

        @Override
        public ObjectFactory objectFactory() {
            return null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object other) {
            return this == other;
        }
    }
}