package com.fasterxml.jackson.databind.deser.std; // adjust the package if needed

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Test cases for {@link JsonNodeDeserializer} class.
 */
public class JsonNodeDeserializerTest {

    

    @Test
    public void testGetDeserializer() throws IOException {
        JsonNode node = JsonNodeFactory.instance.objectNode();
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(node.getClass());
        Assert.assertEquals(JsonNodeDeserializer.instance.getClass(), deserializer.getClass());
    }

    @Test
    public void testGetNullValue() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
        JsonNode nullNode = deserializer.getNullValue(null);
        Assert.assertNotNull(nullNode);
        Assert.assertEquals(JsonNode.ValueType.NULL, nullNode.getNodeType());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("{\"name\": \"John\", \"age\": 30}");
        JsonNode expected = nodeFactory.objectNode()
                .put("name", nodeFactory.textNode("John"))
                .put("age", nodeFactory.numberNode(30));

        JsonNode actual = deserializer.deserialize(parser, null, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("[1, 2, 3]");
        JsonNode expected = nodeFactory.arrayNode()
                .add(nodeFactory.numberNode(1))
                .add(nodeFactory.numberNode(2))
                .add(nodeFactory.numberNode(3));

        JsonNode actual = deserializer.deserialize(parser, null, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("{ \"name\": \"John\" }");
        JsonNode expectedObject = nodeFactory.objectNode()
                .put("name", nodeFactory.textNode("John"));

        parser.setValue("[1, 2, 3]");
        JsonNode expectedArray = nodeFactory.arrayNode()
                .add(nodeFactory.numberNode(1))
                .add(nodeFactory.numberNode(2))
                .add(nodeFactory.numberNode(3));

        parser.setValue("\"Hello World\"");
        JsonNode expectedString = nodeFactory.textNode("Hello World");

        parser.setValue(true);
        JsonNode expectedBooleanTrue = nodeFactory.booleanNode(true);

        parser.setValue(false);
        JsonNode expectedBooleanFalse = nodeFactory.booleanNode(false);

        parser.setValue(null);
        JsonNode expectedNull = JsonNodeFactory.instance.nullNode();

        JsonNode actual = deserializer.deserializeAny(parser, null, null);

        Assert.assertEquals(expectedObject, actual);
        Assert.assertEquals(expectedArray, actual);
        Assert.assertEquals(expectedString, actual);
        Assert.assertEquals(expectedBooleanTrue, actual);
        Assert.assertEquals(expectedBooleanFalse, actual);
        Assert.assertEquals(expectedNull, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("{\"invalid\": {"); // missing end object
        deserializer.deserialize(parser, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson2() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("{ \"name\": [1, 2, 3] }"); // invalid JSON structure
        deserializer.deserialize(parser, null, null);
    }

    @Test
    public void testDeserializeObjectWithDuplicateKeys() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("{\"name\": \"John\", \"name\": \"Jane\"}");
        JsonNode actual = deserializer.deserialize(parser, null, null);
        JsonNode expected = nodeFactory.objectNode()
                .put("name", nodeFactory.textNode("Jane")); // the last one takes precedence

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeNullObject() throws IOException {
        JsonParser parser = new JsonParser();
        parser.setCodec(new ObjectMapper());
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;

        parser.setValue("{}");
        JsonNode actual = deserializer.deserialize(parser, null, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode();

        Assert.assertEquals(expected, actual);
    }

    
}