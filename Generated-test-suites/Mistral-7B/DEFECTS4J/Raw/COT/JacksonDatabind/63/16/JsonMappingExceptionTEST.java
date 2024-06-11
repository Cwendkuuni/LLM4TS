package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMappingExceptionTest {

    

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithMessage() {
        String message = "Test JsonMappingException with message";
        JsonMappingException jsonMappingException = new JsonMappingException(message);
        assertEquals(message, jsonMappingException.getMessage());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithMessageAndCause() {
        String message = "Test JsonMappingException with message and cause";
        Throwable cause = new IOException("IO exception");
        JsonMappingException jsonMappingException = new JsonMappingException(message, cause);
        assertEquals(message, jsonMappingException.getMessage());
        assertEquals(cause, jsonMappingException.getCause());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithMessageLocation() {
        String message = "Test JsonMappingException with message and location";
        JsonLocation location = new JsonLocation(1, 1, "TestFile.java");
        JsonMappingException jsonMappingException = new JsonMappingException(message, location);
        assertEquals(message, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithMessageLocationCause() {
        String message = "Test JsonMappingException with message, location, and cause";
        JsonLocation location = new JsonLocation(1, 1, "TestFile.java");
        Throwable cause = new IOException("IO exception");
        JsonMappingException jsonMappingException = new JsonMappingException(message, location, cause);
        assertEquals(message, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(cause, jsonMappingException.getCause());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithProcessorMessage() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(stringWriter);
        String message = "Test JsonMappingException with message and processor";
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, message);
        assertEquals(message, jsonMappingException.getMessage());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithProcessorMessageCause() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(stringWriter);
        String message = "Test JsonMappingException with message, processor, and cause";
        Throwable cause = new IOException("IO exception");
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, message, cause);
        assertEquals(message, jsonMappingException.getMessage());
        assertEquals(cause, jsonMappingException.getCause());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithProcessorMessageLocation() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(stringWriter);
        String message = "Test JsonMappingException with message, processor, and location";
        JsonLocation location = new JsonLocation(1, 1, "TestFile.java");
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, message, location);
        assertEquals(message, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
    }

    @org.junit.Test
    public void testJsonMappingExceptionConstructorWithProcessorMessageLocationCause() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(stringWriter);
        String message = "Test JsonMappingException with message, processor, location, and cause";
        JsonLocation location = new JsonLocation(1, 1, "TestFile.java");
        Throwable cause = new IOException("IO exception");
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, message, location, cause);
        assertEquals(message, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(cause, jsonMappingException.getCause());
    }

    @org.junit.Test
    public void testJsonMappingExceptionFromUnexpectedIOE() {
        IOException ioException = new IOException("IO exception");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        assertTrue(jsonMappingException.getMessage().contains("Unexpected IOException"));
    }

    @org.junit.Test
    public void testJsonMappingExceptionWrapWithPath() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(stringWriter);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "{\"test\":[]}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        Object refFrom = new Object() {};
        String refFieldName = "test";
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(new JsonProcessingException(jsonParser), refFrom, refFieldName);
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
    }

    @org.junit.Test
    public void testJsonMappingExceptionWrapWithPathWithIndex() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(stringWriter);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "[{\"test\":null}]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        Object refFrom = new Object() {};
        int index = 0;
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(new JsonProcessingException(jsonParser), refFrom, index);
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(index, reference.getIndex());
    }

    
}