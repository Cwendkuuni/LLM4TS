package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

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
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsText_WithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean_WithDefaultValue() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsInt_WithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong_WithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble_WithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(null);
        // Test serialization code here
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value, node.getPojo());
    }

    @Test
    public void testEquals_SameObject() {
        POJONode node = new POJONode(null);
        assertTrue(node.equals(node));
    }

    @Test
    public void testEquals_NullObject() {
        POJONode node = new POJONode(null);
        assertFalse(node.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        POJONode node = new POJONode(null);
        assertFalse(node.equals(new Object()));
    }

    @Test
    public void testEquals_SameValues() {
        POJONode node1 = new POJONode("value");
        POJONode node2 = new POJONode("value");
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testEquals_DifferentValues() {
        POJONode node1 = new POJONode("value1");
        POJONode node2 = new POJONode("value2");
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
    }

    @Test
    public void testToString_ByteArray() {
        byte[] value = {1, 2, 3};
        POJONode node = new POJONode(value);
        assertEquals("(binary value of 3 bytes)", node.toString());
    }

    @Test
    public void testToString_RawValue() {
        RawValue value = new RawValue("raw");
        POJONode node = new POJONode(value);
        assertEquals("(raw value 'raw')", node.toString());
    }

    @Test
    public void testToString_Default() {
        POJONode node = new POJONode("value");
        assertEquals("value", node.toString());
    }

}