package com.example.jsonnodedeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    private ObjectMapper mapper;
    private JsonNodeFactory nodeFactory;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
    }

    @Test
    public void testGetNullValue() throws IOException {
        JsonParser p = mapper.getFactory().createParser("");
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(p, null);
        Assert.assertEquals(JsonNode.Value.nullValue(), result);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"field1\":1,\"field2\":\"value2\"}");
        ObjectNode result = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(p, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("field1", result.fieldNames().nextValue());
        Assert.assertEquals(JsonNode.Type.NUMBER, result.getType(0));
        Assert.assertEquals(1, result.get(0).asInt());
        Assert.assertEquals("field2", result.fieldNames().nextValue());
        Assert.assertEquals(JsonNode.Type.TEXT, result.getType(1));
        Assert.assertEquals("value2", result.get(1).asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[1,2,\"three\",null]");
        ArrayNode result = (ArrayNode) JsonNodeDeserializer.getInstance().deserialize(p, null);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(JsonNode.Type.NUMBER, result.getType(0));
        Assert.assertEquals(1, result.get(0).asInt());
        Assert.assertEquals(JsonNode.Type.NUMBER, result.getType(1));
        Assert.assertEquals(2, result.get(1).asInt());
        Assert.assertEquals(JsonNode.Type.TEXT, result.getType(2));
        Assert.assertEquals("three", result.get(2).asText());
        Assert.assertEquals(JsonNode.Value.nullValue(), result.get(3));
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnexpectedToken() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{1}");
        JsonNodeDeserializer.getInstance().deserialize(p, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{1}");
        p.nextToken(); // consume [
        p.nextToken(); // consume {
        p.nextToken(); // consume field name
        p.nextToken(); // consume :
        p.nextToken(); // consume invalid token
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{ \"embedded\": { \"field\": 1 } }");
        ObjectNode result = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(p, null);
        ObjectNode embeddedNode = (ObjectNode) result.get("embedded");
        Assert.assertEquals(1, embeddedNode.size());
        Assert.assertEquals("field", embeddedNode.fieldNames().nextValue());
        Assert.assertEquals(JsonNode.Type.NUMBER, embeddedNode.getType(0));
        Assert.assertEquals(1, embeddedNode.get(0).asInt());
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{}");
        ObjectNode result = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(p, null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[]");
        ArrayNode result = (ArrayNode) JsonNodeDeserializer.getInstance().deserialize(p, null);
        Assert.assertEquals(0, result.size());
    }
}