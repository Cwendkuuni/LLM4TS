package com.example.jsonmappingexception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class JsonMappingExceptionTest {

    @Mock
    private Closeable processor;

    @Mock
    private JsonParser parser;

    @Mock
    private JsonGenerator generator;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private SerializerProvider serializationProvider;

    private JsonMappingException jsonMappingException;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jsonMappingException = new JsonMappingException(processor, "Test Message");
    }

    @Test
    public void testJsonMappingExceptionWithMessage() {
        assertEquals("Test Message", jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndThrowable() {
        Throwable throwable = new IOException("Test IOException");
        jsonMappingException = new JsonMappingException(processor, "Test Message", throwable);
        assertEquals("Test Message", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndLocation() {
        JsonLocation location = new JsonLocation(1, 1, "Test File");
        jsonMappingException = new JsonMappingException(processor, "Test Message", location);
        assertEquals("Test Message\n[1, 1] Test File", jsonMappingException.getLocalizedMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndLocationAndThrowable() {
        JsonLocation location = new JsonLocation(1, 1, "Test File");
        Throwable throwable = new IOException("Test IOException");
        jsonMappingException = new JsonMappingException(processor, "Test Message", location, throwable);
        assertEquals("Test Message\n[1, 1] Test File", jsonMappingException.getLocalizedMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParser() {
        String msg = "Test Message";
        jsonMappingException = JsonMappingException.from(parser, msg);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertEquals(parser, jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserWithThrowable() {
        String msg = "Test Message";
        Throwable throwable = new IOException("Test IOException");
        jsonMappingException = JsonMappingException.from(parser, msg, throwable);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertEquals(parser, jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGenerator() {
        String msg = "Test Message";
        jsonMappingException = JsonMappingException.from(generator, msg);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertEquals(generator, jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithThrowable() {
        String msg = "Test Message";
        Throwable throwable = new IOException("Test IOException");
        jsonMappingException = JsonMappingException.from(generator, msg, throwable);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertEquals(generator, jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContext() {
        String msg = "Test Message";
        jsonMappingException = JsonMappingException.from(deserializationContext, msg);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertNull(jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromDeserializationContextWithThrowable() {
        String msg = "Test Message";
        Throwable throwable = new IOException("Test IOException");
        jsonMappingException = JsonMappingException.from(deserializationContext, msg, throwable);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertNull(jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromSerializerProvider() {
        String msg = "Test Message";
        jsonMappingException = JsonMappingException.from(serializationProvider, msg);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertNull(jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromSerializerProviderWithThrowable() {
        String msg = "Test Message";
        Throwable throwable = new IOException("Test IOException");
        jsonMappingException = JsonMappingException.from(serializationProvider, msg, throwable);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertNull(jsonMappingException.getProcessor());
        assertEquals(msg, jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOException() {
        IOException ioException = new IOException("Test IOException");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals(JsonProcessingException.Class, jsonMappingException.getClass().getSuperclass());
        assertNull(jsonMappingException.getProcessor());
        assertEquals("Unexpected IOException (of type " + ioException.getClass().getName() + "): " + ioException.getMessage(), jsonMappingException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPath() throws IOException {
        Object referrer = new Object();
        String fieldName = "testField";
        List<Object> list = Arrays.asList(referrer, new Object(), new Object());
        int index = 1;
        Reference reference = new JsonMappingException.Reference(referrer, fieldName);
        JsonMappingException jsonMappingException = new JsonMappingException(processor, "Test Message");
        jsonMappingException.prependPath(reference);
        jsonMappingException.prependPath(list.get(index));
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(2, path.size());
        assertEquals(reference, path.get(0));
        assertEquals(list.get(index), path.get(1));
    }

    @Test
    public void testJsonMappingExceptionWithPathAndThrowable() throws IOException {
        Object referrer = new Object();
        String fieldName = "testField";
        List<Object> list = Arrays.asList(referrer, new Object(), new Object());
        int index = 1;
        Reference reference = new JsonMappingException.Reference(referrer, fieldName);
        Throwable throwable = new IOException("Test IOException");
        JsonMappingException jsonMappingException = new JsonMappingException(processor, "Test Message");
        jsonMappingException.prependPath(reference);
        jsonMappingException.prependPath(list.get(index));
        jsonMappingException.initCause(throwable);
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(2, path.size());
        assertEquals(reference, path.get(0));
        assertEquals(list.get(index), path.get(1));
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocation() throws IOException {
        Object referrer = new Object();
        String fieldName = "testField";
        List<Object> list = Arrays.asList(referrer, new Object(), new Object());
        int index = 1;
        Reference reference = new JsonMappingException.Reference(referrer, fieldName);
        JsonLocation location = new JsonLocation(1, 1, "Test File");
        JsonMappingException jsonMappingException = new JsonMappingException(processor, "Test Message");
        jsonMappingException.prependPath(reference);
        jsonMappingException.prependPath(list.get(index));
        jsonMappingException.setLocation(location);
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(2, path.size());
        assertEquals(reference, path.get(0));
        assertEquals(list.get(index), path.get(1));
        assertEquals(location, jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndLocationAndThrowable() throws IOException {
        Object referrer = new Object();
        String fieldName = "testField";
        List<Object> list = Arrays.asList(referrer, new Object(), new Object());
        int index = 1;
        Reference reference = new JsonMappingException.Reference(referrer, fieldName);
        JsonLocation location = new JsonLocation(1, 1, "Test File");
        Throwable throwable = new IOException("Test IOException");
        JsonMappingException jsonMappingException = new JsonMappingException(processor, "Test Message");
        jsonMappingException.prependPath(reference);
        jsonMappingException.prependPath(list.get(index));
        jsonMappingException.setLocation(location);
        jsonMappingException.initCause(throwable);
        List<Reference> path = jsonMappingException.getPath();
        assertEquals(2, path.size());
        assertEquals(reference, path.get(0));
        assertEquals(list.get(index), path.get(1));
        assertEquals(location, jsonMappingException.getLocation());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("Test IOException", jsonMappingException.getCause().getMessage());
    }
}