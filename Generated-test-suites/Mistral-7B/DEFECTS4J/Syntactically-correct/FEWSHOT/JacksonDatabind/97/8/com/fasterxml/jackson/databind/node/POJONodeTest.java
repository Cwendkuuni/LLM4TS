package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValueNull() throws IOException {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());
    }

    @Test
    public void testBinaryValueByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        POJONode node = new POJONode(data);
        assertArrayEquals(data, node.binaryValue());
    }

    @Test
    public void testAsText() {
        String text = "Hello World!";
        POJONode node = new POJONode(text);
        assertEquals(text, node.asText());
        assertEquals("Hello World!", node.asText(null));
    }

    @Test
    public void testAsTextNull() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        assertEquals("null", node.asText(""));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeFalse = new POJONode(false);
        POJONode nodeTrue = new POJONode(true);
        assertFalse(nodeFalse.asBoolean(true));
        assertTrue(nodeTrue.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode nodeIntOne = new POJONode(1);
        POJONode nodeIntMinusOne = new POJONode(-1);
        assertEquals(1, nodeIntOne.asInt(0));
        assertEquals(-1, nodeIntMinusOne.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode nodeLongOne = new POJONode(1L);
        POJONode nodeLongMinusOne = new POJONode(-1L);
        assertEquals(1L, nodeLongOne.asLong(0L));
        assertEquals(-1L, nodeLongMinusOne.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeDoublePi = new POJONode(Math.PI);
        POJONode nodeDoubleHalf = new POJONode(0.5);
        assertEquals(Math.PI, nodeDoublePi.asDouble(0), 0.001);
        assertEquals(0.5, nodeDoubleHalf.asDouble(0), 0.001);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        String value = "Hello World!";
        POJONode node = new POJONode(value);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, mapper.getSerializerProvider());

        String expected = String.format("\"%s\"", value);
        String actual = gen.getOutputContent().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPojo() {
        String value = "Hello World!";
        POJONode node = new POJONode(value);
        assertEquals(value, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertFalse(node1.equals(node2));
    }

    @Test
    public void testEqualsSameInstance() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertTrue(node.equals(node));
    }

    @Test
    public void testEqualsSameValue() {
        Object obj = new Object();
        POJONode node1 = new POJONode(obj);
        POJONode node2 = new POJONode(obj);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        int hashCode = node.hashCode();
        assertEquals(obj.hashCode(), hashCode);
    }

    @Test
    public void testToString() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        String toString = node.toString();
        assertTrue(toString.contains("(POJONode)"));
    }
}