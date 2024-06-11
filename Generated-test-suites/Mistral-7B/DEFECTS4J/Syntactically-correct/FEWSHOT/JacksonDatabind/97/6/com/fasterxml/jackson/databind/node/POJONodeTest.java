package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

    @Test
    public void testEquals() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode1 = mapper.valueToTree(new Object());
        POJONode pojoNode2 = mapper.valueToTree(new Object());
        assertTrue(pojoNode1.equals(pojoNode2));
        assertFalse(pojoNode1.equals("Not an instance of POJONode"));
        assertFalse(pojoNode1.equals(1));
    }

    @Test
    public void testGetPojo() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = mapper.valueToTree(new Object());
        assertEquals(pojoNode.getPojo().getClass(), Object.class);
    }

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsText() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = mapper.valueToTree(new Object());
        assertEquals("null", pojoNode.asText());
        Object obj = new Object();
        String objString = obj.toString();
        assertEquals(objString, pojoNode.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = mapper.valueToTree(new Object());
        String defaultValue = "Default Value";
        assertEquals(defaultValue, pojoNode.asText("Default Value"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(Boolean.TRUE);
        assertTrue(pojoNode.asBoolean(false));
        POJONode pojoNodeFalse = new POJONode(Boolean.FALSE);
        assertFalse(pojoNodeFalse.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(1);
        assertEquals(1, pojoNode.asInt(0));
        POJONode pojoNodeNegative = new POJONode(-1);
        assertEquals(-1, pojoNodeNegative.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(1L);
        assertEquals(1L, pojoNode.asLong(0L));
        POJONode pojoNodeNegative = new POJONode(-1L);
        assertEquals(-1L, pojoNodeNegative.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(1.0);
        assertEquals(1.0, pojoNode.asDouble(0.0), 0.01);
        POJONode pojoNodeNegative = new POJONode(-1.0);
        assertEquals(-1.0, pojoNodeNegative.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class, null);
        POJONode pojoNode = new POJONode(new Object());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        pojoNode.serialize(gen, new SerializerProvider() {
            @Override
            public void serializeValue(JsonGenerator gen, SerializerProvider provider, Object value) throws IOException {
                serializer.serialize(value, gen, provider);
            }
        });
        gen.flush();
        String expectedJson = "{\"class\":\"java.lang.Object\",\"@type\":\"Object\"}";
        String actualJson = mapper.writeValueAsString(mapper.valueToTree(new Object()));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode pojoNode = new POJONode(new Object());
        assertNull(pojoNode.binaryValue());
        byte[] byteArray = new byte[10];
        POJONode byteNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, byteNode.binaryValue());
    }
}