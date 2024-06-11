package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingException() throws Exception {
        // Test null input
        String json = "{}";
        try {
            mapper.readValue(json, Foo.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
            Reference reference = e.getPath().get(0);
            assertEquals("root", reference.getFieldName());
            assertEquals(Foo.class, reference.getFrom().getClass());
        }

        // Test empty input
        json = "";
        try {
            mapper.readValue(json, Foo.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
            assertEquals(JsonMappingException.RootValueException.class, e.getClass());
            assertNull(e.getPath());
        }

        // Test invalid JSON input
        json = "{\"bar\":1,\"baz\":[\"foo\"]}";
        try {
            mapper.readValue(json, Bar.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
            Reference reference = e.getPath().get(0);
            assertEquals("bar", reference.getFieldName());
            assertEquals(JsonNode.class, reference.getFrom().getClass());
        }

        // Test invalid type input
        json = "{\"foo\":1}";
        try {
            mapper.readValue(json, Bar.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
            assertEquals(JsonMappingException.TypeMismatchException.class, e.getClass());
            Reference reference = e.getPath().get(0);
            assertEquals("foo", reference.getFieldName());
            assertEquals(Integer.class, reference.getFrom().getClass());
        }

        // Test custom JsonSerializer
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonSerializer<Foo> fooSerializer = new FooSerializer();
        mapper.registerSerializer(Foo.class, fooSerializer);
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", null);
        try {
            mapper.writeValueAsString(node);
            fail("Expected JsonMappingException");
        } catch (JsonProcessingException e) {
            JsonMappingException jsonMappingException = JsonMappingException.ClassUtil.convertProcessingException(e);
            assertEquals(JsonMappingException.ValueInstantiationException.class, jsonMappingException.getCause().getClass());
            Reference reference = jsonMappingException.getPath().get(0);
            assertEquals("foo", reference.getFieldName());
            assertEquals(ObjectNode.class, reference.getFrom().getClass());
        }
    }

    private static class Foo {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    private static class Bar {
        private List<Foo> foos;

        public List<Foo> getFoos() {
            return foos;
        }

        public void setFoos(List<Foo> foos) {
            this.foos = foos;
        }
    }

    private static class FooSerializer extends JsonSerializer<Foo> {
        @Override
        public void serialize(Foo foo, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
            throw new RuntimeException();
        }
    }
}