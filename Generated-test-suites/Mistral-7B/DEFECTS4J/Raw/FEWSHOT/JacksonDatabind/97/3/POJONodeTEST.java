package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testEquals() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree("{ \"name\": \"Alice\" }");
        POJONode node2 = mapper.valueToTree("{ \"name\": \"Alice\" }");
        POJONode node3 = mapper.valueToTree("{ \"age\": 30 }");

        assertEquals(node1, node1);
        assertEquals(node1, node2);
        assertNotEquals(node1, node3);
    }

    @Test
    public void testGetPojo() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree("{ \"name\": \"Alice\" }");
        Object pojo = node.getPojo();
        assertEquals("Alice", pojo.getClass().getMethod("getName").invoke(pojo));
    }

    @Test
    public void testHashCode() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree("{ \"name\": \"Alice\" }");
        POJONode node2 = mapper.valueToTree("{ \"name\": \"Alice\" }");
        POJONode node3 = mapper.valueToTree("{ \"age\": 30 }");

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertEquals(hashCode1, node1.hashCode());
    }

    @Test
    public void testToString() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree("{ \"name\": \"Alice\" }");
        String toString = node.toString();
        assertTrue(toString.contains("name"));
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = mapper.valueToTree("{ \"name\": \"Alice\" }");

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());
        gen.flush();

        String expected = "{\"name\":\"Alice\"}";
        String actual = System.out.getBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithJsonSerializable() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        class TestClass implements JsonSerializable {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(getName());
            }
        }

        POJONode node = mapper.valueToTree(new TestClass());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());
        gen.flush();

        String expected = "\"Alice\"";
        String actual = System.out.getBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithRawValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        POJONode node = mapper.valueToTree(new RawValue(TextNode.valueOf("Hello World")));

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());
        gen.flush();

        String expected = "\"Hello World\"";
        String actual = System.out.getBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithBinaryValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        byte[] binaryData = new byte[] {0x01, 0x02, 0x03};
        POJONode node = mapper.valueToTree(new POJONode(binaryData));

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());
        gen.flush();

        String expected = "\"binary value of 3 bytes\"";
        String actual = System.out.getBuffer().toString();
        assertEquals(expected, actual);
    }
}