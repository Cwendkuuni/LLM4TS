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
            fail(e.getMessage());
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
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(123, node.asInt(123));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(456L, node.asLong(456L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(3.14, node.asDouble(3.14), 0);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(null);
        // TODO: Add test case for serialize method
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

        assertTrue(node1.equals(node1));
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
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo.toString(), node.toString());
    }
}