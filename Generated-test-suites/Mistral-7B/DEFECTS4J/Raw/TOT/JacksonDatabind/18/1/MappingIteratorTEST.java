package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory factory;
    private JsonParser parser;
    private MappingIterator<Integer> iterator;
    private MappingIterator<String> stringIterator;
    private MappingIterator<Object> objectIterator;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        factory = mapper.getFactory();
        reader = mapper.readerForListOf(Integer.class);
        parser = factory.createParser("[]");
        iterator = reader.readValues(parser).next();
        parser.close();
        reader = mapper.readerForListOf(String.class);
        stringIterator = reader.readValues(parser).next();
        parser.close();
        reader = mapper.readerForValueType(Object.class);
        objectIterator = reader.readValues(parser).next();
        parser.close();
    }

    @Test
    public void testIterateOverEmptyArray() {
        MappingIterator<?> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testIterateOverIterableArray() throws IOException {
        JsonNode node = factory.createValue(new int[]{1, 2, 3, 4, 5});
        ObjectReader reader = mapper.readerForListOf(Integer.class);
        MappingIterator<Integer> iterator = reader.readValues(parserFrom(node)).next();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateOverEmptyList() throws IOException {
        JsonNode node = factory.createValue(Collections.emptyList());
        ObjectReader reader = mapper.readerForListOf(String.class);
        MappingIterator<String> iterator = reader.readValues(parserFrom(node)).next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateOverList() throws IOException {
        JsonNode node = factory.createValue(Arrays.asList("apple", "banana", "cherry"));
        ObjectReader reader = mapper.readerForListOf(String.class);
        MappingIterator<String> iterator = reader.readValues(parserFrom(node)).next();
        assertTrue(iterator.hasNext());
        assertEquals("apple", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("banana", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("cherry", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateOverNullIterator() {
        MappingIterator<?> nullIterator = MappingIterator.emptyIterator();
        assertFalse(nullIterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethod() {
        iterator.remove();
    }

    @Test
    public void testCloseParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("[]");
        ObjectReader reader = mapper.readerForListOf(Integer.class);
        MappingIterator<Integer> iterator = reader.readValues(parser).next();
        assertFalse(iterator.hasNext());
        parser.close();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectReader reader = mapper.readerForListOf(Integer.class);
        List<Integer> list = iterator.readAll();
        assertEquals(5, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals(4, list.get(3).intValue());
        assertEquals(5, list.get(4).intValue());
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectReader reader = mapper.readerForListOf(Integer.class);
        ArrayList<Integer> list = new ArrayList<Integer>(5);
        iterator.readAll(list);
        assertEquals(5, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals(4, list.get(3).intValue());
        assertEquals(5, list.get(4).intValue());
    }

    @Test
    public void testReadAllWithDeserializer() throws IOException {
        ObjectReader reader = mapper.readerForListOf(JsonNode.class);
        List<JsonNode> list = iterator.readAll();
        assertEquals(5, list.size());
        JsonNode first = list.get(0);
        JsonNode second = list.get(1);
        JsonNode third = list.get(2);
        JsonNode fourth = list.get(3);
        JsonNode fifth = list.get(4);
        assertNotNull(first);
        assertNotNull(second);
        assertNotNull(third);
        assertNotNull(fourth);
        assertNotNull(fifth);
        assertFalse(first.isNull());
        assertFalse(second.isNull());
        assertFalse(third.isNull());
        assertFalse(fourth.isNull());
        assertFalse(fifth.isNull());
    }

    @Test
    public void testHandleMappingException() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        reader.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        reader = mapper.readerForValueType(String.class);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextValue("null");
        JsonMappingException ex = new JsonMappingException("test error");
        try {
            reader.readValues(parser).next();
        } catch (JsonMappingException e) {
            assertEquals("test error", e.getMessage());
            assertEquals(ex.getOriginalMessage(), reader.readValues(parser).nextValue().getValueAsString());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMappingExceptionWithNullMessage() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        reader.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        reader = mapper.readerForValueType(String.class);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextValue("null");
        JsonMappingException ex = new JsonMappingException();
        try {
            reader.readValues(parser).next();
        } catch (JsonMappingException e) {
            fail("Unexpected mapping exception: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testHandleIOException() {
        ObjectReader reader = mapper.readerForListOf(Integer.class);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        try {
            reader.readValues(parser).next();
        } catch (JsonMappingException e) {
            reader.close();
            parser.close();
            reader = mapper.readerForListOf(Integer.class);
            reader.readValues(parser).next();
        }
        parser.nextValue("test");
        IOException ex = new IOException("test error");
        try {
            reader.readValues(parser).next();
        } catch (IOException e) {
            assertEquals("test error", e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testHandleIOExceptionWithNullMessage() {
        ObjectReader reader = mapper.readerForListOf(Integer.class);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        try {
            reader.readValues(parser).next();
        } catch (JsonMappingException e) {
            reader.close();
            parser.close();
            reader = mapper.readerForListOf(Integer.class);
            reader.readValues(parser).next();
        }
        parser.nextValue("test");
        IOException ex = new IOException();
        try {
            reader.readValues(parser).next();
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    private JsonParser parserFrom(JsonNode node) {
        try {
            return mapper.getFactory().createParser(node.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}