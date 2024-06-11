package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    

    @Test
    public void testJsonMappingExceptionConstructorWithMessage() {
        JsonMappingException jsonMappingException = new JsonMappingException("Test message");
        assertEquals("Test message", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithMessageAndRootCause() {
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = new JsonMappingException("Test message", rootCause);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getClass(), jsonMappingException.getCause().getClass());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithMessageAndLocation() {
        JsonLocation location = mock(JsonLocation.class);
        JsonMappingException jsonMappingException = new JsonMappingException("Test message", location);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getLocation());
        assertEquals(location.getClass(), jsonMappingException.getLocation().getClass());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithMessageLocationAndRootCause() {
        JsonLocation location = mock(JsonLocation.class);
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = new JsonMappingException("Test message", location, rootCause);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getLocation());
        assertEquals(location.getClass(), jsonMappingException.getLocation().getClass());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getClass(), jsonMappingException.getCause().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonMappingExceptionConstructorWithNullMessage() {
        new JsonMappingException(null);
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessage() {
        JsonParser jsonParser = mock(JsonParser.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Test message");
        assertEquals("Test message", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageAndRootCause() {
        JsonParser jsonParser = mock(JsonParser.class);
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Test message", rootCause);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getClass(), jsonMappingException.getCause().getClass());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageLocation() {
        JsonParser jsonParser = mock(JsonParser.class);
        JsonLocation location = mock(JsonLocation.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Test message", location);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getLocation());
        assertEquals(location.getClass(), jsonMappingException.getLocation().getClass());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageLocationAndRootCause() {
        JsonParser jsonParser = mock(JsonParser.class);
        JsonLocation location = mock(JsonLocation.class);
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonParser, "Test message", location, rootCause);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getLocation());
        assertEquals(location.getClass(), jsonMappingException.getLocation().getClass());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getClass(), jsonMappingException.getCause().getClass());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithMessage() {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, "Test message");
        assertEquals("Test message", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithMessageAndRootCause() {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(jsonGenerator, "Test message", rootCause);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getClass(), jsonMappingException.getCause().getClass());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContextWithMessage() {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(deserializationContext, "Test message");
        assertEquals("Test message", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContextWithMessageAndRootCause() {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = JsonMappingException.from(deserializationContext, "Test message", rootCause);
        assertEquals("Test message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getClass(), jsonMappingException.getCause().getClass());
    }

    @Test
    public void testJsonMappingExceptionFromInvalidIOException() {
        IOException rootCause = mock(IOException.class);
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(rootCause);
        assertNotNull(jsonMappingException);
        assertNull(jsonMappingException.getLocation());
        assertNotNull(jsonMappingException.getCause());
        assertEquals(rootCause.getMessage(), jsonMappingException.getMessage());
        assertEquals(rootCause.getClass(), jsonMappingException.getClass());
    }

    @Test
    public void testJsonMappingExceptionWrapWithPath() {
        Object referrer = new Object();
        String fieldName = "fieldName";
        JsonProcessingException jsonProcessingException = new JsonProcessingException(referrer, "Test message");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(jsonProcessingException, referrer, fieldName);
        List<Reference> path = jsonMappingException.getPath();
        assertNotNull(path);
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());
        assertEquals(JsonMappingException.class, jsonMappingException.getClass());
    }

    @Test
    public void testJsonMappingExceptionWrapWithPathWithIndex() {
        Object referrer = new Object();
        int index = 0;
        JsonProcessingException jsonProcessingException = new JsonProcessingException(referrer, "Test message");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(jsonProcessingException, referrer, index);
        List<Reference> path = jsonMappingException.getPath();
        assertNotNull(path);
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(-1, reference.getIndex());
        assertEquals(JsonMappingException.class, jsonMappingException.getClass());
    }

    @Test
    public void testJsonMappingExceptionGetPath() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test message");
        JsonMappingException jsonMappingException = new JsonMappingException(jsonProcessingException);
        List<Reference> path = jsonMappingException.getPath();
        assertNotNull(path);
        assertEquals(0, path.size());

        JsonProcessor jsonProcessor = mock(JsonProcessor.class);
        JsonMappingException jsonMappingExceptionWithPath = new JsonMappingException(jsonProcessor, "Test message");
        jsonMappingExceptionWithPath.prependPath(new Reference(new Object(), "fieldName"));
        path = jsonMappingExceptionWithPath.getPath();
        assertNotNull(path);
        assertEquals(1, path.size());
    }

    @Test
    public void testJsonMappingExceptionGetPathReference() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test message");
        JsonMappingException jsonMappingException = new JsonMappingException(jsonProcessingException);
        StringBuilder sb = new StringBuilder();
        jsonMappingException.getPathReference(sb);
        assertEquals("JsonMappingException[JsonProcessingException: Test message] (through reference chain: [])", sb.toString());

        JsonProcessor jsonProcessor = mock(JsonProcessor.class);
        JsonMappingException jsonMappingExceptionWithPath = new JsonMappingException(jsonProcessor, "Test message");
        jsonMappingExceptionWithPath.prependPath(new Reference(new Object(), "fieldName"));
        sb = new StringBuilder();
        jsonMappingExceptionWithPath.getPathReference(sb);
        assertEquals("JsonMappingException[JsonProcessingException: Test message] (through reference chain: [\"fieldName\"])", sb.toString());
    }

    @Test
    public void testJsonMappingExceptionGetProcessor() {
        Closeable processor = mock(Closeable.class);
        JsonMappingException jsonMappingException = new JsonMappingException(processor, "Test message");
        assertEquals(processor, jsonMappingException.getProcessor());
    }

    
}