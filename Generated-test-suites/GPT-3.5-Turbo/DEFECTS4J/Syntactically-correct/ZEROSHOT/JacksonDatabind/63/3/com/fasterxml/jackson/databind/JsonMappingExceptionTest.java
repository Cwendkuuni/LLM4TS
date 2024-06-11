package com.fasterxml.jackson.databind;

import junit.framework.TestCase;
import java.util.*;

public class JsonMappingExceptionTest extends TestCase {

    public void testGetPath() {
        // Test if path is null, returns empty list
        JsonMappingException exception = new JsonMappingException("Test Exception");
        List<JsonMappingException.Reference> path = exception.getPath();
        assertEquals(Collections.emptyList(), path);

        // Test if path has values, returns unmodifiable list
        List<JsonMappingException.Reference> references = new ArrayList<>();
        Exception.Reference reference = new Exception.Reference("test", 1);
        references.add(reference);
        exception._path = references;

        path = exception.getPath();
        assertEquals(Collections.unmodifiableList(references), path);
    }

    public void testGetPathReference() {
        // Test if path reference is properly generated
        JsonMappingException exception = new JsonMappingException("Test Exception");
        List<JsonMappingException.Reference> references = new ArrayList<>();
        Exception.Reference reference = new Exception.Reference("test", 1);
        references.add(reference);
        exception._path = references;

        String pathReference = exception.getPathReference();
        assertEquals("test[1]", pathReference);
    }

    public void testPrependPath() {
        // Test if reference is properly prepended to path
        JsonMappingException exception = new JsonMappingException("Test Exception");

        Exception.Reference reference = new Exception.Reference("test", 1);
        exception.prependPath(reference);

        assertEquals(1, exception._path.size());
        assertEquals(reference, exception._path.get(0));
    }

    public void testWrapWithPath() {
        // Test if wrapWithPath method is properly wrapping exception
        Exception originalException = new Exception("Original Exception");

        Exception.Reference reference = new Exception.Reference("test", 1);
        Exception wrappedException = JsonMappingException.wrapWithPath(originalException, reference);

        assertTrue(wrappedException instanceof JsonMappingException);
        assertEquals(originalException.getMessage(), wrappedException.getMessage());
        assertNotNull(wrappedException._path);
        assertEquals(1, wrappedException._path.size());
        assertEquals(reference, wrappedException._path.get(0));
    }
}