package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());

        iterator = createIterator(String[].class);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        assertEquals("value1", iterator.next());
        iterator.next();
        assertEquals("value2", iterator.next());
        assertNull(iterator.next());

        iterator = createIterator(String[].class);
        Object[] expected = new Object[]{"value1", "value2", "value3"};
        assertArrayEquals(expected, iterator.nextValues(3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        List<String> list = iterator.readAll();
        assertEquals(Arrays.asList("value1", "value2"), list);
    }

    @Test
    public void testReadAllArrayList() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        ArrayList<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(Arrays.asList("value1", "value2"), list);
    }

    @Test
    public void testReadAllLinkedList() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        List<String> list = new LinkedList<>();
        iterator.readAll(list);
        assertEquals(Arrays.asList("value1", "value2"), list);
    }

    @Test
    public void testReadAllCopyOnWriteArrayList() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        List<String> list = new CopyOnWriteArrayList<>();
        iterator.readAll(list);
        assertEquals(Arrays.asList("value1", "value2"), list);
    }

    @Test
    public void testReadAllVector() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        List<String> list = new Vector<>();
        iterator.readAll(list);
        assertEquals(Arrays.asList("value1", "value2"), list);
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        FormatSchema schema = iterator.getParserSchema();
        assertNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> iterator = createIterator(String.class);
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);
    }

    private MappingIterator<T> createIterator(Class<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<T> deserializer = mapper.findValueDeserializer(type);
        MappingIterator<T> iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, null);
        return iterator;
    }
}