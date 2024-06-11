package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        try {
            JsonProcessingException originalException = new JsonProcessingException("Unexpected error occurred.");
            JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(originalException, "testObject", "testField");
            assertEquals("Unexpected error occurred. (through reference chain: testObject->testField)", jsonMappingException.getMessage());
            assertEquals(1, jsonMappingException.getPath().size());
            assertEquals("testObject", jsonMappingException.getPath().get(0).getFrom().getClass().getName());
            assertEquals("testField", jsonMappingException.getPath().get(0).getFieldName());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            JsonProcessingException originalException = new JsonProcessingException("Unexpected error occurred.");
            JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(originalException, new Object[]{1, 2}, 0);
            assertEquals("Unexpected error occurred. (through reference chain: [1,2])", jsonMappingException.getMessage());
            assertEquals(1, jsonMappingException.getPath().size());
            assertEquals("[1,2]", jsonMappingException.getPathReference().toString());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            JsonParser parser = createParser();
            JsonMappingException jsonMappingException = JsonMappingException.from(parser, "Unexpected error occurred.");
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            JsonParser parser = createParser();
            JsonMappingException jsonMappingException = JsonMappingException.from(parser, "Unexpected error occurred.", new Throwable() {});
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            JsonGenerator generator = createGenerator();
            JsonMappingException jsonMappingException = JsonMappingException.from(generator, "Unexpected error occurred.");
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            JsonGenerator generator = createGenerator();
            JsonMappingException jsonMappingException = JsonMappingException.from(generator, "Unexpected error occurred.", new Throwable() {});
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonMappingException jsonMappingException = JsonMappingException.from(mapper.getSerializerProvider(), "Unexpected error occurred.");
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonMappingException jsonMappingException = JsonMappingException.from(mapper.getSerializerProvider(), "Unexpected error occurred.", new Throwable() {});
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (JsonProcessingException e) {
            fail("Should not reach here.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonParser parser = mapper.getFactory().createParser(new byte[0]);
            JsonMappingException jsonMappingException = JsonMappingException.from(parser, "Unexpected error occurred.");
            assertEquals("Unexpected error occurred", jsonMappingException.getMessage());
            assertNull(jsonMappingException.getPath());
        } catch (IOException e) {
            fail("Should not reach here.");
        }
    }

    private JsonParser createParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));
    }

    private JsonGenerator createGenerator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createGenerator(System.out);
    }
}