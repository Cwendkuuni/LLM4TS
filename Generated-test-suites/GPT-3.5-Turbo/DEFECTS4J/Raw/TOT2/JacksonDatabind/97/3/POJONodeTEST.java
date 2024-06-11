package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        POJONode node = new POJONode(new byte[]{0x01, 0x02, 0x03});
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        assertEquals(100L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() {
        // Test serialization
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.getPojo());
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