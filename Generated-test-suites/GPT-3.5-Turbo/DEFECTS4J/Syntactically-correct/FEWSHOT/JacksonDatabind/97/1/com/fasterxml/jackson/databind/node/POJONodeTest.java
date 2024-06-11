package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;

public class POJONodeTest {
    
    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }
    
    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }
    
    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = {1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertEquals(bytes, node.binaryValue());
        
        POJONode stringNode = new POJONode("test");
        assertNull(stringNode.binaryValue());
    }
    
    @Test
    public void testAsText() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());
        
        POJONode stringNode = new POJONode("test");
        assertEquals("test", stringNode.asText());
    }
    
    @Test
    public void testAsTextWithDefault() {
        POJONode nullNode = new POJONode(null);
        assertEquals("default", nullNode.asText("default"));
        
        POJONode stringNode = new POJONode("test");
        assertEquals("test", stringNode.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode nullNode = new POJONode(null);
        assertTrue(nullNode.asBoolean(true));
        
        POJONode booleanNode = new POJONode(true);
        assertTrue(booleanNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode nullNode = new POJONode(null);
        assertEquals(10, nullNode.asInt(10));
        
        POJONode intNode = new POJONode(5);
        assertEquals(5, intNode.asInt(10));
    }
    
    @Test
    public void testAsLong() {
        POJONode nullNode = new POJONode(null);
        assertEquals(100L, nullNode.asLong(100L));
        
        POJONode longNode = new POJONode(50L);
        assertEquals(50L, longNode.asLong(100L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode nullNode = new POJONode(null);
        assertEquals(3.14, nullNode.asDouble(3.14), 0);
        
        POJONode doubleNode = new POJONode(2.718);
        assertEquals(2.718, doubleNode.asDouble(3.14), 0);
    }
    
    @Test
    public void testSerialize() throws IOException {
        POJONode nullNode = new POJONode(null);
        // TODO: Add test case for serializing POJO
        
        // Write JSON to string and assert the output
    }
    
    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }
    
    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertEquals(node1, node2);
        
        POJONode nullNode1 = new POJONode(null);
        POJONode nullNode2 = new POJONode(null);
        assertEquals(nullNode1, nullNode2);
    }
    
    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertEquals(node1.hashCode(), node2.hashCode());
        
        POJONode nullNode1 = new POJONode(null);
        POJONode nullNode2 = new POJONode(null);
        assertEquals(nullNode1.hashCode(), nullNode2.hashCode());
    }
    
    @Test
    public void testToString() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());
        
        POJONode stringNode = new POJONode("test");
        assertEquals("test", stringNode.toString());
        
        byte[] bytes = {1, 2, 3};
        POJONode binaryNode = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", binaryNode.toString());
    }
}