package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        JsonMappingException e = new JsonMappingException("Message");
        assertNotNull(e.getPath());
        assertEquals(0, e.getPath().size());
        assertNull(e.getFrom());
        assertEquals("Message", e.getMessage());
        assertEquals("Message", e.getLocalizedMessage());
        assertEquals("JsonMappingException: Message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        JsonMappingException e = new JsonMappingException("Message", new Reference("from", "fieldName"));
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals("from", ref.getFrom());
        assertEquals("fieldName", ref.getFieldName());
        assertEquals("from[fieldName]", ref.getDescription());
        assertEquals("JsonMappingException: Message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndDescription() {
        JsonMappingException e = new JsonMappingException("Message", new Reference("from", "fieldName"), "description");
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals("from", ref.getFrom());
        assertEquals("fieldName", ref.getFieldName());
        assertEquals("from[fieldName]", ref.getDescription());
        assertEquals("JsonMappingException: description (through reference chain: from[fieldName])", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException("JsonMappingException message", ioE);
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("JsonMappingException: JsonMappingException message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndRootCause() {
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException("JsonMappingException message", new Reference("from", "fieldName"), ioE);
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals("from", ref.getFrom());
        assertEquals("fieldName", ref.getFieldName());
        assertEquals("from[fieldName]", ref.getDescription());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("JsonMappingException: JsonMappingException message", e.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testJsonMappingExceptionWithNullFieldName() {
        new JsonMappingException("Message", null);
    }

    @Test(expected = NullPointerException.class)
    public void testJsonMappingExceptionWithNullFrom() {
        new JsonMappingException("Message", new Reference(null, "fieldName"));
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocation() {
        JsonParser parser = null;
        JsonMappingException e = new JsonMappingException(parser, "Message");
        assertEquals(0, e.getPath().size());
        assertNull(e.getLocation());
        assertEquals("JsonMappingException: Message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocationAndRootCause() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE);
        assertEquals(0, e.getPath().size());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(1, e.getLocation().getColumnNumber());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("JsonMappingException: Message (at [1,1])", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocationAndRootCauseAndDescription() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE, "description");
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(1, e.getLocation().getColumnNumber());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("description (at [1,1])", e.getDescription());
        assertEquals("JsonMappingException: description (at [1,1])", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessor() {
        JsonParser parser = null;
        JsonMappingException e = new JsonMappingException(parser, "Message");
        assertEquals(0, e.getPath().size());
        assertNull(e.getProcessor());
        assertEquals("JsonMappingException: Message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndRootCause() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE);
        assertEquals(0, e.getPath().size());
        assertNull(e.getProcessor());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("JsonMappingException: Message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndRootCauseAndDescription() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE, "description");
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertNull(e.getProcessor());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("description", e.getDescription());
        assertEquals("JsonMappingException: description", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndLocation() {
        JsonParser parser = null;
        JsonMappingException e = new JsonMappingException(parser, "Message");
        assertEquals(0, e.getPath().size());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertEquals("JsonMappingException: Message", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndLocationAndRootCause() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE);
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertNull(e.getProcessor());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(1, e.getLocation().getColumnNumber());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("JsonMappingException: Message (at [1,1])", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndLocationAndRootCauseAndDescription() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE, "description");
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertNull(e.getProcessor());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(1, e.getLocation().getColumnNumber());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("description (at [1,1])", e.getDescription());
        assertEquals("JsonMappingException: description (at [1,1])", e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndLocationAndDescriptionAndWrapper() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE, "description");
        JsonMappingException wrappedE = JsonMappingException.wrapWithPath(ioE, parser, "wrapper");
        List<Reference> path = wrappedE.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(parser, ref.getFrom());
        assertEquals("wrapper", ref.getFieldName());
        assertEquals("parser[wrapper]", ref.getDescription());
        assertEquals("JsonMappingException: description (through reference chain: parser[wrapper])", wrappedE.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndProcessorAndLocationAndRootCauseAndWrapper() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE, "description");
        JsonMappingException wrappedE = JsonMappingException.wrapWithPath(ioE, parser, "wrapper");
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertNull(e.getProcessor());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        assertEquals("description (at [1,1])", e.getDescription());
        List<Reference> wrappedPath = wrappedE.getPath();
        assertEquals(1, wrappedPath.size());
        Reference wrappedRef = wrappedPath.get(0);
        assertEquals(parser, wrappedRef.getFrom());
        assertEquals("wrapper", wrappedRef.getFieldName());
        assertEquals("parser[wrapper]", wrappedRef.getDescription());
        assertEquals("JsonMappingException: description (through reference chain: parser[wrapper])", wrappedE.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocationAndRootCauseAndWrapper() {
        JsonParser parser = null;
        IOException ioE = new IOException("IO Exception message");
        JsonMappingException e = new JsonMappingException(parser, "Message", ioE, 1);
        JsonMappingException wrappedE = JsonMappingException.wrapWithPath(ioE, parser, 1);
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertNull(e.getProcessor());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(1, e.getLocation().getColumnNumber());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        List<Reference> wrappedPath = wrappedE.getPath();
        assertEquals(1, wrappedPath.size());
        Reference wrappedRef = wrappedPath.get(0);
        assertEquals(parser, wrappedRef.getFrom());
        assertEquals("1", wrappedRef.getFieldName());
        assertEquals("parser[1]", wrappedRef.getDescription());
        assertEquals("JsonMappingException: IO Exception message (at [1,1])", wrappedE.toString());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocationAndRootCauseAndWrapperWithArrayIndex() {
        JsonParser parser = null;
        Object array = new Object[1];
        JsonMappingException e = new JsonMappingException(parser, "Message", new IOException("IO Exception message"), 0);
        JsonMappingException wrappedE = JsonMappingException.wrapWithPath(new IOException("IO Exception message"), parser, 0, array);
        List<Reference> path = e.getPath();
        assertEquals(0, path.size());
        assertNull(e.getProcessor());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(1, e.getLocation().getColumnNumber());
        assertNotNull(e.getRootCause());
        assertEquals("IO Exception message", e.getRootCause().getMessage());
        List<Reference> wrappedPath = wrappedE.getPath();
        assertEquals(1, wrappedPath.size());
        Reference wrappedRef = wrappedPath.get(0);
        assertEquals(parser, wrappedRef.getFrom());
        assertEquals("0", wrappedRef.getFieldName());
        assertEquals("parser[0]", wrappedRef.getDescription());
        assertEquals("JsonMappingException: IO Exception message (at [1,1])", wrappedE.toString());
    }
}