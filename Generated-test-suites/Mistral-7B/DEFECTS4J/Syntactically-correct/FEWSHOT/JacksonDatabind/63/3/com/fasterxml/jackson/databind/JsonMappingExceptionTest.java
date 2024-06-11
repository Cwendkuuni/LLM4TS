package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        try {
            JsonMappingException e = new JsonMappingException("Test exception message");
            assertEquals("Test exception message", e.getMessage());
            assertNull(e.getPath());
        } catch (JsonMappingException ex) {
            // expected
        }

        try {
            JsonMappingException e = new JsonMappingException("Test exception message", new JsonLocation("Test file", 1, 1));
            assertEquals("Test exception message", e.getMessage());
            assertEquals("Test file:1:1", e.getDescription());
            assertNull(e.getPath());
        } catch (JsonMappingException ex) {
            // expected
        }

        try {
            Closeable processor = mock(Closeable.class);
            JsonMappingException e = new JsonMappingException(processor, "Test exception message");
            assertEquals("Test exception message", e.getMessage());
            assertNull(e.getPath());
            assertEquals(processor, e.getProcessor());
        } catch (JsonMappingException ex) {
            // expected
        }

        try {
            Closeable processor = mock(Closeable.class);
            JsonMappingException e = new JsonMappingException(processor, "Test exception message", new JsonLocation("Test file", 1, 1));
            assertEquals("Test exception message", e.getMessage());
            assertEquals("Test file:1:1", e.getDescription());
            assertNull(e.getPath());
            assertEquals(processor, e.getProcessor());
        } catch (JsonMappingException ex) {
            // expected
        }

        try {
            Closeable processor = mock(Closeable.class);
            JsonMappingException e = new JsonMappingException(processor, "Test exception message", new Throwable("Test throwable message"));
            assertEquals("Test exception message", e.getMessage());
            assertNull(e.getPath());
            assertEquals(processor, e.getProcessor());
            assertEquals("Test throwable message", e.getCause().getMessage());
        } catch (JsonMappingException ex) {
            // expected
        }
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        Object refFrom = new Object() {};
        String refFieldName = "fieldName";

        Reference ref = new Reference(refFrom, refFieldName);
        JsonMappingException e = JsonMappingException.wrapWithPath(new RuntimeException("Test exception message"), ref);

        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref, path.get(0));

        assertEquals("Test exception message (through reference chain: [Test class#fieldName])", e.getMessage());
        assertEquals("Test class#fieldName", e.getPathReference().toString());
    }

    @Test
    public void testJsonMappingExceptionWithIndex() {
        Object refFrom = new Object()[0];
        int refIndex = 0;

        Reference ref = new Reference(refFrom, refIndex);
        JsonMappingException e = JsonMappingException.wrapWithPath(new RuntimeException("Test exception message"), ref);

        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref, path.get(0));

        assertEquals("Test exception message (through reference chain: [Test array#0])", e.getMessage());
        assertEquals("Test array#0", e.getPathReference().toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndIndex() {
        Object refFrom = new Object() {
            List<Object> list = new ArrayList<>();
        };
        String refFieldName = "list";
        int refIndex = 0;

        Reference ref = new Reference(refFrom, refFieldName, refIndex);
        JsonMappingException e = JsonMappingException.wrapWithPath(new RuntimeException("Test exception message"), ref);

        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref, path.get(0));

        assertEquals("Test exception message (through reference chain: [Test class#list->0])", e.getMessage());
        assertEquals("Test class#list->0", e.getPathReference().toString());
    }
}