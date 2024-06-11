package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Unit tests for {@link JsonMappingException} class.
 */
public class JsonMappingExceptionTest {

    @Test
    public void testDefaultConstructor() {
        JsonMappingException e = new JsonMappingException("Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getPath());
    }

    @Test
    public void testFromCloseableConstructor() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException e = new JsonMappingException(processor, "Test message");
        assertEquals("Test message", e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertNull(e.getPath());
    }

    @Test
    public void testFromCloseableConstructorWithLocation() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonLocation loc = new JsonLocation(1, 2, 3);
        JsonMappingException e = new JsonMappingException(processor, "Test message", loc);
        assertEquals("Test message", e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertNull(e.getPath());
    }

    @Test
    public void testFromCloseableConstructorWithLocationAndCause() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonLocation loc = new JsonLocation(1, 2, 3);
        Throwable cause = new IOException("Cause message");
        JsonMappingException e = new JsonMappingException(processor, "Test message", loc, cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertEquals(cause, e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromParserConstructor() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        JsonMappingException e = JsonMappingException.from(parser, "Test message");
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getCause());
        assertNotNull(e.getPath());
    }

    @Test
    public void testFromParserConstructorWithLocation() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        JsonLocation loc = new JsonLocation(1, 2, 3);
        JsonMappingException e = JsonMappingException.from(parser, "Test message", loc);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertNull(e.getCause());
        assertNotNull(e.getPath());
    }

    @Test
    public void testFromParserConstructorWithLocationAndCause() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        JsonLocation loc = new JsonLocation(1, 2, 3);
        Throwable cause = new IOException("Cause message");
        JsonMappingException e = JsonMappingException.from(parser, "Test message", loc, cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertEquals(cause, e.getCause());
        assertNotNull(e.getPath());
    }

    @Test
    public void testFromGeneratorConstructor() {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        JsonMappingException e = JsonMappingException.from(generator, "Test message");
        assertEquals("Test message", e.getMessage());
        assertEquals(generator, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromGeneratorConstructorWithLocation() {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        JsonLocation loc = new JsonLocation(1, 2, 3);
        JsonMappingException e = JsonMappingException.from(generator, "Test message", loc);
        assertEquals("Test message", e.getMessage());
        assertEquals(generator, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertNull(e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromGeneratorConstructorWithLocationAndCause() {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        JsonLocation loc = new JsonLocation(1, 2, 3);
        Throwable cause = new IOException("Cause message");
        JsonMappingException e = JsonMappingException.from(generator, "Test message", loc, cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(generator, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertEquals(cause, e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromDeserializationContextConstructor() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        DeserializationContext ctxt = new JsonTestContext();
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getCause());
        assertNotNull(e.getPath());
    }

    @Test
    public void testFromDeserializationContextConstructorWithLocation() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        DeserializationContext ctxt = new JsonTestContext();
        JsonLocation loc = new JsonLocation(1, 2, 3);
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", loc);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertNull(e.getCause());
        assertNotNull(e.getPath());
    }

    @Test
    public void testFromDeserializationContextConstructorWithLocationAndCause() {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        DeserializationContext ctxt = new JsonTestContext();
        JsonLocation loc = new JsonLocation(1, 2, 3);
        Throwable cause = new IOException("Cause message");
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", loc, cause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertEquals(cause, e.getCause());
        assertNotNull(e.getPath());
    }

    @Test
    public void testFromSerializerProviderConstructor() {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider ctxt = new JsonTestProvider();
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message");
        assertEquals("Test message", e.getMessage());
        assertEquals(generator, e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromSerializerProviderConstructorWithLocation() {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider ctxt = new JsonTestProvider();
        JsonLocation loc = new JsonLocation(1, 2, 3);
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", loc);
        assertEquals("Test message", e.getMessage());
        assertEquals(generator, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertNull(e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromSerializerProviderConstructorWithLocationAndCause() {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider ctxt = new JsonTestProvider();
        JsonLocation loc = new JsonLocation(1, 2, 3);
        Throwable cause = new IOException("Cause message");
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", loc, cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(generator, e.getProcessor());
        assertEquals(loc, e.getLocation());
        assertEquals(cause, e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testFromUnexpectedIOEConstructor() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type " + ioException.getClass().getName() + "): " + ioException.getMessage(), e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getCause());
        assertNull(e.getPath());
    }

    @Test
    public void testWrapWithPath() {
        Object referrer = new Object();
        String refFieldName = "fieldName";
        Reference ref = new Reference(referrer, refFieldName);
        Throwable underlyingException = new IOException("Underlying exception message");
        JsonMappingException e = new JsonMappingException(underlyingException.getMessage());
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(underlyingException, referrer, refFieldName);
        assertEquals(wrappedException.getMessage(), e.getMessage() + " (through reference chain: " + ref.toString() + ")");
        assertEquals(e.getProcessor(), wrappedException.getProcessor());
        assertNotNull(wrappedException.getLocation());
        assertNotNull(wrappedException.getCause());
        assertEquals(underlyingException, wrappedException.getCause());
        assertEquals(e.getPath(), wrappedException.getPath());
    }

    @Test
    public void testWrapWithPathWithIndex() {
        Object referrer = new Object()[0];
        int refIndex = 0;
        Reference ref = new Reference(referrer, refIndex);
        Throwable underlyingException = new IOException("Underlying exception message");
        JsonMappingException e = new JsonMappingException(underlyingException.getMessage());
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(underlyingException, referrer, refIndex);
        assertEquals(wrappedException.getMessage(), e.getMessage() + " (through reference chain: " + ref.toString() + ")");
        assertEquals(e.getProcessor(), wrappedException.getProcessor());
        assertNotNull(wrappedException.getLocation());
        assertNotNull(wrappedException.getCause());
        assertEquals(underlyingException, wrappedException.getCause());
        assertEquals(e.getPath(), wrappedException.getPath());
    }

}

class JsonTestContext implements ContextualSerializer, ContextualDeserializer {
    @Override
    public JsonSerializer<Object> findValueSerializer(Class<?> type, JsonSerializer<Object> serializer) {
        return null;
    }

    @Override
    public JsonDeserializer<Object> findValueDeserializer(Class<?> type, JsonDeserializer<Object> deserializer) {
        return null;
    }
}

class JsonTestProvider implements SerializerProvider, DeserializationConfig {
    @Override
    public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
        return null;
    }

    @Override
    public JsonDeserializer<?> findValueDeserializer(Class<?> type, JsonDeserializer<?> deserializer) {
        return null;
    }

    @Override
    public void configure(DeserializationConfig config, DeserializationFeature feature) {
    }

    @Override
    public void configure(SerializerConfig config, SerializerFeature feature) {
    }
}