package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        JsonMappingException e = new JsonMappingException("Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());

        JsonMappingException eWithLocation = new JsonMappingException(new JsonLocation("Test File:123", 1), "Test Message");
        assertEquals("Test Message", eWithLocation.getMessage());
        assertEquals("Test File:123", eWithLocation.getLocation().getDescription());
        assertSame(eWithLocation.getLocation(), eWithLocation.getLocalizedMessage());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNotNull(e.getProcessor());
        assertSame(processor, e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        Reference ref1 = new JsonMappingException.Reference("Ref1", "field1");
        JsonMappingException eWithPath = JsonMappingException.wrapWithPath(e, ref1);
        List<JsonMappingException.Reference> path = eWithPath.getPath();
        assertEquals(1, path.size());
        assertEquals("Ref1", path.get(0).getFieldName());
        assertEquals("field1", path.get(0).getDescription());
        assertSame(processor, eWithPath.getProcessor());
        assertSame(e.getMessage(), eWithPath.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPathList() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        Reference ref1 = new JsonMappingException.Reference("Ref1", "field1");
        Reference ref2 = new JsonMappingException.Reference("Ref2", "field2");
        JsonMappingException eWithPath = JsonMappingException.wrapWithPath(e, ref1);
        JsonMappingException eWithPath2 = JsonMappingException.wrapWithPath(eWithPath, ref2);
        List<JsonMappingException.Reference> path = eWithPath2.getPath();
        assertEquals(2, path.size());
        assertEquals("Ref2", path.get(1).getFieldName());
        assertEquals("field2", path.get(1).getDescription());
        assertEquals("Ref1", path.get(0).getFieldName());
        assertEquals("field1", path.get(0).getDescription());
        assertSame(processor, eWithPath2.getProcessor());
        assertSame(e.getMessage(), eWithPath2.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testJsonMappingExceptionNullPath() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        JsonMappingException.Reference ref1 = null;
        JsonMappingException eWithPath = JsonMappingException.wrapWithPath(e, ref1);
    }

    @Test
    public void testJsonMappingExceptionToString() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        StringBuilder expected = new StringBuilder("JsonMappingException: Test Message (through reference chain: ");
        StringBuilder actual = new StringBuilder(e.toString());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJsonMappingExceptionGetLocalizedMessage() {
        Closeable processor = new Object();
        String locationDescription = "Test File:123";
        JsonMappingException e = new JsonMappingException(processor, "Test Message", new JsonLocation(locationDescription, 1));
        StringBuilder expected = new StringBuilder("Test Message\nTest File:123: ");
        StringBuilder actual = new StringBuilder(e.getLocalizedMessage());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testJsonMappingExceptionGetPathElementAtInvalidIndex() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        JsonMappingException.Reference[] references = new JsonMappingException.Reference[1];
        references[0] = new JsonMappingException.Reference("Ref1", "field1");
        e.setPath(Arrays.asList(references));
        JsonMappingException.Reference ref = e.getPath().get(1);
    }

    @Test
    public void testJsonMappingExceptionGetPathElementAtValidIndex() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        JsonMappingException.Reference ref1 = new JsonMappingException.Reference("Ref1", "field1");
        JsonMappingException.Reference ref2 = new JsonMappingException.Reference("Ref2", "field2");
        JsonMappingException eWithPath = JsonMappingException.wrapWithPath(e, ref1);
        JsonMappingException eWithPath2 = JsonMappingException.wrapWithPath(eWithPath, ref2);
        JsonMappingException.Reference ref = eWithPath2.getPath().get(1);
        assertEquals("Ref2", ref.getFieldName());
        assertEquals("field2", ref.getDescription());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testJsonMappingExceptionUnmodifiablePath() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message");
        List<JsonMappingException.Reference> path = Arrays.asList(new JsonMappingException.Reference("Ref1", "field1"));
        e.setPath(path);
        e.getPath().add(new JsonMappingException.Reference("Ref2", "field2"));
    }
}