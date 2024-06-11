package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @org.junit.Test
    public void testJsonMappingException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with message
        String msg = "Error message";
        JsonMappingException e1 = new JsonMappingException(msg);
        assertEquals(msg, e1.getMessage());

        // Test constructor with message and cause
        Throwable cause = new IOException("IO error");
        JsonMappingException e2 = new JsonMappingException(msg, cause);
        assertEquals(msg, e2.getMessage());
        assertEquals(cause, e2.getCause());

        // Test constructor with message, location and cause
        JsonLocation location = new JsonLocation(1, 1, "file.json");
        JsonMappingException e3 = new JsonMappingException(msg, location, cause);
        assertEquals(msg, e3.getMessage());
        assertEquals(location, e3.getLocation());
        assertEquals(cause, e3.getCause());

        // Test constructor with processor
        Closeable processor = mapper.getFactory().getParserCreator().createParser("[]");
        JsonMappingException e4 = new JsonMappingException(processor, msg);
        assertEquals(msg, e4.getMessage());
        assertSame(processor, e4.getProcessor());

        // Test factory methods
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonMappingException e5 = JsonMappingException.from(parser, msg);
        assertEquals(msg, e5.getMessage());
        assertSame(parser, e5.getProcessor());

        // Test wrapWithPath method
        String fieldName = "field";
        Object referrer = new Object();
        JsonMappingException e6 = JsonMappingException.wrapWithPath(new IOException(), referrer, fieldName);
        assertEquals(1, e6.getPath().size());
        Reference reference = e6.getPath().get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());

        // Test fromUnexpectedIOE method
        IOException src = new IOException("Unexpected IO error");
        JsonMappingException e7 = JsonMappingException.fromUnexpectedIOE(src);
        assertEquals(src.getMessage(), e7.getMessage());
        assertSame(src.getClass(), e7.getCause().getClass());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithPath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with message, location and cause and path
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonMappingException e1 = new JsonMappingException(parser, "Error message", new Object(), "field");
        assertEquals(1, e1.getPath().size());
        Reference reference = e1.getPath().get(0);
        assertEquals(new Object(), reference.getFrom());
        assertEquals("field", reference.getFieldName());

        // Test constructor with message, location, cause and path
        JsonLocation location = new JsonLocation(1, 1, "file.json");
        JsonMappingException e2 = new JsonMappingException(parser, "Error message", location, new Object(), "field");
        assertEquals(1, e2.getPath().size());
        reference = e2.getPath().get(0);
        assertEquals(new Object(), reference.getFrom());
        assertEquals("field", reference.getFieldName());
        assertEquals(location, e2.getLocation());

        // Test constructor with message, processor, location and cause and path
        Closeable processor = mapper.getFactory().getParserCreator().createParser("[]");
        JsonMappingException e3 = new JsonMappingException(processor, "Error message", new Object(), "field");
        assertEquals(1, e3.getPath().size());
        reference = e3.getPath().get(0);
        assertNull(reference.getFrom());
        assertEquals("field", reference.getFieldName());
        assertSame(processor, e3.getProcessor());
    }

    @org.junit.Test
    public void testJsonMappingExceptionWithNoPath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with message and cause
        Throwable cause = new IOException("IO error");
        JsonMappingException e1 = new JsonMappingException(cause);
        assertTrue(e1.getPath().isEmpty());

        // Test constructor with message, location and cause
        JsonLocation location = new JsonLocation(1, 1, "file.json");
        JsonMappingException e2 = new JsonMappingException(location, "Error message", cause);
        assertTrue(e2.getPath().isEmpty());

        // Test constructor with processor
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonMappingException e3 = new JsonMappingException(parser, "Error message");
        assertTrue(e3.getPath().isEmpty());
    }

    @org.junit.Test
    public void testJsonMappingExceptionWithLargePath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with message, processor, location, cause and large path
        JsonParser parser = mapper.getFactory().createParser("[]");
        List<Reference> path = new ArrayList<>();
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST; i++) {
            path.add(new Reference(new Object(), "field" + i));
        }
        JsonMappingException e1 = new JsonMappingException(parser, "Error message", null, null, path.toArray(new Reference[0]));
        assertEquals(JsonMappingException.MAX_REFS_TO_LIST, e1.getPath().size());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testJsonMappingExceptionWithTooLargePath() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with message, processor, location, cause and too large path
        JsonParser parser = mapper.getFactory().createParser("[]");
        List<Reference> path = new ArrayList<>();
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            path.add(new Reference(new Object(), "field" + i));
        }
        try {
            JsonMappingException e1 = new JsonMappingException(parser, "Error message", null, null, path.toArray(new Reference[0]));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            // Expected
        }
    }

    @org.junit.Test
    public void testJsonMappingExceptionToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test toString method
        JsonMappingException e1 = new JsonMappingException("Error message");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Error message", e1.toString());

        // Test toString method with path
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonMappingException e2 = new JsonMappingException(parser, "Error message", new Object(), "field");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Error message (through reference chain: Object->field)", e2.toString());
    }

    @org.junit.Test
    public void testJsonMappingExceptionWriteReplace() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test writeReplace method
        JsonMappingException e1 = new JsonMappingException("Error message");
        JsonMappingException serialize = (JsonMappingException) e1.writeReplace();
        assertEquals(e1.getMessage(), serialize.getMessage());
    }
}