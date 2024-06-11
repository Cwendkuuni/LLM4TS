package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testDefaultConstructor() {
        assertNull(new POJONode(null));
    }

    @Test
    public void testConstructor() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        assertNull(node.binaryValue());
        assertEquals("null", node.asText());
        assertSame(node.getPojo(), new Object());
        assertEquals(node, node);
        assertNotEquals(node, new Object());
        assertEquals(node.hashCode(), node.hashCode());
        assertEquals(node.toString(), "POJONode{value=null}");
    }

    @Test
    public void testGetter() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());
        assertEquals(node.getPojo(), new Object());
    }

    @Test
    public void testEquals() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("string");
        POJONode node4 = new POJONode(1);
        POJONode node5 = new POJONode(new byte[0]);
        ObjectNode objectNode = mapper.createObjectNode();
        ObjectNode objectNode2 = mapper.createObjectNode();

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(node4));
        assertFalse(node1.equals(node5));
        assertFalse(node1.equals(objectNode));
        assertFalse(node1.equals(objectNode2));
    }

    @Test
    public void testHashCode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        POJONode node3 = new POJONode("string");
        POJONode node4 = new POJONode(1);
        POJONode node5 = new POJONode(new byte[0]);
        ObjectNode objectNode = mapper.createObjectNode();
        ObjectNode objectNode2 = mapper.createObjectNode();

        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
        assertNotEquals(node1.hashCode(), node3.hashCode());
        assertNotEquals(node1.hashCode(), node4.hashCode());
        assertNotEquals(node1.hashCode(), node5.hashCode());
        assertNotEquals(node1.hashCode(), objectNode.hashCode());
        assertNotEquals(node1.hashCode(), objectNode2.hashCode());
    }

    @Test
    public void testToString() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());
        ObjectNode objectNode = mapper.createObjectNode();
        assertEquals("POJONode{value=null}", node.toString());
        assertEquals("{}", objectNode.toString());
    }

    @Test
    public void testNodeTypeGet() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testNodeTypeIsValueNode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());
        assertTrue(node.isValueNode());
    }

    @Test
    public void testAsText() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode("string");
        assertEquals("string", node.asText());
        POJONode node2 = new POJONode(new Object());
        assertEquals("null", node2.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode("string");
        assertEquals("string", node.asText());
        POJONode node2 = new POJONode(new Object());
        assertEquals("null", node2.asText());
        POJONode node3 = new POJONode(new StringBuffer("string"));
        assertEquals("string", node3.asText());
        POJONode node4 = new POJONode(null);
        assertEquals("null", node4.asText());
        POJONode node5 = new POJONode(new Object());
        String defaultValue = "default";
        assertEquals(defaultValue, node5.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        POJONode node2 = new POJONode(false);
        assertFalse(node2.asBoolean(true));
        POJONode node3 = new POJONode(null);
        assertFalse(node3.asBoolean(true));
        POJONode node4 = new POJONode("true");
        assertTrue(node4.asBoolean(false));
        POJONode node5 = new POJONode("false");
        assertFalse(node5.asBoolean(true));
        POJONode node6 = new POJONode("foo");
        assertFalse(node6.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
        POJONode node2 = new POJONode("1");
        assertEquals(1, node2.asInt(0));
        POJONode node3 = new POJONode("1.1");
        assertEquals(0, node3.asInt(0));
        POJONode node4 = new POJONode(null);
        assertEquals(0, node4.asInt(0));
    }

    @Test
    public void testAsLong() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));
        POJONode node2 = new POJONode("1");
        assertEquals(1L, node2.asLong(0L));
        POJONode node3 = new POJONode("1.1");
        assertEquals(0L, node3.asLong(0L));
        POJONode node4 = new POJONode(null);
        assertEquals(0L, node4.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(1.1);
        assertEquals(1.1, node.asDouble(0.0), 0.001);
        POJONode node2 = new POJONode("1.1");
        assertEquals(1.1, node2.asDouble(0.0), 0.001);
        POJONode node3 = new POJONode("1");
        assertEquals(1.0, node3.asDouble(0.0), 0.001);
        POJONode node4 = new POJONode("1.1E3");
        assertEquals(1100.0, node4.asDouble(0.0), 0.001);
        POJONode node5 = new POJONode(null);
        assertEquals(0.0, node5.asDouble(0.0), 0.001);
    }

    @Test
    public void testBinaryValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = new byte[] { 1, 2, 3 };
        POJONode node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
        POJONode node2 = new POJONode(new Object());
        assertNull(node2.binaryValue());
    }

    @Test
    public void testSerialize() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        POJONode node = new POJONode(new Object());
        node.serialize(gen, mapper.getSerializerProvider());
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputStream();
        assertEquals("null", new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

        Object obj = new Object();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        JsonSerializer<Object> serializer2 = mapper.findValueSerializer(Object.class, DefaultTyping.INSTANCE);
        StringBuilder stringBuilder = new StringBuilder();
        JsonGenerator gen2 = mapper.getFactory().createGenerator(stringBuilder, null);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        POJONode node2 = new POJONode(obj);

        node2.serialize(gen2, serializerProvider);
        gen2.flush();
        assertEquals("{\"@class\":\"java.lang.Object\"}", stringBuilder.toString());

        stringBuilder.setLength(0);
        node2.serialize(gen2, serializer);
        gen2.flush();
        assertEquals("null", stringBuilder.toString());

        String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"Seattle\"}";
        JsonNode jsonNode = mapper.readTree(jsonString);
        POJONode node3 = new POJONode(jsonNode);
        gen2.reset();
        node3.serialize(gen2, serializer2);
        gen2.flush();
        assertEquals(jsonString, stringBuilder.toString());
    }

    @Test
    public void testSerializeWithJsonSerializable() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"Seattle\"}";
        JsonNode jsonNode = mapper.readTree(jsonString);
        POJONode node = new POJONode(jsonNode);
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class, DefaultTyping.INSTANCE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createGenerator(outputStream, null);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        node.serialize(gen, serializerProvider);
        String resultJsonString = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(jsonString, resultJsonString);
    }
}