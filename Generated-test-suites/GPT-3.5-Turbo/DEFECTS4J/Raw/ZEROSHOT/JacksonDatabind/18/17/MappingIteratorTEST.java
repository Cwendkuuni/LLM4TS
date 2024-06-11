package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        // Test when iterator is closed
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.close();
        assertFalse(iterator.hasNext());

        // Test when iterator has next value
        iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertTrue(iterator.hasNext());

        // Test when iterator does not have next value
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        // Test when iterator is closed
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.close();

        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }

        // Test when iterator has next value
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertNotNull(iterator.next());

        // Test when iterator does not have next value
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.remove();
    }

    @Test
    public void testClose() {
        // Test when parser is null
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.close();

        // Test when parser is not null
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        try {
            iterator.close();
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testHasNextValue() throws IOException {
        // Test when parser is null
        MappingIterator<String> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());

        // Test when parser is not null and current token is null
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertTrue(iterator.hasNextValue());

        // Test when parser is not null and current token is not null or END_ARRAY
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        iterator.getParser().setInputBuffer(new char[5]);
        assertTrue(iterator.hasNextValue());

        // Test when parser is not null and current token is END_ARRAY
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        iterator.getParser().setInputBuffer(new char[5]);
        iterator.getParser().setCurrentToken(JsonToken.END_ARRAY);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        // Test when iterator does not have next value
        MappingIterator<String> iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }

        // Test when iterator has next value
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertNotNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        // Test when iterator has no values
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertEquals(0, iterator.readAll().size());

        // Test when iterator has values
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertEquals(1, iterator.readAll().size());
    }

    @Test
    public void testReadAllList() throws IOException {
        // Test when iterator has no values
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertEquals(0, iterator.readAll(new ArrayList<>()).size());

        // Test when iterator has values
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertEquals(1, iterator.readAll(new ArrayList<>()).size());
    }

    @Test
    public void testReadAllCollection() throws IOException {
        // Test when iterator has no values
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertEquals(0, iterator.readAll(new HashSet<>()).size());

        // Test when iterator has values
        iterator = new MappingIterator<>(null, new JsonParser(null), new DeserializationContext(), null, false, null);
        assertEquals(1, iterator.readAll(new HashSet<>()).size());
    }

    @Test
    public void testGetParser() {
        JsonParser parser = new JsonParser(null);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertEquals(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        FormatSchema schema = new FormatSchema() {};
        JsonParser parser = new JsonParser(null);
        parser.setSchema(schema);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertEquals(schema, iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = new JsonLocation(null, -1, -1, -1, -1);
        JsonParser parser = new JsonParser(null);
        parser.setCurrentLocation(location);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertEquals(location, iterator.getCurrentLocation());
    }
}