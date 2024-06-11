package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

/**
 * Test file for POJONode class
 */
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
        POJONode node = new POJONode("testString".getBytes());
        byte[] result = node.binaryValue();
        assertArrayEquals("testString".getBytes(), result);
    }

    @Test
    public void testAsText() {
        POJONode node1 = new POJONode(new Object());
        assertEquals("null", node1.asText());

        POJONode node2 = new POJONode("Test String");
        assertEquals("Test String", node2.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("Default Value"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node1 = new POJONode(false);
        assertFalse(node1.asBoolean(true));

        POJONode node2 = new POJONode(true);
        assertTrue(node2.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node1 = new POJONode(1);
        assertEquals(1, node1.asInt(0));

        POJONode node2 = new POJONode(new Integer(2));
        assertEquals(2, node2.asInt(3));
    }

    @Test
    public void testAsLong() {
        POJONode node1 = new POJONode(1L);
        assertEquals(1, node1.asLong(0L));

        POJONode node2 = new POJONode(new Long(2L));
        assertEquals(2, node2.asLong(3L));
    }

    @Test
    public void testAsDouble() {
        POJONode node1 = new POJONode(1.0);
        assertEquals(1.0, node1.asDouble(0.0), 0.001);

        POJONode node2 = new POJONode(new Double(2.0));
        assertEquals(2.0, node2.asDouble(3.0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode("Test String");
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        node.serialize(gen, null);
        assertEquals("\"Test String\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        Object pojo = node.getPojo();
        assertNotNull(pojo);
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());

        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());

        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode("Test String");
        POJONode node3 = new POJONode(new byte[10]);

        assertEquals("(null)", node1.toString());
        assertEquals("(POJONode)", node2.toString());
        assertEquals("(binary value of 10 bytes)", node3.toString());
    }

    @Test
    public void testSerializeWithJsonSerializable() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = new MyClass();
        obj.setId(1);
        obj.setName("Test");

        POJONode node = new POJONode(obj);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        node.serialize(gen, null);
        assertEquals("{\"id\":1,\"name\":\"Test\"}", gen.getOutputBuffer().toString());
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "{\"id\":1,\"name\":\"Test\"}";
        POJONode node = mapper.readValue(jsonString, POJONode.class);

        MyClass deserializedObject = node.getPojo();
        assertEquals(1, deserializedObject.getId());
        assertEquals("Test", deserializedObject.getName());
    }

    static class MyClass implements JsonSerializable {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
            TypeDeserializer typeDeserializer = ctxt.findValueTypeDeserializer(this.getClass(), null);
            TypeFactory typeFactory = ctxt.findValueTypeFactory();
            typeDeserializer.deserializeTypedObject(gen, typeFactory.constructType(this.getClass()), this, ctxt);
        }
    }
}