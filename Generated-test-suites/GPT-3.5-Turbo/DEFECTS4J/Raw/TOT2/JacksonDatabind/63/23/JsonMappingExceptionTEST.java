package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        JsonMappingException baseException = new JsonMappingException("Base exception");
        JsonMappingException exception = JsonMappingException.wrapWithPath(baseException, new Object(), "fieldName");
        assertNotNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionPrependPath() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath(new Object(), "fieldName");
        assertEquals(1, exception.getPath().size());
    }

    @Test
    public void testJsonMappingExceptionGetPathReference() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath(new Object(), "fieldName");
        assertEquals("UNKNOWN.Object[fieldName]", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionToString() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test message", exception.toString());
    }
}