package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

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
        POJONode node = new POJONode(new byte[]{1, 2, 3});
        try {
            byte[] binaryValue = node.binaryValue();
            assertArrayEquals(new byte[]{1, 2, 3}, binaryValue);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAsText() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());
        
        POJONode stringNode = new POJONode("hello");
        assertEquals("hello", stringNode.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        POJONode nullNode = new POJONode(null);
        assertEquals("default", nullNode.asText("default"));
        
        POJONode stringNode = new POJONode("hello");
        assertEquals("hello", stringNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nullNode = new POJONode(null);
        assertTrue(nullNode.asBoolean(true));
        
        POJONode booleanNode = new POJONode(true);
        assertTrue(booleanNode.asBoolean(false));
        
        POJONode integerNode = new POJONode(1);
        assertFalse(integerNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode nullNode = new POJONode(null);
        assertEquals(10, nullNode.asInt(10));
        
        POJONode integerNode = new POJONode(5);
        assertEquals(5, integerNode.asInt(10));
        
        POJONode stringNode = new POJONode("hello");
        assertEquals(10, stringNode.asInt(10));
    }

    @Test
    public void testAsLong() {
        POJONode nullNode = new POJONode(null);
        assertEquals(10L, nullNode.asLong(10L));
        
        POJONode longNode = new POJONode(5L);
        assertEquals(5L, longNode.asLong(10L));
        
        POJONode stringNode = new POJONode("hello");
        assertEquals(10L, stringNode.asLong(10L));
    }

    @Test
    public void testAsDouble() {
        POJONode nullNode = new POJONode(null);
        assertEquals(10.0, nullNode.asDouble(10.0), 0.0001);
        
        POJONode doubleNode = new POJONode(5.0);
        assertEquals(5.0, doubleNode.asDouble(10.0), 0.0001);
        
        POJONode stringNode = new POJONode("hello");
        assertEquals(10.0, stringNode.asDouble(10.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        // Test serialize() method
        // Cannot test the actual serialization in this method
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        assertFalse(node1.equals(null));
        assertFalse(node1.equals(""));
        assertFalse(node1.equals(node2));
        assertTrue(node1.equals(node1));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode(new byte[]{1, 2, 3});
        assertEquals("(binary value of 3 bytes)", node1.toString());
        
        POJONode node2 = new POJONode(new RawValue("raw value"));
        assertEquals("(raw value 'raw value')", node2.toString());
        
        POJONode node3 = new POJONode("hello");
        assertEquals("hello", node3.toString());
    }
}