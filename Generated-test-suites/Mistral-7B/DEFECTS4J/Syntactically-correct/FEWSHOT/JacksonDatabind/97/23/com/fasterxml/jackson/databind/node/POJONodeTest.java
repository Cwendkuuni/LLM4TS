package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

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
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        assertEquals(new byte[0], node.binaryValue());

        POJONode nodeWithOtherValue = new POJONode(new Object());
        assertNull(nodeWithOtherValue.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.asText());

        POJONode nodeWithNullValue = new POJONode(null);
        assertEquals("null", nodeWithNullValue.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.asText());

        POJONode nodeWithNullValue = new POJONode(null);
        assertEquals("", nodeWithNullValue.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeWithBooleanValue = new POJONode(true);
        assertTrue(nodeWithBooleanValue.asBoolean(false));

        POJONode nodeWithOtherValue = new POJONode(new Object());
        assertFalse(nodeWithOtherValue.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode nodeWithIntValue = new POJONode(1);
        assertEquals(1, nodeWithIntValue.asInt(0));

        POJONode nodeWithOtherValue = new POJONode(new Object());
        assertEquals(0, nodeWithOtherValue.asInt(1));
    }

    @Test
    public void testAsLong() {
        POJONode nodeWithLongValue = new POJONode(1L);
        assertEquals(1L, nodeWithLongValue.asLong(0L));

        POJONode nodeWithOtherValue = new POJONode(new Object());
        assertEquals(0L, nodeWithOtherValue.asLong(1L));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeWithDoubleValue = new POJONode(1.0);
        assertEquals(1.0, nodeWithDoubleValue.asDouble(0.0), 0.001);

        POJONode nodeWithOtherValue = new POJONode(new Object());
        assertEquals(0.0, nodeWithOtherValue.asDouble(1.0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new POJONode(new Object()));

        JsonNode expectedJsonNode = mapper.readTree(json);
        JsonNode actualJsonNode = new POJONode(new Object());
        assertEquals(expectedJsonNode, actualJsonNode);
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);

        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertFalse(node1.equals(node2));
        assertTrue(node1.equals(node1));

        POJONode node3 = new POJONode(obj1);
        assertTrue(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);

        int hashCode1 = node.hashCode();
        POJONode node2 = new POJONode(obj);

        assertEquals(hashCode1, node2.hashCode());
    }

    @Test
    public void testToString() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);

        String toString1 = node.toString();
        POJONode node2 = new POJONode(obj);

        String toString2 = node2.toString();

        assertEquals(toString1, toString2);
    }
}