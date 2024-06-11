package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private MappingIterator<String> emptyIterator;
    private MappingIterator<String> iterator;
    private List<String> resultList;

    @Before
    public void setUp() {
        emptyIterator = MappingIterator.emptyIterator();
        iterator = new MappingIterator<String>(null, null, null, null, false, null);
        resultList = new ArrayList<String>();
    }

    @Test
    public void testHasNext() throws IOException {
        assertFalse(emptyIterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        try {
            emptyIterator.next();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }

        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testRemove() {
        try {
            emptyIterator.remove();
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }

        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testClose() throws IOException {
        emptyIterator.close();
        iterator.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertFalse(emptyIterator.hasNextValue());
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        try {
            emptyIterator.nextValue();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }

        try {
            iterator.nextValue();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testReadAll() throws IOException {
        emptyIterator.readAll(resultList);
        assertTrue(resultList.isEmpty());

        iterator.readAll(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testReadAll2() throws IOException {
        emptyIterator.readAll(Collection<String> results);
        assertTrue(results.isEmpty());

        iterator.readAll(Collection<String> results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetParser() {
        assertNull(emptyIterator.getParser());
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(emptyIterator.getParserSchema());
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(emptyIterator.getCurrentLocation());
        assertNull(iterator.getCurrentLocation());
    }

}