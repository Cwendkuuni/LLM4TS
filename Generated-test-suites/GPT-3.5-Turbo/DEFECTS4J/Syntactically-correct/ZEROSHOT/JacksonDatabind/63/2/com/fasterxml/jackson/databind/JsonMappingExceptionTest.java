package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("");
        assertTrue(exception.getPath().isEmpty());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("");
        assertEquals("", exception.getPathReference());
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("");
        exception.prependPath(new Object(), "fieldName");
        assertFalse(exception.getPath().isEmpty());
    }
    
    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("");
        assertNotNull(exception.toString());
    }
    
}