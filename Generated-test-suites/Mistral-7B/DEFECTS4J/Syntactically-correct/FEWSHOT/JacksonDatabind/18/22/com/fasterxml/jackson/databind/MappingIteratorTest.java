package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private MappingIterator<String> _iterator;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        _iterator = mapper.readValue(new CharArrayReader("[\"foo\", \"bar\"].\n"),
                String[].class).iterator();
    }

    @Test
    public void testHasNext() {
        assertTrue(_iterator.hasNext());
        assertTrue(_iterator.hasNext());
        assertFalse(_iterator.hasNext());
    }

    @Test
    public void testNext() {
        assertEquals("foo", _iterator.next());
        assertEquals("bar", _iterator.next());
        assertNull(_iterator.next());
    }

    @Test
    public void testClose() throws IOException {
        _iterator.close();
        assertFalse(_iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        _iterator.remove();
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(_iterator.hasNextValue());
        assertTrue(_iterator.hasNextValue());
        assertFalse(_iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        assertEquals("foo", _iterator.nextValue());
        assertEquals("bar", _iterator.nextValue());
        assertNull(_iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> result = _iterator.readAll();
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testReadAllArrayList() throws IOException {
        List<String> result = new ArrayList<String>();
        _iterator.readAll(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testReadAllLinkedList() throws IOException {
        Collection<String> result = new LinkedList<String>();
        _iterator.readAll(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }

    @Test
    public void testGetParser() {
        assertSame(_iterator.getParser(), _iterator.next().getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(_iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        // This test is not comprehensive as the implementation of JsonLocation
        // can change, and the JUnit assertions for JsonLocation do not provide
        // sufficient coverage.
        JsonLocation loc = _iterator.getCurrentLocation();
        assertNull(loc.getColumnNumber());
        assertNull(loc.getLineNumber());
        assertNull(loc.getPath());
    }
}