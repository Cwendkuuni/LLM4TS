package com.fasterxml.jackson.databind.node;

import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        // TODO: Add test case for binaryValue()
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(10, node.asInt(10));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(100L, node.asLong(100L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(10.0, node.asDouble(10.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        // TODO: Add test case for serialize()
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testPojoEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertTrue(node1._pojoEquals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
    }
}