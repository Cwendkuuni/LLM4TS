package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class POJONodeTest {

    @Test
    public void testEquals() {
        Object pojo1 = new Object() {};
        Object pojo2 = new Object() {};
        POJONode node1 = new POJONode(pojo1);
        POJONode node2 = new POJONode(pojo2);
        POJONode node3 = new POJONode(pojo1);

        // Test equality with same POJONodes
        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node3));
        assertTrue(node3.equals(node1));
        assertTrue(node1.equals(node2.hashCode() == node2.hashCode() ? node2 : new POJONode(pojo1)));

        // Test inequality with different POJONodes
        assertFalse(node1.equals(null));
        assertFalse(node1.equals(""));
        assertFalse(node1.equals(new Long(0)));
        assertFalse(node1.equals(new IntNode(1)));
        assertFalse(node1.equals(new TextNode("")));
        assertFalse(node1.equals(node2));

        // Test inequality with different types
        assertFalse(node1.equals(new ArrayNode(new POJONode[0])));
        assertFalse(node1.equals(new ObjectNode(new ObjectMapper().createObjectNode())));
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object() {};
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testHashCode() {
        Object pojo = new Object() {};
        POJONode node1 = new POJONode(pojo);
        POJONode node2 = new POJONode(pojo);
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object pojo = new Object() {};
        POJONode node = new POJONode(pojo);

        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, node);

        String json = writer.toString();
        assertNotNull(json);
        assertTrue(json.startsWith("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\""));
    }

    @Test
    public void testToString() {
        Object pojo = new Object() {};
        POJONode node = new POJONode(pojo);

        String str = node.toString();
        assertNotNull(str);
        assertTrue(str.startsWith("(value="));
    }

    @Test
    public void testAsText() {
        Object pojo = new Object() {};
        POJONode node = new POJONode(pojo);

        String text = node.asText();
        assertEquals("null", text);
    }

    @Test
    public void testAsTextWithDefaultValue() {
        Object pojo = new Object() {};
        POJONode node = new POJONode(pojo);

        String defaultValue = "default";
        String text = node.asText(defaultValue);
        assertEquals("null", text);
    }

    @Test
    public void testAsBoolean() {
        Object pojo = new Boolean(true);
        POJONode node = new POJONode(pojo);

        boolean booleanValue = node.asBoolean(false);
        assertTrue(booleanValue);
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        Object pojo = null;
        POJONode node = new POJONode(pojo);

        boolean booleanValue = node.asBoolean(false);
        assertFalse(booleanValue);
    }

    @Test
    public void testAsInt() {
        Object pojo = new Integer(1);
        POJONode node = new POJONode(pojo);

        int intValue = node.asInt(0);
        assertEquals(1, intValue);
    }

    @Test
    public void testAsIntWithDefaultValue() {
        Object pojo = null;
        POJONode node = new POJONode(pojo);

        int intValue = node.asInt(0);
        assertEquals(0, intValue);
    }

    @Test
    public void testAsLong() {
        Object pojo = new Long(1);
        POJONode node = new POJONode(pojo);

        long longValue = node.asLong(0);
        assertEquals(1, longValue);
    }

    @Test
    public void testAsLongWithDefaultValue() {
        Object pojo = null;
        POJONode node = new POJONode(pojo);

        long longValue = node.asLong(0);
        assertEquals(0, longValue);
    }

    @Test
    public void testAsDouble() {
        Object pojo = new Double(1.0);
        POJONode node = new POJONode(pojo);

        double doubleValue = node.asDouble(0.0);
        assertEquals(1.0, doubleValue, 0.001);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        Object pojo = null;
        POJONode node = new POJONode(pojo);

        double doubleValue = node.asDouble(0.0);
        assertEquals(0.0, doubleValue, 0.001);
    }

    @Test
    public void testBinaryValue() throws IOException {
        Object pojo = new byte[] { 1, 2, 3 };
        POJONode node = new POJONode(pojo);

        byte[] binaryValue = node.binaryValue();
        assertNotNull(binaryValue);
        assertArrayEquals(new byte[] { 1, 2, 3 }, binaryValue);
    }
}