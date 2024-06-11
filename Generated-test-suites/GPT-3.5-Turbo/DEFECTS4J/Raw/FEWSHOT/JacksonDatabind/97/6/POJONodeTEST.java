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
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        
        POJONode nodeWithValue = new POJONode(42);
        assertEquals("42", nodeWithValue.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
        
        POJONode nodeWithValue = new POJONode(42);
        assertEquals("42", nodeWithValue.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
        
        POJONode trueNode = new POJONode(true);
        assertTrue(trueNode.asBoolean(false));
        assertTrue(trueNode.asBoolean(true));
        
        POJONode falseNode = new POJONode(false);
        assertFalse(falseNode.asBoolean(true));
        assertFalse(falseNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(42, node.asInt(42));
        
        POJONode intNode = new POJONode(123);
        assertEquals(123, intNode.asInt(42));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(42L, node.asLong(42L));
        
        POJONode longNode = new POJONode(123456789L);
        assertEquals(123456789L, longNode.asLong(42L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(3.14, node.asDouble(3.14), 0.001);
        
        POJONode doubleNode = new POJONode(1.23);
        assertEquals(1.23, doubleNode.asDouble(3.14), 0.001);
    }
    
    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(null);
        // TODO: add assertions for serialization
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
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());

        byte[] bytes = new byte[]{1, 2, 3};
        POJONode binaryNode = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", binaryNode.toString());

        RawValue rawValue = new RawValue("raw value");
        POJONode rawValueNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw value')", rawValueNode.toString());

        POJONode stringNode = new POJONode("hello");
        assertEquals("hello", stringNode.toString());
    }
}