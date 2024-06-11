package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), null, true, null);
        assertTrue(iterator.hasNext());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextThrowsJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John\", \"age\": 30 }");
        MappingIterator<Object> iterator = new MappingIterator<>(mapper.getTypeFactory().constructValueType(String.class), parser, mapper.getDeserializationContext(), null, true, null);
        iterator.next();
        JsonMappingException e = new JsonMappingException("Test Error");
        when(parser.nextToken()).thenThrow(e);
        try {
            iterator.next();
            fail();
        } catch (JsonMappingException ex) {
            assertEquals(e.getMessage(), ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextThrowsIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John\", \"age\": 30 }");
        MappingIterator<Object> iterator = new MappingIterator<>(mapper.getTypeFactory().constructValueType(String.class), parser, mapper.getDeserializationContext(), null, true, null);
        iterator.next();
        IOException e = new IOException("Test Error");
        when(parser.nextToken()).thenThrow(e);
        try {
            iterator.next();
            fail();
        } catch (RuntimeException ex) {
            assertEquals(e.getMessage(), ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextValueThrowsJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John\", \"age\": 30 }");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructValueType(String.class), parser, mapper.getDeserializationContext(), null, true, null);
        iterator.nextValue();
        JsonMappingException e = new JsonMappingException("Test Error");
        when(parser.getCurrentToken()).thenReturn(null);
        when(parser.nextToken()).thenThrow(e);
        try {
            iterator.nextValue();
            fail();
        } catch (JsonMappingException ex) {
            assertEquals(e.getMessage(), ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextValueThrowsIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John\", \"age\": 30 }");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructValueType(String.class), parser, mapper.getDeserializationContext(), null, true, null);
        iterator.nextValue();
        IOException e = new IOException("Test Error");
        when(parser.getCurrentToken()).thenReturn(null);
        when(parser.nextToken()).thenThrow(e);
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeException ex) {
            assertEquals(e.getMessage(), ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), null, true, null);
        List<String> result = iterator.readAll();
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAllWithEmptyList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), null, true, null);
        List<String> result = iterator.readAll();
        assertEquals(0, result.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAllWithCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        Collection<String> result = iterator.readAll(new ArrayList<>());
        assertEquals(3, result.size());
        assertEquals("apple", result.iterator().next());
        assertEquals("banana", result.iterator().next());
        assertEquals("cherry", result.iterator().next());
    }

    @Test
    public void testCloseParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John\", \"age\": 30 }");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructValueType(String.class), parser, mapper.getDeserializationContext(), null, true, null);
        iterator.nextValue();
        iterator.close();
        assertFalse(parser.isOpen());
    }

    @Test
    public void testCloseParserNotManaged() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"name\": \"John\", \"age\": 30 }");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructValueType(String.class), parser, mapper.getDeserializationContext(), null, false, null);
        iterator.nextValue();
        iterator.close();
        assertTrue(parser.isOpen());
    }
}