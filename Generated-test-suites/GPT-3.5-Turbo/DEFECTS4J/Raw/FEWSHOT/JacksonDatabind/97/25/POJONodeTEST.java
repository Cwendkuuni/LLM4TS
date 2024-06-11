package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

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
    public void testBinaryValue() throws IOException {
        byte[] bytes = {1, 2, 3};
        POJONode pojoNode = new POJONode(bytes);
        assertEquals(bytes, pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertEquals(true, pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(10);
        assertEquals(10, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(100L);
        assertEquals(100L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(3.14);
        assertEquals(3.14, pojoNode.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator gen = null; // initialize with actual JsonGenerator object
        SerializerProvider ctxt = null; // initialize with actual SerializerProvider object
        pojoNode.serialize(gen, ctxt);
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);
        assertEquals(true, pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0, pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.toString());
    }
}