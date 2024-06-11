
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonToken;

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
    public void testBinaryValue() throws IOException {
        POJONode pojoNode = new POJONode(new byte[] {1, 2, 3});
        assertArrayEquals(new byte[] {1, 2, 3}, pojoNode.binaryValue());
    }
    
    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.asText());
        
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.asText("default"));
        
        POJONode nullNode = new POJONode(null);
        assertEquals("default", nullNode.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode trueNode = new POJONode(true);
        assertTrue(trueNode.asBoolean(false));
        
        POJONode falseNode = new POJONode(false);
        assertFalse(falseNode.asBoolean(true));
        
        POJONode nullNode = new POJONode(null);
        assertFalse(nullNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode intNode = new POJONode(1);
        assertEquals(1, intNode.asInt(0));
        
        POJONode nullNode = new POJONode(null);
        assertEquals(0, nullNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode longNode = new POJONode(1L);
        assertEquals(1L, longNode.asLong(0L));
        
        POJONode nullNode = new POJONode(null);
        assertEquals(0L, nullNode.asLong(0L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode doubleNode = new POJONode(1.0);
        assertEquals(1.0, doubleNode.asDouble(0.0), 0);
        
        POJONode nullNode = new POJONode(null);
        assertEquals(0.0, nullNode.asDouble(0.0), 0);
    }
    
    @Test
    public void testSerialize() throws IOException {
        // TODO: Add test case for serialization
        
        POJONode nullNode = new POJONode(null);
        // TODO: Add assertion for serialized null value
    }
    
    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode pojoNode = new POJONode(obj);
        assertEquals(obj, pojoNode.getPojo());
    }
    
    @Test
    public void testEquals() {
        Object obj = new Object();
        POJONode pojoNode1 = new POJONode(obj);
        POJONode pojoNode2 = new POJONode(obj);
        assertTrue(pojoNode1.equals(pojoNode2));
        
        POJONode nullNode = new POJONode(null);
        assertFalse(pojoNode1.equals(nullNode));
        
        POJONode emptyNode1 = new POJONode(null);
        POJONode emptyNode2 = new POJONode(null);
        assertTrue(emptyNode1.equals(emptyNode2));
        
        assertFalse(pojoNode1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode pojoNode = new POJONode(obj);
        assertEquals(obj.hashCode(), pojoNode.hashCode());
    }
    
    @Test
    public void testToString() {
        byte[] byteArray = new byte[] {1, 2, 3};
        POJONode binaryNode = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", binaryNode.toString());
        
        RawValue rawValue = new RawValue("raw");
        POJONode rawNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", rawNode.toString());
        
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.toString());
        
        POJONode otherNode = new POJONode("test");
        assertEquals("test", otherNode.toString());
    }

}
