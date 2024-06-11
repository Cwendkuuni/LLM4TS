package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = new POJONode("Hello World");
        String json = mapper.writeValueAsString(pojoNode);
        assertEquals("\"Hello World\"", json);

        ObjectMapper roundTripMapper = new ObjectMapper();
        JsonNode jsonNode = roundTripMapper.readTree(json);
        POJONode deserializedPojoNode = (POJONode) jsonNode;
        assertEquals("Hello World", deserializedPojoNode.getPojo());
    }

    @Test
    public void testPOJONodeSerializationWithDefaultValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = new POJONode(null);
        String json = mapper.writeValueAsString(pojoNode);
        assertEquals("null", json);

        ObjectMapper roundTripMapper = new ObjectMapper();
        JsonNode jsonNode = roundTripMapper.readTree(json);
        POJONode deserializedPojoNode = (POJONode) jsonNode;
        assertNull(deserializedPojoNode.getPojo());
    }

    @Test
    public void testPOJONodeSerializationWithJsonSerializableObject() throws JsonProcessingException {
        class TestObject implements JsonSerializable {
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(name);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        TestObject testObject = new TestObject("TestObject");
        POJONode pojoNode = new POJONode(testObject);
        String json = mapper.writeValueAsString(pojoNode);
        assertEquals("\"TestObject\"", json);

        ObjectMapper roundTripMapper = new ObjectMapper();
        JsonNode jsonNode = roundTripMapper.readTree(json);
        POJONode deserializedPojoNode = (POJONode) jsonNode;
        assertEquals("TestObject", deserializedPojoNode.getPojo().getClass().getSimpleName());
    }

    @Test
    public void testPOJONodeBinaryValue() throws IOException {
        byte[] binaryData = {1, 2, 3};
        POJONode pojoNode = new POJONode(binaryData);
        byte[] result = pojoNode.binaryValue();
        assertArrayEquals(binaryData, result);
    }

    @Test
    public void testPOJONodeAsText() {
        POJONode pojoNode = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode.asText());
        pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testPOJONodeAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText("Default Value"));
        pojoNode = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode.asText("Default Value"));
    }

    @Test
    public void testPOJONodeAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
        pojoNode = new POJONode(new Boolean(false));
        assertFalse(pojoNode.asBoolean(true));
        pojoNode = new POJONode(null);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testPOJONodeAsInt() {
        POJONode pojoNode = new POJONode(1);
        assertEquals(1, pojoNode.asInt(0));
        pojoNode = new POJONode(new Integer(2));
        assertEquals(2, pojoNode.asInt(0));
        pojoNode = new POJONode(null);
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testPOJONodeAsLong() {
        POJONode pojoNode = new POJONode(1L);
        assertEquals(1L, pojoNode.asLong(0L));
        pojoNode = new POJONode(new Long(2L));
        assertEquals(2L, pojoNode.asLong(0L));
        pojoNode = new POJONode(null);
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testPOJONodeAsDouble() {
        POJONode pojoNode = new POJONode(1.0);
        assertEquals(1.0, pojoNode.asDouble(0.0), 0.0);
        pojoNode = new POJONode(new Double(2.0));
        assertEquals(2.0, pojoNode.asDouble(0.0), 0.0);
        pojoNode = new POJONode(null);
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.0);
    }
}