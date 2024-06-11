package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreatePOJONode() {
        POJONode node = new POJONode(new Object());
        assertNotNull(node);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testSerializeNullPOJONode() throws JsonProcessingException {
        POJONode node = new POJONode(null);
        JsonNode serializedNode = mapper.writeValueAsTree(node);
        assertEquals(JsonNodeType.NULL, serializedNode.getNodeType());
    }

    @Test
    public void testSerializeSimplePOJONode() throws JsonProcessingException {
        String testString = "Test String";
        POJONode node = new POJONode(testString);
        JsonNode serializedNode = mapper.writeValueAsTree(node);
        assertEquals(JsonNodeType.POJO, serializedNode.getNodeType());
        assertEquals(testString, serializedNode.asText());
    }

    @Test
    public void testSerializeComplexPOJONode() throws JsonProcessingException {
        Object complexObject = new ComplexObject();
        POJONode node = new POJONode(complexObject);
        JsonNode serializedNode = mapper.writeValueAsTree(node);
        assertEquals(JsonNodeType.POJO, serializedNode.getNodeType());
        JsonNode deserializedNode = mapper.readTree(serializedNode.toString());
        assertEquals(complexObject.getClass(), deserializedNode.getPojo().getClass());
    }

    @Test(expected = IOException.class)
    public void testSerializeNonSerializableObject() throws IOException {
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        POJONode node = new POJONode(nonSerializableObject);
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] binaryData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        POJONode node = new POJONode(binaryData);
        byte[] result = node.binaryValue();
        assertArrayEquals(binaryData, result);
    }

    @Test
    public void testSerializePOJONodeWithBinaryData() throws JsonProcessingException {
        byte[] binaryData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        POJONode node = new POJONode(binaryData);
        JsonNode serializedNode = mapper.writeValueAsTree(node);
        assertEquals(JsonNodeType.BINARY, serializedNode.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("Test String");
        assertEquals("Test String", node.asText());
        node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(Boolean.TRUE);
        assertEquals(true, node.asBoolean(false));
        node = new POJONode(Boolean.FALSE);
        assertEquals(false, node.asBoolean(true));
        node = new POJONode(null);
        assertEquals(false, node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
        node = new POJONode("Test String");
        assertEquals(0, node.asInt(1));
        node = new POJONode(null);
        assertEquals(0, node.asInt(1));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));
        node = new POJONode("Test String");
        assertEquals(0L, node.asLong(1L));
        node = new POJONode(null);
        assertEquals(0L, node.asLong(1L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.0);
        assertEquals(1.0, node.asDouble(0.0), 0.001);
        node = new POJONode("Test String");
        assertEquals(0.0, node.asDouble(1.0), 0.001);
        node = new POJONode(null);
        assertEquals(0.0, node.asDouble(1.0), 0.001);
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode(new Object());
        POJONode node4 = new POJONode("Test String");
        assertEquals(node1, node1);
        assertEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, node4);
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode(new Object());
        POJONode node4 = new POJONode("Test String");
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
        assertNotEquals(node1.hashCode(), node3.hashCode());
        assertNotEquals(node1.hashCode(), node4.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("(binary value of 0 bytes)", node.toString());
        node = new POJONode(new Object());
        assertEquals("(POJONode: /\n  Pojo = com.fasterxml.jackson.databind.node.POJONode@7852e9)", node.toString());
        node = new POJONode("Test String");
        assertEquals("(raw value 'Test String')", node.toString());
    }
}

class ComplexObject {
    // Add complex object properties and methods here as needed
}

class NonSerializableObject {
    // Do not add any serialization methods or fields to this class
}