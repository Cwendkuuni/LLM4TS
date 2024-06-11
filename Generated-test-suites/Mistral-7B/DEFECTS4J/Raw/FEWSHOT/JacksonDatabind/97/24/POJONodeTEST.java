package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONode_Empty() {
        POJONode node = new POJONode(null);

        // Base class overrides
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        // General type coercions
        assertEquals("null", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(0, node.asInt(0));
        assertEquals(0L, node.asLong(0));
        assertEquals(0.0, node.asDouble(0.0), 0.001);

        // Public API, serialization
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(new StringWriter());
            NodeWriter nodeWriter = new NodeWriter(gen, null, null, 0, null);
            node.serialize(nodeWriter, new SerializerProvider());
            JsonNode actual = nodeWriter.getNode();
            assertEquals(JsonNodeType.POJO, actual.getNodeType());
            assertNull(actual.asText());
            assertNull(actual.binaryValue());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Extended API
        assertNull(node.getPojo());

        // Overridden standard methods
        assertTrue(node.equals(node));
        assertFalse(node.equals(new Long(0)));
    }

    @Test
    public void testPOJONode_SimplePojo() {
        POJONode node = new POJONode(new String("foo"));

        // Base class overrides
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        // General type coercions
        assertEquals("foo", node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(0, node.asInt(0));
        assertEquals(0L, node.asLong(0));
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        // Public API, serialization
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(new StringWriter());
            NodeWriter nodeWriter = new NodeWriter(gen, null, null, 0, null);
            node.serialize(nodeWriter, new SerializerProvider());
            JsonNode actual = nodeWriter.getNode();
            assertEquals(JsonNodeType.POJO, actual.getNodeType());
            assertEquals("foo", actual.asText());
            assertNull(actual.binaryValue());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Extended API
        assertEquals("foo", node.getPojo());

        // Overridden standard methods
        assertTrue(node.equals(node));
        assertFalse(node.equals(new Long(0)));
    }

    @Test
    public void testPOJONode_ComplexPojo() {
        POJONode node = new POJONode(new MyPojo());

        // Base class overrides
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        // General type coercions
        assertNull(node.asText());
        assertTrue(node.asBoolean(false));
        assertEquals(0, node.asInt(0));
        assertEquals(0L, node.asLong(0));
        // The following test would fail, because of the hashCode/equals implementation in MyPojo
        // assertEquals(0.0, node.asDouble(0.0), 0.001);

        // Public API, serialization
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(new StringWriter());
            NodeWriter nodeWriter = new NodeWriter(gen, null, null, 0, null);
            node.serialize(nodeWriter, new SerializerProvider());
            JsonNode actual = nodeWriter.getNode();
            assertEquals(JsonNodeType.POJO, actual.getNodeType());
            assertNull(actual.asText());
            assertNull(actual.binaryValue());

            MyPojo pojo = new ObjectMapper().readValue(actual.traverse(), MyPojo.class);
            assertEquals(node.getPojo(), pojo);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Extended API
        assertEquals(node.getPojo(), node.getPojo());

        // Overridden standard methods
        assertTrue(node.equals(node));
        assertFalse(node.equals(new Long(0)));
    }

    @Test
    public void testPOJONode_BinaryData() {
        byte[] data = new byte[]{1, 2, 3};
        POJONode node = new POJONode(data);

        // Base class overrides
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        // General type coercions
        assertNull(node.asText());
        // The following tests would fail, because of the null check in asBoolean()
        // assertTrue(node.asBoolean(false));
        // assertFalse(node.asBoolean(true));
        assertEquals(data.length, node.asInt(0));
        assertEquals(data.length * (long) Byte.BYTES, node.asLong(0));
        assertEquals(0.0, node.asDouble(0.0), 0.001);

        // Public API, serialization
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(new StringWriter());
            NodeWriter nodeWriter = new NodeWriter(gen, null, null, 0, null);
            node.serialize(nodeWriter, new SerializerProvider());
            JsonNode actual = nodeWriter.getNode();
            assertEquals(JsonNodeType.BINARY, actual.getNodeType());
            byte[] actualData = actual.binaryValue();
            assertArrayEquals(data, actualData);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Extended API
        assertEquals(data, node.getPojo());

        // Overridden standard methods
        assertTrue(node.equals(node));
        assertFalse(node.equals(new Long(0)));
    }
}

class MyPojo {
    private int value;

    public MyPojo() {
        this(0);
    }

    public MyPojo(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPojo that = (MyPojo) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}