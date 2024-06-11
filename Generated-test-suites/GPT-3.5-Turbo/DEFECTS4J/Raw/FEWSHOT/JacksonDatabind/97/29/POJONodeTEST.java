package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;

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
        // Test when _value is byte[]
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
        
        // Test when _value is not byte[]
        POJONode pojoNode2 = new POJONode("test");
        Exception exception = assertThrows(Exception.class, () -> {
            pojoNode2.binaryValue();
        });
        assertTrue(exception instanceof IOException);
    }
    
    @Test
    public void testAsText() {
        // Test when _value is null
        POJONode pojoNode1 = new POJONode(null);
        assertEquals("null", pojoNode1.asText());
        
        // Test when _value is not null
        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        // Test when _value is null
        POJONode pojoNode1 = new POJONode(null);
        assertEquals("default", pojoNode1.asText("default"));
        
        // Test when _value is not null
        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        // Test when _value is null
        POJONode pojoNode1 = new POJONode(null);
        assertTrue(pojoNode1.asBoolean(true));
        
        // Test when _value is Boolean
        POJONode pojoNode2 = new POJONode(true);
        assertTrue(pojoNode2.asBoolean(false));
        
        // Test when _value is not Boolean
        POJONode pojoNode3 = new POJONode("test");
        assertFalse(pojoNode3.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        // Test when _value is integer
        POJONode pojoNode1 = new POJONode(10);
        assertEquals(10, pojoNode1.asInt(0));
        
        // Test when _value is not integer
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(0, pojoNode2.asInt(0));
    }
    
    @Test
    public void testAsLong() {
        // Test when _value is long
        POJONode pojoNode1 = new POJONode(1000L);
        assertEquals(1000L, pojoNode1.asLong(0));
        
        // Test when _value is not long
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(0, pojoNode2.asLong(0));
    }
    
    @Test
    public void testAsDouble() {
        // Test when _value is double
        POJONode pojoNode1 = new POJONode(3.14);
        assertEquals(3.14, pojoNode1.asDouble(0), 0);
        
        // Test when _value is not double
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(0, pojoNode2.asDouble(0), 0);
    }
    
    @Test
    public void testSerialize() {
        // Test when _value is null
        POJONode pojoNode1 = new POJONode(null);
        //TODO: Add assertion for serialization
        
        // Test when _value implements JsonSerializable
        //TODO: Implement test
        
        // Test when _value does not implement JsonSerializable
        //TODO: Implement test
    }
    
    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.getPojo());
    }
    
    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertFalse(pojoNode1.equals(null));
        assertFalse(pojoNode1.equals(new Object()));
        assertTrue(pojoNode1.equals(pojoNode1));
        assertTrue(pojoNode1.equals(pojoNode2));
    }
    
    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }
    
    @Test
    public void testToString() {
        // Test when _value is byte[]
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode1 = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", pojoNode1.toString());
        
        // Test when _value is RawValue
        POJONode pojoNode2 = new POJONode(new RawValue("test"));
        assertEquals("(raw value 'test')", pojoNode2.toString());
        
        // Test when _value is not byte[] or RawValue
        POJONode pojoNode3 = new POJONode("test");
        assertEquals("test", pojoNode3.toString());
    }
}