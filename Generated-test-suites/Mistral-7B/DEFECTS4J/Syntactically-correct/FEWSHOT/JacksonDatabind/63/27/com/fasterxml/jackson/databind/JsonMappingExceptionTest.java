package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testWrapWithPath() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);
        Reference ref = new Reference(new Object(), "fieldName");
        JsonMappingException jmeWithPath = JsonMappingException.wrapWithPath(jme, ref);
        List<Reference> path = jmeWithPath.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals("fieldName", reference.getFieldName());
        assertEquals("java.lang.Object", reference.getDescription());
    }

    @Test
    public void testWrapWithPathWithIndex() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);
        Reference ref = new Reference(new Object()[0], 0);
        JsonMappingException jmeWithPath = JsonMappingException.wrapWithPath(jme, ref);
        List<Reference> path = jmeWithPath.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals("java.lang.Object", reference.getDescription());
        assertEquals("[0]", reference.toString());
    }

    @Test
    public void testGetPathReference() {
        String expected = "JsonMappingException[ Unexpected IOException(of type [class java.io.IOException]) , through reference chain: [Ljava.lang.Object;->[Ljava.lang.Object;->0->'fieldName']]";
        JsonMappingException jme = new JsonMappingException(null, "Unexpected IOException", (Throwable) null);
        Reference ref = new Reference(new Object(), "fieldName");
        JsonMappingException jmeWithPath = JsonMappingException.wrapWithPath(jme, ref);
        String actual = jmeWithPath.getPathReference().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPath() {
        Reference ref1 = new Reference(new Object(), "field1");
        Reference ref2 = new Reference(new Object(), "field2");
        JsonMappingException jme = new JsonMappingException(null, "Unexpected Exception", (Throwable) null);
        jme.prependPath(ref1);
        jme.prependPath(ref2);
        List<Reference> expectedPath = Arrays.asList(ref2, ref1);
        List<Reference> actualPath = jme.getPath();
        assertEquals(expectedPath, actualPath);
    }
}