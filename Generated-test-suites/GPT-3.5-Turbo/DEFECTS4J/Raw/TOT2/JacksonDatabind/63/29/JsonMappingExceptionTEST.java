package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        assertEquals("Test Exception", ex.getMessage());
    }

    @Test
    public void testAppendPath() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        ex.prependPath("field1", 1);

        assertEquals("field1", ex.getPath().get(0).getFieldName());
        assertEquals(1, ex.getPath().get(0).getIndex());
    }

    @Test
    public void testGetProcessor() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        assertNull(ex.getProcessor());
    }

    @Test
    public void testGetPath() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        assertNotNull(ex.getPath());
        assertTrue(ex.getPath().isEmpty());
    }

    @Test
    public void testToString() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test Exception", ex.toString());
    }

    @Test
    public void testGetLocalizedMessage() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        assertEquals("Test Exception", ex.getLocalizedMessage());
    }

    @Test
    public void testGetMessage() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        assertEquals("Test Exception", ex.getMessage());
    }

    @Test
    public void testWrapWithPath() {
        JsonMappingException ex = new JsonMappingException("Test Exception");
        JsonMappingException wrappedEx = JsonMappingException.wrapWithPath(ex, "field2", 2);

        assertEquals("field2", wrappedEx.getPath().get(0).getFieldName());
        assertEquals(2, wrappedEx.getPath().get(0).getIndex());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("IO Exception");
        JsonMappingException ex = JsonMappingException.fromUnexpectedIOE(ioException);

        assertEquals("Unexpected IOException (of type java.io.IOException): IO Exception", ex.getMessage());
    }

    @Test
    public void testFrom() {
        JsonParser parser = null;
        String msg = "Test message";
        JsonMappingException ex = JsonMappingException.from(parser, msg);

        assertEquals(msg, ex.getMessage());
        assertNull(ex.getProcessor());
    }

    @Test
    public void testFromWithProblem() {
        JsonParser parser = null;
        String msg = "Test message";
        Throwable problem = new Exception("Test problem");
        JsonMappingException ex = JsonMappingException.from(parser, msg, problem);

        assertEquals(msg, ex.getMessage());
        assertEquals(problem, ex.getCause());
        assertNull(ex.getProcessor());
    }
    
}