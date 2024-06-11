package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory factory;
    private JsonParser parser;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        reader = mapper.readerForListOf(String.class);
        factory = mapper.getFactory();
        parser = factory.createParser("[]");
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        assertTrue(iterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testHasNextValueWithEmptyParser() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        iterator.setParser(null);
        assertFalse(iterator.hasNextValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testHasNextValueWithException() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        parser.nextToken(); // Advance parser to a malformed token
        assertTrue(iterator.hasNextChecked);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        assertEquals(iterator.nextValue(), mapper.readValue(parser, String.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        List<String> result = iterator.readAll();
        assertEquals(4, result.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(Integer.toString(i), result.get(i));
        }
    }

    @Test
    public void testReadAllWithList() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        ArrayList<String> result = new ArrayList<>();
        iterator.readAll(result);
        assertEquals(4, result.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(Integer.toString(i), result.get(i));
        }
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        MappingIterator<String> iterator = reader.iterator();
        ArrayList<String> result = new ArrayList<>();
        iterator.readAll(result);
        assertEquals(4, result.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(Integer.toString(i), result.get(i));
        }
    }

    @Test
    public void testGetParser() {
        MappingIterator<String> iterator = reader.iterator();
        JsonParser parser = iterator.getParser();
        assertSame(parser, iterator._parser);
    }

    @Test
    public void testGetParserSchema() {
        MappingIterator<String> iterator = reader.iterator();
        FormatSchema schema = iterator.getParserSchema();
        assertNotNull(schema);
    }

    @Test
    public void testGetCurrentLocation() {
        MappingIterator<String> iterator = reader.iterator();
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);
    }
}