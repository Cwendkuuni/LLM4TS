package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        // Test default constructor
        JsonMappingException e = new JsonMappingException("Message");
        assertEquals("Message", e.getMessage());
        assertNull(e.getPath());

        // Test constructor with message and location
        JsonLocation loc = new JsonLocation("Test.java", 1, 1);
        JsonMappingException e1 = new JsonMappingException("Message", loc);
        assertEquals("Message", e1.getMessage());
        assertEquals(loc, e1.getLocation());

        // Test constructor with message, location, and root cause
        IOException rootCause = new IOException("Root cause");
        JsonMappingException e2 = new JsonMappingException("Message", loc, rootCause);
        assertEquals("Message", e2.getMessage());
        assertEquals(loc, e2.getLocation());
        assertSame(rootCause, e2.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        // Create reference objects
        Object ref1 = new Object();
        String ref1Name = "ref1";
        Reference ref1R = new Reference(ref1, ref1Name);

        Object ref2 = new Object[1];
        int ref2Index = 0;
        Reference ref2R = new Reference(ref2, ref2Index);

        Object ref3 = new Object();
        Reference ref3R = new Reference(ref3);

        // Test wrapWithPath method
        Throwable throwable = new RuntimeException("Message");
        JsonMappingException e = JsonMappingException.wrapWithPath(throwable, ref1R);
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref1R, path.get(0));

        e = JsonMappingException.wrapWithPath(throwable, ref2R);
        path = e.getPath();
        assertEquals(2, path.size());
        assertEquals(ref2R, path.get(1));
        assertEquals(ref1R, path.get(0));

        e = JsonMappingException.wrapWithPath(throwable, ref3R);
        path = e.getPath();
        assertEquals(1, path.size());
        assertEquals(ref3R, path.get(0));

        // Test prependPath method
        JsonMappingException e1 = new JsonMappingException("Message");
        e1.prependPath(ref1R);
        path = e1.getPath();
        assertEquals(1, path.size());
        assertEquals(ref1R, path.get(0));

        e1.prependPath(ref2R);
        path = e1.getPath();
        assertEquals(2, path.size());
        assertEquals(ref2R, path.get(1));
        assertEquals(ref1R, path.get(0));

        e1.prependPath(ref3R);
        path = e1.getPath();
        assertEquals(3, path.size());
        assertEquals(ref3R, path.get(2));
        assertEquals(ref2R, path.get(1));
        assertEquals(ref1R, path.get(0));
    }

    @Test
    public void testJsonMappingExceptionFrom() {
        Object ref1 = new Object();
        String ref1Name = "ref1";
        Reference ref1R = new Reference(ref1, ref1Name);

        Object ref2 = new Object[1];
        int ref2Index = 0;
        Reference ref2R = new Reference(ref2, ref2Index);

        Object ref3 = new Object();
        Reference ref3R = new Reference(ref3);

        JsonParser parser = new JsonParser();
        JsonMappingException e1 = JsonMappingException.from(parser, "Message");
        List<Reference> path = e1.getPath();
        assertNull(path);

        JsonParser parser1 = new JsonParser();
        JsonMappingException e2 = JsonMappingException.from(parser1, "Message", new RuntimeException("Root cause"));
        path = e2.getPath();
        assertNull(path);

        JsonGenerator generator = new JsonGenerator();
        JsonMappingException e3 = JsonMappingException.from(generator, "Message");
        path = e3.getPath();
        assertNull(path);

        JsonGenerator generator1 = new JsonGenerator();
        JsonMappingException e4 = JsonMappingException.from(generator1, "Message", new RuntimeException("Root cause"));
        path = e4.getPath();
        assertNull(path);

        DeserializationContext ctxt = new MinimalDeserializationContext();
        JsonMappingException e5 = JsonMappingException.from(ctxt, "Message");
        path = e5.getPath();
        assertNull(path);

        DeserializationContext ctxt1 = new MinimalDeserializationContext();
        JsonMappingException e6 = JsonMappingException.from(ctxt1, "Message", new RuntimeException("Root cause"));
        path = e6.getPath();
        assertNull(path);

        SerializerProvider ctxt2 = new MinimalSerializerProvider();
        JsonMappingException e7 = JsonMappingException.from(ctxt2, "Message");
        path = e7.getPath();
        assertNull(path);

        SerializerProvider ctxt3 = new MinimalSerializerProvider();
        JsonMappingException e8 = JsonMappingException.from(ctxt3, "Message", new RuntimeException("Root cause"));
        path = e8.getPath();
        assertNull(path);

        // Test fromUnexpectedIOE method
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException e9 = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO exception", e9.getMessage());
        assertNull(e9.getPath());

        // Test wrapWithPath method with indexed object
        Object indexedObj = new Object[2];
        JsonMappingException e10 = JsonMappingException.wrapWithPath(new RuntimeException("Root cause"), indexedObj, 0);
        List<Reference> path10 = e10.getPath();
        assertEquals(1, path10.size());
        Reference ref10 = new Reference(indexedObj, 0);
        assertEquals(ref10, path10.get(0));

        JsonMappingException e11 = JsonMappingException.wrapWithPath(new RuntimeException("Root cause"), indexedObj, 1);
        List<Reference> path11 = e11.getPath();
        assertEquals(2, path11.size());
        Reference ref11 = new Reference(indexedObj, 1);
        assertEquals(ref11, path11.get(1));
        assertEquals(ref10, path11.get(0));
    }
}