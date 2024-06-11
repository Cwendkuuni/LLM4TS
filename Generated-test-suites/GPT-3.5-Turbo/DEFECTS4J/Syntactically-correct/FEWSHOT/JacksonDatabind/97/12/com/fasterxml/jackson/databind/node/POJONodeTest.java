
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        byte[] bytes = new byte[]{1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode trueNode = new POJONode(true);
        assertTrue(trueNode.asBoolean(false));

        POJONode falseNode = new POJONode(false);
        assertFalse(falseNode.asBoolean(true));

        POJONode nullNode = new POJONode(null);
        assertFalse(nullNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode intNode = new POJONode(42);
        assertEquals(42, intNode.asInt(0));

        POJONode doubleNode = new POJONode(42.5);
        assertEquals(42, doubleNode.asInt(0));

        POJONode nullNode = new POJONode(null);
        assertEquals(0, nullNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode longNode = new POJONode(42L);
        assertEquals(42L, longNode.asLong(0L));

        POJONode doubleNode = new POJONode(42.5);
        assertEquals(42L, doubleNode.asLong(0L));

        POJONode nullNode = new POJONode(null);
        assertEquals(0L, nullNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode doubleNode = new POJONode(42.5);
        assertEquals(42.5, doubleNode.asDouble(0.0), 0.01);

        POJONode booleanNode = new POJONode(true);
        assertEquals(0.0, booleanNode.asDouble(0.0), 0.01);

        POJONode nullNode = new POJONode(null);
        assertEquals(0.0, nullNode.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode("test");
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider ctxt = new SerializerProvider();
        assertDoesNotThrow(() -> node.serialize(gen, ctxt));
    }

    @Test
    public void testGetPojo() {
        Object pojo = "test";
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertEquals(node1, node2);
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }
}

