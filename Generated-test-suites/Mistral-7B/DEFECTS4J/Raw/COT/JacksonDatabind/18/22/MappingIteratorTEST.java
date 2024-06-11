package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("[]");
        mappingIterator = objectMapper.readValue(jsonParser, MappingIterator.class);
    }

    @Test
    public void testHasNext() {
        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(mappingIterator.hasNextValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNext() throws IOException {
        String next = mappingIterator.next();
        assertNull(next);

        mappingIterator = objectMapper.readValue(jsonParser, MappingIterator.class);
        String nextValue = mappingIterator.next();
        assertEquals("", nextValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();

        mappingIterator.next(); // This should throw NoSuchElementException
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> list = mappingIterator.readAll();
        assertEquals(Collections.emptyList(), list);

        jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        mappingIterator = objectMapper.readValue(jsonParser, MappingIterator.class);
        list = mappingIterator.readAll();
        assertEquals(Arrays.asList("test1", "test2"), list);
    }

    @Test
    public void testReadAllWithArrayList() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        mappingIterator.readAll(arrayList);
        assertEquals(Collections.emptyList(), arrayList);

        jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        mappingIterator = objectMapper.readValue(jsonParser, MappingIterator.class);
        mappingIterator.readAll(arrayList);
        assertEquals(Arrays.asList("test1", "test2"), arrayList);
    }

    @Test
    public void testReadAllWithLinkedList() throws IOException {
        List<String> list = new LinkedList<>();
        mappingIterator.readAll(list);
        assertEquals(Collections.emptyList(), list);

        jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
        mappingIterator = objectMapper.readValue(jsonParser, MappingIterator.class);
        mappingIterator.readAll(list);
        assertEquals(Arrays.asList("test1", "test2"), list);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMappingException() throws IOException {
        JsonMappingException jsonMappingException = new JsonMappingException("Test mapping exception");
        mappingIterator._handleMappingException(jsonMappingException);
    }

    @Test(expected = IOException.class)
    public void testHandleIOException() throws IOException {
        IOException ioException = new IOException("Test I/O exception");
        mappingIterator._handleIOException(ioException);
    }
}