package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("Hello");
        assertEquals("Hello", node.asText());
        node = new POJONode(new Object());
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode("Hello");
        assertEquals("Hello", node.asText());
        node = new POJONode(null);
        assertEquals("null", node.asText());
        node = new POJONode(new Object());
        assertEquals("", node.asText("Default Value"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        node = new POJONode(false);
        assertFalse(node.asBoolean(true));
        node = new POJONode(new Boolean(false));
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
        node = new POJONode(false);
        assertFalse(node.asBoolean(true));
        node = new POJONode(null);
        assertFalse(node.asBoolean(true));
        node = new POJONode(new Boolean(false));
        assertFalse(node.asBoolean(true));
        node = new POJONode(new Boolean(true));
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
        node = new POJONode(Integer.valueOf(1));
        assertEquals(1, node.asInt(0));
        node = new POJONode(new Integer(2));
        assertEquals(2, node.asInt(1));
        node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
        node = new POJONode(Integer.valueOf(1));
        assertEquals(1, node.asInt(0));
        node = new POJONode(new Integer(2));
        assertEquals(2, node.asInt(1));
        node = new POJONode(null);
        assertEquals(0, node.asInt(0));
        node = new POJONode(new Integer(-1));
        assertEquals(-1, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(Long.valueOf(1L));
        assertEquals(1L, node.asLong(0L));
        node = new POJONode(new Long(2L));
        assertEquals(2L, node.asLong(1L));
        node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(Long.valueOf(1L));
        assertEquals(1L, node.asLong(0L));
        node = new POJONode(new Long(2L));
        assertEquals(2L, node.asLong(1L));
        node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
        node = new POJONode(new Long(-1L));
        assertEquals(-1L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0.0), 0.001);
        node = new POJONode(Double.valueOf(3.14));
        assertEquals(3.14, node.asDouble(0.0), 0.001);
        node = new POJONode(new Double(3.14));
        assertEquals(3.14, node.asDouble(0.0), 0.001);
        node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.001);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0.0), 0.001);
        node = new POJONode(Double.valueOf(3.14));
        assertEquals(3.14, node.asDouble(0.0), 0.001);
        node = new POJONode(new Double(3.14));
        assertEquals(3.14, node.asDouble(0.0), 0.001);
        node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.001);
        node = new POJONode(new Double(Double.NaN));
        assertEquals(Double.NaN, node.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<POJONode> serializer = mapper.findValueSerializer(POJONode.class, null);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        POJONode node = new POJONode(new Object());
        serializer.serialize(node, gen, new SerializerProvider());
        gen.flush();
        gen.close();
        assertEquals("null", node.asText());
        node = new POJONode(new Object());
        node._value = new Object() {
            public String toString() {
                return "TestObject";
            }
        };
        serializer.serialize(node, gen, new SerializerProvider());
        gen.flush();
        gen.close();
        String result = mapper.readValue(System.in, String.class);
        assertEquals("\"TestObject\"", result);
        node = new POJONode(new Object() {
            public int hashCode() {
                return 1;
            }
        });
        serializer.serialize(node, gen, new SerializerProvider());
        gen.flush();
        gen.close();
        result = mapper.readValue(System.in, String.class);
        assertEquals("{\"class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"pojo\":{\"hashCode\":1}}", result);
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[] { 1, 2, 3 });
        assertArrayEquals(new byte[] { 1, 2, 3 }, node.binaryValue());
        node = new POJONode(null);
        assertNull(node.binaryValue());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("Hello");
        POJONode node2 = new POJONode("Hello");
        POJONode node3 = new POJONode("World");
        POJONode node4 = new POJONode(null);
        POJONode node5 = new POJONode(node1);
        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node5));
        assertFalse(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(node4));
        assertFalse(node1.equals(null));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object() {
            public int hashCode() {
                return 1;
            }

            public String toString() {
                return "Obj1";
            }
        };
        POJONode node1 = new POJONode(obj1);
        Object obj2 = new Object() {
            public int hashCode() {
                return 2;
            }

            public String toString() {
                return "Obj2";
            }
        };
        POJONode node2 = new POJONode(obj2);
        assertEquals(obj1.hashCode(), node1.hashCode());
        assertEquals(obj2.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new Object());
        assertEquals("(null)", node.toString());
        node = new POJONode("Hello");
        assertEquals("(value of class \"java.lang.String\" with content \"Hello\")", node.toString());
        node = new POJONode(new Object() {
            public int hashCode() {
                return 1;
            }

            public String toString() {
                return "Obj1";
            }
        });
        assertEquals("(POJONode containing Obj1)", node.toString());
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<POJONode> deserializer = mapper.findValueDeserializer(POJONode.class, null);
        POJONode node;
        String jsonString = "{\"pojo\":{\"class\":\"java.lang.String\",\"value\":\"TestString\"}}";
        node = mapper.readValue(jsonString, POJONode.class);
        assertEquals("TestString", node.getPojo().toString());
        jsonString = "{\"pojo\":{\"class\":\"java.lang.Integer\",\"value\":42}}";
        node = mapper.readValue(jsonString, POJONode.class);
        assertEquals(42, node.getPojo().hashCode());
        jsonString = "{\"pojo\":{\"class\":\"java.util.Date\",\"value\":\"2023-03-15T14:30:00.000Z\"}}";
        node = mapper.readValue(jsonString, POJONode.class);
        assertTrue(node.getPojo() instanceof Date);
    }

    @Test
    public void testJsonDeserializationWithTypeHandling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithTypeHandling = mapper.copy();
        mapperWithTypeHandling.configure(MapperFeature.AUTO_DETECT_TYPES, true);
        JsonDeserializer<POJONode> deserializer = mapperWithTypeHandling.findValueDeserializer(POJONode.class, null);
        TypeDeserializer typeDeserializer = mapperWithTypeHandling.getTypeFactory()
                .constructType(POJONodeWithType.class);
        POJONode node;
        String jsonString = "{\"pojo\":{\"class\":\"java.lang.String\",\"value\":\"TestString\"}}";
        node = mapperWithTypeHandling.readValue(jsonString, POJONodeWithType.class);
        assertEquals("TestString", node.getPojo().toString());
        assertEquals(JsonNodeType.STRING, node.getNodeType());
        jsonString = "{\"pojo\":{\"class\":\"java.lang.Integer\",\"value\":42}}";
        node = mapperWithTypeHandling.readValue(jsonString, POJONodeWithType.class);
        assertEquals(42, node.getPojo().hashCode());
        assertEquals(JsonNodeType.NUMBER, node.getNodeType());
        jsonString = "{\"pojo\":{\"class\":\"java.util.Date\",\"value\":\"2023-03-15T14:30:00.000Z\"}}";
        node = mapperWithTypeHandling.readValue(jsonString, POJONodeWithType.class);
        assertTrue(node.getPojo() instanceof Date);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<POJONode> serializer = mapper.findValueSerializer(POJONode.class, null);
        JsonSerializer<POJONodeWithType> serializerWithType = mapper.findValueSerializer(POJONodeWithType.class, null);
        POJONode node;
        JsonNode expectedNode;
        node = new POJONode(new Object());
        expectedNode = mapper.valueToTree(null);
        assertEquals(expectedNode, serializer.serialize(node, null, null));
        node = new POJONode("Hello");
        expectedNode = mapper.valueToTree("\"Hello\"");
        assertEquals(expectedNode, serializer.serialize(node, null, null));
        node = new POJONode(new Object() {
            public int hashCode() {
                return 1;
            }

            public String toString() {
                return "Obj1";
            }
        });
        expectedNode = mapper.valueToTree("{\"pojo\":{\"class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"pojo\":{\"class\":\"java.lang.Object\",\"hashCode\":1}}");
        assertEquals(expectedNode, serializer.serialize(node, null, null));
        node = new POJONode(new Object() {
            public int hashCode() {
                return 1;
            }

            public String toString() {
                return "Obj1";
            }
        });
        expectedNode = mapper.valueToTree("{\"pojo\":{\"class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"pojo\":{\"class\":\"java.lang.Object\",\"hashCode\":1}}}");
        assertEquals(expectedNode, serializerWithType.serialize(node, null, null));
    }

    @Test
    public void testJsonSerializationWithTypeHandling() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<POJONode> serializer = mapper.findValueSerializer(POJONode.class, null);
        POJONode node = new POJONode(new Object());
        JsonNode expectedNode = mapper.valueToTree("null");
        assertEquals(expectedNode, serializer.serialize(node, null, null));
        TypeSerializer typeSerializer = mapper.getTypeFactory().constructType(POJONodeWithType.class);
        JsonNode expectedNodeWithType = mapper.valueToTree("{\"nodeType\":\"POJO\",\"pojoNode\":{\"nodeType\":\"Null\"}}");
        assertEquals(expectedNodeWithType, serializer.serialize(node, null, typeSerializer));
    }

    static class POJONodeWithType extends POJONode {
        public JsonNodeType nodeType;

        public POJONodeWithType(Object pojo) {
            super(pojo);
        }
    }
}