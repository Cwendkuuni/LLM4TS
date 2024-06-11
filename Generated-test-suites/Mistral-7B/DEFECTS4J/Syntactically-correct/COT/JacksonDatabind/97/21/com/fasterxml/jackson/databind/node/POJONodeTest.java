package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

    @Test
    public void testConstructor() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("Hello World");
        assertEquals("Hello World", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("Default Value"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node1 = new POJONode(true);
        POJONode node2 = new POJONode(false);

        assertTrue(node1.asBoolean(false));
        assertFalse(node2.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node1 = new POJONode(1);
        POJONode node2 = new POJONode(2);

        assertEquals(1, node1.asInt(0));
        assertEquals(2, node2.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node1 = new POJONode(1L);
        POJONode node2 = new POJONode(2L);

        assertEquals(1L, node1.asLong(0L));
        assertEquals(2L, node2.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node1 = new POJONode(1.0);
        POJONode node2 = new POJONode(2.0);

        assertEquals(1.0, node1.asDouble(0.0), 0.001);
        assertEquals(2.0, node2.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        POJONode node = new POJONode(new Object());

        JsonNode rootNode = mapper.valueToTree(node);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());

        JsonNode generatedNode = mapper.readTree(System.in);

        assertEquals(rootNode, generatedNode);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node.getPojo(), node._value);
    }

    @Test(expected = IOException.class)
    public void testBinaryValueNullValue() throws IOException {
        POJONode node = new POJONode(null);
        node.binaryValue();
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("Hello World");

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("Hello World");

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}