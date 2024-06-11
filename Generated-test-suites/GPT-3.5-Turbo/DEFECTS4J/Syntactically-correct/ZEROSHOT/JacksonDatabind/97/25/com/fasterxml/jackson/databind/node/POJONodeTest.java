package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import org.junit.Test;

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
    public void testBinaryValue() {
        POJONode node = new POJONode(null);
        try {
            assertNull(node.binaryValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
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
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(null);
        try {
            node.serialize(null, null);
            fail("Exception should be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object pojo1 = new Object();
        Object pojo2 = new Object();
        POJONode node1 = new POJONode(pojo1);
        POJONode node2 = new POJONode(pojo1);
        POJONode node3 = new POJONode(pojo2);

        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(null));
        assertFalse(node1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
    }
}