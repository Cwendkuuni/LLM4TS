package com.fasterxml.jackson.databind.node;

import org.junit.*;
import static org.junit.Assert.*;

public class POJONodeTest {

    // Test cases for getNodeType() method
    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }
    
    // Test cases for asToken() method
    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }
    
    // Test cases for binaryValue() method
    @Test
    public void testBinaryValueWithByteArray() throws Exception {
        byte[] byteArray = {1, 2, 3, 4};
        POJONode pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
    }
    
    @Test(expected = IOException.class)
    public void testBinaryValueWithNonByteArray() throws Exception {
        POJONode pojoNode = new POJONode("test");
        pojoNode.binaryValue();
    }
    
    // Test cases for asText() method
    @Test
    public void testAsTextWithStringValue() {
        String value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asText());
    }
    
    @Test
    public void testAsTextWithNullValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }
    
    // Test cases for asText(String defaultValue) method
    @Test
    public void testAsTextWithDefaultValueWithStringValue() {
        String value = "test";
        String defaultValue = "default";
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asText(defaultValue));
    }
    
    @Test
    public void testAsTextWithDefaultValueWithNullValue() {
        String defaultValue = "default";
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asText(defaultValue));
    }
    
    // Test cases for asBoolean(boolean defaultValue) method
    @Test
    public void testAsBooleanWithBooleanValue() {
        boolean value = true;
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asBoolean(false));
    }
    
    @Test
    public void testAsBooleanWithNonBooleanValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(false, pojoNode.asBoolean(false));
    }
    
    // Test cases for asInt(int defaultValue) method
    @Test
    public void testAsIntWithIntValue() {
        int value = 10;
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asInt(0));
    }
    
    @Test
    public void testAsIntWithNonIntValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(0, pojoNode.asInt(0));
    }
    
    // Test cases for asLong(long defaultValue) method
    @Test
    public void testAsLongWithLongValue() {
        long value = 100L;
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asLong(0L));
    }
    
    @Test
    public void testAsLongWithNonLongValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(0L, pojoNode.asLong(0L));
    }
    
    // Test cases for asDouble(double defaultValue) method
    @Test
    public void testAsDoubleWithDoubleValue() {
        double value = 10.5;
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asDouble(0.0), 0.001);
    }

    @Test
    public void testAsDoubleWithNonDoubleValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);
    }
    
    // Test cases for serialize() method
    @Test
    public void testSerializeWithNullValue() throws Exception {
        POJONode pojoNode = new POJONode(null);
        // Add assertion here
    }
    
    // Test cases for getPojo() method
    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }
    
    // Test cases for equals() method
    @Test
    public void testEqualsWithSameObject() {
        POJONode pojoNode = new POJONode(new Object());
        assertTrue(pojoNode.equals(pojoNode));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        POJONode pojoNode = new POJONode(new Object());
        assertFalse(pojoNode.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        POJONode pojoNode = new POJONode(new Object());
        assertFalse(pojoNode.equals("test"));
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        Object value = new Object();
        POJONode pojoNode1 = new POJONode(value);
        POJONode pojoNode2 = new POJONode(value);
        assertTrue(pojoNode1.equals(pojoNode2));
    }
    
    @Test
    public void testEqualsWithUnequalObjects() {
        POJONode pojoNode1 = new POJONode(new Object());
        POJONode pojoNode2 = new POJONode(new Object());
        assertFalse(pojoNode1.equals(pojoNode2));
    }
    
    // Test cases for hashCode() method
    @Test
    public void testHashCode() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value.hashCode(), pojoNode.hashCode());
    }
    
    // Test cases for toString() method
    @Test
    public void testToStringWithByteArrayValue() {
        byte[] byteArray = {1, 2, 3, 4};
        POJONode pojoNode = new POJONode(byteArray);
        assertEquals("(binary value of 4 bytes)", pojoNode.toString());
    }
    
    @Test
    public void testToStringWithRawValue() {
        RawValue rawValue = new RawValue("test");
        POJONode pojoNode = new POJONode(rawValue);
        assertEquals("(raw value 'test')", pojoNode.toString());
    }
    
    @Test
    public void testToStringWithOtherValue() {
        String value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.toString());
    }

}