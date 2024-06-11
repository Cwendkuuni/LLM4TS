package com.fasterxml.jackson.databind;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = createIterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        MappingIterator<String> iterator = createIterator();
        assertEquals("foo", iterator.next());
        assertEquals("bar", iterator.next());
        assertEquals("baz", iterator.next());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> iterator = createIterator();
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = createIterator();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = createIterator();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = createIterator();
        assertEquals("foo", iterator.nextValue());
        assertEquals("bar", iterator.nextValue());
        assertEquals("baz", iterator.nextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = createIterator();
        List<String> list = iterator.readAll();
        assertEquals(3, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
        assertEquals("baz", list.get(2));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        MappingIterator<String> iterator = createIterator();
        ArrayList<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(3, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
        assertEquals("baz", list.get(2));
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> iterator = createIterator();
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> iterator = createIteratorWithSchema("[]");
        FormatSchema schema = iterator.getParserSchema();
        assertNotNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> iterator = createIterator();
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);
    }

    private MappingIterator<String> createIterator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\n" +
                "  \"foo\",\n" +
                "  \"bar\",\n" +
                "  \"baz\"\n" +
                "]");
        return mapper.getValueIterator(node);
    }

    private MappingIterator<String> createIteratorWithSchema(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(content);
        return mapper.getValueIterator(node);
    }
}