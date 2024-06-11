package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testConstructor() {
        POJONode pojoNode = new POJONode(new Object());
        assertNotNull(pojoNode);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode.asText());
        POJONode pojoNodeNull = new POJONode(null);
        assertEquals("null", pojoNodeNull.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode.asText());
        POJONode pojoNodeNull = new POJONode(null);
        assertEquals("null", pojoNodeNull.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNodeTrue = new POJONode(true);
        assertTrue(pojoNodeTrue.asBoolean(false));
        POJONode pojoNodeFalse = new POJONode(false);
        assertFalse(pojoNodeFalse.asBoolean(true));
        POJONode pojoNodeNull = new POJONode(null);
        assertFalse(pojoNodeNull.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNodeInt = new POJONode(1);
        assertEquals(1, pojoNodeInt.asInt(0));
        POJONode pojoNodeNull = new POJONode(null);
        assertEquals(0, pojoNodeNull.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNodeLong = new POJONode(1L);
        assertEquals(1L, pojoNodeLong.asLong(0L));
        POJONode pojoNodeNull = new POJONode(null);
        assertEquals(0L, pojoNodeNull.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNodeDouble = new POJONode(1.0);
        assertEquals(1.0, pojoNodeDouble.asDouble(0.0), 0.001);
        POJONode pojoNodeNull = new POJONode(null);
        assertEquals(0.0, pojoNodeNull.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        POJONode pojoNode = new POJONode("Hello World");
        String json = mapper.writeValueAsString(pojoNode);
        assertEquals("\"Hello World\"", json);
    }

    @Test(expected = IOException.class)
    public void testBinaryValue() throws IOException {
        POJONode pojoNode = new POJONode(new byte[0]);
        pojoNode.binaryValue();
    }

    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode(new Object());
        assertNotNull(pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("Hello World");
        POJONode pojoNode2 = new POJONode("Hello World");
        assertTrue(pojoNode1.equals(pojoNode2));
        POJONode pojoNode3 = new POJONode("Hello");
        assertFalse(pojoNode1.equals(pojoNode3));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode1 = new POJONode("Hello World");
        POJONode pojoNode2 = new POJONode("Hello World");
        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
        String text = "Hello World";
        POJONode pojoNode3 = new POJONode(text);
        POJONode pojoNode4 = new POJONode(new Object());
        assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
        assertNotEquals(pojoNode1.hashCode(), pojoNode4.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode1 = new POJONode("Hello World");
        assertEquals("(value=[Hello World])", pojoNode1.toString());
        POJONode pojoNode2 = new POJONode(new Object());
        assertEquals("(value=[])", pojoNode2.toString());
    }
}