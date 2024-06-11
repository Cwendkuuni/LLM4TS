package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceUntil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @SuppressWarnings("unchecked")
    private static <T> Iterator<T> createMappingIterator(JsonParser parser, Class<T> type) {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        JsonDeserializer<T> deserializer = new None<>();
        return new MappingIterator<T>(null, parser, ctxt, deserializer, false, null);
    }

    @org.junit.Test
    public void testEmptyIterator() {
        Iterator<Object> it = MappingIterator.emptyIterator();
        assertFalse(it.hasNext());
        assertNull(it.next());
    }

    @org.junit.Test
    public void testIteratorClosed() throws IOException {
        String input = "[\"foo\",\"bar\"]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        it.close();
        assertFalse(it.hasNext());
        assertNull(it.next());
        parser.close();
    }

    @org.junit.Test
    public void testSingleValueIterator() throws IOException {
        String input = "[\"foo\"]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        List<Object> values = new ArrayList<>();
        values.add(it.next());
        assertFalse(it.hasNext());
        assertEquals("foo", values.get(0));
        parser.close();
    }

    @org.junit.Test
    public void testArrayOfValuesIterator() throws IOException {
        String input = "[\"foo\",\"bar\"]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        List<Object> values = new ArrayList<>();
        while (it.hasNext()) {
            values.add(it.next());
        }
        assertFalse(it.hasNext());
        assertEquals(2, values.size());
        assertEquals("foo", values.get(0));
        assertEquals("bar", values.get(1));
        parser.close();
    }

    @org.junit.Test
    public void testListOfValuesIterator() throws IOException {
        String input = "[[\"foo\"],[\"bar\",\"baz\"]]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        List<List<Object>> values = new ArrayList<>();
        while (it.hasNext()) {
            values.add((List<Object>) it.next());
        }
        assertFalse(it.hasNext());
        assertEquals(2, values.size());
        assertEquals(1, values.get(0).size());
        assertEquals("foo", values.get(0).get(0));
        assertEquals(3, values.get(1).size());
        assertEquals("bar", values.get(1).get(0));
        assertEquals("baz", values.get(1).get(2));
        parser.close();
    }

    @org.junit.Test
    public void testMappingIteratorWithNullValues() throws IOException {
        String input = "[null, null]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        List<Object> values = new ArrayList<>();
        while (it.hasNext()) {
            values.add(it.next());
        }
        assertFalse(it.hasNext());
        assertEquals(2, values.size());
        assertEquals(null, values.get(0));
        assertEquals(null, values.get(1));
        parser.close();
    }

    @org.junit.Test
    public void testMappingIteratorWithJsonMappingException() throws IOException {
        String input = "[{ \"@type\": \"java.util.Date\", \"date\": \"2023-01-11\" }]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Date.class);
        List<Object> values = new ArrayList<>();
        try {
            while (it.hasNext()) {
                values.add(it.next());
            }
        } catch (JsonMappingException e) {
            ReferenceUntil referenceUntil = new ReferenceUntil("@type");
            assertEquals("Expected a String but was BEGIN_ARRAY", e.getMessage());
            assertEquals(ReferenceUntil.Field.START_ARRAY, referenceUntil.getField());
            assertEquals("java.lang.ClassCastException: com.fasterxml.jackson.core.JsonParser cannot be cast to com.fasterxml.jackson.databind.JsonDeserializer.Reference", e.getCausedBy().getMessage());
        }
        assertFalse(it.hasNext());
        parser.close();
    }

    @org.junit.Test
    public void testMappingIteratorWithIOException() throws IOException {
        String input = "[\"foo\"]";
        ObjectReader reader = MAPPER.readerForListOf(String.class);
        Iterator<String> it = reader.iterator(new ByteArrayInputStream(input.getBytes()));
        List<String> values = new ArrayList<>();
        try {
            while (it.hasNext()) {
                values.add(it.next());
            }
        } catch (IOException e) {
            assertEquals("Unexpected character (UTF-8): 0 dabei", e.getMessage());
        }
        assertFalse(it.hasNext());
    }

    @org.junit.Test
    public void testMappingIteratorWithEmptyInput() throws IOException {
        String input = "";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        assertFalse(it.hasNext());
        parser.close();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMappingIteratorRemove() throws IOException {
        String input = "[\"foo\",\"bar\"]";
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(input.getBytes()));
        Iterator<Object> it = createMappingIterator(parser, Object.class);
        it.next();
        it.remove();
    }
}