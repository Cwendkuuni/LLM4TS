package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());
        
        byte[] value = {0x01, 0x02, 0x03};
        node = new POJONode(value);
        assertTrue(Arrays.equals(value, node.binaryValue()));
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());

        node = new POJONode("Test");
        assertEquals("Test", node.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));

        node = new POJONode("Test");
        assertEquals("Test", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));

        node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        assertTrue(node.asBoolean(true));
        
        node = new POJONode(false);
        assertTrue(node.asBoolean(true));
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(100, node.asInt(100));

        node = new POJONode(200);
        assertEquals(200, node.asInt(100));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(100L, node.asLong(100L));

        node = new POJONode(200L);
        assertEquals(200L, node.asLong(100L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(3.14, node.asDouble(3.14), 0);

        node = new POJONode(2.71);
        assertEquals(2.71, node.asDouble(3.14), 0);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(null);
        // Test serialization logic
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1, node2);

        POJONode node3 = new POJONode("Test");
        POJONode node4 = new POJONode("Test");
        assertEquals(node3, node4);
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1.hashCode(), node2.hashCode());

        POJONode node3 = new POJONode("Test");
        POJONode node4 = new POJONode("Test");
        assertEquals(node3.hashCode(), node4.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode(null);
        assertEquals("null", node1.toString());

        POJONode node2 = new POJONode("Test");
        assertEquals("Test", node2.toString());

        POJONode node3 = new POJONode(new byte[]{0x01, 0x02, 0x03});
        assertEquals("(binary value of 3 bytes)", node3.toString());

        POJONode node4 = new POJONode(new RawValue("Raw"));
        assertEquals("(raw value 'Raw')", node4.toString());
    }
}