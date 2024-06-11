package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

public class POJONodeTest {

    @Test
    public void testPOJONodeBasicFunctionality() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());

        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        JsonNode expected = mapper.valueToTree(new Object());
        assertEquals(expected.getNodeType(), node.getNodeType());
        assertEquals(expected.asText(), node.asText());
        assertEquals(expected.asInt(0), node.asInt(0));
        assertEquals(expected.asLong(0), node.asLong(0));
        assertEquals(expected.asDouble(0.0), node.asDouble(0.0));
        assertEquals(expected, node.deepClone());

        JsonNode nullNode = mapper.valueToTree(null);
        assertNotNull(node.binaryValue());
        assertNull(nullNode.binaryValue());
    }

    @Test
    public void testPOJONodeSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        POJONode node = new POJONode(new Object());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        node.serialize(gen, new SerializerProvider());
        gen.close();

        JsonNode expected = mapper.valueToTree(new Object());
        assertEquals(expected, mapper.readTree(new StringReader(mapper.writerWithView().writeValueAsString(node))));
    }

    @Test
    public void testPOJONodeFromJsonSerializable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializable jsonSerializable = new Object() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("Test");
            }
        };
        JsonNode node = new POJONode(jsonSerializable);

        assertEquals("Test", node.asText());
    }

    @Test
    public void testPOJONodeEquals() {
        ObjectMapper mapper = new ObjectMapper();

        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("Test".equals("Test") ? new Object() : new String("Test"));

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(new TextNode("Test")));
    }

    @Test
    public void testPOJONodeHashCode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();

        assertEquals(hashCode1, hashCode2);

        POJONode node3 = new POJONode(new Object() {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        });

        assertNotEquals(hashCode1, node3.hashCode());
    }

    @Test
    public void testPOJONodeToString() {
        ObjectMapper mapper = new ObjectMapper();

        POJONode node = new POJONode(new Object());
        String expected = String.format("(POJONode %s)", node.getPojo().getClass().getName());

        assertEquals(expected, node.toString());
    }
}