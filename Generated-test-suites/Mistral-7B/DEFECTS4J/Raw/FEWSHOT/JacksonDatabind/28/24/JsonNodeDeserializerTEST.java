package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Test;

public class JsonNodeDeserializerTest extends TestBase {

    @Test
    public void testDeserializeObject() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(
                "{\"foo\": 1, \"bar\": [1, 2], \"baz\": null, \"qux\": {}}");
        JsonNode result = deserializer.deserialize(JsonFactory.instance.createParser(mapper.writeValueAsString(node)), null);
        assertEquals(JsonNodeType.OBJECT, result.getNodeType());
        ObjectNode obj = (ObjectNode) result;
        assertEquals("1", obj.get("foo").asText());
        assertEquals(1, obj.get("bar").size());
        assertNotNull(obj.get("qux"));
        assertEquals(JsonNodeType.ARRAY, obj.get("bar").getNodeType());
        assertEquals("1", obj.get("bar").get(0).asText());
        assertEquals("2", obj.get("bar").get(1).asText());
        assertNull(obj.get("baz"));
    }

    @Test
    public void testDeserializeArray() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, 2, null, {}]");
        JsonNode result = deserializer.deserialize(JsonFactory.instance.createParser(mapper.writeValueAsString(node)), null);
        assertEquals(JsonNodeType.ARRAY, result.getNodeType());
        ArrayNode arr = (ArrayNode) result;
        assertEquals(4, arr.size());
        assertEquals("1", arr.get(0).asText());
        assertEquals(JsonNodeType.NULL, arr.get(2).getNodeType());
        assertNotNull(arr.get(3));
        assertEquals(JsonNodeType.OBJECT, arr.get(3).getNodeType());
    }

    @Test
    public void testDeserializeNull() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = JsonFactory.instance.createParser("null");
        JsonNode result = deserializer.deserialize(parser, null);
        assertEquals(JsonNodeType.NULL, result.getNodeType());
    }

    @Test
    public void testDeserializeInvalidJson() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = JsonFactory.instance.createParser("{invalid:json}");
        assertNull(deserializer.deserialize(parser, null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonException() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"foo\":[\"bar\"");
        deserializer.deserialize(JsonFactory.instance.createParser(mapper.writeValueAsString(node)), null);
    }
}