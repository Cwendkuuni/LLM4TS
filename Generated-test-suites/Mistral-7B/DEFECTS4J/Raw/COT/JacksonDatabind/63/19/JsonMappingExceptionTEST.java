package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for JsonMappingException.
 */
@RunWith(JUnit4.class)
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionDefaultConstructor() {
        JsonMappingException jsonMappingException = new JsonMappingException("Default message");
        assertEquals("Default message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithMessage() {
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor");
        assertEquals("Message with processor", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndLocation() {
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", new JsonLocation(1, 1, "test.json"));
        assertEquals("Message with processor", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndLocationAndCause() {
        IOException ioException = new IOException("IO error message");
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", new JsonLocation(1, 1, "test.json"), ioException);
        assertEquals("Message with processor: IO error message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
        assertEquals(ioException.getClass(), jsonMappingException.getCause().getClass());
        assertEquals(ioException.getMessage(), jsonMappingException.getCause().getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonMappingExceptionWithNullMessage() {
        new JsonMappingException(new JsonProcessor(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testJsonMappingExceptionWithNullProcessor() {
        new JsonMappingException(null, "Message with null processor");
    }

    @Test
    public void testJsonMappingExceptionWithReference() {
        Object refFrom = new Object();
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", new Reference(refFrom, "fieldName"));
        assertEquals("Message with processor", jsonMappingException.getMessage());
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals("fieldName", reference.getFieldName());
    }

    @Test
    public void testJsonMappingExceptionWithReferenceAndIndex() {
        Object refFrom = new Object();
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", new Reference(refFrom, 0));
        assertEquals("Message with processor", jsonMappingException.getMessage());
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(0, reference.getIndex());
    }

    @Test
    public void testJsonMappingExceptionWithMaxReferences() {
        Object refFrom = new Object();
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", new Reference(refFrom, i % 2 == 0 ? "fieldName" : null));
        }
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", new Reference(refFrom, "fieldName"));
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(JsonMappingException.MAX_REFS_TO_LIST, path.size());
    }

    @Test
    public void testJsonMappingExceptionWithNullReference() {
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), "Message with processor", null);
        assertEquals("Message with processor", jsonMappingException.getMessage());
        List<Reference> path = jsonMappingException.getPath();
        assertNotNull(path);
        assertEquals(0, path.size());
    }

    @Test
    public void testJsonMappingExceptionWithCustomMessage() {
        String customMessage = "Test custom message";
        JsonMappingException jsonMappingException = new JsonMappingException(new JsonProcessor(), customMessage);
        assertEquals(customMessage, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithIOException() {
        IOException ioException = new IOException("IO error message");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): IO error message", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithJsonParser() {
        JsonParser jsonParser = new JsonFactory().createParser(getClass().getResource("/test.json"));
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Message with parser");
        assertEquals("Message with parser", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonParserAndMessage() {
        JsonParser jsonParser = new JsonFactory().createParser(getClass().getResource("/test.json"));
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Message with parser: test message");
        assertEquals("Message with parser: test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonParserAndLocation() {
        JsonParser jsonParser = new JsonFactory().createParser(getClass().getResource("/test.json"));
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Message with parser", new JsonLocation(1, 1, "test.json"));
        assertEquals("Message with parser", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
    }

    @Test
    public void testJsonMappingExceptionWithJsonParserAndMessageAndCause() {
        JsonParser jsonParser = new JsonFactory().createParser(getClass().getResource("/test.json"));
        IOException ioException = new IOException("IO error message");
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Message with parser", new JsonLocation(1, 1, "test.json"), ioException);
        assertEquals("Message with parser: IO error message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
        assertEquals(ioException.getClass(), jsonMappingException.getCause().getClass());
        assertEquals(ioException.getMessage(), jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithJsonGenerator() {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(getClass().getOutputStream());
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, "Message with generator");
        assertEquals("Message with generator", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonGeneratorAndMessage() {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(getClass().getOutputStream());
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, "Message with generator: test message");
        assertEquals("Message with generator: test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonGeneratorAndLocation() {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(getClass().getOutputStream());
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, "Message with generator", new JsonLocation(1, 1, "test.json"));
        assertEquals("Message with generator", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
    }

    @Test
    public void testJsonMappingExceptionWithJsonGeneratorAndMessageAndCause() {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(getClass().getOutputStream());
        IOException ioException = new IOException("IO error message");
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, "Message with generator", new JsonLocation(1, 1, "test.json"), ioException);
        assertEquals("Message with generator: IO error message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
        assertEquals(ioException.getClass(), jsonMappingException.getCause().getClass());
        assertEquals(ioException.getMessage(), jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithDeserializationContext() {
        DeserializationContext ctxt = new MinimalDeserializationContext();
        JsonMappingException jsonMappingException = JsonMappingException.from(ctxt, "Message with context");
        assertEquals("Message with context", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithDeserializationContextAndMessage() {
        DeserializationContext ctxt = new MinimalDeserializationContext();
        JsonMappingException jsonMappingException = JsonMappingException.from(ctxt, "Message with context: test message");
        assertEquals("Message with context: test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithDeserializationContextAndLocation() {
        DeserializationContext ctxt = new MinimalDeserializationContext();
        JsonMappingException jsonMappingException = JsonMappingException.from(ctxt, "Message with context", new JsonLocation(1, 1, "test.json"));
        assertEquals("Message with context", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
    }

    @Test
    public void testJsonMappingExceptionWithDeserializationContextAndMessageAndCause() {
        DeserializationContext ctxt = new MinimalDeserializationContext();
        IOException ioException = new IOException("IO error message");
        JsonMappingException jsonMappingException = JsonMappingException.from(ctxt, "Message with context", new JsonLocation(1, 1, "test.json"), ioException);
        assertEquals("Message with context: IO error message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
        assertEquals(ioException.getClass(), jsonMappingException.getCause().getClass());
        assertEquals(ioException.getMessage(), jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithSerializerProvider() {
        SerializerProvider serializerProvider = new MinimalSerializerProvider();
        JsonMappingException jsonMappingException = JsonMappingException.from(serializerProvider, "Message with provider");
        assertEquals("Message with provider", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithSerializerProviderAndMessage() {
        SerializerProvider serializerProvider = new MinimalSerializerProvider();
        JsonMappingException jsonMappingException = JsonMappingException.from(serializerProvider, "Message with provider: test message");
        assertEquals("Message with provider: test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithSerializerProviderAndLocation() {
        SerializerProvider serializerProvider = new MinimalSerializerProvider();
        JsonMappingException jsonMappingException = JsonMappingException.from(serializerProvider, "Message with provider", new JsonLocation(1, 1, "test.json"));
        assertEquals("Message with provider", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
    }

    @Test
    public void testJsonMappingExceptionWithSerializerProviderAndMessageAndCause() {
        SerializerProvider serializerProvider = new MinimalSerializerProvider();
        IOException ioException = new IOException("IO error message");
        JsonMappingException jsonMappingException = JsonMappingException.from(serializerProvider, "Message with provider", new JsonLocation(1, 1, "test.json"), ioException);
        assertEquals("Message with provider: IO error message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getPath());
        assertEquals(1, jsonMappingException.getLocation().getLineNumber());
        assertEquals("test.json", jsonMappingException.getLocation().getFile());
        assertEquals(ioException.getClass(), jsonMappingException.getCause().getClass());
        assertEquals(ioException.getMessage(), jsonMappingException.getCause().getMessage());
    }
}