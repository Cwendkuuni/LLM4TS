package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.util.Converter;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    private static <T> MappingIterator<T> createIterator(Class<T> type, ObjectMapper mapper)
            throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        return new MappingIterator<>(type, jsonParser, mapper.getDeserializationContext(),
                StdDeserializer.instance(type), false, null);
    }

    @Test
    public void testHasNextAndNext() {
        MappingIterator<Integer> it = createIterator(Integer.class, new ObjectMapper());
        assertTrue(it.hasNext());
        assertEquals((Integer) 0, it.next());
        assertTrue(it.hasNext());
        // Test that we can move past all elements
        for (int i = 0; i < 100; i++) {
            it.next();
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testHasNextValueAndNextValue() throws IOException {
        MappingIterator<Integer> it = createIterator(Integer.class, new ObjectMapper());
        assertTrue(it.hasNextValue());
        assertEquals((Integer) 0, it.nextValue());
        assertTrue(it.hasNextValue());
        // Test that we can move past all elements
        for (int i = 0; i < 100; i++) {
            it.nextValue();
        }
        assertFalse(it.hasNextValue());
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<Integer> it = MappingIterator.emptyIterator();
        assertFalse(it.hasNext());
        assertFalse(it.hasNextValue());
    }

    @Test
    public void testCloseParser() throws IOException {
        MappingIterator<Integer> it = createIterator(Integer.class, new ObjectMapper());
        it.close();
        assertFalse(it.hasNext());
        assertFalse(it.hasNextValue());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testHasNextValueThrowsException() throws IOException {
        MappingIterator<Integer> it = createIterator(Integer.class, new ObjectMapper());
        it._parser = null;
        try {
            it.hasNextValue();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testNextValueThrowsException() throws IOException {
        MappingIterator<Integer> it = createIterator(Integer.class, new ObjectMapper());
        it._parser = null;
        try {
            it.nextValue();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MappingIterator<Integer> it = createIterator(Integer.class, mapper);
        List<Integer> result = it.readAll();
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, result);
    }

    @Test
    public void testReadAllWithCustomList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MappingIterator<Integer> it = createIterator(Integer.class, mapper);
        ArrayList<Integer> result = new ArrayList<>();
        it.readAll(result);
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, result);
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MappingIterator<Integer> it = createIterator(Integer.class, mapper);
        ArrayList<Integer> result = new ArrayList<>();
        it.readAll(result);
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, result);

        List<Integer> customList = Collections.singletonList(10);
        it.readAll(customList);
        assertEquals(Collections.singletonList(10), customList);
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<Integer> it = createIterator(Integer.class, new ObjectMapper());
        JsonParser parser = it.getParser();
        assertSame(parser, it._parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{schema: []}");
        MappingIterator<Integer> it = new MappingIterator<>(Integer.class, jsonParser,
                mapper.getDeserializationContext(), StdDeserializer.instance(Integer.class),
                false, null);
        FormatSchema schema = it.getParserSchema();
        assertNotNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        MappingIterator<Integer> it = new MappingIterator<>(Integer.class, jsonParser,
                mapper.getDeserializationContext(), StdDeserializer.instance(Integer.class),
                false, null);
        JsonLocation location = it.getCurrentLocation();
        assertNotNull(location);
    }
}