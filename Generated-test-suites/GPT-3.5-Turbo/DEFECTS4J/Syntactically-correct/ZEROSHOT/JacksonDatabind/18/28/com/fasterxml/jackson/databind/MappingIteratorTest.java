package com.fasterxml.jackson.databind;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = createIterator();
        assertTrue(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = createIterator();
        assertTrue(iterator.hasNextValue());
        iterator.close();
    }

    @Test
    public void testNext() throws IOException {
        MappingIterator<String> iterator = createIterator();
        String value = iterator.next();
        assertEquals("test", value);
        iterator.close();
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = createIterator();
        String value = iterator.nextValue();
        assertEquals("test", value);
        iterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> iterator = createIterator();
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.close();
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = createIterator();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAllList() throws IOException {
        MappingIterator<String> iterator = createIterator();
        List<String> resultList = new ArrayList<>();
        List<String> result = iterator.readAll(resultList);
        assertEquals(resultList, result);
        iterator.close();
    }

    @Test
    public void testReadAllCollection() throws IOException {
        MappingIterator<String> iterator = createIterator();
        List<String> result = iterator.readAll(new ArrayList<String>());
        assertEquals(new ArrayList<>(), result);
        iterator.close();
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> iterator = createIterator();
        JsonParser parser = iterator.getParser();
        assertEquals(parser, iterator.getParser());
        iterator.close();
    }

    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> iterator = createIterator();
        FormatSchema schema = iterator.getParserSchema();
        assertEquals(null, schema);
        iterator.close();
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> iterator = createIterator();
        JsonLocation location = iterator.getCurrentLocation();
        assertEquals(null, location);
        iterator.close();
    }

    private MappingIterator<String> createIterator() throws IOException {
        JsonParser parser = null; // Use your own JsonParser
        DeserializationContext context = null; // Use your own DeserializationContext
        JsonDeserializer<String> deserializer = null; // Use your own JsonDeserializer
        boolean managedParser = true; // Use your own flag
        Object valueToUpdate = null; // Use your own value
        return new MappingIterator<>(null, parser, context, deserializer, managedParser, valueToUpdate);
    }
}