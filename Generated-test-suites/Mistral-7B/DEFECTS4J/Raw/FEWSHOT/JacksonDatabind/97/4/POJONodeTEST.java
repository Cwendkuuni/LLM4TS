package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("hello");
        POJONode node2 = new POJONode("hello");
        POJONode node3 = new POJONode(123);
        POJONode node4 = new POJONode(new Object());

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(node4));
        assertFalse(node1.equals(null));
        assertFalse(node1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode("hello");
        POJONode node2 = new POJONode("hello");
        POJONode node3 = new POJONode(123);
        POJONode node4 = new POJONode(new Object());

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        int hashCode4 = node4.hashCode();

        assertEquals(hashCode1, node1.hashCode());
        assertEquals(hashCode1, node2.hashCode());
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, 123);
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode("hello");
        POJONode node2 = new POJONode(123);
        POJONode node3 = new POJONode(new Object());

        assertEquals("hello", node1.toString());
        assertEquals("123", node2.toString());
        assertEquals("(POJO value of class java.lang.Object)", node3.toString());
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.getPojo());
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<POJONode> serializer = mapper.findValueSerializer(POJONode.class);
        SerializerProvider sp = mapper.getSerializerProvider();
        JsonGenerator gen = sp.getGenerator();

        POJONode node = new POJONode("hello");
        serializer.serialize(node, gen, sp);

        assertEquals("\"hello\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testNodeType() {
        POJONode node = new POJONode("hello");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.asText());
        assertEquals("hello", node.asText(null));
    }

    @Test
    public void testAsBoolean() {
        POJONode node1 = new POJONode(true);
        POJONode node2 = new POJONode("true");
        POJONode node3 = new POJONode(1);
        POJONode node4 = new POJONode(null);

        assertTrue(node1.asBoolean(false));
        assertTrue(node2.asBoolean(false));
        assertFalse(node3.asBoolean(false));
        assertFalse(node4.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node1 = new POJONode(1);
        POJONode node2 = new POJONode("1");
        POJONode node3 = new POJONode("hello");
        POJONode node4 = new POJONode(null);

        assertEquals(1, node1.asInt(0));
        assertEquals(1, node2.asInt(0));
        assertNotEquals(1, node3.asInt(0));
        assertNull(node4.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node1 = new POJONode(1L);
        POJONode node2 = new POJONode("1");
        POJONode node3 = new POJONode("hello");
        POJONode node4 = new POJONode(null);

        assertEquals(1L, node1.asLong(0L));
        assertEquals(1L, node2.asLong(0L));
        assertNotEquals(1L, node3.asLong(0L));
        assertNull(node4.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node1 = new POJONode(1.0);
        POJONode node2 = new POJONode("1.0");
        POJONode node3 = new POJONode("hello");
        POJONode node4 = new POJONode(null);

        assertEquals(1.0, node1.asDouble(0.0), 0.01);
        assertEquals(1.0, node2.asDouble(0.0), 0.01);
        assertNotEquals(1.0, node3.asDouble(0.0), 0.01);
        assertNull(node4.asDouble(0.0));
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[] { 1, 2, 3 });
        byte[] binary = node.binaryValue();
        assertArrayEquals(new byte[] { 1, 2, 3 }, binary);
    }
}