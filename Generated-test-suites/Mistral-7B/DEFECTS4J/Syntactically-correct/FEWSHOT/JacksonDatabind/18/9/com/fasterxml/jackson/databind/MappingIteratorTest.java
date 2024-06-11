package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        String next = iterator.next();
        assertEquals("expected value", next);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        String value = iterator.nextValue();
        assertEquals("expected value", value);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        List<String> resultList = iterator.readAll();
        assertEquals(2, resultList.size());
        assertEquals("expected value 1", resultList.get(0));
        assertEquals("expected value 2", resultList.get(1));
    }

    @Test
    public void testReadAllArrayList() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        ArrayList<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("expected value 1", resultList.get(0));
        assertEquals("expected value 2", resultList.get(1));
    }

    @Test
    public void testReadAllLinkedList() throws IOException {
        MappingIterator<String> iterator = createMappingIterator();
        LinkedList<String> resultList = new LinkedList<>();
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("expected value 1", resultList.getFirst());
        assertEquals("expected value 2", resultList.getLast());
    }

    private MappingIterator<String> createMappingIterator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String json = "[\n" +
                "  \"expected value 1\",\n" +
                "  \"expected value 2\"\n" +
                "]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        return new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), jsonParser, null, null, true, null);
    }
}