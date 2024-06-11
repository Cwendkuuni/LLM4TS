package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testWrapWithPath() {
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);

        Object refFrom = new Object() {};
        String refFieldName = "fieldName";
        JsonMappingException jmeWithPath = JsonMappingException.wrapWithPath(ioException, refFrom, refFieldName);

        assertEquals(jme.getMessage(), jmeWithPath.getMessage());
        List<JsonMappingException.Reference> path = jmeWithPath.getPath();
        assertEquals(1, path.size());
        JsonMappingException.Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());
    }

    @Test
    public void testPrependPath() {
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);

        Object refFrom = new Object() {};
        String refFieldName = "fieldName";
        JsonMappingException jmeWithPath = JsonMappingException.wrapWithPath(ioException, refFrom, refFieldName);

        JsonMappingException jmeWithPrependedPath = new JsonMappingException(ioException);
        jmeWithPrependedPath.prependPath(refFrom, refFieldName);

        assertEquals(jmeWithPath, jmeWithPrependedPath);
    }

    @Test
    public void testGetPathReference() {
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);

        Object refFrom = new Object() {};
        String refFieldName = "fieldName";
        JsonMappingException jmeWithPath = JsonMappingException.wrapWithPath(ioException, refFrom, refFieldName);

        StringBuilder expectedPathReference = new StringBuilder();
        expectedPathReference.append("Unexpected IO exception (through reference chain: ");
        expectedPathReference.append(refFrom.getClass().getName());
        expectedPathReference.append(" ");
        expectedPathReference.append(refFieldName);
        expectedPathReference.append("->");
        expectedPathReference.append(JsonMappingException.Reference.class.getName());
        expectedPathReference.append(" [");
        expectedPathReference.append("\"Unexpected IO exception\"", 0, "Unexpected IO exception".length());
        expectedPathReference.append("]");

        StringBuilder actualPathReference = new StringBuilder();
        actualPathReference = jmeWithPath.getPathReference(actualPathReference);

        assertEquals(expectedPathReference.toString(), actualPathReference.toString());
    }

    @Test
    public void testGetPath() {
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);

        Object refFrom1 = new Object() {};
        String refFieldName1 = "fieldName1";
        JsonMappingException jmeWithPath1 = JsonMappingException.wrapWithPath(ioException, refFrom1, refFieldName1);

        Object refFrom2 = new Object() {};
        String refFieldName2 = "fieldName2";
        JsonMappingException jmeWithPath2 = JsonMappingException.wrapWithPath(ioException, refFrom2, refFieldName2);

        JsonMappingException jmeWithPaths = new JsonMappingException(ioException);
        jmeWithPaths.prependPath(refFrom1, refFieldName1);
        jmeWithPaths.prependPath(refFrom2, refFieldName2);

        List<JsonMappingException.Reference> expectedPaths = new LinkedList<>();
        JsonMappingException.Reference reference1 = new JsonMappingException.Reference(refFrom1, refFieldName1);
        JsonMappingException.Reference reference2 = new JsonMappingException.Reference(refFrom2, refFieldName2);
        expectedPaths.add(reference1);
        expectedPaths.add(reference2);

        List<JsonMappingException.Reference> actualPaths = jmeWithPaths.getPath();
        assertEquals(expectedPaths, actualPaths);
    }

    @Test
    public void testGetProcessor() throws IOException {
        JsonParser parser = new JsonParser();
        JsonMappingException jme = JsonMappingException.from(parser, "Unexpected error");
        assertNull(jme.getProcessor());

        JsonGenerator generator = new JsonGenerator();
        JsonMappingException jme2 = JsonMappingException.from(generator, "Unexpected error");
        assertNull(jme2.getProcessor());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetProcessorWithoutInitPath() {
        JsonMappingException jme = new JsonMappingException(new IOException("Unexpected error"));
        assertNull(jme.getProcessor());
    }

    @Test
    public void testGetDescription() {
        JsonMappingException jme = new JsonMappingException(new IOException("Unexpected error"));
        String description = jme.getDescription();
        assertEquals("UNKNOWN [IOException: Unexpected error]", description);

        JsonMappingException jmeWithPath = new JsonMappingException(new IOException("Unexpected error"), new Object() {});
        description = jmeWithPath.getDescription();
        assertEquals("UNKNOWN [IOException: Unexpected error]", description);

        JsonMappingException jmeWithReference = JsonMappingException.from(new JsonParser(), "Unexpected error");
        jmeWithReference.prependPath(new Object() {});
        description = jmeWithReference.getDescription();
        assertEquals("UNKNOWN [JsonParser@722f6d]", description);
    }
}