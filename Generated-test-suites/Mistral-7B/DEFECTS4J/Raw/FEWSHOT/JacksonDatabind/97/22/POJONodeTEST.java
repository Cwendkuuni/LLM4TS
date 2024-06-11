package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Test;

public class POJONodeTest {
    @Test
    public void testPOJONode_Empty() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertNull(node.asText());
        assertNull(node.asText(null));
        assertFalse(node.asBoolean(false));
        assertEquals(0, node.asInt(0));
        assertEquals(0L, node.asLong(0L));
        assertEquals(0.0, node.asDouble(0.0), 0.001);
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testPOJONode_Boolean() {
        POJONode node = new POJONode(true);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertTrue(node.asBoolean(false));
        assertEquals("true", node.asText());
        assertEquals("true", node.asText(null));
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            assertEquals("true", gen.getOutputStream().toString());
        } catch (JsonProcessingException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testPOJONode_String() {
        POJONode node = new POJONode("foo");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("foo", node.asText());
        assertEquals("foo", node.asText(null));
        assertFalse(node.asBoolean(false));
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            assertEquals("\"foo\"", gen.getOutputStream().toString());
        } catch (JsonProcessingException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testPOJONode_Number() {
        POJONode node = new POJONode(123);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("123", node.asText());
        assertEquals("123", node.asText(null));
        assertEquals(123, node.asInt(0));
        assertEquals(123L, node.asLong(0L));
        assertEquals(123.0, node.asDouble(0.0), 0.001);
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            assertEquals("123", gen.getOutputStream().toString());
        } catch (JsonProcessingException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testPOJONode_Number_Long() {
        POJONode node = new POJONode(Long.MAX_VALUE);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("9223372036854775807", node.asText());
        assertEquals("9223372036854775807", node.asText(null));
        assertEquals(Long.MAX_VALUE, node.asLong(0L));
        assertEquals(Long.MAX_VALUE, node.asInt((int) Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, node.asDouble(0.0), 0.001);
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            assertEquals("\"9223372036854775807\"", gen.getOutputStream().toString());
        } catch (JsonProcessingException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testPOJONode_Number_Double() {
        POJONode node = new POJONode(Double.POSITIVE_INFINITY);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("null", node.asText());
        assertNull(node.asText(null));
        assertFalse(node.asBoolean(false));
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }

        node = new POJONode(Double.NEGATIVE_INFINITY);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("null", node.asText());
        assertNull(node.asText(null));
        assertFalse(node.asBoolean(false));
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper2 = new ObjectMapper();
        ObjectWriter writer = mapper2.writer();
        writer.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ToStringSerializer serializer = new ToStringSerializer(String.class);
        serializer.setInclusivity(JsonInclude.Include.NON_NULL);
        writer.setSerializerProvider(new SerializerProvider() {
            @Override
            public void findAndRegisterSerializers(SerializerProvider provider) {}

            @Override
            public JsonSerializer<Object> findValueSerializer(Class<? extends Object> valueType, JsonSerializer<?> valueSerializer) {
                return serializer;
            }
        });

        try {
            String json = writer.writeValueAsString(new POJONode(Double.NEGATIVE_INFINITY));
            assertEquals("-Infinity", json);
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPOJONode_Object() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertSame(obj, node.getPojo());
        assertEquals("{\"class\":\"java.lang.Object\",\"@types\":[]}", node.asText());
        assertEquals("{\"class\":\"java.lang.Object\",\"@types\":[]}", node.asText(null));
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            // TODO: assertJsonStringEquals("{\"class\":\"java.lang.Object\",\"@types\":[]}", gen.getOutputStream().toString());
        } catch (JsonProcessingException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testPOJONode_SerializableObject() {
        MySerializableObject obj = new MySerializableObject();
        POJONode node = new POJONode(obj);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertSame(obj, node.getPojo());
        assertEquals("{\"class\":\"com.example.MySerializableObject\",\"@types\":[]}", node.asText());
        assertEquals("{\"class\":\"com.example.MySerializableObject\",\"@types\":[]}", node.asText(null));
        assertArrayEquals(new byte[0], node.binaryValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        try {
            gen = mapper.getFactory().createGenerator(System.out, null);
            node.serialize(gen, null);
            // TODO: assertJsonStringEquals("{\"class\":\"com.example.MySerializableObject\",\"@types\":[]}", gen.getOutputStream().toString());
        } catch (JsonProcessingException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
}

class MySerializableObject implements JsonSerializable {
    public int value;

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value);
    }
}