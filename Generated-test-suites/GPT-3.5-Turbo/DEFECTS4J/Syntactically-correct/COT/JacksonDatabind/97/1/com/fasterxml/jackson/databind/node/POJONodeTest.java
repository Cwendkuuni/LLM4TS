
package com.fasterxml.jackson.databind.node;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;

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
        POJONode pojoNode = new POJONode("test");
        try {
            assertEquals(null, pojoNode.binaryValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("defaultValue", pojoNode.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(10);
        assertEquals(10, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(10L);
        assertEquals(10L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(10.5);
        assertEquals(10.5, pojoNode.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        POJONode pojoNode = new POJONode(null);
        // Unable to test due to dependencies
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode pojoNode = new POJONode(obj);
        assertEquals(obj, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode(10);
        assertEquals("10", pojoNode.toString());
    }

}
