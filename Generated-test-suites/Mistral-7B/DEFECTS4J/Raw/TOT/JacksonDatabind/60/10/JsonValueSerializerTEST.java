package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonValueSerializer_String() throws JsonMappingException, JsonProcessingException {
        String input = "Hello";
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        assertEquals("\"Hello\"", serializer.serializeValueAsString(input));
    }

    @Test
    public void testJsonValueSerializer_Integer() throws JsonMappingException, JsonProcessingException {
        Integer input = 123;
        JsonValueSerializer serializer = mapper.findValueSerializer(Integer.class, null);
        assertEquals("123", serializer.serializeValueAsString(input));
    }

    @Test
    public void testJsonValueSerializer_Boolean() throws JsonMappingException, JsonProcessingException {
        Boolean input = true;
        JsonValueSerializer serializer = mapper.findValueSerializer(Boolean.class, null);
        assertEquals("true", serializer.serializeValueAsString(input));
    }

    @Test
    public void testJsonValueSerializer_CustomClass() throws JsonMappingException, JsonProcessingException {
        class CustomClass {
            @JsonValue
            public String getValue() {
                return "CustomValue";
            }
        }

        CustomClass customClass = new CustomClass();
        JsonValueSerializer serializer = mapper.findValueSerializer(CustomClass.class, null);
        assertEquals("\"CustomValue\"", serializer.serializeValueAsString(customClass));
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializer_Null() throws JsonMappingException, JsonProcessingException {
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        serializer.serializeValue(null, new StringBuilder(), null);
    }

    @Test
    public void testJsonValueSerializer_Schema() throws JsonMappingException, JsonProcessingException {
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), null);
        assertTrue(schema.isObject());
        assertTrue(schema.has("title"));
        assertTrue(schema.has("type"));
        assertTrue(schema.get("type").isTextual());
        assertTrue(schema.get("type").asText().equals("string"));
    }
}