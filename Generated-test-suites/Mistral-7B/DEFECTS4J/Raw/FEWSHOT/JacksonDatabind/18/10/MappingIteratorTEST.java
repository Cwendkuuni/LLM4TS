package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<Object> iterator = createMappingIterator();
        assertFalse(iterator.hasNext());

        JsonParser parser = createJsonParserWithStream("[]");
        iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertTrue(iterator.hasNext());

        JsonParser parserWithValue = createJsonParserWithStream("[\"value\"]");
        iterator = new MappingIterator<>(null, parserWithValue, null, null, false, null);
        assertTrue(iterator.hasNext());

        JsonParser managedParser = createJsonParserWithStream("{ \"data\": [\"value1\", \"value2\"] }");
        iterator = new MappingIterator<>(null, managedParser, null, null, true, null);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        MappingIterator<Object> iterator = createMappingIterator();
        assertNull(iterator.next());

        JsonParser parser = createJsonParserWithStream("[]");
        iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertNull(iterator.next());

        JsonParser parserWithValue = createJsonParserWithStream("[\"value\"]");
        iterator = new MappingIterator<>(null, parserWithValue, null, null, false, null);
        assertEquals("value", iterator.next());

        JsonParser managedParser = createJsonParserWithStream("{ \"data\": [\"value1\", \"value2\"] }");
        iterator = new MappingIterator<>(null, managedParser, null, null, true, null);
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<Object> iterator = createMappingIterator();
        assertNull(iterator.next());

        JsonParser parser = createJsonParserWithStream("[]");
        iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.close();
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<Object> iterator = createMappingIterator();
        assertFalse(iterator.hasNextValue());

        JsonParser parser = createJsonParserWithStream("[]");
        iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertFalse(iterator.hasNextValue());

        JsonParser parserWithValue = createJsonParserWithStream("[\"value\"]");
        iterator = new MappingIterator<>(null, parserWithValue, null, null, false, null);
        assertTrue(iterator.hasNextValue());

        JsonParser managedParser = createJsonParserWithStream("{ \"data\": [\"value1\", \"value2\"] }");
        iterator = new MappingIterator<>(null, managedParser, null, null, true, null);
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<Object> iterator = createMappingIterator();
        assertNull(iterator.nextValue());

        JsonParser parser = createJsonParserWithStream("[]");
        iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertNull(iterator.nextValue());

        JsonParser parserWithValue = createJsonParserWithStream("[\"value\"]");
        iterator = new MappingIterator<>(null, parserWithValue, null, null, false, null);
        assertEquals("value", iterator.nextValue());

        JsonParser managedParser = createJsonParserWithStream("{ \"data\": [\"value1\", \"value2\"] }");
        iterator = new MappingIterator<>(null, managedParser, null, null, true, null);
        assertEquals("value1", iterator.nextValue());
        assertEquals("value2", iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        JsonParser parser = createJsonParserWithStream("[\"value1\", \"value2\"]");

        List<Object> list = new ArrayList<>();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.readAll(list);
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        JsonParser parser = createJsonParserWithStream("[\"value1\", \"value2\"]");

        HashSet<Object> set = new HashSet<>();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.readAll(set);
        assertEquals(2, set.size());
        assertTrue(set.contains("value1"));
        assertTrue(set.contains("value2"));
    }

    private JsonParser createJsonParserWithStream(String json) throws IOException {
        StringReader reader = new StringReader(json);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(reader);
    }

    private MappingIterator<Object> createMappingIterator() {
        return new MappingIterator<>(null, null, null, null, false, null);
    }
}