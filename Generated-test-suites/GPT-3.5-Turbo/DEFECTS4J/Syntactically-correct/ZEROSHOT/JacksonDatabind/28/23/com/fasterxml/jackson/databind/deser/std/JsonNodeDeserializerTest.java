package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        Assert.assertNotNull(deserializer);

        deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        Assert.assertNotNull(deserializer);

        deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.getNullValue(null);
        Assert.assertTrue(jsonNode.isNull());

        jsonNode = jsonNodeDeserializer.getNullValue();
        Assert.assertTrue(jsonNode.isNull());
    }

    @Test
    public void testDeserializeWithStartObjectToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.START_OBJECT);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser, null);

        Assert.assertTrue(jsonNode instanceof ObjectNode);
    }

    @Test
    public void testDeserializeWithStartArrayToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.START_ARRAY);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser, null);

        Assert.assertTrue(jsonNode instanceof ArrayNode);
    }
    
    @Test
    public void testDeserializeWithCurrentTokenField() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.FIELD_NAME);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser, null);

        Assert.assertTrue(jsonNode instanceof ObjectNode);
    }

    @Test
    public void testDeserializeWithExpectedStartArrayToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.END_ARRAY);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser, null);

        Assert.assertTrue(jsonNode instanceof ArrayNode);
    }

    @Test
    public void testDeserializeWithEmbeddedObject() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.VALUE_EMBEDDED_OBJECT);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser,null);

        Assert.assertNotNull(jsonNode);
    }

    @Test
    public void testDeserializeWithStringToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.VALUE_STRING);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser,null);

        Assert.assertTrue(jsonNode.isTextual());
    }

    @Test
    public void testDeserializeWithIntToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.VALUE_NUMBER_INT);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser,null);

        Assert.assertTrue(jsonNode.isInt());
    }

    @Test
    public void testDeserializeWithBooleanToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.VALUE_TRUE);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser,null);

        Assert.assertTrue(jsonNode.isBoolean());
    }

    @Test
    public void testDeserializeWithNullToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.VALUE_NULL);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser,null);

        Assert.assertTrue(jsonNode.isNull());
    }

    @Test(expected = RuntimeException.class)
    public void testDeserializeWithUnexpectedToken() throws IOException {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonParser jsonParser = new MockJsonParser(JsonToken.END_OBJECT);

        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser,null);
    }
}