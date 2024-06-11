package com.example;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

import org.junit.Test;

public class NumberSerializerTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void serializeTest() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        Number value = 123;
        JsonNode expected = mapper.valueToTree(value);

        ObjectNode actual = mapper.writeValueAsTree(numberSerializer.serialize(value, null, null));

        JsonTestUtils.assertDeepEquals(expected, actual);
    }

    // Add other basic test cases for serialize, getSchema, and acceptJsonFormatVisitor methods

    @Test
    public void serializeNullTest() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode expected = mapper.valueToTree(null);

        ObjectNode actual = mapper.writeValueAsTree(numberSerializer.serialize(null, null, null));

        JsonTestUtils.assertDeepEquals(expected, actual);
    }

    // Add other edge case and exception handling test cases

    

    
}