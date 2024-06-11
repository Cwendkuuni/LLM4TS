package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {
    private POJONode pojoNode;

    @Before
    public void setUp() {
        pojoNode = new POJONode("test");
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
    public void testBinaryValue() throws Exception {
        assertNull(pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        assertEquals("test", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        assertFalse(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        assertTrue(pojoNode.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        assertEquals(0, pojoNode.asInt(10));
    }

    @Test
    public void testAsLong() {
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        assertEquals(0L, pojoNode.asLong(10L));
    }

    @Test
    public void testAsDouble() {
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        assertEquals(0.0, pojoNode.asDouble(10.0), 0.0);
    }

    @Test
    public void testSerialize() {
        ToStringSerializer serializer = new ToStringSerializer();
        assertNotNull(pojoNode.serialize(serializer, null));
    }

    @Test
    public void testGetPojo() {
        assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        assertTrue(pojoNode.equals(new POJONode("test")));
        assertFalse(pojoNode.equals(new POJONode("test2")));
        assertFalse(pojoNode.equals(null));
        assertFalse(pojoNode.equals("test"));
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", pojoNode.toString());
    }
}