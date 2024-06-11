package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        Object obj = new byte[]{1, 2, 3};
        POJONode node = new POJONode(obj);
        assertArrayEquals((byte[]) obj, node.binaryValue());
    }

    @Test
    public void testAsText() {
        Object obj = "Test";
        POJONode node = new POJONode(obj);
        assertEquals(obj.toString(), node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        Object obj = null;
        String defaultValue = "Default";
        POJONode node = new POJONode(obj);
        assertEquals(defaultValue, node.asText(defaultValue));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        Object obj = true;
        boolean defaultValue = false;
        POJONode node = new POJONode(obj);
        assertTrue(node.asBoolean(defaultValue));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        Object obj = 10;
        int defaultValue = 0;
        POJONode node = new POJONode(obj);
        assertEquals((int) obj, node.asInt(defaultValue));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        Object obj = 100L;
        long defaultValue = 0L;
        POJONode node = new POJONode(obj);
        assertEquals((long) obj, node.asLong(defaultValue));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        Object obj = 3.14;
        double defaultValue = 0.0;
        POJONode node = new POJONode(obj);
        assertEquals((double) obj, node.asDouble(defaultValue), 0.0001);
    }

    @Test
    public void testSerialize() {
        Object obj = "Test";
        POJONode node = new POJONode(obj);
        // TODO: Add test for serialization
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj1);
        POJONode node3 = new POJONode(obj2);
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        Object obj = new byte[]{1, 2, 3};
        POJONode node = new POJONode(obj);
        assertEquals("(binary value of 3 bytes)", node.toString());
    }
}