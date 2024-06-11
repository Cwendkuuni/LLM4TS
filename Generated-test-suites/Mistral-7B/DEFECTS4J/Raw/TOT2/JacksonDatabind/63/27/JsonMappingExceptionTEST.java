package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonMappingExceptionTest {

    @Test
    public void testDefaultConstructor() {
        JsonMappingException e = new JsonMappingException();
        assertNotNull(e.getMessage());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructorWithMessage() {
        JsonMappingException e = new JsonMappingException("Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructorWithMessageAndLocation() {
        JsonParser parser = createMockParser();
        JsonMappingException e = new JsonMappingException(parser, "Test message", new JsonLocation(1, 2, 3));
        assertEquals("Test message", e.getMessage());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(2, e.getLocation().getColumnNumber());
        assertEquals(3, e.getLocation().getPosition());
        assertNotNull(e.getProcessor());
    }

    @Test
    public void testConstructorWithMessageAndProcessor() {
        JsonParser parser = createMockParser();
        JsonMappingException e = new JsonMappingException(parser, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNotNull(e.getProcessor());
        assertNull(e.getLocation());
    }

    @Test
    public void testConstructorWithMessageAndLocationAndRootCause() {
        JsonParser parser = createMockParser();
        Throwable rootCause = new RuntimeException("Test root cause");
        JsonMappingException e = new JsonMappingException(parser, "Test message", new JsonLocation(1, 2, 3), rootCause);
        assertEquals("Test message", e.getMessage());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(2, e.getLocation().getColumnNumber());
        assertEquals(3, e.getLocation().getPosition());
        assertNotNull(e.getProcessor());
        assertEquals(rootCause.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getCausedBy().getMessage());
    }

    @Test
    public void testConstructorWithMessageAndLocationAndRootCauseAndRefs() {
        JsonParser parser = createMockParser();
        Throwable rootCause = new RuntimeException("Test root cause");
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException e = new JsonMappingException(parser, "Test message", new JsonLocation(1, 2, 3), rootCause, new Reference(refFrom, refFieldName));
        assertEquals("Test message", e.getMessage());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(2, e.getLocation().getColumnNumber());
        assertEquals(3, e.getLocation().getPosition());
        assertNotNull(e.getProcessor());
        assertEquals(rootCause.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getCausedBy().getMessage());
        assertEquals(refFrom, e.getReference().getFrom());
        assertEquals(refFieldName, e.getReference().getFieldName());
    }

    @Test
    public void testFromJsonParser() {
        JsonParser parser = createMockParser();
        JsonMappingException e = JsonMappingException.from(parser, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNotNull(e.getProcessor());
        assertNull(e.getLocation());
    }

    @Test
    public void testFromJsonParserWithRootCause() {
        JsonParser parser = createMockParser();
        Throwable rootCause = new RuntimeException("Test root cause");
        JsonMappingException e = JsonMappingException.from(parser, "Test message", rootCause);
        assertEquals("Test message", e.getMessage());
        assertNotNull(e.getProcessor());
        assertEquals(rootCause.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getCausedBy().getMessage());
    }

    @Test
    public void testFromJsonParserWithLocation() {
        JsonParser parser = createMockParser();
        JsonMappingException e = JsonMappingException.from(parser, "Test message", new JsonLocation(1, 2, 3));
        assertEquals("Test message", e.getMessage());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(2, e.getLocation().getColumnNumber());
        assertEquals(3, e.getLocation().getPosition());
        assertNotNull(e.getProcessor());
    }

    @Test
    public void testFromJsonParserWithLocationAndRootCause() {
        JsonParser parser = createMockParser();
        Throwable rootCause = new RuntimeException("Test root cause");
        JsonMappingException e = JsonMappingException.from(parser, "Test message", new JsonLocation(1, 2, 3), rootCause);
        assertEquals("Test message", e.getMessage());
        assertEquals(1, e.getLocation().getLineNumber());
        assertEquals(2, e.getLocation().getColumnNumber());
        assertEquals(3, e.getLocation().getPosition());
        assertNotNull(e.getProcessor());
        assertEquals(rootCause.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getCausedBy().getMessage());
    }

    @Test
    public void testFromJsonGenerator() {
        JsonGenerator generator = createMockGenerator();
        JsonMappingException e = JsonMappingException.from(generator, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
    }

    @Test
    public void testFromJsonGeneratorWithRootCause() {
        JsonGenerator generator = createMockGenerator();
        Throwable rootCause = new RuntimeException("Test root cause");
        JsonMappingException e = JsonMappingException.from(generator, "Test message", rootCause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertEquals(rootCause.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getCausedBy().getMessage());
    }

    @Test
    public void testFromSerializerProvider() {
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return generator;
            }
        };
        JsonMappingException e = JsonMappingException.from(provider, "Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
    }

    @Test
    public void testFromSerializerProviderWithRootCause() {
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return generator;
            }
        };
        Throwable rootCause = new RuntimeException("Test root cause");
        JsonMappingException e = JsonMappingException.from(provider, "Test message", rootCause);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertEquals(rootCause.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(rootCause.getMessage(), e.getCausedBy().getMessage());
    }

    @Test
    public void testFromUnexpectedIOException() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IOException", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
    }

    @Test
    public void testWrapWithPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        Throwable src = new RuntimeException("Test source exception");
        JsonMappingException e = JsonMappingException.wrapWithPath(src, refFrom, refFieldName);
        assertEquals("Test source exception (through reference chain: [com.example.MyClass \"fieldName\"])", e.getMessage());
        assertEquals(src.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(src.getMessage(), e.getCausedBy().getMessage());
        assertEquals(refFrom, e.getReference().getFrom());
        assertEquals(refFieldName, e.getReference().getFieldName());
    }

    @Test
    public void testWrapWithPathIndex() {
        Object refFrom = new Object()[0];
        int refIndex = 0;
        Throwable src = new RuntimeException("Test source exception");
        JsonMappingException e = JsonMappingException.wrapWithPath(src, refFrom, refIndex);
        assertEquals("Test source exception (through reference chain: [Lcom.example.MyArray;[Ljava.lang.Object;@0])", e.getMessage());
        assertEquals(src.getClass().getName(), e.getCausedBy().getClass().getName());
        assertEquals(src.getMessage(), e.getCausedBy().getMessage());
        assertArrayEquals(new Object[]{refFrom}, e.getReference().getFroms());
        assertEquals(refIndex, e.getReference().getIndex());
    }

    @Test
    public void testGetPath() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        Object refFrom2 = new Object();
        int refIndex = 0;
        JsonMappingException e = new JsonMappingException(new JsonParser(), "Test message", new JsonLocation(1, 2, 3), new Reference(refFrom, refFieldName), new Reference(refFrom2, refIndex));
        List<Reference> path = e.getPath();
        assertEquals(2, path.size());
        Reference ref1 = path.get(0);
        assertEquals(refFrom, ref1.getFrom());
        assertEquals(refFieldName, ref1.getFieldName());
        Reference ref2 = path.get(1);
        assertEquals(refFrom2, ref2.getFrom());
        assertEquals(refIndex, ref2.getIndex());
    }

    @Test
    public void testGetPathReference() {
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException e = new JsonMappingException(new JsonParser(), "Test message", new JsonLocation(1, 2, 3), new Reference(refFrom, refFieldName));
        StringBuilder sb = new StringBuilder();
        e.getPathReference(sb);
        assertEquals("[com.example.MyClass \"fieldName\"]", sb.toString());
    }

    @Test
    public void testGetPathReferenceWithEmptyPath() {
        JsonMappingException e = new JsonMappingException();
        StringBuilder sb = new StringBuilder();
        e.getPathReference(sb);
        assertTrue(sb.toString().isEmpty());
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private JsonGenerator createMockGenerator() {
        return mock(JsonGenerator.class);
    }
}