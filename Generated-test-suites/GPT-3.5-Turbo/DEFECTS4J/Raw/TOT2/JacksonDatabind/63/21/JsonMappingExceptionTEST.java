package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithLocation() {
        String message = "Test message with location";
        JsonLocation location = new JsonLocation("testSource", 1, 1, 1);
        JsonMappingException exception = new JsonMappingException(message, location);
        assertEquals(message, exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @Test
    public void testJsonMappingExceptionWithCause() {
        String message = "Test message with cause";
        Throwable cause = new NullPointerException();
        JsonMappingException exception = new JsonMappingException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionFromParser() {
        String message = "Test message from parser";
        JsonParser parser = new JsonFactory().createParser("{}");
        JsonMappingException exception = JsonMappingException.from(parser, message);
        assertEquals(message, exception.getMessage());
        assertEquals(parser.getCurrentLocation(), exception.getLocation());
    }

    @Test
    public void testJsonMappingExceptionFromGenerator() {
        String message = "Test message from generator";
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonMappingException exception = JsonMappingException.from(generator, message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWrapWithPath() {
        String message = "Test message to wrap with path";
        JsonMappingException innerException = new JsonMappingException("Inner exception message");
        JsonMappingException exception = JsonMappingException.wrapWithPath(innerException, this, "testField");
        assertEquals(message, exception.getMessage());
        assertEquals(innerException, exception.getCause());
    }
}