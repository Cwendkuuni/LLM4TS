package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class JsonMappingExceptionTest {

    @Test
    public void testDefaultConstructor() {
        JsonMappingException exception = new JsonMappingException();
        assertNotNull(exception.getMessage());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testConstructorWithMessage() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        Throwable cause = new IOException("Test IO Exception");
        JsonMappingException exception = new JsonMappingException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testConstructorWithMessageLocation() {
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        JsonMappingException exception = new JsonMappingException("Test Message", location);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertNull(exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testConstructorWithMessageLocationAndCause() {
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        Throwable cause = new IOException("Test IO Exception");
        JsonMappingException exception = new JsonMappingException("Test Message", location, cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testConstructorWithProcessorAndMessage() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException exception = new JsonMappingException(processor, "Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getPath());
        assertEquals(processor, exception.getProcessor());
    }

    @Test
    public void testConstructorWithProcessorMessageAndCause() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        Throwable cause = new IOException("Test IO Exception");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getPath());
        assertEquals(processor, exception.getProcessor());
    }

    @Test
    public void testConstructorWithProcessorMessageLocation() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", location);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertNull(exception.getCause());
        assertNull(exception.getPath());
        assertEquals(processor, exception.getProcessor());
    }

    @Test
    public void testConstructorWithProcessorMessageLocationAndCause() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        Throwable cause = new IOException("Test IO Exception");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", location, cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getPath());
        assertEquals(processor, exception.getProcessor());
    }

    @Test
    public void testFromJsonParser() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonParserWithCause() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        Throwable cause = new IOException("Test IO Exception");
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonParserWithLocation() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message", location);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertNull(exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonParserWithLocationAndCause() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        JsonLocation location = new JsonLocation(1, 1, "Test File", 10);
        Throwable cause = new IOException("Test IO Exception");
        JsonMappingException exception = JsonMappingException.from(parser, "Test Message", location, cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonGenerator() {
        JsonMappingException exception = JsonMappingException.from(new ObjectMapper(), "Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonGeneratorWithCause() {
        JsonMappingException exception = JsonMappingException.from(new ObjectMapper(), "Test Message", new IOException("Test IO Exception"));
        assertEquals("Test Message", exception.getMessage());
        assertEquals(exception.getCause(), new IOException("Test IO Exception"));
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonGeneratorWithLocation() {
        JsonMappingException exception = JsonMappingException.from(new ObjectMapper(), "Test Message", new JsonLocation(1, 1, "Test File", 10));
        assertEquals("Test Message", exception.getMessage());
        assertEquals(exception.getLocation(), new JsonLocation(1, 1, "Test File", 10));
        assertNull(exception.getCause());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testFromJsonGeneratorWithLocationAndCause() {
        JsonMappingException exception = JsonMappingException.from(new ObjectMapper(), "Test Message", new JsonLocation(1, 1, "Test File", 10), new IOException("Test IO Exception"));
        assertEquals("Test Message", exception.getMessage());
        assertEquals(exception.getLocation(), new JsonLocation(1, 1, "Test File", 10));
        assertEquals(exception.getCause(), new IOException("Test IO Exception"));
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
    }

    @Test
    public void testWrapWithPath() {
        Object referrer = new LinkedHashMap<>();
        JsonMappingException originalException = new JsonMappingException("Test Original Message");
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, "Test Field");
        assertEquals("Test Original Message (through reference chain: Test Field->Test Object->JsonMappingException[Test Original Message])", wrappedException.getMessage());
        assertEquals(Collections.singletonList(new Reference(referrer, "Test Field")), wrappedException.getPath());
        assertNull(wrappedException.getProcessor());
        assertEquals(originalException, wrappedException.getCause());

        JsonMappingException originalException2 = new JsonMappingException("Test Original Message", new IOException("Test IO Exception"));
        JsonMappingException wrappedException2 = JsonMappingException.wrapWithPath(originalException2, referrer, "Test Field");
        assertEquals("Test Original Message (through reference chain: Test Field->Test Object->JsonMappingException[Test Original Message (was IOException[Test IO Exception])])", wrappedException2.getMessage());
        assertEquals(Collections.singletonList(new Reference(referrer, "Test Field")), wrappedException2.getPath());
        assertNull(wrappedException2.getProcessor());
        assertEquals(originalException2, wrappedException2.getCause());

        JsonMappingException originalException3 = new JsonMappingException("Test Original Message", new IOException("Test IO Exception"), new JsonLocation(1, 1, "Test File", 10));
        JsonMappingException wrappedException3 = JsonMappingException.wrapWithPath(originalException3, referrer, "Test Field");
        assertEquals("Test Original Message (through reference chain: Test Field->Test Object->JsonMappingException[Test Original Message (was IOException[Test IO Exception] at Test File:1:10)])", wrappedException3.getMessage());
        assertEquals(Collections.singletonList(new Reference(referrer, "Test Field")), wrappedException3.getPath());
        assertNull(wrappedException3.getProcessor());
        assertEquals(originalException3, wrappedException3.getCause());
    }

    @Test
    public void testWrapWithPathWithIndex() {
        Object[] referrer = {new LinkedHashMap<>(), new Integer(1)};
        JsonMappingException originalException = new JsonMappingException("Test Original Message");
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(originalException, referrer, 0);
        assertEquals("Test Original Message (through reference chain: [0]->Test Object->JsonMappingException[Test Original Message])", wrappedException.getMessage());
        assertEquals(Arrays.asList(new Reference(referrer[0], null), new Reference(referrer[1], 0)), wrappedException.getPath());
        assertNull(wrappedException.getProcessor());
        assertEquals(originalException, wrappedException.getCause());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullReferrer() {
        JsonMappingException.wrapWithPath(new JsonMappingException("Test Original Message"), null, "Test Field");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFieldName() {
        JsonMappingException.wrapWithPath(new JsonMappingException("Test Original Message"), new LinkedHashMap<>(), null);
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("Test IO Exception");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Test IO Exception", exception.getMessage());
        assertNull(exception.getPath());
        assertNull(exception.getProcessor());
        assertEquals(ioException, exception.getCause());
    }
}