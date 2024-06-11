package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {
    
    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }
    
    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }
    
    @Test
    public void testBinaryValue() {
        POJONode pojoNode = new POJONode(null);
        try {
            byte[] binaryValue = pojoNode.binaryValue();
            assertNull(binaryValue);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("123");
        assertEquals("123", pojoNode.asText());
    }
    
    @Test
    public void testAsText_WithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
    }
    
    @Test
    public void testAsBoolean_WithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertFalse(pojoNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt_WithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123, pojoNode.asInt(123));
    }
    
    @Test
    public void testAsLong_WithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123L, pojoNode.asLong(123L));
    }
    
    @Test
    public void testAsDouble_WithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123.0, pojoNode.asDouble(123.0), 0.0001);
    }
    
    @Test
    public void testSerialize() {
        POJONode pojoNode = new POJONode(null);
        // Add test case for serialization
    }
    
    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }
    
    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);
        assertTrue(pojoNode1.equals(pojoNode1));
        assertFalse(pojoNode1.equals(null));
        assertFalse(pojoNode1.equals(new String()));
        assertTrue(pojoNode1.equals(pojoNode2));
    }
    
    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("123");
        assertEquals("123".hashCode(), pojoNode.hashCode());
    }
    
    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode("123");
        assertEquals("123", pojoNode.toString());
    }
    
}