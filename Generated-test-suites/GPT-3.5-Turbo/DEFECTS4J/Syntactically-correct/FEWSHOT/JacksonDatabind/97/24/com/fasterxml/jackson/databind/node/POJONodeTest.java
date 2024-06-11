package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

import org.junit.Test;

public class POJONodeTest {
    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        node = new POJONode(10);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        node = new POJONode(10);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[] {1, 2, 3});
        assertEquals(new byte[] {1, 2, 3}, node.binaryValue());
        node = new POJONode("test");
        assertNull(node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        node = new POJONode(10);
        assertEquals("10", node.asText());
        node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
        node = new POJONode(10);
        assertEquals("10", node.asText("default"));
        node = new POJONode("test");
        assertEquals("test", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertEquals(true, node.asBoolean(true));
        assertEquals(false, node.asBoolean(false));
        node = new POJONode(true);
        assertEquals(true, node.asBoolean(false));
        node = new POJONode(10);
        assertEquals(false, node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(10, node.asInt(10));
        node = new POJONode(20);
        assertEquals(20, node.asInt(10));
        node = new POJONode("test");
        assertEquals(10, node.asInt(10));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(100L, node.asLong(100L));
        node = new POJONode(200L);
        assertEquals(200L, node.asLong(100L));
        node = new POJONode("test");
        assertEquals(100L, node.asLong(100L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(1.0, node.asDouble(1.0), 0.0);
        node = new POJONode(2.0);
        assertEquals(2.0, node.asDouble(1.0), 0.0);
        node = new POJONode("test");
        assertEquals(1.0, node.asDouble(1.0), 0.0);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(null);
        JsonGenerator gen = null;
        SerializerProvider ctxt = null;
        node.serialize(gen, ctxt);
        node = new POJONode("test");
        node.serialize(gen, ctxt);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(null);
        assertNull(node.getPojo());
        node = new POJONode(10);
        assertEquals(10, node.getPojo());
        node = new POJONode("test");
        assertEquals("test", node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(true, node1.equals(node2));
        node1 = new POJONode(10);
        node2 = new POJONode(10);
        assertEquals(true, node1.equals(node2));
        node1 = new POJONode(10);
        node2 = new POJONode(20);
        assertEquals(false, node1.equals(node2));
        node1 = new POJONode("test");
        node2 = new POJONode("test");
        assertEquals(true, node1.equals(node2));
        node1 = new POJONode("test1");
        node2 = new POJONode("test2");
        assertEquals(false, node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
        node = new POJONode(10);
        assertEquals(10, node.hashCode());
        node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
        node = new POJONode(new byte[] {1, 2, 3});
        assertEquals("(binary value of 3 bytes)", node.toString());
        node = new POJONode(new RawValue(""));
        assertEquals("(raw value '')", node.toString());
        node = new POJONode("test");
        assertEquals("test", node.toString());
    }
}