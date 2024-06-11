package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class POJONodeTest {

    // Test data
    private Object testValue;

    // POJONode instance for testing
    private POJONode pojoNode;

    @Before
    public void setUp() {
        testValue = "test";
        pojoNode = new POJONode(testValue);
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
    public void testBinaryValue() {
        try {
            assertNull(pojoNode.binaryValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        byte[] bytes = new byte[]{0, 1, 2, 3};
        POJONode binaryNode = new POJONode(bytes);
        try {
            assertArrayEquals(bytes, binaryNode.binaryValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testAsText() {
        assertEquals(testValue.toString(), pojoNode.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        String defaultValue = "default";
        POJONode nullNode = new POJONode(null);
        assertEquals(defaultValue, nullNode.asText(defaultValue));
    }

    @Test
    public void testAsBoolean() {
        boolean defaultValue = true;
        POJONode boolNode = new POJONode(true);
        assertTrue(boolNode.asBoolean(!defaultValue));
        assertFalse(pojoNode.asBoolean(defaultValue));
    }

    @Test
    public void testAsInt() {
        int defaultValue = 123;
        POJONode intNode = new POJONode(123);
        assertEquals(123, intNode.asInt(defaultValue));
        assertEquals(defaultValue, pojoNode.asInt(defaultValue));
    }

    @Test
    public void testAsLong() {
        long defaultValue = 123L;
        POJONode longNode = new POJONode(123L);
        assertEquals(123L, longNode.asLong(defaultValue));
        assertEquals(defaultValue, pojoNode.asLong(defaultValue));
    }

    @Test
    public void testAsDouble() {
        double defaultValue = 123.0;
        POJONode doubleNode = new POJONode(123.0);
        assertEquals(123.0, doubleNode.asDouble(defaultValue), 0.0);
        assertEquals(defaultValue, pojoNode.asDouble(defaultValue), 0.0);
    }

    @Test
    public void testSerialize() {
        // Cannot test directly since it calls external methods
        assertNotNull(pojoNode.serialize());
    }

    @Test
    public void testGetPojo() {
        assertEquals(testValue, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        assertTrue(pojoNode.equals(pojoNode));
        assertFalse(pojoNode.equals(null));

        String testString = "test";
        POJONode stringNode = new POJONode(testString);
        POJONode nullNode = new POJONode(null);

        assertTrue(pojoNode.equals(stringNode));
        assertFalse(pojoNode.equals(nullNode));

        POJONode differentValueNode = new POJONode("different");
        assertFalse(pojoNode.equals(differentValueNode));
    }

    @Test
    public void testHashCode() {
        assertEquals(testValue.hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(testValue.toString(), pojoNode.toString());
    }
}