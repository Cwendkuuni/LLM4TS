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
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(123);
        assertEquals(123, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(123L);
        assertEquals(123L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(123.456);
        assertEquals(123.456, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(null);
        JsonGenerator gen = null;
        SerializerProvider ctxt = null;
        try {
            node.serialize(gen, ctxt);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
        // Validate serialization here
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(null);
        assertNull(node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
    }
}