package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        List<Object> resultList = iterator.readAll();
        assertNotNull(resultList);
    }

    @Test
    public void testReadAllWithList() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        List<Object> resultList = new ArrayList<Object>();
        List<Object> output = iterator.readAll(resultList);
        assertSame(resultList, output);
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        Collection<Object> results = new ArrayList<Object>();
        Collection<Object> output = iterator.readAll(results);
        assertSame(results, output);
    }

    @Test
    public void testGetParser() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertNull(iterator.getCurrentLocation());
    }
}