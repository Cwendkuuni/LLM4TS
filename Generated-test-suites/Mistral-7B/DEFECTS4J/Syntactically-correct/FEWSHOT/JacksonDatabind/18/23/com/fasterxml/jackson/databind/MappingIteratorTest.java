package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextAndNext() throws IOException {
        JsonParser parser = createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<>(parser.getCodec().constructType(Object.class), parser, null, null, true, null);
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertNull(next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNull(next);
        assertFalse(iterator.hasNext());
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue() throws IOException {
        JsonParser parser = createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<>(parser.getCodec().constructType(Object.class), parser, null, null, true, null);
        assertFalse(iterator.hasNextValue());
        parser.nextToken();
        assertTrue(iterator.hasNextValue());
        parser.nextToken();
        assertFalse(iterator.hasNextValue());
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextValue() throws IOException {
        JsonParser parser = createParser("[\n" +
                "  {\"name\": \"Alice\", \"age\": 30},\n" +
                "  {\"name\": \"Bob\", \"age\": 25}\n" +
                "]");
        MappingIterator<Object> iterator = new MappingIterator<>(parser.getCodec().constructType(Object.class), parser, null, null, true, null);
        Object next = iterator.nextValue();
        Map<String, Object> alice = (Map<String, Object>) next;
        assertEquals("Alice", alice.get("name"));
        assertEquals(30, alice.get("age"));
        next = iterator.nextValue();
        Map<String, Object> bob = (Map<String, Object>) next;
        assertEquals("Bob", bob.get("name"));
        assertEquals(25, bob.get("age"));
        assertNull(iterator.nextValue());
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAll() throws IOException {
        JsonParser parser = createParser("[\n" +
                "  {\"name\": \"Alice\", \"age\": 30},\n" +
                "  {\"name\": \"Bob\", \"age\": 25}\n" +
                "]");
        List<Object> resultList = new ArrayList<>();
        MappingIterator<Object> iterator = new MappingIterator<>(parser.getCodec().constructType(List.class), parser, null, null, true, null);
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        Map<String, Object> alice = (Map<String, Object>) resultList.get(0);
        assertEquals("Alice", alice.get("name"));
        assertEquals(30, alice.get("age"));
        Map<String, Object> bob = (Map<String, Object>) resultList.get(1);
        assertEquals("Bob", bob.get("name"));
        assertEquals(25, bob.get("age"));
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAllCollection() throws IOException {
        JsonParser parser = createParser("[\n" +
                "  {\"name\": \"Alice\", \"age\": 30},\n" +
                "  {\"name\": \"Bob\", \"age\": 25}\n" +
                "]");
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        MappingIterator<Map<String, Object>> iterator = new MappingIterator<>(parser.getCodec().constructType(List.class.getTypeParameters()[0]), parser, null, null, true, null);
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        Map<String, Object> alice = resultList.get(0);
        assertEquals("Alice", alice.get("name"));
        assertEquals(30, alice.get("age"));
        Map<String, Object> bob = resultList.get(1);
        assertEquals("Bob", bob.get("name"));
        assertEquals(25, bob.get("age"));
        parser.close();
    }

    private JsonParser createParser(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }
}