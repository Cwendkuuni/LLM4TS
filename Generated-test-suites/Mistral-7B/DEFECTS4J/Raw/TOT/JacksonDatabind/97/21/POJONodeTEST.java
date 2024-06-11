package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeWithNullValue() throws JsonProcessingException {
        POJONode node = new POJONode(null);
        String json = mapper.writeValueAsString(node);
        assertEquals("null", json);
        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertNull(deserializedNode.getPojo());
    }

    @Test
    public void testPOJONodeWithStringValue() throws JsonProcessingException {
        String value = "testString";
        POJONode node = new POJONode(value);
        String json = mapper.writeValueAsString(node);
        assertEquals("\"testString\"", json);
        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertEquals(value, deserializedNode.getPojo());
    }

    @Test
    public void testPOJONodeWithNumberValue() throws JsonProcessingException {
        int value = 123;
        POJONode node = new POJONode(value);
        String json = mapper.writeValueAsString(node);
        assertEquals("123", json);
        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertEquals(value, deserializedNode.getPojo());
    }

    @Test
    public void testPOJONodeWithBooleanValue() throws JsonProcessingException {
        boolean value = true;
        POJONode node = new POJONode(value);
        String json = mapper.writeValueAsString(node);
        assertEquals("true", json);
        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertTrue(deserializedNode.getPojo() instanceof Boolean);
        assertTrue(((Boolean)deserializedNode.getPojo()).booleanValue());
    }

    @Test
    public void testPOJONodeWithObjectValue() throws JsonProcessingException {
        String testObject = "testObject";
        Object testObjectInstance = new TestObject(testObject);
        POJONode node = new POJONode(testObjectInstance);
        String json = mapper.writeValueAsString(node);
        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertEquals(testObjectInstance, deserializedNode.getPojo());
    }

    @Test(expected = IOException.class)
    public void testPOJONodeBinaryValue() throws JsonProcessingException {
        byte[] binaryData = new byte[] { 0x1, 0x2, 0x3 };
        POJONode node = new POJONode(binaryData);
        String json = mapper.writeValueAsString(node);
        mapper.readValue(json, POJONode.class);
        // This line should throw an IOException as binary data cannot be directly serialized to JSON
    }
}

class TestObject {
    private final String value;

    public TestObject(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject testObject = (TestObject) o;
        return value.equals(testObject.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}