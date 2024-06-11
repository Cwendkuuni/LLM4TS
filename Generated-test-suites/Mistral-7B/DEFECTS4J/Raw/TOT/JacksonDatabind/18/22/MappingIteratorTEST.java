package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    private static <T> MappingIterator<T> createMappingIterator(JavaType type, String inputJson)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(inputJson);
        ObjectReader reader = mapper.readerForListOf(type);
        DeserializationContext ctxt = new StdDeserializationContext();
        JsonDeserializer<T> deserializer = reader.findDeserializer(type);
        return (MappingIterator<T>) reader.iterator(parser, deserializer, false, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testEmptyMappingIterator() {
        MappingIterator<?> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertTrue(iterator.isEmpty());
        iterator.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testMappingIteratorHasNext() throws JsonProcessingException {
        JavaType intType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        String inputJson = "[1, 2, 3]";
        MappingIterator<Integer> iterator = createMappingIterator(intType, inputJson);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testMappingIteratorHasNoMoreValues() throws JsonProcessingException {
        JavaType intType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        String inputJson = "[1, 2, 3]";
        MappingIterator<Integer> iterator = createMappingIterator(intType, inputJson);
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testMappingIteratorNext() throws JsonProcessingException {
        JavaType intType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        String inputJson = "[1, 2, 3]";
        MappingIterator<Integer> iterator = createMappingIterator(intType, inputJson);
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertNull(iterator.next());
        iterator.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testMappingIteratorCloseParser() throws JsonProcessingException {
        JavaType intType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        String inputJson = "[1, 2, 3]";
        MappingIterator<Integer> iterator = createMappingIterator(intType, inputJson);
        iterator.close();
        JsonParser parser = iterator.getParser();
        assertFalse(parser.hasTokens());
        parser.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testMappingIteratorNullValueProvider() throws JsonProcessingException {
        JavaType intType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        String inputJson = "[null, 2, 3]";
        MappingIterator<Integer> iterator = createMappingIterator(intType, inputJson);
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        iterator.close();
    }

    @Test
    public void testMappingIteratorExceptionHandling() throws JsonProcessingException {
        JavaType intType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        String inputJson = "[1, \"not a number\", 3]";
        MappingIterator<Integer> iterator = createMappingIterator(intType, inputJson);
        try {
            iterator.next();
            iterator.next();
            iterator.next();
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Could not instantiate value of type [simple type, class java.lang.Integer] from JSON String \"not a number\"", e.getMessage());
        }
        assertFalse(iterator.hasNext());
        iterator.close();
    }
}