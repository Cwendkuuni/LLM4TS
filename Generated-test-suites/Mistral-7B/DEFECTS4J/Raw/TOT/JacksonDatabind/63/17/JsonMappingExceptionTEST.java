package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testSerializationWithPath() throws JsonProcessingException {
        ObjectNode node = ObjectMapper.createObjectMapper().createObjectNode();
        node.put("obj", new Object() {
            int x;
            int y;

            @JsonProperty("z")
            public int z() {
                return x + y;
            }
        });

        try {
            ObjectMapper mapper = ObjectMapper.createObjectMapper();
            String json = mapper.writerWithRootValue(null).writeValueAsString(node);
            mapper.readValue(json, Object.class);
            fail("Expected JsonMappingException during serialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: Object reference not set to an instance of a valid type", e.getMessage());
            Assert.assertEquals("(through reference chain: \"[\" \"obj\" \"$[\" \"]\"]\")", e.getPathReference());
            Assert.assertNull(e.getProcessor());
        }
    }

    @Test
    public void testSerializationWithPathAndProcessor() throws JsonProcessingException {
        ObjectNode node = ObjectMapper.createObjectMapper().createObjectNode();
        node.put("obj", new Object() {
            int x;
            int y;

            @JsonProperty("z")
            public int z() {
                return x + y;
            }
        });

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(node.toString());

        try {
            ObjectMapper mapper = ObjectMapper.createObjectMapper();
            mapper.readTree(parser);
            fail("Expected JsonMappingException during serialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: Object reference not set to an instance of a valid type", e.getMessage());
            Assert.assertEquals("(through reference chain: \"[\" \"obj\" \"$[\" \"]\"]\")", e.getPathReference());
            Assert.assertEquals(parser.getClass(), e.getProcessor());
        }
    }

    @Test
    public void testDeserializationWithPath() throws IOException {
        ObjectMapper mapper = ObjectMapper.createObjectMapper();
        String json = "{\"obj\":{\"x\":1,\"y\":2,\"z\":3}}";

        try {
            ObjectNode node = mapper.readTree(json);
            fail("Expected JsonMappingException during deserialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: Object reference not set to an instance of a valid type", e.getMessage());
            Assert.assertEquals("(through reference chain: \"obj\")", e.getPathReference());
            Assert.assertNull(e.getProcessor());
        }
    }

    @Test
    public void testDeserializationWithPathAndProcessor() throws IOException {
        ObjectMapper mapper = ObjectMapper.createObjectMapper();
        String json = "{\"obj\":{\"x\":1,\"y\":2,\"z\":3}}";

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        try {
            ObjectMapper m = ObjectMapper.createObjectMapper();
            m.readerForUpdating(parser);
            fail("Expected JsonMappingException during deserialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: Object reference not set to an instance of a valid type", e.getMessage());
            Assert.assertEquals("(through reference chain: \"obj\")", e.getPathReference());
            Assert.assertEquals(parser.getClass(), e.getProcessor());
        }
    }

    @Test
    public void testSerializationWithMaxReferencesReached() throws JsonProcessingException {
        ObjectNode node = ObjectMapper.createObjectMapper().createObjectNode();
        for (int i = 0; i < 1001; i++) {
            node.put("obj" + i, new Object());
        }

        try {
            ObjectMapper mapper = ObjectMapper.createObjectMapper();
            String json = mapper.writerWithRootValue(null).writeValueAsString(node);
            mapper.readValue(json, Object.class);
            fail("Expected JsonMappingException during serialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: Failed to serialize value of type java.lang.Object to JSON: com.fasterxml.jackson.databind.JsonMappingException: Failed to serialize value of type java.lang.Object to JSON: com.fasterxml.jackson.databind.JsonMappingException: Failed to serialize value of type java.lang.Object to JSON: ... (through reference chain: \"[\" \"obj1000\" \"$[\" \"]\"]\")", e.getMessage());
            Assert.assertNull(e.getProcessor());
        }
    }

    @Test
    public void testDeserializationWithMaxReferencesReached() throws IOException {
        ObjectMapper mapper = ObjectMapper.createObjectMapper();
        String json = "{\"obj0\":{}}";
        for (int i = 1; i < 1001; i++) {
            json += ",\"obj" + i + "\":{}}";
        }

        try {
            ObjectMapper m = ObjectMapper.createObjectMapper();
            m.readValue(new File("JsonMappingExceptionTest.java"), Object.class);
            fail("Expected JsonMappingException during deserialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: Failed to deserialize value of type java.io.File from JSON: com.fasterxml.jackson.databind.JsonMappingException: Failed to deserialize value of type java.lang.Object from JSON: com.fasterxml.jackson.databind.JsonMappingException: Failed to deserialize value of type java.lang.Object from JSON: ... (through reference chain: \"[\" \"obj1\" \"$[\" \"obj1000\" \"$[\" \"]\"]\"]\")", e.getMessage());
            Assert.assertNull(e.getProcessor());
        }
    }

    @Test
    public void testSerializationWithEmptyPath() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.createObjectMapper();
        String json = mapper.writerWithRootValue(null).writeValueAsString(null);

        try {
            mapper.readValue(json, Object.class);
            fail("Expected JsonMappingException during serialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: No Value to serialize", e.getMessage());
            Assert.assertNull(e.getPathReference());
            Assert.assertNull(e.getProcessor());
        }
    }

    @Test
    public void testDeserializationWithEmptyPath() throws IOException {
        ObjectMapper mapper = ObjectMapper.createObjectMapper();
        String json = "null";

        try {
            mapper.readValue(json, Object.class);
            fail("Expected JsonMappingException during deserialization");
        } catch (JsonMappingException e) {
            Assert.assertEquals("JsonMappingException: No Value to deserialize", e.getMessage());
            Assert.assertNull(e.getPathReference());
            Assert.assertNull(e.getProcessor());
        }
    }
}