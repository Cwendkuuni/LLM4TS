
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

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
    public void testBinaryValue() throws IOException {
        byte[] byteArray = {0x01, 0x02, 0x03};
        POJONode node = new POJONode(byteArray);
        assertEquals(byteArray, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode nodeNull = new POJONode(null);
        assertEquals("null", nodeNull.asText());
        POJONode nodeString = new POJONode("test");
        assertEquals("test", nodeString.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode nodeNull = new POJONode(null);
        assertEquals("default", nodeNull.asText("default"));
        POJONode nodeString = new POJONode("test");
        assertEquals("test", nodeString.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeNull = new POJONode(null);
        assertTrue(nodeNull.asBoolean(true));
        POJONode nodeBoolean = new POJONode(true);
        assertTrue(nodeBoolean.asBoolean(true));
        POJONode nodeString = new POJONode("true");
        assertTrue(nodeString.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode nodeNull = new POJONode(null);
        assertEquals(5, nodeNull.asInt(5));
        POJONode nodeInt = new POJONode(10);
        assertEquals(10, nodeInt.asInt(5));
        POJONode nodeString = new POJONode("15");
        assertEquals(15, nodeString.asInt(5));
    }

    @Test
    public void testAsLong() {
        POJONode nodeNull = new POJONode(null);
        assertEquals(5L, nodeNull.asLong(5L));
        POJONode nodeLong = new POJONode(10L);
        assertEquals(10L, nodeLong.asLong(5L));
        POJONode nodeString = new POJONode("15");
        assertEquals(15L, nodeString.asLong(5L));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeNull = new POJONode(null);
        assertEquals(5.0, nodeNull.asDouble(5.0), 0.000001);
        POJONode nodeDouble = new POJONode(10.0);
        assertEquals(10.0, nodeDouble.asDouble(5.0), 0.000001);
        POJONode nodeString = new POJONode("15.0");
        assertEquals(15.0, nodeString.asDouble(5.0), 0.000001);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode nodeNull = new POJONode(null);
        JsonGenerator genNull = new JsonFactory().createGenerator(System.out);
        nodeNull.serialize(genNull, new SerializerProvider(null, null, null));
        
        POJONode nodeString = new POJONode("test");
        JsonGenerator genString = new JsonFactory().createGenerator(System.out);
        nodeString.serialize(genString, new SerializerProvider(null, null, null));
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode nodeNull1 = new POJONode(null);
        POJONode nodeNull2 = new POJONode(null);
        assertTrue(nodeNull1.equals(nodeNull1));
        assertTrue(nodeNull1.equals(nodeNull2));
        assertFalse(nodeNull1.equals(null));
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
