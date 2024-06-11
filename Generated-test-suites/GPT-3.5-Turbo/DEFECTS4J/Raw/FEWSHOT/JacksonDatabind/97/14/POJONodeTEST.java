package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

public class POJONodeTest {
    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(bytes);
        assertEquals(bytes, pojoNode.binaryValue());
    }

    @Test
    public void testBinaryValueNonBinary() throws IOException {
        String value = "test";
        POJONode pojoNode = new POJONode(value);
        assertTrue(pojoNode.binaryValue() instanceof IOException);
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        String defaultValue = "default";
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asText(defaultValue));
    }

    @Test
    public void testAsBoolean() {
        boolean defaultValue = true;
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asBoolean(defaultValue));
    }

    @Test
    public void testAsInt() {
        int defaultValue = 1;
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asInt(defaultValue));
    }

    @Test
    public void testAsLong() {
        long defaultValue = 1L;
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asLong(defaultValue));
    }

    @Test
    public void testAsDouble() {
        double defaultValue = 1.0;
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asDouble(defaultValue), 0.0);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        POJONode pojoNode = new POJONode(null);
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(pojoNode));
        assertNull(jsonNode.get("value"));
    }

    @Test
    public void testSerializeWithJsonSerializable() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        POJONode pojoNode = new POJONode(new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
                gen.writeString("test");
            }
        });
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(pojoNode));
        assertEquals("test", jsonNode.get("value").textValue());
    }

    @Test
    public void testSerializeWithOtherObject() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        POJONode pojoNode = new POJONode(new Object());
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(pojoNode));
        assertEquals("java.lang.Object@<hashcode>", jsonNode.get("value").textValue());
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }

    @Test
    public void testEqualsWithSameValue() {
        Object value = new Object();
        POJONode pojoNode1 = new POJONode(value);
        POJONode pojoNode2 = new POJONode(value);
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testEqualsWithDifferentValue() {
        Object value1 = new Object();
        Object value2 = new Object();
        POJONode pojoNode1 = new POJONode(value1);
        POJONode pojoNode2 = new POJONode(value2);
        assertTrue(!pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value.hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToStringWithBinaryValue() {
        byte[] bytes = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }

    @Test
    public void testToStringWithRawValue() {
        RawValue rawValue = new RawValue("test");
        POJONode pojoNode = new POJONode(rawValue);
        assertEquals("(raw value 'test')", pojoNode.toString());
    }

    @Test
    public void testToStringWithOtherValue() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value.toString(), pojoNode.toString());
    }
}