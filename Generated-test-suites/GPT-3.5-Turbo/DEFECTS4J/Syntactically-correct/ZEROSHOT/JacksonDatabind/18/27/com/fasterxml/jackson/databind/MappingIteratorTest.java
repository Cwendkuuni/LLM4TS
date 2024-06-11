package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);

        Iterator<String> valuesIterator = values.iterator();

        while (valuesIterator.hasNext()) {
            assertTrue(iterator.hasNext());
            valuesIterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);

        Iterator<String> valuesIterator = values.iterator();

        while (valuesIterator.hasNext()) {
            assertEquals(valuesIterator.next(), iterator.next());
        }

        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // Test passed
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.close();
        assertNull(iterator.getParser());
    }

    @Test
    public void testHasNextValue() throws IOException {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);

        Iterator<String> valuesIterator = values.iterator();

        while (valuesIterator.hasNext()) {
            assertTrue(iterator.hasNextValue());
            valuesIterator.next();
        }

        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);

        Iterator<String> valuesIterator = values.iterator();

        while (valuesIterator.hasNext()) {
            assertEquals(valuesIterator.next(), iterator.nextValue());
        }

        try {
            iterator.nextValue();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Test passed
        }
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        assertEquals(values, resultList);
    }

    @Test
    public void testGetParser() {
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(iterator.getCurrentLocation());
    }
}