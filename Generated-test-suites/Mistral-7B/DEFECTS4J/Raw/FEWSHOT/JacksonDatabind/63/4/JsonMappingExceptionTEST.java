package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.Reference;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Test cases for {@link JsonMappingException} class.
 */
public class JsonMappingExceptionTest {

    @Test
    public void testCreateFromCloseable() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message");
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getPath());
    }

    @Test
    public void testCreateFromCloseableAndMessage() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new IOException("Test IO exception"));
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getPath());
        assertEquals(IOException.class, exception.getCause().getClass());
    }

    @Test
    public void testCreateFromCloseableAndLocation() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonLocation location = new JsonLocation("Test file", 1, 2);
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", location);
        assertEquals("Test message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertNull(exception.getPath());
    }

    @Test
    public void testCreateFromCloseableAndMessageAndLocation() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonLocation location = new JsonLocation("Test file", 1, 2);
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", location, new IOException("Test IO exception"));
        assertEquals("Test message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertNull(exception.getPath());
        assertEquals(IOException.class, exception.getCause().getClass());
    }

    @Test
    public void testCreateFromJsonParser() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message");
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getPath());
    }

    @Test
    public void testCreateFromJsonParserAndMessage() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new IOException("Test IO exception"));
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getPath());
        assertEquals(IOException.class, exception.getCause().getClass());
    }

    @Test
    public void testCreateFromJsonParserAndLocation() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new JsonLocation("Test file", 1, 2));
        assertEquals("Test message", exception.getMessage());
        assertEquals(new JsonLocation("Test file", 1, 2), exception.getLocation());
        assertNull(exception.getPath());
    }

    @Test
    public void testCreateFromJsonParserAndMessageAndLocation() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new JsonLocation("Test file", 1, 2), new IOException("Test IO exception"));
        assertEquals("Test message", exception.getMessage());
        assertEquals(new JsonLocation("Test file", 1, 2), exception.getLocation());
        assertNull(exception.getPath());
        assertEquals(IOException.class, exception.getCause().getClass());
    }

    @Test
    public void testCreateFromJsonGenerator() {
        JsonGenerator generator = null; // replace with actual JsonGenerator instance for testing
        JsonMappingException exception = JsonMappingException.from(generator, "Test message");
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getPath());
    }

    @Test
    public void testCreateFromJsonGeneratorAndMessage() {
        JsonGenerator generator = null; // replace with actual JsonGenerator instance for testing
        JsonMappingException exception = JsonMappingException.from(generator, "Test message", new IOException("Test IO exception"));
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getPath());
        assertEquals(IOException.class, exception.getCause().getClass());
    }

    @Test
    public void testCreateFromDeserializationContext() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message");
        JsonContext context = new JsonContext();
        JsonMappingException newException = JsonMappingException.from(context, "Test message");
        assertEquals("Test message", newException.getMessage());
        assertEquals(exception.getLocation(), newException.getLocation());
        assertEquals(exception.getCause(), newException.getCause());
        assertArrayEquals(exception.getPath().toArray(), newException.getPath().toArray());
    }

    @Test
    public void testCreateFromDeserializationContextAndMessage() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new IOException("Test IO exception"));
        JsonContext context = new JsonContext();
        JsonMappingException newException = JsonMappingException.from(context, "Test message", new IOException("Test IO exception"));
        assertEquals("Test message", newException.getMessage());
        assertEquals(exception.getLocation(), newException.getLocation());
        assertEquals(exception.getCause(), newException.getCause());
        assertArrayEquals(exception.getPath().toArray(), newException.getPath().toArray());
    }

    @Test
    public void testCreateFromDeserializationContextAndMessageAndLocation() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new JsonLocation("Test file", 1, 2), new IOException("Test IO exception"));
        JsonContext context = new JsonContext();
        JsonMappingException newException = JsonMappingException.from(context, "Test message", new JsonLocation("Test file", 1, 2), new IOException("Test IO exception"));
        assertEquals("Test message", newException.getMessage());
        assertEquals(exception.getLocation(), newException.getLocation());
        assertEquals(exception.getCause(), newException.getCause());
        assertArrayEquals(exception.getPath().toArray(), newException.getPath().toArray());
    }

    @Test
    public void testCreateFromDeserializationContextAndMessageAndLocationAndCause() {
        JsonParser parser = null; // replace with actual JsonParser instance for testing
        JsonMappingException exception = JsonMappingException.from(parser, "Test message", new JsonLocation("Test file", 1, 2), new IOException("Test IO exception"), new Exception("Test exception"));
        JsonContext context = new JsonContext();
        JsonMappingException newException = JsonMappingException.from(context, "Test message", new JsonLocation("Test file", 1, 2), new IOException("Test IO exception"), new Exception("Test exception"));
        assertEquals("Test message", newException.getMessage());
        assertEquals(exception.getLocation(), newException.getLocation());
        assertEquals(exception.getCause(), newException.getCause());
        assertArrayEquals(exception.getPath().toArray(), newException.getPath().toArray());
        assertEquals("Test exception", newException.getCause().getCause().getMessage());
    }

    @Test
    public void testWrapWithPath() {
        Object referrer = new Object();
        String fieldName = "testField";
        JsonMappingException originalException = new JsonMappingException("Test message", new IOException("Test IO exception"));
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, fieldName);
        List<Reference> path = wrappedException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());
        assertEquals("Test message (through reference chain: TestObject(testField)", wrappedException.getMessage());
    }

    @Test
    public void testWrapWithPathForIndexedReference() {
        Object referrer = new Object();
        int index = 0;
        JsonMappingException originalException = new JsonMappingException("Test message", new IOException("Test IO exception"));
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, index);
        List<Reference> path = wrappedException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(-1, reference.getIndex());
        assertEquals("Test message (through reference chain: TestObject[0])", wrappedException.getMessage());
    }

    @Test
    public void testWrapWithPathForArrayReference() {
        Object referrer = new Object[] { new Object(), new Object() };
        int index = 0;
        JsonMappingException originalException = new JsonMappingException("Test message", new IOException("Test IO exception"));
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, index);
        List<Reference> path = wrappedException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(index, reference.getIndex());
        assertEquals("Test message (through reference chain: Object[][0])", wrappedException.getMessage());
    }

    @Test
    public void testWrapWithPathForMapReference() {
        Object referrer = new HashMap<>();
        String key = "testKey";
        JsonMappingException originalException = new JsonMappingException("Test message", new IOException("Test IO exception"));
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, key);
        List<Reference> path = wrappedException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(key, reference.getFieldName());
        assertEquals("Test message (through reference chain: Map<String, Object>(\"testKey\"))", wrappedException.getMessage());
    }

    @Test
    public void testWrapWithPathForMapReferenceWithIndex() {
        Object referrer = new HashMap<>();
        String key = "testKey";
        JsonMappingException originalException = new JsonMappingException("Test message", new IOException("Test IO exception"));
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, key, 1);
        List<Reference> path = wrappedException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(key, reference.getFieldName());
        assertEquals(1, reference.getIndex());
        assertEquals("Test message (through reference chain: Map<String, Object>(\"testKey\")[1])", wrappedException.getMessage());
    }
}