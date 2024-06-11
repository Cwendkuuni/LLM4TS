package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("");
        assertNotNull(exception.getPath());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("");
        assertNotNull(exception.getPathReference());
    }

    @Test
    public void testPrependPath_Object_String() {
        JsonMappingException exception = new JsonMappingException("");
        exception.prependPath(new Object(), "fieldName");
        assertTrue(exception.getPath().size() > 0);
    }

    @Test
    public void testPrependPath_Object_int() {
        JsonMappingException exception = new JsonMappingException("");
        exception.prependPath(new Object(), 0);
        assertTrue(exception.getPath().size() > 0);
    }

    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("");
        assertNotNull(exception.toString());
    }
}