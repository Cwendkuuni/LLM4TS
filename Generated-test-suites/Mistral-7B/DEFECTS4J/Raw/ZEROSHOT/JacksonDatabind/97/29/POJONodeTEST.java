package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

    @Test
    public void testGetNodeType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new byte[0]);
        assertNull(node.binaryValue());

        node = mapper.valueToTree(new MyPojo());
        assertNull(node.binaryValue());

        node = mapper.valueToTree(new byte[5]);
        assertArrayEquals(new byte[5], node.binaryValue());
    }

    @Test
    public void testAsText() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo());
        assertEquals("null", node.asText());

        node = mapper.valueToTree("hello");
        assertEquals("hello", node.asText());

        node = mapper.valueToTree(new MyPojo("hello"));
        assertEquals("hello", node.asText());

        node = mapper.valueToTree(new MyPojo(123));
        assertEquals("123", node.asText());

        node = mapper.valueToTree(new MyPojo(123, "hello"));
        assertEquals("123", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo());
        assertEquals("null", node.asText("defaultValue"));

        node = mapper.valueToTree("hello");
        assertEquals("hello", node.asText("defaultValue"));

        node = mapper.valueToTree(new MyPojo("hello"));
        assertEquals("hello", node.asText("defaultValue"));

        node = mapper.valueToTree(new MyPojo(123));
        assertEquals("123", node.asText("defaultValue"));

        node = mapper.valueToTree(new MyPojo(123, "hello"));
        assertEquals("123", node.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo(true));
        assertTrue(node.asBoolean(false));

        node = mapper.valueToTree(new MyPojo(false));
        assertFalse(node.asBoolean(true));

        node = mapper.valueToTree(new MyPojo());
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo(1));
        assertEquals(1, node.asInt(0));

        node = mapper.valueToTree(new MyPojo(-1));
        assertEquals(-1, node.asInt(0));

        node = mapper.valueToTree(new MyPojo());
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo(1L));
        assertEquals(1L, node.asLong(0L));

        node = mapper.valueToTree(new MyPojo(-1L));
        assertEquals(-1L, node.asLong(0L));

        node = mapper.valueToTree(new MyPojo());
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new MyPojo(1.0));
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        node = mapper.valueToTree(new MyPojo(-1.0));
        assertEquals(-1.0, node.asDouble(0.0), 0.001);

        node = mapper.valueToTree(new MyPojo());
        assertEquals(0.0, node.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MyPojo());
        POJONode node = mapper.readValue(json, POJONode.class);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out, new JsonFactory());
        node.serialize(gen, mapper.getSerializerProvider());
        assertEquals(json, gen.getOutputContent().toString());
    }

    @Test
    public void testGetPojo() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyPojo pojo = new MyPojo("hello");
        POJONode node = mapper.valueToTree(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyPojo pojo1 = new MyPojo("hello");
        MyPojo pojo2 = new MyPojo("hello");
        POJONode node1 = mapper.valueToTree(pojo1);
        POJONode node2 = mapper.valueToTree(pojo2);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyPojo pojo = new MyPojo("hello");
        POJONode node = mapper.valueToTree(pojo);
        int hashCode1 = node.hashCode();
        POJONode node2 = mapper.valueToTree(pojo);
        int hashCode2 = node2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new byte[0]);
        assertEquals("(binary value of 0 bytes)", node.toString());

        node = mapper.valueToTree(new MyPojo());
        assertEquals("null", node.toString());

        node = mapper.valueToTree(new MyPojo("hello"));
        assertEquals("(raw value 'hello')", node.toString());

        node = mapper.valueToTree(new MyPojo(123));
        assertEquals("123", node.toString());

        node = mapper.valueToTree(new MyPojo(123, "hello"));
        assertEquals("123", node.toString());
    }

    private static class MyPojo implements JsonSerializable {
        private final String str;
        private final int intVal;

        public MyPojo() {
            this(null, 0);
        }

        public MyPojo(String str) {
            this(str, 0);
        }

        public MyPojo(int intVal) {
            this(null, intVal);
        }

        public MyPojo(String str, int intVal) {
            this.str = str;
            this.intVal = intVal;
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (str != null) {
                gen.writeString(str);
            } else {
                gen.writeNumber(intVal);
            }
        }
    }
}