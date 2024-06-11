package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for JsonMappingException.
 */
public class JsonMappingExceptionTest {

    private Closeable closeable;

    @Before
    public void setUp() {
        closeable = mock(Closeable.class);
    }

    @After
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    /**
     * Test constructor with message.
     */
    @Test
    public void testJsonMappingExceptionWithMessage() {
        JsonMappingException e = new JsonMappingException("Test Exception");
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    /**
     * Test constructor with message and root cause.
     */
    @Test
    public void testJsonMappingExceptionWithMessageAndRootCause() {
        IOException rootCause = new IOException("Root Cause");
        JsonMappingException e = new JsonMappingException("Test Exception", rootCause);
        assertEquals("Test Exception", e.getMessage());
        assertEquals(rootCause, e.getCause());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    /**
     * Test constructor with message, location and root cause.
     */
    @Test
    public void testJsonMappingExceptionWithMessageLocationAndRootCause() {
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        JsonMappingException e = new JsonMappingException("Test Exception", location, new IOException("Root Cause"));
        assertEquals("Test Exception", e.getMessage());
        assertEquals(location, e.getLocation());
        assertEquals(new IOException("Root Cause"), e.getCause());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    /**
     * Test constructor with processor, message and root cause.
     */
    @Test
    public void testJsonMappingExceptionWithProcessorMessageAndRootCause() {
        JsonMappingException e = new JsonMappingException(closeable, "Test Exception");
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    /**
     * Test constructor with processor, message, location and root cause.
     */
    @Test
    public void testJsonMappingExceptionWithProcessorMessageLocationAndRootCause() {
        JsonMappingException e = new JsonMappingException(closeable, "Test Exception", new JsonLocation(1, 1, "Test File", 10));
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNotNull(e.getProcessor());
    }

    /**
     * Test factory method fromJsonParser.
     */
    @Test
    public void testFromJsonParser() {
        JsonParser parser = mock(JsonParser.class);
        JsonMappingException e = JsonMappingException.from(parser, "Test Exception");
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNotNull(e.getProcessor());
        assertEquals(parser, e.getProcessor());
    }

    /**
     * Test factory method fromJsonParser with message.
     */
    @Test
    public void testFromJsonParserWithMessage() {
        JsonParser parser = mock(JsonParser.class);
        JsonMappingException e = JsonMappingException.from(parser, "Test Exception", "Root Cause");
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNotNull(e.getProcessor());
        assertEquals(parser, e.getProcessor());
    }

    /**
     * Test factory method fromJsonParser with location and message.
     */
    @Test
    public void testFromJsonParserWithLocationAndMessage() {
        JsonParser parser = mock(JsonParser.class);
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        JsonMappingException e = JsonMappingException.from(parser, "Test Exception", location, "Root Cause");
        assertEquals("Test Exception", e.getMessage());
        assertEquals(location, e.getLocation());
        assertNotNull(e.getProcessor());
        assertEquals(parser, e.getProcessor());
    }

    /**
     * Test factory method fromJsonGenerator.
     */
    @Test
    public void testFromJsonGenerator() {
        JsonGenerator generator = mock(JsonGenerator.class);
        JsonMappingException e = JsonMappingException.from(generator, "Test Exception");
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    /**
     * Test factory method fromJsonGenerator with message.
     */
    @Test
    public void testFromJsonGeneratorWithMessage() {
        JsonGenerator generator = mock(JsonGenerator.class);
        JsonMappingException e = JsonMappingException.from(generator, "Test Exception", new IOException("Root Cause"));
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getPath());
        assertNotNull(e.getProcessor());
        assertEquals(generator, e.getProcessor());
    }

    /**
     * Test factory method fromJsonGenerator with location and message.
     */
    @Test
    public void testFromJsonGeneratorWithLocationAndMessage() {
        JsonGenerator generator = mock(JsonGenerator.class);
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        JsonMappingException e = JsonMappingException.from(generator, "Test Exception", location, new IOException("Root Cause"));
        assertEquals("Test Exception", e.getMessage());
        assertEquals(location, e.getLocation());
        assertNotNull(e.getProcessor());
        assertEquals(generator, e.getProcessor());
    }

    /**
     * Test factory method fromUnexpectedIOException.
     */
    @Test
    public void testFromUnexpectedIOException() {
        IOException e = new IOException("Unexpected IOException");
        JsonMappingException ex = JsonMappingException.fromUnexpectedIOE(e);
        assertEquals("Unexpected IOException (of type IOException: Unexpected IOException)", ex.getMessage());
        assertNull(ex.getPath());
        assertNull(ex.getProcessor());
    }

    /**
     * Test method wrapWithPath.
     */
    @Test
    public void testWrapWithPath() {
        IOException e = new IOException("Test IOException");
        JsonParser parser = mock(JsonParser.class);
        Object refFrom = new Object();
        JsonMappingException jme = JsonMappingException.wrapWithPath(e, refFrom);
        assertEquals("Test IOException", jme.getMessage());
        assertNull(jme.getPath());
        assertNotNull(jme.getProcessor());
        assertEquals(parser, jme.getProcessor());

        Reference ref = new Reference(refFrom, "fieldName");
        jme = JsonMappingException.wrapWithPath(e, ref);
        List<Reference> path = jme.getPath();
        assertEquals(1, path.size());
        Reference ref0 = path.get(0);
        assertEquals(refFrom, ref0.getFrom());
        assertEquals("fieldName", ref0.getFieldName());
        assertNull(ref0.getIndex());
        assertNull(ref0.getDescription());

        ref = new Reference(refFrom, 0);
        jme = JsonMappingException.wrapWithPath(e, ref);
        path = jme.getPath();
        assertEquals(1, path.size());
        ref0 = path.get(0);
        assertEquals(refFrom, ref0.getFrom());
        assertNull(ref0.getFieldName());
        assertEquals(0, ref0.getIndex());
        assertNull(ref0.getDescription());
    }

    /**
     * Test method getPathReference.
     */
    @Test
    public void testGetPathReference() {
        JsonMappingException e = new JsonMappingException("Test Exception");
        StringBuilder sb = new StringBuilder();
        e.getPathReference(sb);
        assertEquals("Test Exception (through reference chain:)", sb.toString());

        Reference ref = new Reference(new Object(), "fieldName");
        e.prependPath(ref);
        sb = new StringBuilder();
        e.getPathReference(sb);
        assertEquals("Test Exception (through reference chain: Test File->fieldName->)", sb.toString());

        ref = new Reference(new Object(), 0);
        e.prependPath(ref);
        sb = new StringBuilder();
        e.getPathReference(sb);
        assertEquals("Test Exception (through reference chain: Test File->[0]->)", sb.toString());
    }
}