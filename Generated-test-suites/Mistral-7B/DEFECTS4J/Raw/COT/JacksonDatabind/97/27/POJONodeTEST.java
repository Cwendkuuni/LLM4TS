package com.example.pojonode; // replace with your package name

import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class POJONodeTest {

    @Test
    public void testConstructor() {
        POJONode node = new POJONode(new Object());
        assertInstanceOf(POJONode.class, node);
    }

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
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        byte[] binaryData = node.binaryValue();
        assertArrayEquals(new byte[0], binaryData);

        node = new POJONode(new String("test").getBytes());
        binaryData = node.binaryValue();
        assertArrayEquals(new byte[]{"t", "e", "s", "t"}, binaryData);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());

        node = new POJONode("Hello World");
        assertEquals("Hello World", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("Default Value"));

        node = new POJONode("Hello World");
        assertEquals("Hello World", node.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));

        node = new POJONode(false);
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));

        node = new POJONode(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, node.asInt(0));

        node = new POJONode(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, node.asInt(0));

        node = new POJONode("test");
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));

        node = new POJONode(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, node.asLong(0L));

        node = new POJONode(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, node.asLong(0L));

        node = new POJONode("test");
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.0);
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        node = new POJONode(Double.NaN);
        assertEquals(Double.NaN, node.asDouble(0.0), 0.001);

        node = new POJONode("test");
        assertEquals(0.0, node.asDouble(0.0), 0.001);
    }

    @Test(expected = IOException.class)
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(new String("test"));
        node.serialize(null, null);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertSame(new Object(), node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("test");

        assertEquals(node1, node1);
        assertFalse(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("test");

        assertEquals(node1.hashCode(), node1.hashCode());
        assertNotEquals(node1.hashCode(), node2.hashCode());
        assertNotEquals(node1.hashCode(), node3.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new byte[0]);
        POJONode node3 = new POJONode("test");

        assertEquals("POJONode{_value=Object@1b6d3142}", node1.toString());
        assertEquals("(binary value of 0 bytes)", node2.toString());
        assertEquals("POJONode{_value=test}", node3.toString());
    }
}