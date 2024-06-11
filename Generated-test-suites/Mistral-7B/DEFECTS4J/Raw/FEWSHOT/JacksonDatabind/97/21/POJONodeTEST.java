package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONodeWithNullValue() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertNull(node.asText());
        assertFalse(node.asBoolean(false));
        assertEquals(0, node.asInt(0));
        assertEquals(0L, node.asLong(0L));
        assertEquals(0.0, node.asDouble(0.0), 0.001);

        try {
            node.serialize(new JsonGenerator(null), new SerializerProvider());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPOJONodeWithBooleanValue() {
        POJONode node = new POJONode(true);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("true", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(1, node.asInt(0));
        assertEquals(1L, node.asLong(0L));
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        try {
            node.serialize(new JsonGenerator(null), new SerializerProvider());
            assertTrue(node._value instanceof Boolean);
            JsonGenerator gen = new JsonGenerator(null);
            node.serialize(gen, new SerializerProvider());
            assertEquals("true", gen.getOutputValue());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testPOJONodeWithNumberValue() {
        POJONode node = new POJONode(123);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("123", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(123, node.asInt(0));
        assertEquals(123L, node.asLong(0L));
        assertEquals(123.0, node.asDouble(0.0), 0.001);

        try {
            node.serialize(new JsonGenerator(null), new SerializerProvider());
            assertTrue(node._value instanceof Number);
            JsonGenerator gen = new JsonGenerator(null);
            node.serialize(gen, new SerializerProvider());
            assertEquals("123", gen.getOutputValue());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testPOJONodeWithStringValue() {
        POJONode node = new POJONode("foo");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("\"foo\"", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals("foo", node.asText(null));
        assertEquals("foo", node.asText("default"));
        assertEquals('f', node.asText(null).charAt(0));

        try {
            node.serialize(new JsonGenerator(null), new SerializerProvider());
            assertTrue(node._value instanceof String);
            JsonGenerator gen = new JsonGenerator(null);
            node.serialize(gen, new SerializerProvider());
            assertEquals("\"foo\"", gen.getOutputValue());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testPOJONodeWithJsonSerializableValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        MyClass myClass = new MyClass("hello");
        POJONode node = new POJONode(myClass);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("\"hello\"", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(myClass.toString(), node.asText(null));
        assertEquals(myClass.toString(), node.asText("default"));

        try {
            node.serialize(new JsonGenerator(null), new SerializerProvider());
            assertTrue(node._value instanceof JsonSerializable);
            JsonGenerator gen = new JsonGenerator(null);
            node.serialize(gen, new SerializerProvider());
            String result = gen.getOutputValue();
            JsonNode jsonNode = mapper.readTree(result);
            assertEquals("hello", jsonNode.get("message").asText());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testPOJONodeWithByteArrayValue() throws IOException {
        byte[] bytes = new byte[] { 1, 2, 3 };
        POJONode node = new POJONode(bytes);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertNull(node.asText());
        assertFalse(node.asBoolean(false));
        assertEquals(bytes.length, node.asInt(0));
        assertEquals(bytes.length * (long) Byte.SIZE, node.asLong(0L));

        try {
            node.serialize(new JsonGenerator(null), new SerializerProvider());
            fail();
        } catch (IOException e) {
            // expected
        }

        try (ObjectMapper mapper = new ObjectMapper()) {
            String jsonString = mapper.writeValueAsString(bytes);
            POJONode nodeFromJson = new POJONode(mapper.readValue(jsonString, POJONode.class));
            byte[] result = nodeFromJson.binaryValue();
            assertEquals(bytes.length, result.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(bytes[i], result[i]);
            }
        }
    }
}

class MyClass implements JsonSerializable {
    private String message;

    MyClass(String message) {
        this.message = message;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("message");
        gen.writeString(message);
        gen.writeEndObject();
    }
}