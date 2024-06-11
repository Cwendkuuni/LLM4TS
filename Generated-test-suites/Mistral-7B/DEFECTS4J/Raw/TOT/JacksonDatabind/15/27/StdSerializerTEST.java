package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTests;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testDefaultSerializer() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);

        String input = "Hello, World!";
        ObjectNode result = mapper.treeWriter().writeValueAsTree(input);

        StdSerializerTests.assertSerialization(stringSerializer, input, result);

        String output = mapper.readValue(result, String.class);
        assertEquals(input, output);
    }

    @Test
    public void testCustomSerializer() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Assuming we have a custom serializer called MyStringSerializer
        StdSerializer<String> customSerializer = mapper.findValueSerializer(MyStringSerializer.class);

        String input = "Hello, World!";
        ObjectNode result = mapper.treeWriter().writeValueAsTree(input);

        StdSerializerTests.assertSerialization(customSerializer, input, result);

        String output = mapper.readValue(result, String.class);
        assertEquals(input, output);
    }

    @Test
    public void testErrorHandling() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        StdSerializer<Object> objectSerializer = mapper.findValueSerializer(Object.class);

        try {
            objectSerializer.serialize(null, mapper.getFactory().createGenerator(System.out), mapper.getSerializationConfig());
            fail("Expected JsonMappingException");
        } catch (IOException e) {
            JsonMappingException jsonMappingException = e.getCause();
            assertTrue(jsonMappingException.isPathOrTextValueOfNullObject());
        }
    }

    @Test
    public void testSchemaGeneration() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        StdSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);

        ObjectNode schema = intSerializer.getSchema(mapper.getSerializationConfig(), Integer.class);

        ObjectNode expectedSchema = mapper.createObjectNode();
        expectedSchema.put("type", "number");
        expectedSchema.put("items", mapper.createObjectNode().put("type", "integer"));

        assertEquals(expectedSchema, schema);
    }
}