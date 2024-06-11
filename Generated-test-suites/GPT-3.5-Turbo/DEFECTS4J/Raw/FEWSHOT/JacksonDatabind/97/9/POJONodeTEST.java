package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
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
        POJONode node = new POJONode(1);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = {1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertEquals(bytes, node.binaryValue());
        
        Object value = new Object();
        POJONode node2 = new POJONode(value);
        assertTrue(node2.binaryValue() instanceof IOException);
    }
    
    @Test
    public void testAsText() {
        POJONode node = new POJONode("foo");
        assertEquals("foo", node.asText());
        
        POJONode node2 = new POJONode(null);
        assertEquals("null", node2.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode("foo");
        assertEquals("foo", node.asText("default"));
        
        POJONode node2 = new POJONode(null);
        assertEquals("default", node2.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        
        POJONode node2 = new POJONode(null);
        assertTrue(node2.asBoolean(true));
    }
    
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
        
        POJONode node2 = new POJONode(null);
        assertEquals(0, node2.asInt(0));
    }
    
    @Test
    public void testAsLong() {
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));
        
        POJONode node2 = new POJONode(null);
        assertEquals(0L, node2.asLong(0L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.0);
        assertEquals(1.0, node.asDouble(0.0), 0);
        
        POJONode node2 = new POJONode(null);
        assertEquals(0.0, node2.asDouble(0.0), 0);
    }
    
    @Test
    public void testSerialize() throws IOException {
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        
        POJONode node = new POJONode(null);
        node.serialize(generator, provider);
        
        Object pojo = new Object();
        POJONode node2 = new POJONode(pojo);
        node2.serialize(generator, provider);
    }
    
    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }
    
    @Test
    public void testEquals() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        
        assertTrue(node.equals(node));
        assertTrue(node.equals(new POJONode(pojo)));
        
        POJONode node2 = new POJONode(null);
        assertTrue(!node.equals(node2));
    }
    
    @Test
    public void testHashCode() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo.hashCode(), node.hashCode());
    }
    
    @Test
    public void testToString() {
        byte[] bytes = {1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", node.toString());
        
        RawValue rawValue = new RawValue("");
        POJONode node2 = new POJONode(rawValue);
        assertEquals("(raw value '')", node2.toString());
        
        Object pojo = new Object();
        POJONode node3 = new POJONode(pojo);
        assertEquals(pojo.toString(), node3.toString());
    }
}