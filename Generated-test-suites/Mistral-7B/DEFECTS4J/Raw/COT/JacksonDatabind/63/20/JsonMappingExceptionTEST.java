package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() {
        JsonMappingException exception = new JsonMappingException();
        assertNotNull(exception.getMessage());
        assertNull(exception.getFrom());
        assertNull(exception.getLocation());

        exception = new JsonMappingException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getFrom());
        assertNull(exception.getLocation());
    }

    @Test
    public void testWithProcessor() {
        Closeable processor = mapper.getFactory().createParser("[]");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(processor, exception.getProcessor());
        assertNull(exception.getLocation());
    }

    @Test
    public void testWithProcessorAndLocation() {
        Closeable processor = mapper.getFactory().createParser("[]");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", new JsonLocation(1, 1));
        assertEquals("Test Message", exception.getMessage());
        assertEquals(processor, exception.getProcessor());
        assertEquals(new JsonLocation(1, 1), exception.getLocation());
    }

    @Test
    public void testWithProcessorAndRootCause() {
        Closeable processor = mapper.getFactory().createParser("[]");
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(processor, exception.getProcessor());
        assertEquals(rootCause, exception.getRootCause());
        assertNull(exception.getLocation());
    }

    @Test
    public void testWithProcessorAndLocationAndRootCause() {
        Closeable processor = mapper.getFactory().createParser("[]");
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", new JsonLocation(1, 1), rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(processor, exception.getProcessor());
        assertEquals(new JsonLocation(1, 1), exception.getLocation());
        assertEquals(rootCause, exception.getRootCause());
    }

    @Test
    public void testFromJsonParser() {
        Closeable parser = mapper.getFactory().createParser("[1,2,3]");
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(parser, exception.getProcessor());
        assertNull(exception.getLocation());
    }

    @Test
    public void testFromJsonParserWithLocation() {
        Closeable parser = mapper.getFactory().createParser("[1,2,3]");
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message", new JsonLocation(1, 1));
        assertEquals("Test Message", exception.getMessage());
        assertEquals(parser, exception.getProcessor());
        assertEquals(new JsonLocation(1, 1), exception.getLocation());
    }

    @Test
    public void testFromJsonParserWithRootCause() {
        Closeable parser = mapper.getFactory().createParser("[1,2,3]");
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message", rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(parser, exception.getProcessor());
        assertEquals(rootCause, exception.getRootCause());
        assertNull(exception.getLocation());
    }

    @Test
    public void testFromJsonParserWithLocationAndRootCause() {
        Closeable parser = mapper.getFactory().createParser("[1,2,3]");
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message", new JsonLocation(1, 1), rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(parser, exception.getProcessor());
        assertEquals(new JsonLocation(1, 1), exception.getLocation());
        assertEquals(rootCause, exception.getRootCause());
    }

    @Test
    public void testFromJsonGenerator() {
        Closeable generator = mapper.getFactory().createGenerator(System.out);
        JsonMappingException exception = JsonMappingException.from(generator, "Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(generator, exception.getProcessor());
        assertNull(exception.getLocation());
    }

    @Test
    public void testFromJsonGeneratorWithLocation() {
        Closeable generator = mapper.getFactory().createGenerator(System.out);
        JsonMappingException exception = JsonMappingException.from(generator, "Test Message", new JsonLocation(1, 1));
        assertEquals("Test Message", exception.getMessage());
        assertEquals(generator, exception.getProcessor());
        assertEquals(new JsonLocation(1, 1), exception.getLocation());
    }

    @Test
    public void testFromJsonGeneratorWithRootCause() {
        Closeable generator = mapper.getFactory().createGenerator(System.out);
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(generator, "Test Message", rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(generator, exception.getProcessor());
        assertEquals(rootCause, exception.getRootCause());
        assertNull(exception.getLocation());
    }

    @Test
    public void testFromJsonGeneratorWithLocationAndRootCause() {
        Closeable generator = mapper.getFactory().createGenerator(System.out);
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(generator, "Test Message", new JsonLocation(1, 1), rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(generator, exception.getProcessor());
        assertEquals(new JsonLocation(1, 1), exception.getLocation());
        assertEquals(rootCause, exception.getRootCause());
    }

    @Test
    public void testFromUnexpectedIOException() {
        IOException unexpectedIoException = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(unexpectedIoException);
        assertEquals("Unexpected IOException (of type IOException): Test IOException", exception.getMessage());
        assertNull(exception.getProcessor());
        assertNull(exception.getLocation());
        assertNull(exception.getRootCause());
    }

    @Test
    public void testWrapWithPath_ObjectRef() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        Throwable throwable = new RuntimeException("Test Exception");

        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(throwable, refFrom, refFieldName);
        Reference reference = jsonMappingException.getPath().get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
        assertEquals("Unknown", jsonMappingException.getPathReference());
    }

    @Test
    public void testWrapWithPath_ArrayIndex() {
        Object refFrom = new Object()[0];
        int index = 0;
        Throwable throwable = new RuntimeException("Test Exception");

        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(throwable, refFrom, index);
        Reference reference = jsonMappingException.getPath().get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(index, reference.getIndex());
        assertEquals("Unknown[0]", jsonMappingException.getPathReference());
    }

    @Test
    public void testWrapWithPath_List() {
        List<Object> refFrom = Arrays.asList(new Object(), new Object());
        String refFieldName = "fieldName";
        Throwable throwable = new RuntimeException("Test Exception");

        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(throwable, refFrom, refFieldName);
        List<Reference> references = jsonMappingException.getPath();
        assertEquals(2, references.size());
        Reference reference0 = references.get(0);
        Reference reference1 = references.get(1);
        assertEquals(refFrom.get(0), reference0.getFrom());
        assertEquals(refFieldName, reference0.getFieldName());
        assertEquals(refFrom.get(1), reference1.getFrom());
        assertEquals("-", jsonMappingException.getPathReference());
    }

    @Test
    public void testGetPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        Throwable throwable = new RuntimeException("Test Exception");

        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(throwable, refFrom, refFieldName);
        List<Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        Reference reference = references.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());

        jsonMappingException = JsonMappingException.wrapWithPath(throwable, refFrom, refFieldName, new Object());
        references = jsonMappingException.getPath();
        assertEquals(2, references.size());
        reference = references.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
        reference = references.get(1);
        assertEquals(new Object(), reference.getFrom());
        assertEquals("-", jsonMappingException.getPathReference());
    }
}