package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MappingIteratorTest {

    private MappingIterator<Object> mappingIterator;

    @Before
    public void setUp() throws IOException {
        mappingIterator = MappingIterator.emptyIterator();
    }

    @Test
    public void testHasNext() throws IOException {
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testNextValue() throws IOException {
        try {
            mappingIterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testReadAll() throws IOException {
        assertEquals(0, mappingIterator.readAll().size());
    }

    @Test
    public void testReadAllWithList() throws IOException {
        assertEquals(0, mappingIterator.readAll(new ArrayList<Object>()).size());
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        assertEquals(0, mappingIterator.readAll(new HashSet<Object>()).size());
    }

    @Test
    public void testGetParser() {
        assertNull(mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(mappingIterator.getCurrentLocation());
    }
}