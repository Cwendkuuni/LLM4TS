package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.TestBase;

public class POJONodeTest extends TestBase {

    private ObjectMapper mapper;
    private SerializerProvider provider;
    private JsonSerializer<Object> serializer;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        provider = mapper.getSerializerProvider();
        serializer = mapper.getDefaultSerializerProvider().findValueSerializer(Object.class, null);
    }

    @Test
    public void testNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.asText());
        node = new POJONode(new Object());
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode("hello");
        assertEquals("hello", node.asText("default"));
        node = new POJONode(new Object());
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        node = new POJONode('a');
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(123);
        assertEquals(123, node.asInt(0));
        node = new POJONode(1.23F);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(123L);
        assertEquals(123L, node.asLong(0L));
        node = new POJONode(1.23F);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.23);
        assertEquals(1.23, node.asDouble(0), 0.001);
        node = new POJONode(123);
        assertEquals(123.0, node.asDouble(0), 0.001);
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[] { 0x01, 0x02 });
        assertArrayEquals(new byte[] { 0x01, 0x02 }, node.binaryValue());
        node = new POJONode("hello");
        assertNull(node.binaryValue());
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        JsonGenerator gen = createMockGenerator();

        node.serialize(gen, provider);
        verify(gen, atLeastOnce()).writeStartObject();
        verify(gen, atLeastOnce()).writeFieldName("value");
        verify(gen, atLeastOnce()).writeObject(obj);
        verify(gen, atLeastOnce()).writeEndObject();
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node._value, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);
        POJONode node3 = new POJONode(obj1);

        assertEquals(node1, node1);
        assertEquals(node1, node3);
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("hello");
        assertEquals("POJONode{[value=hello]}", node.toString());
        node = new POJONode(new Object());
        assertEquals("POJONode{[value=null]}", node.toString());
    }

    @Test
    public void testSerialization() throws IOException {
        Object obj = new Object();
        POJONode node = new POJONode(obj);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(node);
        os.close();

        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        POJONode node2 = (POJONode) is.readObject();
        is.close();

        assertEquals(node._value, node2._value);
    }

    @Test
    public void testSerializationWithCustomSerializer() throws IOException {
        Object obj = new Object();
        POJONode node = new POJONode(obj);

        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.serializeValues(anyBoolean(), any(SerializerProvider.class), any(Object.class))).thenReturn(new TextNode("value"));
        POJONode nodeWithCustomSerializer = new POJONode(obj, serializer);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(nodeWithCustomSerializer);
        os.close();

        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        POJONode node2 = (POJONode) is.readObject();
        is.close();

        TextNode expectedTextNode = new TextNode("value");
        assertEquals(expectedTextNode, node2);
    }
}