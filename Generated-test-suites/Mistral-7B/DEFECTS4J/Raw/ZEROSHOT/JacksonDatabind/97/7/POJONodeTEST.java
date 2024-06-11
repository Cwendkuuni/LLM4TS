package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONodeBasic() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new String("test"));

        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        assertNull(node.binaryValue());

        assertEquals("test", node.asText());
        assertEquals("test", node.asText(null));
        assertTrue(node.asBoolean(false));
        assertEquals(0, node.asInt(0));
        assertEquals(0L, node.asLong(0L));
        assertEquals(0.0, node.asDouble(0.0), 0.001);

        Object pojo = node.getPojo();
        assertEquals("test", pojo.toString());
    }

    @Test
    public void testPOJONodeBoolean() {
        POJONode node = new POJONode(Boolean.TRUE);
        assertTrue(node.asBoolean(Boolean.FALSE));
        assertFalse(node.asBoolean(Boolean.TRUE));
    }

    @Test
    public void testPOJONodeNumber() {
        POJONode nodeInt = new POJONode(Integer.valueOf(1));
        assertEquals(1, nodeInt.asInt(0));

        POJONode nodeLong = new POJONode(Long.valueOf(1L));
        assertEquals(1L, nodeLong.asLong(0L));

        POJONode nodeDouble = new POJONode(Double.valueOf(1.1));
        assertEquals(1.1, nodeDouble.asDouble(0.0), 0.001);
    }

    @Test
    public void testPOJONodeEquals() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree("test1");
        POJONode node2 = mapper.valueToTree("test1");
        POJONode node3 = mapper.valueToTree("test2");

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(null));
    }

    @Test
    public void testPOJONodeHashCode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree("test1");
        POJONode node2 = mapper.valueToTree("test1");
        POJONode node3 = mapper.valueToTree("test2");

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testPOJONodeSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        POJONode node = mapper.valueToTree(new String("test"));

        String json = "{\"value\":\"test\"}";
        JsonParser parser = factory.createParser(json);

        POJONode deserializedNode = mapper.readValue(parser, POJONode.class);
        assertEquals(node.getPojo(), deserializedNode.getPojo());
    }
}