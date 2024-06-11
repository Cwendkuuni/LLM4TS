package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link JsonNodeDeserializer} and its subclasses.
 */
public class JsonNodeDeserializerTest extends TestBase {

    /**
     * Test for the generic {@link JsonNodeDeserializer}.
     */
    @Test
    public void testGenericJsonNodeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        String json = "{\"foo\": 1, \"bar\": [1, 2], \"baz\": null}";
        JsonNode node = mapper.readTree(json, deserializer);
        assertEquals(Object.class, node.getClass());
        assertTrue(node instanceof ObjectNode);
        ObjectNode objNode = (ObjectNode) node;
        assertEquals(2, objNode.size());
        JsonNode fooNode = objNode.get("foo");
        assertEquals(Integer.class, fooNode.getClass().getComponentType());
        assertEquals(1, ((Number) fooNode).intValue());
        JsonNode barNode = objNode.get("bar");
        assertTrue(barNode instanceof ArrayNode);
        ArrayNode arrNode = (ArrayNode) barNode;
        assertEquals(2, arrNode.size());
        JsonNode first = arrNode.get(0);
        assertEquals(Integer.class, first.getClass().getComponentType());
        assertEquals(1, ((Number) first).intValue());
        JsonNode second = arrNode.get(1);
        assertEquals(Integer.class, second.getClass().getComponentType());
        assertEquals(2, ((Number) second).intValue());
        JsonNode nullNode = objNode.get("baz");
        assertNull(nullNode);
    }

    /**
     * Test for the {@link ObjectDeserializer}.
     */
    @Test
    public void testObjectDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ObjectNode> deserializer = ObjectDeserializer.getInstance();
        String json = "{\"foo\": 1, \"bar\": [1, 2], \"baz\": null}";
        ObjectNode node = mapper.readValue(json, deserializer);
        assertEquals(ObjectNode.class, node.getClass());
        assertTrue(node instanceof ObjectNode);
        ObjectNode objNode = (ObjectNode) node;
        assertEquals(2, objNode.size());
        JsonNode fooNode = objNode.get("foo");
        assertEquals(Integer.class, fooNode.getClass().getComponentType());
        assertEquals(1, ((Number) fooNode).intValue());
        JsonNode barNode = objNode.get("bar");
        assertTrue(barNode instanceof ArrayNode);
        ArrayNode arrNode = (ArrayNode) barNode;
        assertEquals(2, arrNode.size());
        JsonNode first = arrNode.get(0);
        assertEquals(Integer.class, first.getClass().getComponentType());
        assertEquals(1, ((Number) first).intValue());
        JsonNode second = arrNode.get(1);
        assertEquals(Integer.class, second.getClass().getComponentType());
        assertEquals(2, ((Number) second).intValue());
        JsonNode nullNode = objNode.get("baz");
        assertNull(nullNode);
    }

    /**
     * Test for the {@link ArrayDeserializer}.
     */
    @Test
    public void testArrayDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ArrayNode> deserializer = ArrayDeserializer.getInstance();
        String json = "[1, 2, null]";
        ArrayNode node = mapper.readValue(json, deserializer);
        assertEquals(ArrayNode.class, node.getClass());
        assertTrue(node instanceof ArrayNode);
        ArrayNode arrNode = (ArrayNode) node;
        assertEquals(3, arrNode.size());
        JsonNode first = arrNode.get(0);
        assertEquals(Integer.class, first.getClass().getComponentType());
        assertEquals(1, ((Number) first).intValue());
        JsonNode second = arrNode.get(1);
        assertEquals(Integer.class, second.getClass().getComponentType());
        assertEquals(2, ((Number) second).intValue());
        JsonNode third = arrNode.get(2);
        assertNull(third);
    }
}