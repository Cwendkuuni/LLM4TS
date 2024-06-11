package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testReferenceConstructor() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("testClass", "testField");
        assertEquals("testClass", reference.getFrom());
        assertEquals("testField", reference.getFieldName());
    }

    @Test
    public void testReferenceConstructorWithIndex() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("testClass", 1);
        assertEquals("testClass", reference.getFrom());
        assertEquals(1, reference.getIndex());
    }

    @Test
    public void testReferenceDescription() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("testClass", "testField");
        assertEquals("testClass['testField']", reference.getDescription());
    }

    @Test
    public void testPathPrepending() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("testClass", "testField");
        JsonMappingException exception = new JsonMappingException("Test", new Throwable());
        exception.prependPath(reference);
        assertEquals(1, exception.getPath().size());
    }

    @Test
    public void testProcessorAccess() {
        JsonMappingException exception = new JsonMappingException(null, "Test");
        assertNull(exception.getProcessor());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("testClass", "testField");
        JsonMappingException exception = new JsonMappingException("Test", new Throwable());
        exception.prependPath(reference);
        StringBuilder sb = new StringBuilder();
        assertEquals("testClass['testField']", exception.getPathReference(sb).toString());
    }

    @Test
    public void testBuildMessage() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("testClass", "testField");
        JsonMappingException exception = new JsonMappingException("Test", new Throwable());
        exception.prependPath(reference);
        assertEquals("Test (through reference chain: testClass['testField'])", exception.getLocalizedMessage());
    }

    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("Test", new Throwable());
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test", exception.toString());
    }
}