package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionEmpty() {
        JsonMappingException exception = new JsonMappingException("Empty Message");
        assertEquals("Empty Message", exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException exception = JsonMappingException.wrapWithPath(new IOException("IOException"), refFrom, refFieldName);
        List<Reference> path = exception.getPath();
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
    }

    @Test
    public void testJsonMappingExceptionWithList() {
        Object refFrom = new Object();
        int refIndex = 0;
        JsonMappingException exception = JsonMappingException.wrapWithPath(new IOException("IOException"), refFrom, refIndex);
        List<Reference> path = exception.getPath();
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refIndex, reference.getIndex());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithLimit() {
        Object refFrom = new Object();
        int limit = JsonMappingException.MAX_REFS_TO_LIST + 1;
        for (int i = 0; i < limit; i++) {
            JsonMappingException exception = new JsonMappingException("Empty Message"); {
                Reference reference = new Reference(refFrom, i);
                exception.prependPath(reference);
            }
        }
    }

    @Test
    public void testJsonMappingExceptionWithCloseable() {
        Closeable closeable = new Object();
        JsonMappingException exception = new JsonMappingException(closeable, "Empty Message");
        assertEquals(closeable, exception.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithLocation() {
        Closeable closeable = new Object();
        JsonLocation location = new JsonLocation();
        JsonMappingException exception = new JsonMappingException(closeable, "Empty Message", location);
        assertEquals(location, exception.getLocation());
    }
}