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
    public void testBinaryValue() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        try {
            byte[] result = node.binaryValue();
            assertNull(result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        // Test when _value is a byte[]
        byte[] byteArray = {1, 2, 3};
        node = new POJONode(byteArray);
        try {
            byte[] result = node.binaryValue();
            assertArrayEquals(byteArray, result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        // Test when _value is not a byte[]
        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        try {
            byte[] result = stringNode.binaryValue();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testAsText() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        
        // Test when _value is not null
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(stringValue, node.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        String defaultValue = "default";
        assertEquals(defaultValue, node.asText(defaultValue));
        
        // Test when _value is not null
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(stringValue, node.asText(defaultValue));
    }
    
    @Test
    public void testAsBoolean() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));
        
        // Test when _value is a Boolean
        Boolean booleanValue = true;
        node = new POJONode(booleanValue);
        assertTrue(node.asBoolean(false));
        
        // Test when _value is not a Boolean
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertFalse(node.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        int defaultValue = 100;
        assertEquals(defaultValue, node.asInt(defaultValue));
        
        // Test when _value is a Number
        int intValue = 42;
        node = new POJONode(intValue);
        assertEquals(intValue, node.asInt(defaultValue));
        
        // Test when _value is not a Number
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(defaultValue, node.asInt(defaultValue));
    }
    
    @Test
    public void testAsLong() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        long defaultValue = 100L;
        assertEquals(defaultValue, node.asLong(defaultValue));
        
        // Test when _value is a Number
        long longValue = 1000L;
        node = new POJONode(longValue);
        assertEquals(longValue, node.asLong(defaultValue));
        
        // Test when _value is not a Number
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(defaultValue, node.asLong(defaultValue));
    }
    
    @Test
    public void testAsDouble() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        double defaultValue = 1.0;
        assertEquals(defaultValue, node.asDouble(defaultValue), 0.0001);
        
        // Test when _value is a Number
        double doubleValue = 3.14;
        node = new POJONode(doubleValue);
        assertEquals(doubleValue, node.asDouble(defaultValue), 0.0001);
        
        // Test when _value is not a Number
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(defaultValue, node.asDouble(defaultValue), 0.0001);
    }
    
    @Test
    public void testSerialize() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator gen = new JsonFactory().createGenerator(out);
            node.serialize(gen, null);
            gen.flush();
            String result = out.toString("UTF-8");
            assertEquals("null", result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        // Test when _value is a JsonSerializable
        JsonSerializable jsonSerializable = new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
                gen.writeNumber(42);
            }
            
            @Override
            public void serializeWithType(JsonGenerator gen, SerializerProvider ctxt, TypeSerializer typeSer) throws IOException {
                serialize(gen, ctxt);
            }
        };
        node = new POJONode(jsonSerializable);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator gen = new JsonFactory().createGenerator(out);
            node.serialize(gen, null);
            gen.flush();
            String result = out.toString("UTF-8");
            assertEquals("42", result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        // Test when _value is not a JsonSerializable
        String stringValue = "test";
        node = new POJONode(stringValue);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator gen = new JsonFactory().createGenerator(out);
            node.serialize(gen, null);
            gen.flush();
            String result = out.toString("UTF-8");
            assertEquals("\"test\"", result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(null);
        assertNull(node.getPojo());
        
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(stringValue, node.getPojo());
    }
    
    @Test
    public void testEquals() {
        // Test when other object is null
        POJONode node = new POJONode(null);
        assertFalse(node.equals(null));
        
        // Test when other object is not a POJONode
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertFalse(node.equals(stringValue));
        
        // Test when other object is a POJONode with null _value
        POJONode nullNode = new POJONode(null);
        assertTrue(node.equals(nullNode));
        
        // Test when other object is a POJONode with non-null _value
        POJONode otherNode = new POJONode(stringValue);
        assertTrue(node.equals(otherNode));
        
        // Test when other object is a POJONode with different _value
        String differentValue = "different";
        otherNode = new POJONode(differentValue);
        assertFalse(node.equals(otherNode));
    }
    
    @Test
    public void testHashCode() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
        
        // Test when _value is not null
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals(stringValue.hashCode(), node.hashCode());
    }
    
    @Test
    public void testToString() {
        // Test when _value is null
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
        
        // Test when _value is a byte[]
        byte[] byteArray = {1, 2, 3};
        node = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", node.toString());
        
        // Test when _value is a RawValue
        String rawValue = "{\"a\": 1}";
        node = new POJONode(new RawValue(rawValue));
        assertEquals("(raw value '{\"a\": 1}')", node.toString());
        
        // Test when _value is not null
        String stringValue = "test";
        node = new POJONode(stringValue);
        assertEquals("test", node.toString());
    }
}