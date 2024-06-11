package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testCreateDefaultException() {
        JsonMappingException e = new JsonMappingException("Default message");
        assertEquals("Default message", e.getMessage());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getPath());
    }

    @Test
    public void testCreateExceptionWithMessageAndLocation() {
        JsonParser parser = null; // assume null for now
        JsonMappingException e = new JsonMappingException(parser, "Message with location");
        assertEquals("Message with location", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
    }

    @Test
    public void testCreateExceptionWithMessageLocationAndRootCause() {
        JsonParser parser = null; // assume null for now
        Throwable cause = new IOException("Root cause");
        JsonMappingException e = new JsonMappingException(parser, "Message with location and root cause", cause);
        assertEquals("Message with location and root cause", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(cause, e.getRootCause());
        assertNull(e.getLocation());
        assertNull(e.getPath());
    }

    @Test
    public void testCreateExceptionWithMessageLocationAndPath() {
        JsonParser parser = null; // assume null for now
        JsonMappingException.Reference ref0 = new JsonMappingException.Reference(null, "Field0");
        JsonMappingException.Reference ref1 = new JsonMappingException.Reference(null, "Field1");
        JsonMappingException e = new JsonMappingException(parser, "Message with location and path", ref0, ref1);
        assertEquals("Message with location and path", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        List<JsonMappingException.Reference> path = e.getPath();
        assertEquals(2, path.size());
        assertEquals(ref0, path.get(0));
        assertEquals(ref1, path.get(1));
    }

    @Test
    public void testWrapExceptionWithPath() {
        IOException ioException = new IOException("Original IOException message");
        JsonMappingException.Reference ref0 = new JsonMappingException.Reference(null, "Field0");
        JsonMappingException e = JsonMappingException.wrapWithPath(ioException, ref0);
        assertEquals("Original IOException message (through reference chain: [Field0])", e.getMessage());
        assertEquals(null, e.getProcessor());
        assertNull(e.getLocation());
        List<JsonMappingException.Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref0, path.get(0));
    }

    @Test
    public void testWrapExceptionWithPathIndex() {
        IOException ioException = new IOException("Original IOException message");
        Object referrer = new Object();
        int index = 0;
        JsonMappingException.Reference ref = new JsonMappingException.Reference(referrer, index);
        JsonMappingException e = JsonMappingException.wrapWithPath(ioException, referrer, index);
        assertEquals("Original IOException message (through reference chain: [Ljava.lang.Object;@78528322[0]])", e.getMessage());
        assertEquals(null, e.getProcessor());
        assertNull(e.getLocation());
        List<JsonMappingException.Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref, path.get(0));
    }

    @Test
    public void testCreateExceptionWithProcessorAndMessage() {
        JsonParser parser = null; // assume null for now
        JsonMappingException e = new JsonMappingException(parser, "Message");
        assertEquals("Message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateExceptionWithNullMessage() {
        JsonParser parser = null; // assume null for now
        JsonMappingException e = new JsonMappingException(parser, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateExceptionWithNullProcessor() {
        String message = "Message";
        JsonMappingException e = new JsonMappingException(null, message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateExceptionWithEmptyMessage() {
        JsonParser parser = null; // assume null for now
        JsonMappingException e = new JsonMappingException(parser, "");
    }
}