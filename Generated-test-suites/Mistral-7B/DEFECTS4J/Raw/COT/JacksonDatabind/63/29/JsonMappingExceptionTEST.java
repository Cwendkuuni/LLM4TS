package com.fasterxml.jackson.databind; // import statements go here

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test cases for JsonMappingException class.
 */
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        String msg = "Test JsonMappingException message";
        JsonMappingException e = new JsonMappingException(msg);
        assertEquals(msg, e.getMessage());
        assertNull(e.getProcessor());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithProcessor() {
        Closeable processor = MockCloseable.create();
        String msg = "Test JsonMappingException message";
        JsonMappingException e = new JsonMappingException(processor, msg);
        assertTrue(e.getProcessor() instanceof Closeable);
        assertEquals(msg, e.getMessage());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithLocation() {
        Closeable processor = MockCloseable.create();
        String msg = "Test JsonMappingException message";
        JsonLocation location = JsonLocation.construct(1, 2, "source");
        JsonMappingException e = new JsonMappingException(processor, msg, location);
        assertTrue(e.getProcessor() instanceof Closeable);
        assertEquals(location, e.getLocation());
        assertEquals(msg, e.getMessage());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithLocationAndRootCause() {
        Closeable processor = MockCloseable.create();
        String msg = "Test JsonMappingException message";
        Throwable rootCause = new Exception("Root cause");
        JsonLocation location = JsonLocation.construct(1, 2, "source");
        JsonMappingException e = new JsonMappingException(processor, msg, location, rootCause);
        assertTrue(e.getProcessor() instanceof Closeable);
        assertEquals(location, e.getLocation());
        assertEquals(msg, e.getMessage());
        assertEquals(rootCause, e.getCause());
    }

    @Test
    public void testJsonMappingExceptionPath() {
        String msg = "Test JsonMappingException message";
        Closeable processor = MockCloseable.create();
        JsonMappingException e = new JsonMappingException(processor, msg);
        Reference ref1 = new JsonMappingException.Reference(new Object(), "field1");
        Reference ref2 = new JsonMappingException.Reference(new Object(), "field2");
        e.prependPath(ref1);
        e.prependPath(ref2);
        List<JsonMappingException.Reference> path = e.getPath();
        assertEquals(2, path.size());
        assertEquals(ref1, path.get(0));
        assertEquals(ref2, path.get(1));
    }

    @Test
    public void testJsonMappingExceptionPathDescription() {
        String msg = "Test JsonMappingException message";
        Closeable processor = MockCloseable.create();
        JsonMappingException e = new JsonMappingException(processor, msg);
        Reference ref1 = new JsonMappingException.Reference(new Object(), "field1");
        Reference ref2 = new JsonMappingException.Reference(new Object(), "field2");
        e.prependPath(ref1);
        e.prependPath(ref2);
        assertEquals("(through reference chain: " + ref1.toString() + "->" + ref2.toString() + ")", e.getPathReference());
    }
}

class MockCloseable implements Closeable {
    private static int counter = 0;

    public MockCloseable() {
        counter++;
    }

    @Override
    public void close() {
        System.out.println("Closing MockCloseable instance " + counter);
    }

    public static MockCloseable create() {
        return new MockCloseable();
    }
}