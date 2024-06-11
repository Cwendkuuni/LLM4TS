package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonNode input;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        String json = "[1, 2, 3, 4]";
        input = objectMapper.readTree(json);
    }

    @Test
    public void testHasNextAndNext() throws IOException {
        Iterator<Integer> mappingIterator = objectMapper.valueIterator(input.asArray(), Integer.class);
        assertTrue(mappingIterator.hasNext());
        assertEquals(1, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());
        assertEquals(2, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());
        assertEquals(3, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());
        assertEquals(4, mappingIterator.next().intValue());
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValueAndNextValue() throws IOException {
        Iterator<Integer> mappingIterator = objectMapper.valueIterator(input.asArray(), Integer.class);
        assertTrue(mappingIterator.hasNextValue());
        assertEquals(1, mappingIterator.nextValue().intValue());
        assertTrue(mappingIterator.hasNextValue());
        assertEquals(2, mappingIterator.nextValue().intValue());
        assertTrue(mappingIterator.hasNextValue());
        assertEquals(3, mappingIterator.nextValue().intValue());
        assertTrue(mappingIterator.hasNextValue());
        assertEquals(4, mappingMapper.nextValue().intValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() throws IOException {
        Iterator<Integer> mappingIterator = objectMapper.valueIterator(input.asArray(), Integer.class);
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
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();

        mappingIterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        ObjectMapper objectMapperWithException = new ObjectMapper();
        ObjectMapper.DefaultTypingContainer defaultTypingContainer = objectMapperWithException.getDefaultTyping();
        ObjectMapper.DefaultTypingContainer.Builder builder = defaultTypingContainer.builder();
        builder.failOnEmptyBeanCollection(true);
        ObjectMapper objectMapperWithFailedTyping = objectMapperWithException.configure(JsonMapper.Feature.FAIL_ON_EMPTY_BEANS, true);
        JsonNode inputWithMappingException = objectMapperWithFailedTyping.readTree("{ \"name\": \"John\", \"age\": null }");
        Iterator<Object> mappingIteratorWithMappingException = objectMapperWithFailedTyping.valueIterator(inputWithMappingException.asObject(), Object.class);
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectMapper objectMapperWithIOException = new ObjectMapper();
        String jsonWithIOException = "{\"name\":\"John\", \"age\":[\"throwException\"]}";
        JsonNode inputWithIOException = objectMapperWithIOException.readTree(jsonWithIOException);
        Iterator<String> mappingIteratorWithIOException = objectMapperWithIOException.valueIterator(inputWithMappingException.paths(), String.class);
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();
        mappingIteratorWithMappingException.next();

        mappingIteratorWithMappingException.next();
    }

    @Test
    public void testReadAllList() throws IOException {
        Iterator<Integer> mappingIterator = objectMapper.valueIterator(input.asArray(), Integer.class);
        List<Integer> list = mappingMapper.readAll(new ArrayList<>());
        assertEquals(4, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals(4, list.get(3).intValue());
    }

    @Test
    public void testReadAllCollection() throws IOException {
        Iterator<Integer> mappingIterator = objectMapper.valueIterator(input.asArray(), Integer.class);
        Collection<Integer> collection = mappingMapper.readAll(new ArrayList<>());
        assertEquals(4, collection.size());
        assertEquals(1, collection.iterator().next().intValue());
        assertEquals(2, collection.iterator().next().intValue());
        assertEquals(3, collection.iterator().next().intValue());
        assertEquals(4, collection.iterator().next().intValue());
    }
}