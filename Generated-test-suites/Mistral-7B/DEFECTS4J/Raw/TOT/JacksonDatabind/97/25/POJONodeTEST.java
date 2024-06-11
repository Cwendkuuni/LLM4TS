package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        POJONode node = new POJONode(new String("Test String"));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"value\":\"Test String\"}", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertEquals("Test String", deserializedNode.getPojo().toString());
    }

    @Test
    public void testPOJONodeNullValue() throws JsonProcessingException {
        POJONode node = new POJONode(null);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);
        assertEquals("null", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertNull(deserializedNode.getPojo());
    }

    @Test
    public void testPOJONodeBooleanValue() throws JsonProcessingException {
        POJONode node = new POJONode(true);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"value\":true}", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertTrue(deserializedNode.asBoolean(false));
    }

    @Test
    public void testPOJONodeNumberValue() throws JsonProcessingException {
        POJONode node = new POJONode(123);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"value\":123}", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertEquals(123, deserializedNode.asInt(0));
    }

    @Test
    public void testPOJONodeObjectValue() throws JsonProcessingException {
        POJONode node = new POJONode(new Object());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"value\":{}}", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertNotNull(deserializedNode.getPojo());
    }

    @Test(expected = IOException.class)
    public void testPOJONodeBytesValueSerialization() throws IOException {
        POJONode node = new POJONode(new byte[10]);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(node);
    }

    @Test
    public void testPOJONodeBytesValueDeserialization() throws JsonProcessingException {
        POJONode node = new POJONode(new byte[10]);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertArrayEquals(deserializedNode.binaryValue(), node.binaryValue());
    }
}