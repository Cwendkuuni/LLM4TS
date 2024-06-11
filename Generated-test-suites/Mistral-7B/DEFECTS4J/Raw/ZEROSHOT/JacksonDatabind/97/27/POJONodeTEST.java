package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * JUnit 4 test file for POJONode class.
 */
public class POJONodeTest {

    @Test
    public void testPOJONode() {
        // Test constructor
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(JsonNodeType.POJO, node.getNodeType());

        // Test getting POJO
        Object pojo = node.getPojo();
        assertEquals(obj, pojo);

        // Test base class overrides
        JsonNodeType nodeType = node.getNodeType();
        assertEquals(JsonNodeType.POJO, nodeType);
        JsonToken jsonToken = node.asToken();
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken);

        // Test binaryValue()
        byte[] binaryData = new byte[10];
        POJONode binaryNode = new POJONode(binaryData);
        byte[] result = binaryNode.binaryValue();
        assertArrayEquals(binaryData, result);

        // Test asText()
        POJONode textNode = new POJONode("test");
        String resultText = textNode.asText();
        assertEquals("test", resultText);

        // Test asBoolean()
        POJONode booleanNodeTrue = new POJONode(Boolean.TRUE);
        POJONode booleanNodeFalse = new POJONode(Boolean.FALSE);
        boolean resultBooleanTrue = booleanNodeTrue.asBoolean(false);
        assertTrue(resultBooleanTrue);
        boolean resultBooleanFalse = booleanNodeFalse.asBoolean(false);
        assertFalse(resultBooleanFalse);

        // Test asInt()
        POJONode intNode = new POJONode(Integer.valueOf(42));
        int resultInt = intNode.asInt(0);
        assertEquals(42, resultInt);

        // Test asLong()
        POJONode longNode = new POJONode(Long.valueOf(42L));
        long resultLong = longNode.asLong(0L);
        assertEquals(42L, resultLong);

        // Test asDouble()
        POJONode doubleNode = new POJONode(Double.valueOf(3.14));
        double resultDouble = doubleNode.asDouble(0.0);
        assertEquals(3.14, resultDouble, 0.001);

        // Test serialization
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(node);
            JsonNode jsonNode = mapper.readTree(json);
            assertEquals(JsonNodeType.POJO, jsonNode.getNodeType());
            assertEquals(node.asText(), jsonNode.asText());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test equals()
        POJONode equalNode = new POJONode(obj);
        POJONode notEqualNode = new POJONode(new Object());
        assertTrue(node.equals(equalNode));
        assertFalse(node.equals(notEqualNode));

        // Test hashCode()
        POJONode sameNode = new POJONode(obj);
        int hashCodeSame = node.hashCode();
        int hashCodeDifferent = sameNode.hashCode();
        assertEquals(hashCodeSame, hashCodeDifferent);

        // Test toString()
        String toStringResult = node.toString();
        assertTrue(toStringResult.contains("POJO"));
        assertTrue(toStringResult.contains("value: " + obj.toString()));
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };
        POJONode node = new POJONode(obj);
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"value\":{\"@class\":\"java.lang.Object\",\"toString\":\"test\"}}", json);
    }

    @Test
    public void testSerializeWithJsonSerializable() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = new MyClass();
        obj.setName("test");
        POJONode node = new POJONode(obj);
        String json = mapper.writeValueAsString(node);
        JsonNode jsonNode = mapper.readTree(json);
        assertEquals("{\"name\":\"test\"}", jsonNode.asText());
    }

    private static class MyClass implements JsonSerializable {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeFieldName("name");
            gen.writeString(name);
        }
    }
}