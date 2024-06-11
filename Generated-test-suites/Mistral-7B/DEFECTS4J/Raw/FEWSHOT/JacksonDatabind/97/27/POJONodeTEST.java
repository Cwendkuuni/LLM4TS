package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class POJONodeTest {

    @Test
    public void testEquals() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree(new Object());
        POJONode node2 = mapper.valueToTree(new Object());
        POJONode node3 = new POJONode(null);

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node2.equals(node3));
    }

    @Test
    public void testGetPojo() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new Object());
        Object pojo = node.getPojo();
        assertEquals(pojo.getClass(), node._value.getClass());
    }

    @Test
    public void testHashCode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree(new Object());
        POJONode node2 = mapper.valueToTree(new Object());
        Object pojo1 = node1.getPojo();
        Object pojo2 = node2.getPojo();

        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), pojo1.hashCode());
        assertEquals(node2.hashCode(), node2.hashCode());
        assertEquals(node2.hashCode(), pojo2.hashCode());
        assertNotEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new Object());

        if (node._value instanceof byte[]) {
            String result = node.toString();
            String expected = String.format("(binary value of %d bytes)", ((byte[]) node._value).length);
            assertEquals(expected, result);
        } else if (node._value instanceof RawValue) {
            String result = node.toString();
            String expected = String.format("(raw value '%s')", ((RawValue) node._value).toString());
            assertEquals(expected, result);
        } else {
            String result = node.toString();
            String expected = String.valueOf(node._value);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree(new Object());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());

        assertEquals("{\"@class\":\"POJONode\",\"_value\":{\"@class\":\"Object\"}}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testSerializationWithJsonSerializableObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyJsonSerializable myObj = new MyJsonSerializable();
        POJONode node = mapper.valueToTree(myObj);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());

        assertEquals("{\"myField\":\"value\"}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(myObj));
    }

    static class MyJsonSerializable implements JsonSerializable {
        private String myField;

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
            gen.writeStringField("myField", "value");
        }
    }
}