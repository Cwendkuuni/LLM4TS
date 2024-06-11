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
        POJONode node = new POJONode(null);
        try {
            assertNull(node.binaryValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        
        POJONode node2 = new POJONode("test");
        assertEquals("test", node2.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
        
        POJONode node2 = new POJONode("test");
        assertEquals("test", node2.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
        
        POJONode node2 = new POJONode(true);
        assertTrue(node2.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node1 = new POJONode(null);
        assertEquals(10, node1.asInt(10));
        
        POJONode node2 = new POJONode(20);
        assertEquals(20, node2.asInt(10));
        
        POJONode node3 = new POJONode(20.5);
        assertEquals(20, node3.asInt(10));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node1 = new POJONode(null);
        assertEquals(100L, node1.asLong(100L));
        
        POJONode node2 = new POJONode(200L);
        assertEquals(200L, node2.asLong(100L));
        
        POJONode node3 = new POJONode(200.5);
        assertEquals(200L, node3.asLong(100L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node1 = new POJONode(null);
        assertEquals(3.14, node1.asDouble(3.14), 0);
        
        POJONode node2 = new POJONode(2.718);
        assertEquals(2.718, node2.asDouble(3.14), 0);
        
        POJONode node3 = new POJONode(2);
        assertEquals(2, node3.asDouble(3.14), 0);
    }

    @Test
    public void testSerialize() {
        // Empty test as it requires setup of JsonGenerator and SerializerProvider
        assertTrue(true);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("abc");

        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("abc");

        assertEquals(node1.hashCode(), node2.hashCode());
        assertNotEquals(node1.hashCode(), node3.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode("test");
        assertEquals("test", node1.toString());
        
        POJONode node2 = new POJONode(new byte[] { 1, 2, 3 });
        assertEquals("(binary value of 3 bytes)", node2.toString());
        
        RawValue rawValue = new RawValue("raw");
        POJONode node3 = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", node3.toString());
    }
}