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
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNext() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), null, true, null);
        assertTrue(iterator.hasNext());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNext_ManagedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), null, true, null);
        assertFalse(iterator.hasNext());
        parser.nextToken();
        assertTrue(iterator.hasNext());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertFalse(iterator.hasNextValue());
        parser.nextToken();
        assertTrue(iterator.hasNextValue());
        parser.nextToken();
        assertFalse(iterator.hasNextValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue_ManagedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertFalse(iterator.hasNextValue());
        parser.nextToken();
        parser.nextToken();
        assertFalse(iterator.hasNextValue());
        parser.clearCurrentToken();
        assertTrue(iterator.hasNextValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNext_ManagedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertEquals("one", iterator.nextValue());
        assertEquals("two", iterator.nextValue());
        assertEquals("three", iterator.nextValue());
        assertFalse(iterator.hasNextValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextValue_ManagedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertEquals("one", iterator.nextValue());
        assertEquals("two", iterator.nextValue());
        assertEquals("three", iterator.nextValue());
        assertFalse(iterator.hasNextValue());
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        List<String> resultList = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("one", resultList.get(0));
        assertEquals("two", resultList.get(1));
        assertEquals("three", resultList.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAll_ManagedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        List<String> resultList = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("one", resultList.get(0));
        assertEquals("two", resultList.get(1));
        assertEquals("three", resultList.get(2));
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAllCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        Collection<String> resultList = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("one", resultList.iterator().next());
        assertEquals("two", resultList.iterator().next());
        assertEquals("three", resultList.iterator().next());
        parser.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertSame(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetParserCurrentLocation() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("['one','two','three']");
        MappingIterator<String> iterator = new MappingIterator<>(mapper.getTypeFactory().constructCollectionType(List.class, String.class), parser, mapper.getDeserializationContext(), StringDeserializer.instance, true, null);
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);
        assertEquals(LocationType.START_ARRAY, location.getType());
        assertEquals(0, location.getLineNumber());
        assertEquals(0, location.getColumnNumber());
    }
}