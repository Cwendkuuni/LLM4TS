package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link JsonMappingException} class.
 */
public class JsonMappingExceptionTest {

    @Test
    public void testBasicConstruction() {
        JsonMappingException e = new JsonMappingException("Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertEquals(JsonMappingException.class, e.getClass());
    }

    @Test
    public void testConstructorWithProcessorAndMessage() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException e = new JsonMappingException(processor, "Test message");
        assertEquals("Test message", e.getMessage());
        assertSame(processor, e.getProcessor());
        assertEquals(JsonMappingException.class, e.getClass());
    }

    @Test
    public void testConstructorWithProcessorMessageAndLocation() {
        JsonLocation location = new JsonLocation();
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException e = new JsonMappingException(processor, "Test message", location);
        assertEquals("Test message", e.getMessage());
        assertSame(processor, e.getProcessor());
        assertEquals(location, e.getLocation());
    }

    @Test
    public void testConstructorWithProcessorMessageThrowable() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        Throwable cause = new IOException("Test IO exception");
        JsonMappingException e = new JsonMappingException(processor, "Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertSame(processor, e.getProcessor());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testConstructorWithProcessorMessageLocationThrowable() {
        JsonLocation location = new JsonLocation();
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        Throwable cause = new IOException("Test IO exception");
        JsonMappingException e = new JsonMappingException(processor, "Test message", location, cause);
        assertEquals("Test message", e.getMessage());
        assertSame(processor, e.getProcessor());
        assertEquals(location, e.getLocation());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testDeprecatedConstructors() {
        try {
            new JsonMappingException("Test message");
            fail("Should not be possible to instantiate with deprecated constructor");
        } catch (ExceptionInInitializerError e) {
            // Expected
        }

        try {
            new JsonMappingException("Test message", new IOException(""));
            fail("Should not be possible to instantiate with deprecated constructor");
        } catch (ExceptionInInitializerError e) {
            // Expected
        }

        try {
            new JsonMappingException("Test message", new JsonLocation());
            fail("Should not be possible to instantiate with deprecated constructor");
        } catch (ExceptionInInitializerError e) {
            // Expected
        }

        try {
            new JsonMappingException("Test message", new JsonLocation(), new IOException(""));
            fail("Should not be possible to instantiate with deprecated constructor");
        } catch (ExceptionInInitializerError e) {
            // Expected
        }
    }

    @Test
    public void testFromJsonParser() {
        JsonParser parser = new DefaultParser();
        JsonMappingException e = JsonMappingException.from(parser, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(parser, e.getParser());
    }

    @Test
    public void testFromJsonParserWithThrowable() {
        JsonParser parser = new DefaultParser();
        Throwable cause = new IOException("Test IO exception");
        JsonMappingException e = JsonMappingException.from(parser, "Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(parser, e.getParser());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testFromJsonGenerator() {
        JsonGenerator generator = new DefaultGenerator();
        JsonMappingException e = JsonMappingException.from(generator, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(generator, e.getGenerator());
    }

    @Test
    public void testFromJsonGeneratorWithThrowable() {
        JsonGenerator generator = new DefaultGenerator();
        Throwable cause = new IOException("Test IO exception");
        JsonMappingException e = JsonMappingException.from(generator, "Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(generator, e.getGenerator());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testFromDeserializationContext() {
        DeserializationContext ctxt = new MinimalDeserializationContext();
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(ctxt.getParser(), e.getParser());
    }

    @Test
    public void testFromDeserializationContextWithThrowable() {
        DeserializationContext ctxt = new MinimalDeserializationContext();
        Throwable cause = new IOException("Test IO exception");
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(ctxt.getParser(), e.getParser());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testFromSerializerProvider() {
        SerializerProvider ctxt = new MinimalSerializerProvider();
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(ctxt.getGenerator(), e.getGenerator());
    }

    @Test
    public void testFromSerializerProviderWithThrowable() {
        SerializerProvider ctxt = new MinimalSerializerProvider();
        Throwable cause = new IOException("Test IO exception");
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertSame(ctxt.getGenerator(), e.getGenerator());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testFromUnexpectedIOException() {
        IOException src = new IOException("Test IO exception");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(src);
        assertEquals("Unexpected IOException (of type " + src.getClass().getName() + "): Test IO exception", e.getMessage());
        assertNull(e.getProcessor());
    }

    @Test
    public void testWrapWithPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        Object referrer = new Object();
        JsonMappingException e1 = new JsonMappingException("Test message");
        Reference ref1 = new Reference(referrer, refFieldName);
        JsonMappingException e2 = JsonMappingException.wrapWithPath(e1, refFrom, refFieldName);
        assertEquals("Test message (through reference chain: " + ref1.toString() + ")", e2.getMessage());

        int index = 0;
        JsonMappingException e3 = new JsonMappingException("Test message");
        Reference ref3 = new Reference(referrer, index);
        JsonMappingException e4 = JsonMappingException.wrapWithPath(e3, refFrom, index);
        assertEquals("Test message (through reference chain: " + ref3.toString() + ")", e4.getMessage());
    }

    @Test
    public void testPrependPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException e1 = new JsonMappingException("Test message");
        Reference ref1 = new Reference(refFrom, refFieldName);
        JsonMappingException e2 = new JsonMappingException("Test message");
        e2.prependPath(ref1);
        assertEquals("Test message (through reference chain: " + ref1.toString() + "->" + e1.getMessage() + ")", e2.getMessage());

        int index = 0;
        JsonMappingException e3 = new JsonMappingException("Test message");
        Reference ref3 = new Reference(refFrom, index);
        JsonMappingException e4 = new JsonMappingException("Test message");
        e4.prependPath(ref3);
        assertEquals("Test message (through reference chain: " + ref3.toString() + "->" + e4.getMessage() + ")", e4.getMessage());
    }

    @Test
    public void testGetPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException e = new JsonMappingException("Test message");
        Reference ref1 = new Reference(refFrom, refFieldName);
        e.prependPath(ref1);

        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref1, path.get(0));
    }

    @Test
    public void testGetPathReference() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException e = new JsonMappingException("Test message");
        Reference ref1 = new Reference(refFrom, refFieldName);
        e.prependPath(ref1);

        StringBuilder sb = new StringBuilder();
        assertEquals("Test message (through reference chain: " + ref1.toString() + ")", e.getPathReference(sb).toString());
    }
}

class MinimalDeserializationContext implements DeserializationContext {
    public JsonParser getParser() {
        return new DefaultParser();
    }
}

class MinimalSerializerProvider implements SerializerProvider {
    public JsonGenerator getGenerator() {
        return new DefaultGenerator();
    }
}