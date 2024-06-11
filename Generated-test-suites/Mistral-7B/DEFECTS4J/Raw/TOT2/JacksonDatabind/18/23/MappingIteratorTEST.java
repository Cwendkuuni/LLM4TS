package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.Contextualizer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    private static <T> MappingIterator<T> createIterator(JavaType type, ObjectMapper mapper) throws IOException {
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<T> deserializer = (JsonDeserializer<T>) mapper.findValueDeserializer(type, null);
        return new MappingIterator<>(type, parser, ctxt, deserializer, false, null);
    }

    @org.junit.Test
    public void testHasNextAndNextReturnsTrue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), 0);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), 1);
        assertTrue(iterator.hasNext());
        // Test reading all elements
        List<Integer> list = iterator.readAll();
        assertEquals(3, list.size());
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
    }

    @org.junit.Test
    public void testHasNextAndNextReturnsFalse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        // Test empty iterator
        assertFalse(iterator.hasNext());
        assertEquals(null, iterator.next());
        // Test iterator with no more elements
        iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testCloseParserWhenManaged() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        JsonParser parser = iterator.getParser();
        iterator.close();
        assertFalse(parser.isOpen());
    }

    @org.junit.Test
    public void testHasNextValueThrowsIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        iterator.closeParser = true;
        iterator.close();
        assertFalse(iterator.hasNextValue());
    }

    @org.junit.Test
    public void testHasNextValueReadsNextToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        assertTrue(iterator.hasNextValue());
        assertEquals(JsonToken.START_ARRAY, iterator.getParser().getCurrentToken());
    }

    @org.junit.Test
    public void testHasNextValueThrowsJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<String> iterator = createIterator(mapper.getTypeFactory().constructValueType(String.class, "Invalid"), mapper);
        try {
            iterator.hasNextValue();
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize instance of java.lang.String out of value of type [simple type, class java.lang.String]\n at [Source: (String)" +
                            "\"\"; line: 1, column: 1]", e.getMessage());
        }
    }

    @org.junit.Test
    public void testHasNextValueReturnsFalse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        assertFalse(iterator.hasNextValue());
    }

    @org.junit.Test
    public void testNextValueReadsNextObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        assertEquals(0, iterator.nextValue().intValue());
        assertEquals(1, iterator.nextValue().intValue());
        assertEquals(2, iterator.nextValue().intValue());
    }

    @org.junit.Test
    public void testNextValueReturnsNullWhenClosed() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        iterator.close();
        assertNull(iterator.nextValue());
    }

    @org.junit.Test
    public void testNextValueReturnsNullWhenNoMoreElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        iterator.nextValue();
        iterator.nextValue();
        assertNull(iterator.nextValue());
    }

    @org.junit.Test
    public void testNextValueReturnsUpdatedValueWhenProvided() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        Integer updatedValue = 5;
        assertEquals(0, iterator.nextValue().intValue());
        iterator.nextValue();
        assertEquals(2, iterator.nextValue().intValue());
        iterator.nextValue();
        assertEquals(updatedValue, iterator.nextValue().intValue());
    }

    @org.junit.Test
    public void testReadAllReturnsEmptyListWhenEmptyIterator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = (MappingIterator<Integer>) MappingIterator.emptyIterator();
        assertEquals(0, iterator.readAll().size());
    }

    @org.junit.Test
    public void testReadAllReturnsArrayListWhenSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        List<Integer> list = iterator.readAll();
        assertEquals(1, list.size());
        assertEquals(0, list.get(0).intValue());
    }

    @org.junit.Test
    public void testReadAllReturnsArrayListWhenMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterDeserializers();
        MappingIterator<Integer> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), mapper);
        List<Integer> list = iterator.readAll();
        assertEquals(3, list.size());
        assertEquals(0, list.get(0).intValue());
        assertEquals(1, list.get(1).intValue());
        assertEquals(2, list.get(2).intValue());
    }

    @org.junit.Test
    public void testReadAllReturnsSpecifiedCollectionWhenProvided() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MappingIterator<String> iterator = createIterator(mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class), mapper);
        assertEquals(Collections.emptyList(), iterator.readAll(Collections.emptyList()));
        ArrayList<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        list.add("test3");
        assertEquals(list, iterator.readAll(list));
    }
}