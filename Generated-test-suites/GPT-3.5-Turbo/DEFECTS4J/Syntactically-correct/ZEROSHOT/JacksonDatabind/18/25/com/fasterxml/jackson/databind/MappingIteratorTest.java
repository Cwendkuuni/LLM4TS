package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MappingIteratorTest {
    private MappingIterator<Integer> iterator;
    private List<Integer> input;

    @Before
    public void setUp() {
        input = new ArrayList<>();
        input.add(1);
        input.add(2);
        input.add(3);
        iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), null,
                null, null, false, null);
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNextWhenEmpty() throws IOException {
        iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        assertEquals(Integer.valueOf(1), iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWhenEmpty() throws IOException {
        iterator = MappingIterator.emptyIterator();
        iterator.next();
    }

    @Test
    public void testRemove() {
        try {
            iterator.remove();
            fail("remove() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("remove", e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        iterator.close();
        iterator.next();
    }

    @Test
    public void testReadAll() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(input, resultList);
    }

    @Test
    public void testReadAllWithExistingList() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        resultList.add(0);
        iterator.readAll(resultList);
        assertEquals(input, resultList.subList(1, resultList.size()));
    }

    @Test
    public void testReadAllWithEmptyIterator() throws IOException {
        iterator = MappingIterator.emptyIterator();
        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testReadAllInCollection() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(input, resultList);
    }

    @Test
    public void testGetParser() {
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(iterator.getCurrentLocation());
    }
}