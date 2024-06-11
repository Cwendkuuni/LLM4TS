package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

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
        assertArrayEquals(new byte[0], node.binaryValue());

        node = new POJONode(new String("test").getBytes());
        byte[] expected = new byte[]{'t', 'e', 's', 't'};
        assertArrayEquals(expected, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.asText());

        node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));

        node = new POJONode(Boolean.FALSE);
        assertFalse(node.asBoolean(true));

        node = new POJONode(new Object());
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(42);
        assertEquals(42, node.asInt(0));

        node = new POJONode(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, node.asInt(0));

        node = new POJONode(new Object());
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(42L);
        assertEquals(42L, node.asLong(0L));

        node = new POJONode(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, node.asLong(0L));

        node = new POJONode(new Object());
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0), 0.001);

        node = new POJONode(Double.NaN);
        assertEquals(Double.NaN, node.asDouble(0), 0.001);

        node = new POJONode(new Object());
        assertEquals(0, node.asDouble(0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());

        StringWriter writer = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(writer);
        node.serialize(gen, null);
        assertEquals("null", writer.toString());

        node = new POJONode(new String("test"));
        writer = new StringWriter();
        gen = mapper.getFactory().createGenerator(writer);
        node.serialize(gen, null);
        assertEquals("\"test\"", writer.toString());

        node = new POJONode(new int[0]);
        writer = new StringWriter();
        gen = mapper.getFactory().createGenerator(writer);
        node.serialize(gen, null);
        assertEquals("[]", writer.toString());

        node = new POJONode(new Object());
        Object pojo = new Object();
        writer = new StringWriter();
        gen = mapper.getFactory().createGenerator(writer);
        node.serialize(gen, null);
        JsonNode readValue = mapper.readTree(writer.toString());
        assertEquals(JsonNodeType.POJO, readValue.getNodeType());
        assertEquals(pojo.getClass(), readValue.getPojo().getClass());
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node.getPojo().getClass(), node.getClass().getSuperclass());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("foo");

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("foo");

        int hashCode1 = node1.hashCode();
        int hashCode2 = node1.hashCode();
        int hashCode3 = node3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new Object());
        assertEquals("(POJONode)", node.toString());

        node = new POJONode(new byte[0]);
        assertEquals("(binary value of 0 bytes)", node.toString());

        node = new POJONode(new String("test"));
        assertEquals("(raw value 'test')", node.toString());
    }
}