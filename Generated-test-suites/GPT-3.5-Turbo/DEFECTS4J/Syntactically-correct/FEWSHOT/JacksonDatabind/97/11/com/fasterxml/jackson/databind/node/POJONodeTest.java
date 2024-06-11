package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        POJONode node = new POJONode(new byte[]{1, 2, 3});
        byte[] binaryValue = node.binaryValue();
        assertEquals(3, binaryValue.length);
        assertEquals(1, binaryValue[0]);
        assertEquals(2, binaryValue[1]);
        assertEquals(3, binaryValue[2]);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());

        POJONode nodeWithValue = new POJONode("foo");
        assertEquals("foo", nodeWithValue.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));

        POJONode nodeWithValue = new POJONode("foo");
        assertEquals("foo", nodeWithValue.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));

        POJONode nodeWithValue = new POJONode(true);
        assertTrue(nodeWithValue.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.asInt(0));

        POJONode nodeWithValue = new POJONode(42);
        assertEquals(42, nodeWithValue.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));

        POJONode nodeWithValue = new POJONode(10000000000L);
        assertEquals(10000000000L, nodeWithValue.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.0);

        POJONode nodeWithValue = new POJONode(3.14);
        assertEquals(3.14, nodeWithValue.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize() {
        // Test case to be added for serialization
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("foo");
        assertEquals("foo", node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("foo");
        POJONode node2 = new POJONode("foo");
        POJONode node3 = new POJONode("bar");

        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));
        assertFalse(node1.equals(node3));
        assertFalse(node3.equals(node1));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("foo");
        assertEquals("foo".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("foo");
        assertEquals("foo", node.toString());

        POJONode byteArrayNode = new POJONode(new byte[]{1, 2, 3});
        assertEquals("(binary value of 3 bytes)", byteArrayNode.toString());

        POJONode rawValueNode = new POJONode(new RawValue("raw value"));
        assertEquals("(raw value 'raw value')", rawValueNode.toString());
    }
}