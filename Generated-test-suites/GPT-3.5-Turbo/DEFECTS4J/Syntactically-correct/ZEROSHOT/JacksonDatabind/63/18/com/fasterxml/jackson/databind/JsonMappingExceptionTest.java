package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertNotNull(exception.getPath());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertNotNull(exception.getPathReference());
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        exception.prependPath("MockReferrer", "MockField");
        assertEquals(1, exception.getPath().size());
    }

    @Test
    public void testGetProcessor() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertNull(exception.getProcessor());
    }

    @Test
    public void testGetMessage() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        String expected = "Test Exception";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        String expected = "com.fasterxml.jackson.databind.JsonMappingException: Test Exception";
        assertEquals(expected, exception.toString());
    }
}