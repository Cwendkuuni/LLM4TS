package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializationException() throws Exception {
        String json = "{\"name\":\"John\",\"age\":10}";
        String invalidJson = "{\"name\":\"John\",\"age\":[10}";

        try {
            mapper.readValue(invalidJson, Object.class);
        } catch (JsonProcessingException e) {
            JsonMappingException jme = JsonMappingException.from(mapper, e.getMessage());
            assertEquals("Unexpected token: [", jme.getDescription());
            assertEquals("JsonMappingException: Unexpected token: [", jme.toString());
            assertEquals(invalidJson, jme.getOriginalMessage());
            assertEquals(JsonProcessingException.class, jme.getOriginalCause().getClass());
            assertEquals(mapper, jme.getProcessor());
            assertEquals(1, jme.getPath().size());
            assertEquals("invalidJson", jme.getPath().get(0).getDescription());
            assertEquals("[JsonMappingException: Unexpected token: [", jme.getPathReference());
        }
    }

    @Test
    public void testDeserializationException() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        String invalidJson = "{\"name\":\"John\",\"age\":30,\"hobbies\":\"swimming\"}";

        try {
            mapper.writeValueAsString(new Object() {
                int hobbyIndex = 1;
            }, "[\"reading\",\"swimming\"]");
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.readValue(invalidJson, Object.class);
        } catch (JsonMappingException e) {
            JsonMappingException jme = JsonMappingException.from(mapper, e.getMessage());
            assertEquals("Cannot deserialize instance of \"java.lang.Integer\" out of START_ARRAY token", jme.getDescription());
            assertEquals("JsonMappingException: Cannot deserialize instance of \"java.lang.Integer\" out of START_ARRAY token", jme.toString());
            assertEquals(invalidJson, jme.getOriginalMessage());
            assertEquals(JsonMappingException.class, jme.getClass());
            assertEquals(mapper, jme.getProcessor());
            assertEquals(1, jme.getPath().size());
            assertEquals("hobbies", jme.getPath().get(0).getFieldName());
            assertEquals("[JsonMappingException: Cannot deserialize instance of \"java.lang.Integer\" out of START_ARRAY token]", jme.getPathReference());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCustomException() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"],\"exception\":{\"type\":\"MyCustomException\"}}";

        mapper.readValue(json, Object.class);
    }
}