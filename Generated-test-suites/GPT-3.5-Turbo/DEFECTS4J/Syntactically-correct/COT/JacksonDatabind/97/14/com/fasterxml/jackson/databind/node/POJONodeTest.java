
package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import org.junit.Assert;

public class POJONodeTest {

    // POJONode
    @Test
    public void testPOJONode() {
        // Arrange
        Object value = new Object();
        
        // Act
        POJONode node = new POJONode(value);
        
        // Assert
        Assert.assertEquals(value, node.getPojo());
    }
    
    // getNodeType
    @Test
    public void testGetNodeType() {
        // Arrange
        Object value = new Object();
        POJONode node = new POJONode(value);
        
        // Act
        JsonNodeType type = node.getNodeType();
        
        // Assert
        Assert.assertEquals(JsonNodeType.POJO, type);
    }
    
    // asToken
    @Test
    public void testAsToken() {
        // Arrange
        Object value = new Object();
        POJONode node = new POJONode(value);
        
        // Act
        JsonToken token = node.asToken();
        
        // Assert
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, token);
    }
    
    // binaryValue
    @Test
    public void testBinaryValue() throws IOException {
        // Arrange
        byte[] binaryValue = new byte[] {0x01, 0x02};
        POJONode node = new POJONode(binaryValue);
        
        // Act
        byte[] result = node.binaryValue();
        
        // Assert
        Assert.assertArrayEquals(binaryValue, result);
    }
    
    // asText
    @Test
    public void testAsText() {
        // Arrange
        Object value = "test";
        POJONode node = new POJONode(value);
        
        // Act
        String result = node.asText();
        
        // Assert
        Assert.assertEquals(value.toString(), result);
    }
    
    // asText with default value
    @Test
    public void testAsTextWithDefaultValue() {
        // Arrange
        Object value = null;
        POJONode node = new POJONode(value);
        String defaultValue = "default";
        
        // Act
        String result = node.asText(defaultValue);
        
        // Assert
        Assert.assertEquals(defaultValue, result);
    }
    
    // asBoolean with boolean value
    @Test
    public void testAsBooleanWithBooleanValue() {
        // Arrange
        Object value = true;
        POJONode node = new POJONode(value);
        boolean defaultValue = false;
        
        // Act
        boolean result = node.asBoolean(defaultValue);
        
        // Assert
        Assert.assertEquals((boolean)value, result);
    }
    
    // asBoolean with non-boolean value
    @Test
    public void testAsBooleanWithNonBooleanValue() {
        // Arrange
        Object value = "true";
        POJONode node = new POJONode(value);
        boolean defaultValue = false;
        
        // Act
        boolean result = node.asBoolean(defaultValue);
        
        // Assert
        Assert.assertEquals(defaultValue, result);
    }
    
    // asInt with int value
    @Test
    public void testAsIntWithIntValue() {
        // Arrange
        Object value = 10;
        POJONode node = new POJONode(value);
        int defaultValue = 0;
        
        // Act
        int result = node.asInt(defaultValue);
        
        // Assert
        Assert.assertEquals((int)value, result);
    }
    
    // asInt with non-int value
    @Test
    public void testAsIntWithNonIntValue() {
        // Arrange
        Object value = "10";
        POJONode node = new POJONode(value);
        int defaultValue = 0;
        
        // Act
        int result = node.asInt(defaultValue);
        
        // Assert
        Assert.assertEquals(defaultValue, result);
    }
    
    // asLong with long value
    @Test
    public void testAsLongWithLongValue() {
        // Arrange
        Object value = 100L;
        POJONode node = new POJONode(value);
        long defaultValue = 0L;
        
        // Act
        long result = node.asLong(defaultValue);
        
        // Assert
        Assert.assertEquals((long)value, result);
    }
    
    // asLong with non-long value
    @Test
    public void testAsLongWithNonLongValue() {
        // Arrange
        Object value = "100";
        POJONode node = new POJONode(value);
        long defaultValue = 0L;
        
        // Act
        long result = node.asLong(defaultValue);
        
        // Assert
        Assert.assertEquals(defaultValue, result);
    }

    // asDouble with double value
    @Test
    public void testAsDoubleWithDoubleValue() {
        // Arrange
        Object value = 10.5;
        POJONode node = new POJONode(value);
        double defaultValue = 0.0;
        
        // Act
        double result = node.asDouble(defaultValue);
        
        // Assert
        Assert.assertEquals((double)value, result, 0.0001);
    }
    
    // asDouble with non-double value
    @Test
    public void testAsDoubleWithNonDoubleValue() {
        // Arrange
        Object value = "10.5";
        POJONode node = new POJONode(value);
        double defaultValue = 0.0;
        
        // Act
        double result = node.asDouble(defaultValue);
        
        // Assert
        Assert.assertEquals(defaultValue, result, 0.0001);
    }

    // serialize
    @Test
    public void testSerialize() throws IOException {
        // Arrange
        Object value = new Object();
        POJONode node = new POJONode(value);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider ctxt = Mockito.mock(SerializerProvider.class);
        
        // Act
        node.serialize(gen, ctxt);
        
        // Assert
        Mockito.verify(gen).writeObject(value);
    }
    
    // equals with the same object
    @Test
    public void testEqualsWithSameObject() {
        // Arrange
        Object value = new Object();
        POJONode node1 = new POJONode(value);
        POJONode node2 = node1;
        
        // Act
        boolean result = node1.equals(node2);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    // equals with null value
    @Test
    public void testEqualsWithNullValue() {
        // Arrange
        Object value = new Object();
        POJONode node1 = new POJONode(value);
        POJONode node2 = new POJONode(null);
        
        // Act
        boolean result = node1.equals(node2);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    // equals with the same value
    @Test
    public void testEqualsWithSameValue() {
        // Arrange
        Object value = new Object();
        POJONode node1 = new POJONode(value);
        POJONode node2 = new POJONode(value);
        
        // Act
        boolean result = node1.equals(node2);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    // equals with different value
    @Test
    public void testEqualsWithDifferentValue() {
        // Arrange
        Object value1 = new Object();
        Object value2 = new Object();
        POJONode node1 = new POJONode(value1);
        POJONode node2 = new POJONode(value2);
        
        // Act
        boolean result = node1.equals(node2);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    // hashCode
    @Test
    public void testHashCode() {
        // Arrange
        Object value = new Object();
        
        // Act
        POJONode node = new POJONode(value);
        int hashCode = node.hashCode();
        
        // Assert
        Assert.assertEquals(value.hashCode(), hashCode);
    }
    
    // toString with byte[]
    @Test
    public void testToStringWithByteArray() {
        // Arrange
        byte[] binaryValue = new byte[] {0x01, 0x02};
        POJONode node = new POJONode(binaryValue);
        
        // Act
        String toString = node.toString();
        
        // Assert
        Assert.assertEquals("(binary value of 2 bytes)", toString);
    }
    
    // toString with RawValue
    @Test
    public void testToStringWithRawValue() {
        // Arrange
        RawValue rawValue = new RawValue("{\"key\":\"value\"}");
        POJONode node = new POJONode(rawValue);
        
        // Act
        String toString = node.toString();
        
        // Assert
        Assert.assertEquals("(raw value '{\"key\":\"value\"}')", toString);
    }
    
    // toString with other object
    @Test
    public void testToStringWithOtherObject() {
        // Arrange
        Object value = new Object();
        POJONode node = new POJONode(value);
        
        // Act
        String toString = node.toString();
        
        // Assert
        Assert.assertEquals(value.toString(), toString);
    }
}

