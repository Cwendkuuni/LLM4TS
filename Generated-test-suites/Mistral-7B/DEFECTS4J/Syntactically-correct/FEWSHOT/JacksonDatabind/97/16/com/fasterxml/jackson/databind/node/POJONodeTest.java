package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.node.POJONode;

import java.io.IOException;
import java.util.Arrays;

public class POJONodeTest {

    @Test
    public void testPOJONode_constructor() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertSame(obj, node.getPojo());
    }

    @Test
    public void testPOJONode_serialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode("test");
        String result = mapper.writeValueAsString(node);
        assertEquals("\"test\"", result);

        node = new POJONode(42);
        result = mapper.writeValueAsString(node);
        assertEquals("42", result);

        node = new POJONode(new int[0]);
        result = mapper.writeValueAsString(node);
        assertEquals("[]", result);

        node = new POJONode(new String[]{"foo", "bar"});
        result = mapper.writeValueAsString(node);
        assertEquals("[\"foo\", \"bar\"]", result);

        node = new POJONode(new Integer(42));
        result = mapper.writeValueAsString(node);
        assertEquals("42", result);

        node = new POJONode(new Long(42));
        result = mapper.writeValueAsString(node);
        assertEquals("42", result);

        node = new POJONode(new Double(3.14));
        result = mapper.writeValueAsString(node);
        assertEquals("3.14", result);

        node = new POJONode(new Boolean(true));
        result = mapper.writeValueAsString(node);
        assertEquals("true", result);

        node = new POJONode(new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("jsonSerializable");
            }
        });
        result = mapper.writeValueAsString(node);
        assertEquals("\"jsonSerializable\"", result);

        node = new POJONode(null);
        result = mapper.writeValueAsString(node);
        assertEquals("null", result);
    }

    @Test
    public void testPOJONode_equals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);
        assertFalse(node1.equals(node2));

        obj1 = obj2;
        node1 = new POJONode(obj1);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testPOJONode_hashCode() {
        Object obj = new Object();
        POJONode node1 = new POJONode(obj);
        POJONode node2 = new POJONode(obj);
        assertEquals(obj.hashCode(), node1.hashCode());
        assertEquals(obj.hashCode(), node2.hashCode());
    }

    @Test
    public void testPOJONode_toString() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        String result = node.toString();
        if (obj instanceof String) {
            assertEquals("\"{}\"", result);
        } else if (obj instanceof byte[]) {
            assertEquals("(binary value of 1 bytes)", result);
        } else if (obj instanceof RawValue) {
            assertEquals("(raw value '[])')", result);
        } else {
            assertEquals(obj.toString(), result);
        }
    }

    @Test
    public void testPOJONode_binaryValue() throws IOException {
        byte[] binaryData = new byte[]{1, 2, 3};
        POJONode node = new POJONode(binaryData);
        assertArrayEquals(binaryData, node.binaryValue());
    }

    @Test(expected = IOException.class)
    public void testPOJONode_binaryValue_nonBinary() throws IOException {
        POJONode node = new POJONode("test");
        node.binaryValue();
    }

    @Test
    public void testPOJONode_asText() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj.toString(), node.asText());
    }

    @Test
    public void testPOJONode_asText_defaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("defaultValue"));
    }

    @Test
    public void testPOJONode_asBoolean() {
        POJONode node = new POJONode(Boolean.TRUE);
        assertTrue(node.asBoolean(false));

        node = new POJONode(Boolean.FALSE);
        assertFalse(node.asBoolean(true));

        node = new POJONode(null);
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testPOJONode_asInt() {
        POJONode node = new POJONode(Integer.valueOf(42));
        assertEquals(42, node.asInt(0));

        node = new POJONode(Integer.valueOf(-42));
        assertEquals(-42, node.asInt(0));

        node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testPOJONode_asLong() {
        POJONode node = new POJONode(Long.valueOf(42));
        assertEquals(42L, node.asLong(0L));

        node = new POJONode(Long.valueOf(-42));
        assertEquals(-42L, node.asLong(0L));

        node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testPOJONode_asDouble() {
        POJONode node = new POJONode(Double.valueOf(3.14));
        assertEquals(3.14, node.asDouble(0.0), 0.001);

        node = new POJONode(Double.valueOf(Double.NaN));
        assertEquals(Double.NaN, node.asDouble(0.0), 0.001);

        node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.001);
    }
}