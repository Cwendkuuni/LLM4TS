package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer.ObjectDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    @Test
    public void testJsonNodeDeserializer() throws IOException {
        JsonParser jsonParser = createParser("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        JsonNode expectedObjectNode = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.textNode("John"))
                .put("age", JsonNodeFactory.instance.numberNode(30))
                .put("city", JsonNodeFactory.instance.textNode("New York"));

        JsonNode actualObjectNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(expectedObjectNode, actualObjectNode);

        JsonParser jsonParserArray = createParser("[1, 2, 3, 4]");
        JsonNode expectedArrayNode = JsonNodeFactory.instance.arrayNode(
                JsonNodeFactory.instance.numberNode(1),
                JsonNodeFactory.instance.numberNode(2),
                JsonNodeFactory.instance.numberNode(3),
                JsonNodeFactory.instance.numberNode(4)
        );

        JsonNode actualArrayNode = JsonNodeDeserializer.getDeserializer(ArrayNode.class)
                .deserialize(jsonParserArray, new TestContext());

        Assert.assertEquals(expectedArrayNode, actualArrayNode);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonNodeDeserializerInvalidJson() throws IOException {
        JsonParser jsonParser = createParser("{Name: \"John\",age:30,\"city\":\"New York\"}"); // wrong key capitalization
        JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonParser, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonNodeDeserializerEmptyObject() throws IOException {
        JsonParser jsonParser = createParser("{}");
        JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonParser, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonNodeDeserializerEmptyArray() throws IOException {
        JsonParser jsonParser = createParser("[]");
        JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(jsonParser, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonNodeDeserializerDuplicateField() throws IOException {
        JsonParser jsonParser = createParser("{\"name\":\"John\",\"name\":\"John\"}");
        JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonParser, new TestContext());
    }

    @Test
    public void testJsonNodeDeserializerEmptyJson() throws IOException {
        JsonParser jsonParser = createParser("{}");
        JsonNode actualNullNode = JsonNodeDeserializer.getDeserializer(JsonNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(JsonNodeFactory.instance.nullNode(), actualNullNode);
    }

    @Test
    public void testJsonNodeDeserializerNullValue() throws IOException {
        JsonParser jsonParser = createParser("{ \"name\":null }");
        JsonNode actualNullNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(JsonNodeFactory.instance.nullNode(), actualNullNode);
    }

    @Test
    public void testJsonNodeDeserializerBooleanValue() throws IOException {
        JsonParser jsonParser = createParser("{ \"isAdmin\":true }");
        JsonNode actualBooleanNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(JsonNodeFactory.instance.booleanNode(true), actualBooleanNode);
    }

    @Test
    public void testJsonNodeDeserializerIntegerValue() throws IOException {
        JsonParser jsonParser = createParser("{ \"age\":5 }");
        JsonNode actualIntegerNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(JsonNodeFactory.instance.numberNode(5), actualIntegerNode);
    }

    @Test
    public void testJsonNodeDeserializerFloatValue() throws IOException {
        JsonParser jsonParser = createParser("{ \"price\":3.14 }");
        JsonNode actualFloatNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(JsonNodeFactory.instance.numberNode(3.14), actualFloatNode);
    }

    @Test
    public void testJsonNodeDeserializerStringValue() throws IOException {
        JsonParser jsonParser = createParser("{ \"message\":\"Hello, World!\" }");
        JsonNode actualStringNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(JsonNodeFactory.instance.textNode("Hello, World!"), actualStringNode);
    }

    @Test
    public void testJsonNodeDeserializerNestedObject() throws IOException {
        JsonParser jsonParser = createParser("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}");
        JsonNode expectedNestedObjectNode = JsonNodeFactory.instance.objectNode()
                .put("first", JsonNodeFactory.instance.textNode("John"))
                .put("last", JsonNodeFactory.instance.textNode("Doe"));

        JsonNode actualNestedObjectNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        Assert.assertEquals(expectedNestedObjectNode, actualNestedObjectNode);
    }

    @Test
    public void testJsonNodeDeserializerNestedArray() throws IOException {
        JsonParser jsonParser = createParser("{\"hobbies\":[\"swimming\",\"reading\",\"programming\"]}");
        JsonNode expectedNestedArrayNode = JsonNodeFactory.instance.arrayNode(
                JsonNodeFactory.instance.textNode("swimming"),
                JsonNodeFactory.instance.textNode("reading"),
                JsonNodeFactory.instance.textNode("programming")
        );

        JsonNode actualNestedArrayNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        JsonNode hobbiesNode = actualNestedArrayNode.path("hobbies");
        Assert.assertEquals(expectedNestedArrayNode, hobbiesNode);
    }

    @Test
    public void testJsonNodeDeserializerEmptyNestedObject() throws IOException {
        JsonParser jsonParser = createParser("{\"name\":{}}");
        JsonNode actualEmptyNestedObjectNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        JsonNode nameNode = actualEmptyNestedObjectNode.path("name");
        Assert.assertEquals(JsonNodeFactory.instance.objectNode(), nameNode);
    }

    @Test
    public void testJsonNodeDeserializerEmptyNestedArray() throws IOException {
        JsonParser jsonParser = createParser("{\"hobbies\":{}}");
        JsonNode actualEmptyNestedArrayNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class)
                .deserialize(jsonParser, new TestContext());

        JsonNode hobbiesNode = actualEmptyNestedArrayNode.path("hobbies");
        Assert.assertEquals(JsonNodeFactory.instance.arrayNode(), hobbiesNode);
    }

    private JsonParser createParser(String jsonString) throws IOException {
        StringReader reader = new StringReader(jsonString);
        JsonParser jsonParser = new ObjectMapper().readerFor(JsonNode.class).readValue(reader);
        return jsonParser;
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getConfig() {
            return new ObjectMapper();
        }

        @Override
        public JsonNodeFactory nodeFactory() {
            return JsonNodeFactory.instance;
        }
    }
}