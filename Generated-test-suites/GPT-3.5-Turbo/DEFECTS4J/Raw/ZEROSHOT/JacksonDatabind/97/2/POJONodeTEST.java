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
    public void testBinaryValue() {
        POJONode node = new POJONode(null);
        try {
            assertNull(node.binaryValue());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode numericNode = new POJONode(1);
        assertTrue(numericNode.asBoolean(true));
        
        POJONode nullNode = new POJONode(null);
        assertFalse(nullNode.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode numericNode = new POJONode(10);
        assertEquals(10, numericNode.asInt(0));
        
        POJONode nullNode = new POJONode(null);
        assertEquals(0, nullNode.asInt(0));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode numericNode = new POJONode(100L);
        assertEquals(100L, numericNode.asLong(0L));
        
        POJONode nullNode = new POJONode(null);
        assertEquals(0L, nullNode.asLong(0L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode numericNode = new POJONode(3.14);
        assertEquals(3.14, numericNode.asDouble(0.0), 0.0);
        
        POJONode nullNode = new POJONode(null);
        assertEquals(0.0, nullNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize() {
        POJONode nullNode = new POJONode(null);
        // Add test for serialize method
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
        assertTrue(node1.equals(node2));
        
        POJONode numNode1 = new POJONode(10);
        POJONode numNode2 = new POJONode(10);
        assertTrue(numNode1.equals(numNode2));
        
        POJONode strNode1 = new POJONode("test");
        POJONode strNode2 = new POJONode("test");
        assertTrue(strNode1.equals(strNode2));
        
        POJONode diffNode1 = new POJONode(10);
        POJONode diffNode2 = new POJONode(20);
        assertFalse(diffNode1.equals(diffNode2));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1.hashCode(), node2.hashCode());
        
        POJONode numNode1 = new POJONode(10);
        POJONode numNode2 = new POJONode(10);
        assertEquals(numNode1.hashCode(), numNode2.hashCode());
        
        POJONode strNode1 = new POJONode("test");
        POJONode strNode2 = new POJONode("test");
        assertEquals(strNode1.hashCode(), strNode2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());
        
        POJONode numNode = new POJONode(10);
        assertEquals("10", numNode.toString());
        
        POJONode strNode = new POJONode("test");
        assertEquals("test", strNode.toString());
        
        byte[] bytes = new byte[]{1, 2, 3};
        POJONode binaryNode = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", binaryNode.toString());
        
        RawValue rawValue = new RawValue("raw");
        POJONode rawNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", rawNode.toString());
    }
}