package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void hasNextTest() {
        MappingIterator<Integer> mappingIterator = new MappingIterator<>();
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void nextTest() {
        MappingIterator<String> mappingIterator = new MappingIterator<>();
        assertNull(mappingIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeTest() {
        MappingIterator<Double> mappingIterator = new MappingIterator<>();
        mappingIterator.remove();
    }

    @Test
    public void closeTest() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>();
        assertDoesNotThrow(() -> mappingIterator.close());
    }

    @Test
    public void hasNextValueTest() {
        MappingIterator<Boolean> mappingIterator = new MappingIterator<>();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void nextValueTest() {
        MappingIterator<Integer> mappingIterator = new MappingIterator<>();
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void readAllTest() {
        MappingIterator<String> mappingIterator = new MappingIterator<>();
        assertDoesNotThrow(() -> mappingIterator.readAll());
    }

    @Test
    public void readAllWithListTest() {
        MappingIterator<Integer> mappingIterator = new MappingIterator<>();
        assertNotNull(mappingIterator.readAll(new ArrayList<>()));
    }

    @Test
    public void readAllWithCollectionTest() {
        MappingIterator<Double> mappingIterator = new MappingIterator<>();
        assertNotNull(mappingIterator.readAll(new HashSet<>()));
    }

    @Test
    public void getParserTest() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>();
        assertNull(mappingIterator.getParser());
    }

    @Test
    public void getParserSchemaTest() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>();
        assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void getCurrentLocationTest() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>();
        assertNull(mappingIterator.getCurrentLocation());
    }
}