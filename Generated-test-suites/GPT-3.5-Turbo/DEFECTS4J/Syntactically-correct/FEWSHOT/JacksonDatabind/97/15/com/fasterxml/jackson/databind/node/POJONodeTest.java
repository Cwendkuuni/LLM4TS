package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

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

        byte[] value = new byte[] {1, 2, 3};
        node = new POJONode(value);
        assertArrayEquals(value, node.binaryValue());
    }
    
    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        
        node = new POJONode(123);
        assertEquals("123", node.asText());
        
        node = new POJONode("test");
        assertEquals("test", node.asText());
    }
    
    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        assertEquals("defaultValue", node.asText("defaultValue"));
        
        node = new POJONode(123);
        assertEquals("123", node.asText("defaultValue"));
        
        node = new POJONode("test");
        assertEquals("test", node.asText("defaultValue"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));
        
        node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        
        node = new POJONode(false);
        assertFalse(node.asBoolean(true));
    }
    
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(123, node.asInt(123));
        
        node = new POJONode(456);
        assertEquals(456, node.asInt(123));
        
        node = new POJONode("123");
        assertEquals(123, node.asInt(456));
    }
    
    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(123L, node.asLong(123L));
        
        node = new POJONode(456L);
        assertEquals(456L, node.asLong(123L));
        
        node = new POJONode("123");
        assertEquals(123L, node.asLong(456L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(1.23, node.asDouble(1.23), 0.0);
        
        node = new POJONode(4.56);
        assertEquals(4.56, node.asDouble(1.23), 0.0);
        
        node = new POJONode("1.23");
        assertEquals(1.23, node.asDouble(4.56), 0.0);
    }
    
    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(null);
        // TODO: Write test case to test serialization
        
    }
    
    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(null);
        assertNull(node.getPojo());
        
        Object pojo = new Object();
        node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }
    
    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1, node2);
        
        node1 = new POJONode(123);
        node2 = new POJONode(123);
        assertEquals(node1, node2);
        
        node1 = new POJONode("test");
        node2 = new POJONode("test");
        assertEquals(node1, node2);
        
        Object object = new Object();
        node1 = new POJONode(object);
        node2 = new POJONode(object);
        assertEquals(node1, node2);
    }
    
    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1.hashCode(), node2.hashCode());
        
        node1 = new POJONode(123);
        node2 = new POJONode(123);
        assertEquals(node1.hashCode(), node2.hashCode());
        
        node1 = new POJONode("test");
        node2 = new POJONode("test");
        assertEquals(node1.hashCode(), node2.hashCode());
        
        Object object = new Object();
        node1 = new POJONode(object);
        node2 = new POJONode(object);
        assertEquals(node1.hashCode(), node2.hashCode());
    }
    
    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
        
        node = new POJONode(123);
        assertEquals("123", node.toString());
        
        node = new POJONode("test");
        assertEquals("test", node.toString());
        
        byte[] value = new byte[] {1, 2, 3};
        node = new POJONode(value);
        assertEquals("(binary value of 3 bytes)", node.toString());
        
        RawValue rawValue = new RawValue("test");
        node = new POJONode(rawValue);
        assertEquals("(raw value 'test')", node.toString());
    }
    
}