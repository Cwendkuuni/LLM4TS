package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        POJONode node = new POJONode(new String("Hello World"));
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"value\":\"Hello World\"}", json);

        POJONode nullNode = new POJONode(null);
        json = mapper.writeValueAsString(nullNode);
        assertEquals("null", json);

        POJONode numberNode = new POJONode(123);
        json = mapper.writeValueAsString(numberNode);
        assertEquals("123", json);

        POJONode booleanNode = new POJONode(true);
        json = mapper.writeValueAsString(booleanNode);
        assertEquals("true", json);
    }

    @Test
    public void testPOJONodeDeserialization() throws JsonProcessingException {
        String json = "{\"value\":\"Hello World\"}";
        POJONode node = mapper.readValue(json, POJONode.class);
        assertEquals("Hello World", node.getPojo());

        json = "null";
        node = mapper.readValue(json, POJONode.class);
        assertNull(node.getPojo());

        json = "123";
        node = mapper.readValue(json, POJONode.class);
        assertEquals(123, node.getPojo());

        json = "true";
        node = mapper.readValue(json, POJONode.class);
        assertTrue(node.getPojo() instanceof Boolean);
        assertEquals(true, ((Boolean) node.getPojo()).booleanValue());
    }

    @Test
    public void testPOJONodeBinaryValue() throws IOException {
        byte[] binaryData = {1, 2, 3};
        POJONode node = new POJONode(binaryData);
        byte[] result = node.binaryValue();
        assertArrayEquals(binaryData, result);
    }

    @Test(expected = IOException.class)
    public void testPOJONodeBinaryValueNonBinary() {
        POJONode node = new POJONode("Hello World");
        node.binaryValue();
    }
}