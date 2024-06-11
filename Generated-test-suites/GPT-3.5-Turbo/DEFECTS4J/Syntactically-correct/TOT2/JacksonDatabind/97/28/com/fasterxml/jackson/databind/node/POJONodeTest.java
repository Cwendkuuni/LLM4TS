package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class POJONodeTest {

    private Object testObject = new Object();
    private POJONode pojoNode = new POJONode(testObject);

    @Test
    public void testConstructor() {
        assertEquals(testObject, pojoNode.getPojo());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue_withByteArray() throws IOException {
        byte[] byteArray = new byte[] {0, 1, 2};
        POJONode pojoNodeWithByteArray = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNodeWithByteArray.binaryValue());
    }

    @Test
    public void testAsText() {
        assertEquals(testObject.toString(), pojoNode.asText());
    }

    @Test
    public void testAsText_withDefaultValue() {
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        assertTrue(pojoNode.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.00001);
    }

    @Test
    public void testSerialize() {
        // Write tests for serialization
    }

    @Test
    public void testEquals() {
        POJONode samePojoNode = new POJONode(testObject);
        POJONode differentPojoNode = new POJONode(new Object());

        assertTrue(pojoNode.equals(samePojoNode));
        assertFalse(pojoNode.equals(differentPojoNode));
        assertFalse(pojoNode.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(testObject.hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        // Write tests for toString() method
    }
}