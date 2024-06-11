package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        assertArrayEquals(new byte[0], node.binaryValue());
    }

    @Test(expected = IOException.class)
    public void testBinaryValueNonByteArray() throws IOException {
        POJONode node = new POJONode(new Object());
        node.binaryValue();
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("string");
        assertEquals("string", node.asText());
        node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        POJONode node = new POJONode("string");
        assertEquals("string", node.asText());
        node = new POJONode(null);
        assertEquals("defaultValue", node.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        node = new POJONode(false);
        assertFalse(node.asBoolean(true));
        node = new POJONode(null);
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
        node = new POJONode(2.5);
        assertEquals(0, node.asInt(0));
        node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));
        node = new POJONode(2.5);
        assertEquals(0L, node.asLong(0L));
        node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.5);
        assertEquals(1.5, node.asDouble(0), 0.001);
        node = new POJONode(new Object());
        assertEquals(0, node.asDouble(0), 0.001);
        node = new POJONode(null);
        assertEquals(0, node.asDouble(0), 0.001);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
                return null;
            }
        });
        gen.flush();
        assertEquals("null", mapper.readValue(System.in, String.class));

        node = new POJONode(new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("serialized");
            }
        });
        gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
                return null;
            }
        });
        gen.flush();
        assertEquals("serialized", mapper.readValue(System.in, String.class));

        node = new POJONode(new Object());
        JsonGenerator gen2 = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen2, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
                return mapper.findValueSerializer(type, null);
            }
        });
        gen2.flush();
        assertEquals("null", mapper.readValue(System.in, String.class));
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node._value, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        assertTrue(node1.equals(node2));

        Object obj = new Object();
        POJONode node3 = new POJONode(obj);
        POJONode node4 = new POJONode(obj);
        assertTrue(node3.equals(node4));
        assertFalse(node3.equals(node1));
        assertFalse(node3.equals(null));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node1 = new POJONode(obj);
        POJONode node2 = new POJONode(obj);
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode("string");
        assertEquals("(value of class [Ljava.lang.String;@1b6d7a82)", node1.toString());

        POJONode node2 = new POJONode(new Object());
        assertEquals("(value of class java.lang.Object@6d613de6)", node2.toString());

        POJONode node3 = new POJONode(null);
        assertEquals("(null)", node3.toString());

        POJONode node4 = new POJONode(new byte[0]);
        assertEquals("(binary value of 0 bytes)", node4.toString());

        POJONode node5 = new POJONode(new RawValue("raw value"));
        assertEquals("(raw value 'raw value')", node5.toString());
    }
}