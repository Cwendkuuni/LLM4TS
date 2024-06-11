package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNode result = deserializer.getNullValue(ctxt);
        Assert.assertTrue(result instanceof NullNode);
    }

    @Test
    public void testDeprecatedGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue();
        Assert.assertTrue(result instanceof NullNode);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonParser parser = new ObjectMapper().getJsonFactory().createParser("{}");
        JsonNode result = deserializer.deserialize(parser, ctxt);
        Assert.assertTrue(result instanceof ObjectNode);
    }

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        Assert.assertTrue(objectDeserializer instanceof JsonNodeDeserializer.ObjectDeserializer);

        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        Assert.assertTrue(arrayDeserializer instanceof JsonNodeDeserializer.ArrayDeserializer);

        JsonDeserializer<JsonNode> genericDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        Assert.assertTrue(genericDeserializer instanceof JsonNodeDeserializer);
    }

    @Test(expected = RuntimeException.class)
    public void testHandleDuplicateField() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer() {
            @Override
            protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt,
                                                 JsonNodeFactory nodeFactory,
                                                 String fieldName, ObjectNode objectNode,
                                                 JsonNode oldValue, JsonNode newValue) throws IOException {
                throw new RuntimeException("Duplicate field found");
            }
        };
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonParser parser = new ObjectMapper().getJsonFactory().createParser("{}");

        JsonNode result = deserializer.deserialize(parser, ctxt);
        Assert.assertTrue(result instanceof ObjectNode);
    }

    @Test(expected = RuntimeException.class)
    public void testDeserializeInvalidToken() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonParser parser = new ObjectMapper().getJsonFactory().createParser("invalid");
        deserializer.deserialize(parser, ctxt);
    }

}