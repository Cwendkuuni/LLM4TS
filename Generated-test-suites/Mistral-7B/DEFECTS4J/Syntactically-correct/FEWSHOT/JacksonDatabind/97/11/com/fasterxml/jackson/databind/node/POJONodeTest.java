package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONodeWithNullValue() {
        POJONode pojoNode = new POJONode(null);
        assertNull(pojoNode.getPojo());
        assertEquals("null", pojoNode.asText());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithBooleanValue() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.getPojo() instanceof Boolean);
        assertEquals("true", pojoNode.asText());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithIntegerValue() {
        POJONode pojoNode = new POJONode(1);
        assertEquals(1, pojoNode.getPojo().hashCode());
        assertEquals("1", pojoNode.asText());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithLongValue() {
        POJONode pojoNode = new POJONode(1L);
        assertEquals(1, pojoNode.getPojo().hashCode());
        assertEquals("1", pojoNode.asText());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithDoubleValue() {
        POJONode pojoNode = new POJONode(1.5);
        assertEquals(1, pojoNode.getPojo().hashCode());
        assertEquals("1.5", pojoNode.asText());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithStringValue() {
        POJONode pojoNode = new POJONode("Hello World!");
        assertEquals("Hello World!", pojoNode.getPojo());
        assertEquals("Hello World!", pojoNode.asText());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithJsonSerializableValue() {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializable jsonSerializable = mapper.readValue(getClass().getResource("/testJsonSerializable.json"), JsonSerializable.class);
        POJONode pojoNode = new POJONode(jsonSerializable);
        assertEquals(jsonSerializable, pojoNode.getPojo());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeWithByteArrayValue() {
        byte[] byteArray = new byte[] { 1, 2, 3 };
        POJONode pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.getPojo());
        assertEquals("(binary value of 3 bytes)", pojoNode.toString());
        pojoNode.serialize(getTestGenerator(), getTestProvider());
    }

    @Test
    public void testPOJONodeEquals() {
        POJONode pojoNode1 = new POJONode(1);
        POJONode pojoNode2 = new POJONode(1);
        POJONode pojoNode3 = new POJONode(2);

        assertTrue(pojoNode1.equals(pojoNode1));
        assertTrue(pojoNode1.equals(pojoNode2));
        assertFalse(pojoNode1.equals(pojoNode3));
    }

    @Test
    public void testPOJONodeHashCode() {
        POJONode pojoNode1 = new POJONode(1);
        POJONode pojoNode2 = new POJONode(1);
        POJONode pojoNode3 = new POJONode(2);

        assertEquals(pojoNode1.hashCode(), pojoNode1.hashCode());
        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
        assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
    }

    @Test
    public void testPOJONodeToString() {
        POJONode pojoNode = new POJONode("Hello World!");
        assertEquals("Hello World!", pojoNode.toString());
    }

    @Test
    public void testPOJONodeBinaryValue() throws IOException {
        POJONode pojoNode = new POJONode(new byte[] { 1, 2, 3 });
        byte[] result = pojoNode.binaryValue();
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);
    }

    @Test(expected = IOException.class)
    public void testPOJONodeBinaryValueWithNull() throws IOException {
        POJONode pojoNode = new POJONode(null);
        pojoNode.binaryValue();
    }

    @Test
    public void testPOJONodeAsText() {
        POJONode pojoNode = new POJONode("Hello World!");
        assertEquals("Hello World!", pojoNode.asText());
    }

    @Test
    public void testPOJONodeAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode("Hello World!");
        assertEquals("Hello World!", pojoNode.asText());
        assertEquals("", pojoNode.asText(""));
    }

    @Test
    public void testPOJONodeAsBoolean() {
        POJONode pojoNode1 = new POJONode(true);
        POJONode pojoNode2 = new POJONode(false);
        POJONode pojoNode3 = new POJONode(null);

        assertTrue(pojoNode1.asBoolean(false));
        assertFalse(pojoNode2.asBoolean(true));
        assertFalse(pojoNode3.asBoolean(false));
    }

    @Test
    public void testPOJONodeAsInt() {
        POJONode pojoNode1 = new POJONode(1);
        POJONode pojoNode2 = new POJONode(2);
        POJONode pojoNode3 = new POJONode(null);

        assertEquals(1, pojoNode1.asInt(0));
        assertEquals(2, pojoNode2.asInt(1));
        assertEquals(0, pojoNode3.asInt(0));
    }

    @Test
    public void testPOJONodeAsLong() {
        POJONode pojoNode1 = new POJONode(1L);
        POJONode pojoNode2 = new POJONode(2L);
        POJONode pojoNode3 = new POJONode(null);

        assertEquals(1, pojoNode1.asLong(0L));
        assertEquals(2, pojoNode2.asLong(1L));
        assertEquals(0L, pojoNode3.asLong(0L));
    }

    @Test
    public void testPOJONodeAsDouble() {
        POJONode pojoNode1 = new POJONode(1.0);
        POJONode pojoNode2 = new POJONode(2.5);
        POJONode pojoNode3 = new POJONode(null);

        assertEquals(1.0, pojoNode1.asDouble(0.0), 0.0);
        assertEquals(2.5, pojoNode2.asDouble(1.0), 0.0);
        assertEquals(0.0, pojoNode3.asDouble(0.0), 0.0);
    }

    @Test
    public void testPOJONodeSerialize() throws IOException {
        POJONode pojoNode = new POJONode("Hello World!");
        JsonNode jsonNode = mapper.readTree("{}");
        JsonGenerator gen = jsonNode.getRootNode().traverse();
        pojoNode.serialize(gen, getTestProvider());
        assertEquals("Hello World!", jsonNode.get("").asText());
    }

    private JsonGenerator getTestGenerator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.disablePrettyPrint();
        return gen;
    }

    private SerializerProvider getTestProvider() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getSerializerProvider();
    }
}