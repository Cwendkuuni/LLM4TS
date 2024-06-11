package com.example.junit.tests.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_InvalidToken() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("123");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        try {
            deserializer.deserializeWithType(jsonParser, new TestContext(), new StdTypeDeserializer());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithType_Array() throws JsonProcessingException {
        String json = "[1, 2, 3]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new TestContext(), new StdSerializers.ArrayTypeDeserializer(Object.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_Object() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new TestContext(), new MapTypeDeserializer(Object.class, new SimpleType(Object.class)));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_FieldName() throws JsonProcessingException {
        String json = "{\"name\": null}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new TestContext(), new MapTypeDeserializer(String.class, new SimpleType(String.class)));
        assertNull(result);
    }

    /**
     * Test context for JUnit tests.
     */
    public static class TestContext extends DeserializationContext {
        @Override
        public void handleUnexpectedValue(Object value, Class<?> expectedType) throws JsonMappingException {
            // Ignore
        }

        @Override
        public void handleUnexpectedValue(Object value) throws JsonMappingException {
            // Ignore
        }

        @Override
        public void handleUnexpectedValue(JsonParser p, String message) throws JsonMappingException {
            // Ignore
        }

        @Override
        public void handleUnexpectedValue(JsonParser p, String message, Location loc) throws JsonMappingException {
            // Ignore
        }
    }
}