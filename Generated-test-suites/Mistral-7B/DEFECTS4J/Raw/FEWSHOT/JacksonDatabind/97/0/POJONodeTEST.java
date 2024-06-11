package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(new MyPojo());
        POJONode pojoNode = (POJONode) node;

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojoNode);
        JsonNode deserializedNode = mapper.readTree(json);
        POJONode deserializedPojoNode = (POJONode) deserializedNode;

        assertEquals(pojoNode.getPojo(), deserializedPojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj1 = new MyPojo();
        Object obj2 = new MyPojo();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node2));
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new MyPojo();
        Object obj2 = new MyPojo();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Object obj = new MyPojo();
        POJONode node = new POJONode(obj);

        assertNotNull(node.toString());
    }

    @Test
    public void testGetPojo() {
        Object obj = new MyPojo();
        POJONode node = new POJONode(obj);

        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testAsText() {
        Object obj = "Hello World";
        POJONode node = new POJONode(obj);

        assertEquals("Hello World", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        Object obj = null;
        POJONode node = new POJONode(obj);

        assertEquals("", node.asText(""));
    }

    @Test
    public void testAsBoolean() {
        Object obj1 = true;
        Object obj2 = false;
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertTrue(node1.asBoolean(false));
        assertFalse(node2.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        Object obj1 = 1;
        Object obj2 = 2;
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertEquals(1, node1.asInt(0));
        assertEquals(2, node2.asInt(0));
    }

    @Test
    public void testAsLong() {
        Object obj1 = 1L;
        Object obj2 = 2L;
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertEquals(1, node1.asLong(0L));
        assertEquals(2, node2.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        Object obj1 = 1.0;
        Object obj2 = 2.0;
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);

        assertEquals(1.0, node1.asDouble(0.0), 0.0);
        assertEquals(2.0, node2.asDouble(0.0), 0.0);
    }

    @Test
    public void testBinaryValue() throws IOException {
        Object obj = new byte[] {1, 2, 3};
        POJONode node = new POJONode(obj);

        byte[] result = node.binaryValue();

        assertArrayEquals(new byte[] {1, 2, 3}, result);
    }

    static class MyPojo implements JsonSerializable {
        @Override public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException { gen.writeString("MyPojo"); }
    }
}