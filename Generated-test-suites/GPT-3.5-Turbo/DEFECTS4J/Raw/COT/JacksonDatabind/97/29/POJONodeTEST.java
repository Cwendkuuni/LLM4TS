package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;

import java.io.IOException;

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
        POJONode node = new POJONode(new byte[]{1, 2, 3});
        try {
            assertArrayEquals(new byte[]{1, 2, 3}, node.binaryValue());
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
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
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(123);
        assertEquals(123, node.asInt(0));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(456, node.asInt(456));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(123L);
        assertEquals(123L, node.asLong(0L));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(456L, node.asLong(456L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(123.45);
        assertEquals(123.45, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(456.78, node.asDouble(456.78), 0.0001);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode("test");
        try {
            node.serialize(null, null);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }

}