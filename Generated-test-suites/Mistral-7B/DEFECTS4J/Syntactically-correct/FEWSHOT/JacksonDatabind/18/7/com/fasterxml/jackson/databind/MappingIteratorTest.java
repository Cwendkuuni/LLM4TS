package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    private static <T> MappingIterator<T> createMappingIterator(ObjectMapper mapper, Iterable<T> valueSource) {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, T.class);
        return mapper.readerForListOf(javaType).readValues(valueSource.iterator());
    }

    @Test
    public void testHasNext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testNext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertNull(iterator.next());

        iterator.close();
    }

    @Test
    public void testClose() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        assertEquals("one", iterator.next());
        iterator.close();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());

        iterator.close();
        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testNextValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        assertEquals("one", iterator.nextValue());
        assertEquals("two", iterator.nextValue());
        assertEquals("three", iterator.nextValue());
        assertNull(iterator.nextValue());

        iterator.close();
        assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        List<String> result = iterator.readAll();
        assertEquals(values, result);

        iterator.close();
    }

    @Test
    public void testReadAllArrayList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        ArrayList<String> result = new ArrayList<>();
        iterator.readAll(result);
        assertEquals(values, result);

        iterator.close();
    }

    @Test
    public void testReadAllCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        Collection<String> result = new ArrayList<>();
        iterator.readAll(result);
        assertEquals(values, result);

        iterator.close();
    }

    @Test
    public void testGetParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);

        iterator.close();
    }

    @Test
    public void testGetParserSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        FormatSchema schema = iterator.getParserSchema();
        assertNotNull(schema);

        iterator.close();
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);

        iterator.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        List<String> values = Arrays.asList("one", "two", "three");

        MappingIterator<String> iterator = createMappingIterator(mapper, values.iterator());
        iterator.remove();
    }
}