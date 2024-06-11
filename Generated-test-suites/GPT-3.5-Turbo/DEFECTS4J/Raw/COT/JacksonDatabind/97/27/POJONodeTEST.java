
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = new byte[]{0, 1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        Object value = "test";
        POJONode node = new POJONode(value);
        assertEquals(value.toString(), node.asText());
    }

    @Test
    public void testAsTextDefault() {
        Object value = null;
        POJONode node = new POJONode(value);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        Object value = true;
        POJONode node = new POJONode(value);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        Object value = 123;
        POJONode node = new POJONode(value);
        assertEquals(value, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        Object value = 123L;
        POJONode node = new POJONode(value);
        assertEquals(value, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        Object value = 123.45;
        POJONode node = new POJONode(value);
        assertEquals(value, node.asDouble(0.0));
    }

    @Test
    public void testSerialize() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        // TODO: Implement test case
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object value = new Object();
        POJONode node1 = new POJONode(value);
        POJONode node2 = new POJONode(value);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value.toString(), node.toString());
    }
}
