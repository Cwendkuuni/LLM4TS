package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testPOJONodeWithNullValue() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertNull(node.getPojo());
        assertEquals("null", node.asText());
        assertFalse(node.equals(new POJONode(new Object())));
        assertEquals(0, node.hashCode());
        assertTrue(node.toString().startsWith("(null)"));
        try {
            node.serialize(new JsonGenerator(new StringBuilder()), new SerializerProvider());
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPOJONodeWithBooleanValue() {
        POJONode node = new POJONode(true);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(true, node.getPojo());
        assertEquals("true", node.asText());
        assertEquals(true, node.asBoolean(false));
        assertSame(node, node);
        assertNotEquals(node, new POJONode(false));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().equals("true"));
    }

    @Test
    public void testPOJONodeWithNumberValue() {
        POJONode node = new POJONode(42);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(42, node.getPojo());
        assertEquals("42", node.asText());
        assertEquals(42, node.asInt(0));
        assertEquals(42L, node.asLong(0L));
        assertEquals(42.0, node.asDouble(0.0), 0.0);
        assertSame(node, node);
        assertNotEquals(node, new POJONode(43));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().equals("42"));
    }

    @Test
    public void testPOJONodeWithLongValue() {
        POJONode node = new POJONode(Long.MAX_VALUE);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(Long.MAX_VALUE, node.getPojo());
        assertEquals("9223372036854775807", node.asText());
        assertEquals(Long.MAX_VALUE, node.asLong(0L));
        assertSame(node, node);
        assertNotEquals(node, new POJONode(Long.MIN_VALUE));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().equals("9223372036854775807"));
    }

    @Test
    public void testPOJONodeWithDoubleValue() {
        POJONode node = new POJONode(Math.PI);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(Math.PI, node.getPojo(), 0.0);
        assertEquals("3.141592653589793", node.asText());
        assertEquals(Math.PI, node.asDouble(0.0), 0.0);
        assertSame(node, node);
        assertNotEquals(node, new POJONode(1.0));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().startsWith("3.141592653589793"));
    }

    @Test
    public void testPOJONodeWithStringValue() {
        POJONode node = new POJONode("Hello, World!");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("Hello, World!", node.getPojo());
        assertEquals("\"Hello, World!\"", node.asText());
        assertEquals("Hello, World!", node.asText(null));
        assertSame(node, node);
        assertNotEquals(node, new TextNode("Hello"));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().equals("{\"value\":\"Hello, World!\",\"nodeType\":\"POJO\",\"className\":\"com.fasterxml.jackson.databind.node.POJONode\",\"path\":\"\",\"parent\":null}"));
    }

    @Test
    public void testPOJONodeWithObjectValue() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Test Object";
            }
        };
        POJONode node = new POJONode(obj);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(obj, node.getPojo());
        assertEquals("Test Object", node.asText());
        assertSame(node.getPojo(), obj);
        assertSame(node, node);
        assertNotEquals(node, new POJONode(new Object()));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().equals("{\"value\":{\"className\":\"java.lang.Object\",\"nodeType\":\"POJO\",\"parent\":null,\"path\":\"\",\"toString\":\"Test Object\"},\"nodeType\":\"POJO\",\"className\":\"com.fasterxml.jackson.databind.node.POJONode\",\"path\":\"\",\"parent\":null}"));
    }

    @Test
    public void testPOJONodeWithJsonSerializableValue() throws JsonMappingException {
        final class TestSerializable implements JsonSerializable {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
                gen.writeNumber(42);
            }
        }
        POJONode node = new POJONode(new TestSerializable());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(new TestSerializable(), node.getPojo());
        assertEquals("42", node.asText());
        try {
            node.serialize(new JsonGenerator(new StringBuilder()), new SerializerProvider());
        } catch (IOException e) {
            // expected
        }
        assertSame(node.getPojo(), new TestSerializable());
        assertSame(node, node);
        assertNotEquals(node, new POJONode(new Object()));
        assertEquals(1, node.hashCode());
        assertTrue(node.toString().equals("{\"value\":{\"nodeType\":\"POJO\",\"className\":\"com.fasterxml.jackson.databind.node.POJONode\",\"parent\":null,\"path\":\"\",\"serialize\":[class com.fasterxml.jackson.databind.node.POJONode$$POJONodeSerializer]},\"nodeType\":\"POJO\",\"className\":\"com.fasterxml.jackson.databind.node.POJONode\",\"path\":\"\",\"parent\":null}"));
    }

    @Test
    public void testPOJONodeWithByteArrayValue() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        POJONode node = new POJONode(data);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(data, node.getPojo());
        assertEquals("(binary value of 5 bytes)", node.asText());
        assertFalse(node.equals(new POJONode(new Object())));
        assertEquals(data.length, node.hashCode());
        assertTrue(node.toString().startsWith("(binary value of 5 bytes)"));
        try {
            node.serialize(new JsonGenerator(new StringBuilder()), new SerializerProvider());
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPOJONodeWithRawValue() throws IOException {
        RawValue value = new RawValue("foo");
        POJONode node = new POJONode(value);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(value, node.getPojo());
        assertEquals("(raw value 'foo')", node.asText());
        assertFalse(node.equals(new POJONode(new Object())));
        assertEquals(node.hashCode(), value.hashCode());
        assertTrue(node.toString().startsWith("(raw value 'foo')"));
        try {
            node.serialize(new JsonGenerator(new StringBuilder()), new SerializerProvider());
        } catch (IOException e) {
            // expected
        }
    }
}