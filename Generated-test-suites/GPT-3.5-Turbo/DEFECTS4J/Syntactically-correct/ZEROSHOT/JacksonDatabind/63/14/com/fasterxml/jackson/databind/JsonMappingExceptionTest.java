package com.fasterxml.jackson.databind;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        // Test case 1
        JsonMappingException exception1 = new JsonMappingException("Test exception");
        assertEquals(Collections.emptyList(), exception1.getPath());
        
        // Test case 2
        LinkedList<Reference> path = new LinkedList<>();
        path.add(new Reference("testObject", "fieldName"));
        path.add(new Reference("testObject", 0));
        JsonMappingException exception2 = new JsonMappingException("Test exception", path);
        assertEquals(path, exception2.getPath());
    }

    @Test
    public void testGetPathReference() {
        // Test case 1
        JsonMappingException exception1 = new JsonMappingException("Test exception");
        assertEquals("", exception1.getPathReference());
        
        // Test case 2
        LinkedList<Reference> path = new LinkedList<>();
        path.add(new Reference("testObject", "fieldName"));
        path.add(new Reference("testObject", 0));
        JsonMappingException exception2 = new JsonMappingException("Test exception", path);
        assertEquals("testObject.fieldName->testObject[0]", exception2.getPathReference());
    }

    @Test
    public void testPrependPath() {
        // Test case 1
        JsonMappingException exception1 = new JsonMappingException("Test exception");
        exception1.prependPath("testObject", "fieldName");
        assertEquals("testObject.fieldName", exception1.getPathReference());
    }
}