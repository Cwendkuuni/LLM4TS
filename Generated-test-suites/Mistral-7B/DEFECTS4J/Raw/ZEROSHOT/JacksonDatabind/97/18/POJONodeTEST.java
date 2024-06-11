package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

/**
 * Test class for POJONode.java
 */
public class POJONodeTest {

    @Test
    public void testPOJONodeBasic() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertNull(pojoNode.asText());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
        assertNull(pojoNode.binaryValue());
        assertEquals("null", pojoNode.asText(null));
        assertFalse(pojoNode.asBoolean(false));
        assertFalse(pojoNode.asBoolean(true));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            pojoNode.serialize(gen, new SerializerProvider());
            fail("Expected serialization to throw an error since POJONode does not implement writeValue");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testPOJONodeString() {
        POJONode pojoNode = new POJONode("Hello, World!");
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("Hello, World!", pojoNode.asText());
        assertEquals("Hello, World!", pojoNode.asText(null));
        assertFalse(pojoNode.asBoolean(false));
        assertFalse(pojoNode.asBoolean(true));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            pojoNode.serialize(gen, new SerializerProvider());
            String expected = "\"Hello, World!\"";
            String result = mapper.writeValueAsString(pojoNode);
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("Unexpected error during serialization: " + e.getMessage());
        }
    }

    @Test
    public void testPOJONodeBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertTrue(pojoNode.asBoolean(false));
        assertTrue(pojoNode.asBoolean(true));
        assertFalse(pojoNode.asBoolean(false));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(1.0, pojoNode.asDouble(0.0), 0.001);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            pojoNode.serialize(gen, new SerializerProvider());
            String expected = "true";
            String result = mapper.writeValueAsString(pojoNode);
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("Unexpected error during serialization: " + e.getMessage());
        }
    }

    @Test
    public void testPOJONodeNumber() {
        POJONode pojoNode = new POJONode(123);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("123", pojoNode.asText(null));
        assertEquals(123, pojoNode.asInt(0));
        assertEquals(123L, pojoNode.asLong(0L));
        assertEquals(123.0, pojoNode.asDouble(0.0), 0.001);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            pojoNode.serialize(gen, new SerializerProvider());
            String expected = "123";
            String result = mapper.writeValueAsString(pojoNode);
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("Unexpected error during serialization: " + e.getMessage());
        }
    }

    @Test
    public void testPOJONodeJsonSerializable() {
        MyJsonSerializable jsonSerializable = new MyJsonSerializable();
        POJONode pojoNode = new POJONode(jsonSerializable);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertNull(pojoNode.asText());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
        assertNull(pojoNode.binaryValue());
        assertFalse(pojoNode.asBoolean(false));
        assertFalse(pojoNode.asBoolean(true));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            pojoNode.serialize(gen, new SerializerProvider());
            String expected = mapper.writeValueAsString(jsonSerializable);
            String result = mapper.writeValueAsString(pojoNode.getPojo());
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("Unexpected error during serialization: " + e.getMessage());
        }
    }

    // Add more test cases here as needed

    static class MyJsonSerializable implements JsonSerializable {
        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("Hello, JsonSerializable!");
        }
    }
}