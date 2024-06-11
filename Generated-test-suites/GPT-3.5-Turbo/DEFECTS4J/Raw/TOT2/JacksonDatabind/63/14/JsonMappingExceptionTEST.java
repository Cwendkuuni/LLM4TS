package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        Throwable cause = new Throwable("Root cause");
        JsonMappingException exception = new JsonMappingException("Test message", cause);
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath("Test referrer", "fieldName");
        List<Reference> path = exception.getPath();
        assertEquals(1, path.size());
        assertEquals("Test referrer", path.get(0).getFrom());
        assertEquals("fieldName", path.get(0).getFieldName());
    }

    @Test
    public void testJsonMappingExceptionFrom() {
        JsonMappingException exception = JsonMappingException.from(new Closeable() {}, "Test message");
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromWithProblem() {
        Throwable problem = new Throwable("Problem");
        JsonMappingException exception = JsonMappingException.from(new Closeable() {}, "Test message", problem);
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertEquals(problem, exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionFromWithContext() {
        DeserializationContext ctxt = new DeserializationContext(null);
        JsonMappingException exception = JsonMappingException.from(ctxt, "Test message");
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromSerializerProvider() {
        SerializerProvider ctxt = new SerializerProvider(null);
        JsonMappingException exception = JsonMappingException.from(ctxt, "Test message");
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOE() {
        IOException ioException = new IOException("IO Exception");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(ioException);
        assertNotNull(exception);
        assertEquals("Unexpected IOException (of type java.io.IOException): IO Exception", exception.getMessage());
    }

    @Test
    public void testWrapWithPathForObjectAndFieldName() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.wrapWithPath(new Throwable(), "Test referrer", "fieldName");
        List<Reference> path = exception.getPath();
        assertEquals(1, path.size());
        assertEquals("Test referrer", path.get(0).getFrom());
        assertEquals("fieldName", path.get(0).getFieldName());
    }

    @Test
    public void testWrapWithPathForObjectAndIndex() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.wrapWithPath(new Throwable(), "Test referrer", 0);
        List<Reference> path = exception.getPath();
        assertEquals(1, path.size());
        assertEquals("Test referrer", path.get(0).getFrom());
        assertEquals(0, path.get(0).getIndex());
    }

}