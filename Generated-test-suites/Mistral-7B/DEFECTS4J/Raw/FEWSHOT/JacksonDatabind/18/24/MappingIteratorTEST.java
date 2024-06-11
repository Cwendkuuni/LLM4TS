package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.util.*;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
    }

    @Test
    public void testHasNextAndNextWithValidParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, null, null, true, null);

        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test3", iterator.next());
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testHasNextAndNextWithManagedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JsonParser managedParser = parser;
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, managedParser, null, null, true, null);

        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test3", iterator.next());
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testHasNextAndNextWithNullParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, null, null, true, null);

        iterator.next();
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, null, null, true, null);

        iterator.close();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testNextValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, null, null, true, null);

        assertTrue(iterator.hasNextValue());
        assertEquals("test", iterator.nextValue());
        assertTrue(iterator.hasNextValue());
        assertEquals
                ("test2", iterator.nextValue());
        assertTrue(iterator.hasNextValue());
        assertEquals
                ("test3", iterator.nextValue());
        assertFalse(iterator.hasNextValue());

        iterator.close();
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, null, null, true, null);

        List<String> resultList = iterator.readAll();
        List<String> expectedList = Arrays.asList("test", "test2", "test3");

        assertEquals(expectedList.size(), resultList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), resultList.get(i));
        }

        iterator.close();
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.readerForListOf(String.class).readValueAs(JsonNode.class).asParser();
        JavaType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, null, null, true, null);

        Collection<String> results = iterator.readAll(new ArrayList<>());
        List<String> expectedList = Arrays.asList("test", "test2", "test3");

        assertEquals(expectedList.size(), results.size());
        for (String s : results) {
            assertTrue(expectedList.contains(s));
        }

        iterator.close();
    }
}