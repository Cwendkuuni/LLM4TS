package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testConstruction() {
        JsonMappingException e = new JsonMappingException("Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructionWithLocation() {
        JsonParser parser = createMockParser();
        JsonMappingException e = new JsonMappingException(parser, "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertTrue(e.getLocation() instanceof JsonLocation);
    }

    @Test
    public void testConstructionWithLocationAndRootCause() {
        JsonParser parser = createMockParser();
        IOException rootCause = new IOException("Root Cause");
        JsonMappingException e = new JsonMappingException(parser, "Test Message", rootCause);
        assertEquals("Test Message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertTrue(e.getLocation() instanceof JsonLocation);
        assertEquals(rootCause, e.getRootCause());
    }

    @Test
    public void testConstructionWithProcessor() {
        JsonParser parser = createMockParser();
        JsonMappingException e = new JsonMappingException(parser, "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getPath());
    }

    @Test
    public void testConstructionWithProcessorAndMessage() {
        JsonParser parser = createMockParser();
        JsonMappingException e = new JsonMappingException(parser, "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getPath());
    }

    @Test
    public void testConstructionWithProcessorAndMessageAndRootCause() {
        JsonParser parser = createMockParser();
        IOException rootCause = new IOException("Root Cause");
        JsonMappingException e = new JsonMappingException(parser, "Test Message", rootCause);
        assertEquals("Test Message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertTrue(e.getLocation() instanceof JsonLocation);
        assertEquals(rootCause, e.getRootCause());
    }

    @Test
    public void testWrapWithPath() {
        Object referrer = new Object();
        String fieldName = "fieldName";
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = JsonMappingException.wrapWithPath(src, referrer, fieldName);
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(referrer, ref.getFrom());
        assertEquals(fieldName, ref.getFieldName());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testWrapWithPathIndex() {
        Object referrer = new Object()[0];
        int index = 0;
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = JsonMappingException.wrapWithPath(src, referrer, index);
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(referrer, ref.getFrom());
        assertEquals(-1, ref.getIndex());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testGetPathReference() {
        Object referrer = new Object();
        String fieldName = "fieldName";
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = JsonMappingException.wrapWithPath(src, referrer, fieldName);
        StringBuilder sb = new StringBuilder();
        e.getPathReference(sb);
        String expected = String.format("(through reference chain: %s->%s)", referrer.getClass().getName(), fieldName);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetPathReferenceNullBuilder() {
        Object referrer = new Object();
        String fieldName = "fieldName";
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = JsonMappingException.wrapWithPath(src, referrer, fieldName);
        StringBuilder sb = null;
        e.getPathReference(sb);
        assertNotNull(sb);
    }

    @Test
    public void testPrependPath() {
        Object referrer = new Object();
        String fieldName = "fieldName";
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = new JsonMappingException(src);
        e.prependPath(referrer, fieldName);
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(referrer, ref.getFrom());
        assertEquals(fieldName, ref.getFieldName());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testPrependPathIndex() {
        Object referrer = new Object()[0];
        int index = 0;
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = new JsonMappingException(src);
        e.prependPath(referrer, index);
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(referrer, ref.getFrom());
        assertEquals(index, ref.getIndex());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testPrependPathNullReferrer() {
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = new JsonMappingException(src);
        e.prependPath(null, "fieldName");
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testPrependPathNullFieldName() {
        Object referrer = new Object();
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = new JsonMappingException(src);
        e.prependPath(referrer, null);
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testPrependPathMaxReferences() {
        Object[] referrers = new Object[1001];
        for (int i = 0; i < referrers.length; i++) {
            referrers[i] = new Object();
        }
        Throwable src = new IOException("Test IO Exception");
        JsonMappingException e = new JsonMappingException(src);
        for (int i = 0; i < referrers.length; i++) {
            e.prependPath(referrers[i], i);
        }
        List<Reference> path = e.getPath();
        assertEquals(JsonMappingException.MAX_REFS_TO_LIST, path.size());
        assertEquals("Test IO Exception", e.getMessage());
    }

    @Test
    public void testGetPathNullPath() {
        JsonMappingException e = new JsonMappingException("Test Message");
        assertNull(e.getPath());
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }
}