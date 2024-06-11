
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    private final JsonFactory factory = new JsonFactory();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserialize_ObjectNode() throws IOException {
        String json = "{\"key\": \"value\"}";
        JsonParser parser = factory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        JsonNode jsonNode = deserializer.deserialize(parser, context);
        Assert.assertTrue(jsonNode instanceof ObjectNode);

        ObjectNode objectNode = (ObjectNode) jsonNode;
        Assert.assertEquals(new TextNode("value"), objectNode.get("key"));
    }

    @Test
    public void testDeserialize_ObjectNode_Empty() throws IOException {
        String json = "{}";
        JsonParser parser = factory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        JsonNode jsonNode = deserializer.deserialize(parser, context);
        Assert.assertTrue(jsonNode instanceof ObjectNode);

        ObjectNode objectNode = (ObjectNode) jsonNode;
        Assert.assertTrue(objectNode.isEmpty());
    }

    @Test
    public void testDeserialize_ArrayNode() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = factory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        JsonNode jsonNode = deserializer.deserialize(parser, context);
        Assert.assertTrue(jsonNode instanceof ArrayNode);

        ArrayNode arrayNode = (ArrayNode) jsonNode;
        Assert.assertEquals(new IntNode(1), arrayNode.get(0));
        Assert.assertEquals(new IntNode(2), arrayNode.get(1));
        Assert.assertEquals(new IntNode(3), arrayNode.get(2));
    }

    @Test
    public void testDeserialize_NullNode() throws IOException {
        String json = "null";
        JsonParser parser = factory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        JsonNode jsonNode = deserializer.deserialize(parser, context);
        Assert.assertTrue(jsonNode instanceof NullNode);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_Error() throws IOException {
        String json = "{";
        JsonParser parser = factory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        deserializer.deserialize(parser, context);
    }
}
