package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

/**
 * POJONodeTest
 */
public class POJONodeTest {

    @Test
    public void testConstructor() {
        POJONode node = new POJONode(new Object());
        assertNotNull(node);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
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
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        byte[] result = node.binaryValue();
        assertArrayEquals(new byte[0], result);

        node = new POJONode(new String("test").getBytes());
        result = node.binaryValue();
        assertArrayEquals(new byte[]{"t", "e", "s", "t"}, result);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());

        node = new POJONode(new Object());
        assertEquals(node.getPojo().toString(), node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("", node.asText("defaultValue"));

        node = new POJONode(new Object());
        assertEquals(node.getPojo().toString(), node.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(Boolean.TRUE);
        assertTrue(node.asBoolean(false));

        node = new POJONode(Boolean.FALSE);
        assertFalse(node.asBoolean(true));

        node = new POJONode(null);
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));

        node = new POJONode(new Integer(2));
        assertEquals(2, node.asInt(0));

        node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));

        node = new POJONode(new Long(2L));
        assertEquals(2L, node.asLong(0L));

        node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.0);
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        node = new POJONode(new Double(2.0));
        assertEquals(2.0, node.asDouble(0.0), 0.001);

        node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.001);
    }

    @Test(expected = IOException.class)
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(new ObjectMapper().readTree("{}"));
        node.serialize(null, null);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node.getPojo(), node._value);
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode(new Object());

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2)); // should be equal due to POJONode constructor
        assertFalse(node1.equals(node3));

        node2.setValue(new Object()); // change the object wrapper
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();

        assertEquals(hashCode1, hashCode2); // should be equal due to POJONode constructor

        node2.setValue(new Object()); // change the object wrapper
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new Object());
        String result = node.toString();
        if (node.getPojo() instanceof String) {
            assertEquals("(raw value '" + node.getPojo() + "')", result);
        } else {
            assertEquals(node.getPojo().toString(), result);
        }
    }
}