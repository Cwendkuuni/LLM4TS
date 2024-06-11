
package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private MappingIterator<String> iterator;
    private JsonParser jsonParser;
    private DeserializationContext context;
    private JsonDeserializer<String> deserializer;

    @Before
    public void setUp() {
        jsonParser = new JsonParser();
        context = new DeserializationContext();
        deserializer = new JsonDeserializer<>();
        iterator = new MappingIterator<>(null, jsonParser, context, deserializer, true, null);
    }

    @Test
    public void testHasNext() throws IOException {
        // Test empty iterator
        assertFalse(iterator.hasNext());

        // Test iterator with one element
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        assertTrue(iterator.hasNext());

        // Test iterator with multiple elements
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        jsonParser.addToken(JsonToken.VALUE_STRING, "bar");
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        // Test empty iterator
        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            assertNull(e.getMessage());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }

        // Test iterator with one element
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        assertEquals("foo", iterator.next());

        // Test iterator with multiple elements
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        jsonParser.addToken(JsonToken.VALUE_STRING, "bar");
        assertEquals("foo", iterator.next());
    }

    @Test
    public void testRemove() {
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            assertNull(e.getMessage());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testClose() throws IOException {
        // Test with open parser
        iterator.close();
        assertNull(iterator.getParser());

        // Test with null parser
        iterator.close();
        assertNull(iterator.getParser());
    }

    @Test
    public void testHasNextValue() throws IOException {
        // Test empty iterator
        assertFalse(iterator.hasNextValue());

        // Test iterator with one element
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        assertTrue(iterator.hasNextValue());

        // Test iterator with multiple elements
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        jsonParser.addToken(JsonToken.VALUE_STRING, "bar");
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        // Test empty iterator
        try {
            iterator.nextValue();
        } catch (NoSuchElementException e) {
            assertNull(e.getMessage());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }

        // Test iterator with one element
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        assertEquals("foo", iterator.nextValue());

        // Test iterator with multiple elements
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        jsonParser.addToken(JsonToken.VALUE_STRING, "bar");
        assertEquals("foo", iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        // Test empty iterator
        List<String> resultList = iterator.readAll();
        assertNotNull(resultList);
        assertEquals(0, resultList.size());

        // Test iterator with one element
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        jsonParser.addToken(JsonToken.VALUE_STRING, "bar");
        resultList = iterator.readAll();
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(Arrays.asList("foo", "bar"), resultList);

        // Test iterator with multiple elements
        iterator = new MappingIterator<>(null, jsonParser, context, deserializer, true, null);
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        resultList = iterator.readAll(new ArrayList<>());
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(Arrays.asList("foo"), resultList);
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        // Test empty iterator
        List<String> resultList = iterator.readAll(new ArrayList<>());
        assertNotNull(resultList);
        assertEquals(0, resultList.size());

        // Test iterator with one element
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        jsonParser.addToken(JsonToken.VALUE_STRING, "bar");
        resultList = iterator.readAll(new ArrayList<>());
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(Arrays.asList("foo", "bar"), resultList);

        // Test iterator with multiple elements
        iterator = new MappingIterator<>(null, jsonParser, context, deserializer, true, null);
        jsonParser.addToken(JsonToken.VALUE_STRING, "foo");
        resultList = iterator.readAll(new ArrayList<>());
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(Arrays.asList("foo"), resultList);
    }

    @Test
    public void testGetParser() {
        assertEquals(jsonParser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(iterator.getCurrentLocation());
    }

}
