package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() {
        byte[] binaryValue = {0x01, 0x02, 0x03};
        POJONode pojoNode = new POJONode(binaryValue);
        try {
            assertArrayEquals(binaryValue, pojoNode.binaryValue());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode trueNode = new POJONode(true);
        POJONode falseNode = new POJONode(false);
        POJONode nullNode = new POJONode(null);
        assertTrue(trueNode.asBoolean(false));
        assertFalse(falseNode.asBoolean(true));
        assertFalse(nullNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode intNode = new POJONode(42);
        POJONode nullNode = new POJONode(null);
        assertEquals(42, intNode.asInt(0));
        assertEquals(0, nullNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode longNode = new POJONode(10000000000L);
        POJONode nullNode = new POJONode(null);
        assertEquals(10000000000L, longNode.asLong(0));
        assertEquals(0, nullNode.asLong(0));
    }

    @Test
    public void testAsDouble() {
        POJONode doubleNode = new POJONode(3.14);
        POJONode nullNode = new POJONode(null);
        assertEquals(3.14, doubleNode.asDouble(0), 0.001);
        assertEquals(0, nullNode.asDouble(0), 0.001);
    }

    @Test
    public void testSerialize() {
        POJONode pojoNode = new POJONode("test");
        try {
            pojoNode.serialize(null, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode pojoNode = new POJONode(pojo);
        assertEquals(pojo, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        POJONode pojoNode3 = new POJONode("different");
        assertEquals(pojoNode1, pojoNode2);
        assertNotEquals(pojoNode1, pojoNode3);
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.toString());
    }
}