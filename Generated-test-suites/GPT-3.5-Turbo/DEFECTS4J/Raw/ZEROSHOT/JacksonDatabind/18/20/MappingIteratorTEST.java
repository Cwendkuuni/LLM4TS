package com.fasterxml.jackson.databind;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {

    MappingIterator<Object> iterator;

    @Before
    public void setUp() {
        iterator = new MappingIterator<Object>(null, null, null, null, false, null);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHasNextValue() throws IOException {
        // Test when the parser is null
        assertFalse(iterator.hasNextValue());

        // Test when the parser has no more tokens
        iterator = new MappingIterator<Object>(null, null, null, null, true, null);
        assertTrue(iterator.hasNextValue());

        // Test when the parser has more tokens
        iterator = new MappingIterator<Object>(null, null, null, null, true, null);
        iterator._hasNextChecked = true;
        assertTrue(iterator.hasNextValue());
    }

    @Test(expected=NoSuchElementException.class)
    public void testNextValue() throws IOException {
        // Test when there is no next value
        iterator.nextValue();

        // Test when there is a next value
        iterator._parser = new JsonParser(null);
        iterator._hasNextChecked = true;
        iterator.nextValue();
    }

    @Test
    public void testReadAll() throws IOException {
        // Test when there are no elements to read
        List<Object> resultList = iterator.readAll();
        assertTrue(resultList.isEmpty());

        // Test when there are elements to read
        iterator._parser = new JsonParser(null);
        resultList = iterator.readAll();
        assertTrue(resultList.isEmpty());

        // Test when there are elements to read
        iterator._parser = new JsonParser(MockJsonGenerator.empty().toString().getBytes());
        resultList = iterator.readAll();
        assertTrue(resultList.isEmpty());

        // Test when there are elements to read
        iterator._parser = new JsonParser(MockJsonGenerator.singleValue("value").toString().getBytes());
        resultList = iterator.readAll();
        assertFalse(resultList.isEmpty());
    }

    @Test
    public void testGetParser() {
        // Test when the parser is null
        assertNull(iterator.getParser());

        // Test when the parser is not null
        iterator._parser = new JsonParser(null);
        assertNotNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        // Test when the schema is null
        assertNull(iterator.getParserSchema());

        // Test when the schema is not null
        iterator._parser = new JsonParser(null);
        assertNotNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        // Test when the current location is null
        assertNull(iterator.getCurrentLocation());

        // Test when the current location is not null
        iterator._parser = new JsonParser(null);
        assertNotNull(iterator.getCurrentLocation());
    }

}