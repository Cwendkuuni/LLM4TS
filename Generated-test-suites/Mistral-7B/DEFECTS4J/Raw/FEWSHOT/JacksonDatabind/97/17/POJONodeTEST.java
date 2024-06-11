package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());

        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        assertNull(node.binaryValue());

        assertEquals("{\"class\":\"java.lang.Object\", \"hashCode\":\"xxx\", \"toString\":\"java.lang.Object@xxx\"}", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(1, node.asInt(0));
        assertEquals(1L, node.asLong(0L));
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        Object pojo = new Object();
        POJONode nodeWithPojo = new POJONode(pojo);
        Object pojoFromNode = nodeWithPojo.getPojo();
        assertEquals(pojo, pojoFromNode);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, null);

        String expected = String.format("{\"class\":\"%s\", \"%s\":%s}", pojo.getClass().getName(), pojo.getClass().getSimpleName(), mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo));
        String actual = gen.getOutputContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ObjectMapper mapper = new ObjectMapper();
        Object pojo1 = new Object();
        Object pojo2 = new Object();
        POJONode node1 = new POJONode(pojo1);
        POJONode node2 = new POJONode(pojo2);
        POJONode node3 = new POJONode(pojo1);

        assertTrue(node1.equals(node3));
        assertFalse(node1.equals(node2));
    }
}