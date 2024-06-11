package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        // Test default constructor
        JsonMappingException e = new JsonMappingException("Message");
        assertEquals("Message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());

        // Test constructor with message and location
        JsonParser parser = createMockParser();
        JsonMappingException e1 = JsonMappingException.from(parser, "Message");
        assertEquals("Message", e1.getMessage());
        assertEquals(parser, e1.getProcessor());
        assertEquals(parser.getCurrentLocation(), e1.getLocation());

        // Test constructor with message, location and root cause
        Throwable rootCause = new IOException("IO Exception");
        JsonMappingException e2 = JsonMappingException.from(parser, "Message", rootCause);
        assertEquals("Message", e2.getMessage());
        assertEquals(parser, e2.getProcessor());
        assertEquals(parser.getCurrentLocation(), e2.getLocation());
        assertEquals(rootCause, e2.getRootCause());

        // Test constructor with message, location, and no root cause
        JsonMappingException e3 = JsonMappingException.from(parser, "Message", null);
        assertEquals("Message", e3.getMessage());
        assertEquals(parser, e3.getProcessor());
        assertEquals(parser.getCurrentLocation(), e3.getLocation());
        assertNull(e3.getRootCause());

        // Test constructor with message, processor, and no location or root cause
        JsonMappingException e4 = new JsonMappingException(parser, "Message");
        assertEquals("Message", e4.getMessage());
        assertEquals(parser, e4.getProcessor());
        assertNull(e4.getLocation());
        assertNull(e4.getRootCause());
    }

    @Test
    public void testPath() {
        JsonParser parser = createMockParser();
        Object root = new Object();

        // Test empty path
        JsonMappingException e1 = new JsonMappingException(parser, "Message");
        List<JsonMappingException.Reference> path = e1.getPath();
        assertTrue(path.isEmpty());

        // Test path with single reference
        JsonMappingException e2 = JsonMappingException.from(parser, "Message", root);
        path = e2.getPath();
        List<JsonMappingException.Reference> expectedPath = Arrays.asList(new JsonMappingException.Reference(root));
        assertArrayEquals(expectedPath.toArray(), path.toArray());

        // Test path with multiple references
        JsonMappingException e3 = JsonMappingException.from(parser, "Message", root, "field1", "field2");
        path = e3.getPath();
        expectedPath = Arrays.asList(
                new JsonMappingException.Reference(root, "field1"),
                new JsonMappingException.Reference(null, "field2")
        );
        assertArrayEquals(expectedPath.toArray(), path.toArray());

        // Test path with multiple references and different types
        Object nestedObject1 = new Object();
        Object nestedObject2 = new Object[0];
        JsonMappingException e4 = JsonMappingException.from(parser, "Message", root, "field1", nestedObject1, "field2", 0);
        path = e4.getPath();
        expectedPath = Arrays.asList(
                new JsonMappingException.Reference(root, "field1"),
                new JsonMappingException.Reference(nestedObject1, null),
                new JsonMappingException.Reference(nestedObject1, "field2"),
                new JsonMappingException.Reference(null, 0)
        );
        assertArrayEquals(expectedPath.toArray(), path.toArray());
    }

    @Test
    public void testReferenceToString() {
        Object root = new Object();
        JsonMappingException.Reference ref1 = new JsonMappingException.Reference(root, "field1");
        JsonMappingException.Reference ref2 = new JsonMappingException.Reference(root, 0);
        JsonMappingException.Reference ref3 = new JsonMappingException.Reference(null, "field1");

        assertEquals("[" + root.getClass().getName() + "." + "field1]", ref1.toString());
        assertEquals("[Object[] at index 0]", ref2.toString());
        assertEquals("UNKNOWN", ref3.toString());
    }

    @Test
    public void testWriteReplace() throws IOException {
        JsonParser parser = createMockParser();
        JsonMappingException e = JsonMappingException.from(parser, "Message");
        assertEquals(e, e.writeReplace());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("Unexpected IO Exception");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO Exception", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getRootCause());
    }

    @Test
    public void testWrapWithPath() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        JsonMappingException e1 = JsonMappingException.from(parser, "Message");
        JsonMappingException e2 = JsonMappingException.wrapWithPath(e1, root, "field1");
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1)", e2.getMessage());
        assertEquals(parser, e2.getProcessor());
        assertEquals(parser.getCurrentLocation(), e2.getLocation());
        List<JsonMappingException.Reference> path = e2.getPath();
        List<JsonMappingException.Reference> expectedPath = Arrays.asList(
                new JsonMappingException.Reference(root)
        );
        assertArrayEquals(expectedPath.toArray(), path.toArray());

        e2 = JsonMappingException.wrapWithPath(e1, root, "field1", "field2");
        path = e2.getPath();
        expectedPath = Arrays.asList(
                new JsonMappingException.Reference(root, "field1"),
                new JsonMappingException.Reference(null, "field2")
        );
        assertArrayEquals(expectedPath.toArray(), path.toArray());

        e2 = JsonMappingException.wrapWithPath(e1, root, "field1", null);
        path = e2.getPath();
        assertTrue(path.isEmpty());
    }

    @Test
    public void testPrependPath() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        JsonMappingException e1 = JsonMappingException.from(parser, "Message");
        JsonMappingException e2 = new JsonMappingException(parser, "Message");

        e1.prependPath(root, "field1");
        List<JsonMappingException.Reference> path1 = e1.getPath();
        List<JsonMappingException.Reference> expectedPath1 = Arrays.asList(
                new JsonMappingException.Reference(root, "field1"),
                new JsonMappingException.Reference(null)
        );
        assertArrayEquals(expectedPath1.toArray(), path1.toArray());

        e2.prependPath(root, "field1");
        List<JsonMappingException.Reference> path2 = e2.getPath();
        List<JsonMappingException.Reference> expectedPath2 = Arrays.asList(
                new JsonMappingException.Reference(root, "field1")
        );
        assertArrayEquals(expectedPath2.toArray(), path2.toArray());

        e2.prependPath(root, "field1", "field2");
        path2 = e2.getPath();
        List<JsonMappingException.Reference> expectedPath3 = Arrays.asList(
                new JsonMappingException.Reference(root, "field1"),
                new JsonMappingException.Reference(null, "field2")
        );
        assertArrayEquals(expectedPath3.toArray(), path2.toArray());

        e2.prependPath(root, "field1", null);
        path2 = e2.getPath();
        assertTrue(path2.isEmpty());

        e2.prependPath(root, null);
        path2 = e2.getPath();
        assertTrue(path2.isEmpty());

        e2.prependPath(null, "field1");
        path2 = e2.getPath();
        assertTrue(path2.isEmpty());
    }

    @Test
    public void testGetPathReference() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        JsonMappingException e1 = JsonMappingException.from(parser, "Message");
        assertEquals("Message (through reference chain: UNKNOWN)", e1.getPathReference().toString());

        e1.prependPath(root, "field1");
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->UNKNOWN)", e1.getPathReference().toString());

        e1.prependPath(root, "field1", "field2");
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->" + root.getClass().getName() + ".field2" + "->UNKNOWN)", e1.getPathReference().toString());

        e1.prependPath(root, "field1", null);
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->UNKNOWN)", e1.getPathReference().toString());

        e1.prependPath(null, "field1");
        assertEquals("Message (through reference chain: UNKNOWN->UNKNOWN)", e1.getPathReference().toString());

        e1.prependPath(null, "field1", "field2");
        assertEquals("Message (through reference chain: UNKNOWN->UNKNOWN" + "->" + root.getClass().getName() + ".field2)", e1.getPathReference().toString());
    }

    @Test
    public void testGetPathReferenceWithBuilder() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        JsonMappingException e1 = JsonMappingException.from(parser, "Message");
        StringBuilder sb = new StringBuilder();
        assertEquals("Message (through reference chain: UNKNOWN)", e1.getPathReference(sb).toString());

        e1.prependPath(root, "field1");
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->UNKNOWN)", sb.toString());

        e1.prependPath(root, "field1", "field2");
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->" + root.getClass().getName() + ".field2" + "->UNKNOWN)", sb.toString());

        e1.prependPath(root, "field1", null);
        assertEquals("Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->UNKNOWN)", sb.toString());

        e1.prependPath(null, "field1");
        assertEquals("Message (through reference chain: UNKNOWN->UNKNOWN)", sb.toString());

        e1.prependPath(null, "field1", "field2");
        assertEquals("Message (through reference chain: UNKNOWN->UNKNOWN" + "->" + root.getClass().getName() + ".field2)", sb.toString());
    }

    @Test
    public void testMaxReferences() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        List<JsonMappingException.Reference> references = new ArrayList<>();
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            JsonMappingException e = JsonMappingException.from(parser, "Message");
            e.prependPath(root, "field1");
            references.addAll(e.getPath());
        }
        assertEquals(JsonMappingException.MAX_REFS_TO_LIST, references.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxReferencesException() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        JsonMappingException e = JsonMappingException.from(parser, "Message");
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            e.prependPath(root, "field1");
        }
    }

    @Test
    public void testToString() {
        Object root = new Object();
        JsonParser parser = createMockParser();
        JsonMappingException e = JsonMappingException.from(parser, "Message");
        assertEquals("JsonMappingException: Message (through reference chain: " + root.getClass().getName() + ".", e.toString());

        e.prependPath(root, "field1");
        assertEquals("JsonMappingException: Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->UNKNOWN)", e.toString());

        e.prependPath(root, "field1", "field2");
        assertEquals("JsonMappingException: Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->" + root.getClass().getName() + ".field2" + "->UNKNOWN)", e.toString());

        e.prependPath(root, "field1", null);
        assertEquals("JsonMappingException: Message (through reference chain: " + root.getClass().getName() + "." + "field1" + "->UNKNOWN)", e.toString());

        e.prependPath(null, "field1");
        assertEquals("JsonMappingException: Message (through reference chain: UNKNOWN->UNKNOWN)", e.toString());

        e.prependPath(null, "field1", "field2");
        assertEquals("JsonMappingException: Message (through reference chain: UNKNOWN->UNKNOWN" + "->" + root.getClass().getName() + ".field2)", e.toString());
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }
}