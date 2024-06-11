package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        POJONode node = new POJONode(new Object());
        assertNotNull(node);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    /**
     * Test getNodeType().
     */
    @Test
    public void testgetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    /**
     * Test asToken().
     */
    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    /**
     * Test binaryValue().
     */
    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        assertEquals(0, node.binaryValue().length);

        node = new POJONode("test".getBytes());
        assertEquals(5, node.binaryValue().length);

        node = new POJONode(new Object());
        try {
            node.binaryValue();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test asText().
     */
    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());

        node = new POJONode(new String("test"));
        assertEquals("test", node.asText());

        node = new POJONode(new Object());
        assertEquals(node.getClass().getName(), node.asText());
    }

    /**
     * Test asText(String defaultValue).
     */
    @Test
    public void testAsTextDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("defaultValue"));

        node = new POJONode(new String("test"));
        assertEquals("test", node.asText());

        node = new POJONode(new Object());
        assertEquals("defaultValue", node.asText("defaultValue"));
    }

    /**
     * Test asBoolean().
     */
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(Boolean.FALSE);
        assertFalse(node.asBoolean(Boolean.TRUE));
        assertTrue(node.asBoolean(Boolean.FALSE));

        node = new POJONode(Boolean.TRUE);
        assertFalse(node.asBoolean(Boolean.FALSE));
        assertTrue(node.asBoolean(Boolean.TRUE));

        node = new POJONode(null);
        assertFalse(node.asBoolean(Boolean.FALSE));
        assertFalse(node.asBoolean(Boolean.TRUE));

        node = new POJONode(new Object());
        assertFalse(node.asBoolean(Boolean.FALSE));
        assertFalse(node.asBoolean(Boolean.TRUE));
    }

    /**
     * Test asInt().
     */
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(Integer.valueOf(0));
        assertEquals(0, node.asInt(1));
        assertEquals(0, node.asInt());

        node = new POJONode(Integer.valueOf(1));
        assertEquals(1, node.asInt(0));
        assertEquals(1, node.asInt());

        node = new POJONode(new Object());
        assertEquals(0, node.asInt(1));
        assertEquals(0, node.asInt());
    }

    /**
     * Test asLong().
     */
    @Test
    public void testAsLong() {
        POJONode node = new POJONode(Long.valueOf(0L));
        assertEquals(0L, node.asLong(1L));
        assertEquals(0L, node.asLong());

        node = new POJONode(Long.valueOf(1L));
        assertEquals(1L, node.asLong(0L));
        assertEquals(1L, node.asLong());

        node = new POJONode(new Object());
        assertEquals(0L, node.asLong(1L));
        assertEquals(0L, node.asLong());
    }

    /**
     * Test asDouble().
     */
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(Double.valueOf(0.0));
        assertEquals(0.0, node.asDouble(1.0), 0.001);
        assertEquals(0.0, node.asDouble());

        node = new POJONode(Double.valueOf(1.0));
        assertEquals(1.0, node.asDouble(0.0), 0.001);
        assertEquals(1.0, node.asDouble());

        node = new POJONode(new Object());
        assertEquals(0.0, node.asDouble(1.0), 0.001);
        assertEquals(0.0, node.asDouble());
    }

    /**
     * Test serialize().
     */
    @Test
    public void testSerialize() throws JsonProcessingException {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);

        String json = mapper.writeValueAsString(node);
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":{\"@class\":\"java.lang.Object\"}}", json);

        JsonNode jsonNode = mapper.readTree(json);
        assertEquals(JsonNodeType.OBJECT, jsonNode.getNodeType());
        JsonNode pojoNode = jsonNode.get("_value");
        assertEquals(JsonNodeType.OBJECT, pojoNode.getNodeType());
        assertEquals(pojo.getClass().getName(), pojoNode.asText());
    }

    /**
     * Test getPojo().
     */
    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node._value, node.getPojo());
    }

    /**
     * Test equals().
     */
    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode(new Object());

        assertEquals(node1, node1);
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node2, node3);

        node1 = new POJONode(new Object());
        node2 = new POJONode(node1);
        assertTrue(node1.equals(node2));

        node1 = new POJONode("test");
        node2 = new POJONode("test");
        assertTrue(node1.equals(node2));

        node1 = new POJONode(null);
        node2 = new POJONode(null);
        assertTrue(node1.equals(node2));
    }

    /**
     * Test hashCode().
     */
    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode(new Object());

        int hash1 = node1.hashCode();
        int hash2 = node2.hashCode();
        int hash3 = node3.hashCode();

        node1 = new POJONode("test");
        node2 = new POJONode("test");
        int hash4 = node1.hashCode();
        int hash5 = node2.hashCode();

        node1 = new POJONode(null);
        node2 = new POJONode(null);
        int hash6 = node1.hashCode();
        int hash7 = node2.hashCode();

        assertEquals(hash1, node1.hashCode());
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash4, hash5);
        assertNotEquals(hash4, hash6);
        assertNotEquals(hash5, hash6);
    }

    /**
     * Test toString().
     */
    @Test
    public void testToString() {
        POJONode node1 = new POJONode(new Object());
        assertEquals("POJONode(POJO: class java.lang.Object)", node1.toString());

        POJONode node2 = new POJONode(new String("test"));
        assertEquals("POJONode(POJO: class [Ljava.lang.String;@588e052)", node2.toString());

        POJONode node3 = new POJONode(null);
        assertEquals("POJONode(POJO: class [Ljava.lang.Object;@4e7d6d)", node3.toString());
    }
}