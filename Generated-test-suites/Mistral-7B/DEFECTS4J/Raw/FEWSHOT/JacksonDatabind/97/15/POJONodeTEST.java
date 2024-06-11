package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONode() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testGetPojo() {
        ObjectMapper mapper = JsonMapper.builder().build();
        POJONode node = mapper.valueToTree("{\"name\":\"John\", \"age\":30}");
        assertEquals("John", node.getPojo().getClass().getDeclaredField("name").get(node.getPojo()));
        assertEquals(30, node.getPojo().getClass().getDeclaredField("age").get(node.getPojo()));
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        Object pojo = new Object() {
            String name = "John Doe";
            int age = 30;
        };
        POJONode node = new POJONode(pojo);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider() {
            @Override
            public void defaultSerializeValue(JsonGenerator g, Object value) throws IOException {
                g.writeNull();
            }
        });
        assertEquals("{\"name\":\"John Doe\",\"age\":30}", mapper.writerWithRootValue(pojo).writeValueAsString());
    }

    @Test
    public void testEquals() {
        ObjectMapper mapper = JsonMapper.builder().build();
        POJONode node1 = mapper.valueToTree("{\"name\":\"John\", \"age\":30}");
        POJONode node2 = mapper.valueToTree("{\"name\":\"John\", \"age\":30}");
        POJONode node3 = mapper.valueToTree("{\"name\":\"Jane\", \"age\":30}");
        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        ObjectMapper mapper = JsonMapper.builder().build();
        POJONode node1 = mapper.valueToTree("{\"name\":\"John\", \"age\":30}");
        POJONode node2 = mapper.valueToTree("{\"name\":\"John\", \"age\":30}");
        POJONode node3 = mapper.valueToTree("{\"name\":\"Jane\", \"age\":30}");
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        assertEquals(hashCode1, hashCode2); // Should be equal since they represent the same POJO
        assertFalse(hashCode1 == hashCode3); // Should be different since they represent different POJOs
    }
}