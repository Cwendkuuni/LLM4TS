
package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode("test");
        byte[] result = node.binaryValue();
        Assert.assertNull(result);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.asText());
    }

    @Test
    public void testAsText_defaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        Assert.assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsBoolean_defaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        Assert.assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsInt_defaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(10L);
        Assert.assertEquals(10L, node.asLong(0L));
    }

    @Test
    public void testAsLong_defaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(10.0);
        Assert.assertEquals(10.0, node.asDouble(0.0), 0);
    }

    @Test
    public void testAsDouble_defaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(0.0, node.asDouble(0.0), 0);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode("test");
        JsonGenerator jsonGenerator = null; // create the JSON generator with a mock implementation
        SerializerProvider serializerProvider = null;  // create the serializer provider with a mock implementation
        node.serialize(jsonGenerator, serializerProvider);
        // assert the serialization result
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        Assert.assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        Assert.assertEquals(node1, node2);
    }

    @Test
    public void testEquals_nullValue() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        Assert.assertEquals(node1, node2);
    }

    @Test
    public void testNotEquals() {
        POJONode node1 = new POJONode("test1");
        POJONode node2 = new POJONode("test2");
        Assert.assertNotEquals(node1, node2);
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.toString());
    }
}

