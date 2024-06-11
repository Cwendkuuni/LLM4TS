package com.fasterxml.jackson.databind;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;

public class MappingIteratorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNext_NullParser() {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNext_NoNextValue() throws IOException {
        String json = "[]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        parser.close();
    }

    @Test
    public void testHasNext_NextValue() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().getClass().getDeclaredField("intValue").get(iterator.next()));
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().getClass().getDeclaredField("intValue").get(iterator.next()));
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().getClass().getDeclaredField("intValue").get(iterator.next()));
        assertFalse(iterator.hasNext());
        parser.close();
    }

    @Test
    public void testHasNext_NextValueWithUpdatedValue() throws IOException {
        String json = "[1, 2, 3]";
        Object value = new Object();
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, value);
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().getClass().getDeclaredField("intValue").get(iterator.next()));
        iterator.setUpdatedValue(value);
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().getClass().getDeclaredField("intValue").get(iterator.next()));
        iterator.setUpdatedValue(value);
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().getClass().getDeclaredField("intValue").get(iterator.next()));
        assertFalse(iterator.hasNext());
        parser.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.close();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        parser.close();
    }

    @Test
    public void testHasNextValue_NullParser() {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValue_NoNextValue() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("");
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertFalse(iterator.hasNextValue());
        parser.close();
    }

    @Test
    public void testHasNextValue_NextValue() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertTrue(iterator.hasNextValue());
        assertEquals(START_ARRAY, parser.getCurrentToken());
        iterator.nextValue();
        assertEquals(VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(1, iterator.nextValue().getClass().getDeclaredField("intValue").get(iterator.nextValue()));
        assertEquals(VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(2, iterator.nextValue().getClass().getDeclaredField("intValue").get(iterator.nextValue()));
        assertEquals(VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(3, iterator.nextValue().getClass().getDeclaredField("intValue").get(iterator.nextValue()));
        assertEquals(END_ARRAY, parser.getCurrentToken());
        assertFalse(iterator.hasNextValue());
        parser.close();
    }

    @Test
    public void testHasNextValue_NextValueWithUpdatedValue() throws IOException {
        String json = "[1, 2, 3]";
        Object value = new Object();
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, value);
        assertTrue(iterator.hasNextValue());
        assertEquals(START_ARRAY, parser.getCurrentToken());
        iterator.nextValue();
        assertEquals(VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(1, iterator.nextValue().getClass().getDeclaredField("intValue").get(iterator.nextValue()));
        iterator.setUpdatedValue(value);
        iterator.nextValue();
        assertEquals(VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(2, iterator.nextValue().getClass().getDeclaredField("intValue").get(iterator.nextValue()));
        iterator.setUpdatedValue(value);
        iterator.nextValue();
        assertEquals(VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(3, iterator.nextValue().getClass().getDeclaredField("intValue").get(iterator.nextValue()));
        assertEquals(END_ARRAY, parser.getCurrentToken());
        assertFalse(iterator.hasNextValue());
        parser.close();
    }

    @Test
    public void testReadAll() throws IOException {
        String json = "[1, 2, 3]";
        List<Object> resultList = new ArrayList<>();
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        parser.close();
    }

    @Test
    public void testReadAll_Collection() throws IOException {
        String json = "[1, 2, 3]";
        Collection<Object> results = new ArrayList<>();
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        iterator.readAll(results);
        assertEquals(3, results.size());
        parser.close();
    }

    @Test
    public void testGetParser() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        assertEquals(parser, iterator.getParser());
        parser.close();
    }

    @Test
    public void testGetParserSchema() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectReader reader = mapper.readerForArrayOf(Integer.class);
        MappingIterator<Integer> iterator = reader.forEachArrayElement(parser, new ArrayList<>());
        assertEquals(parser.getSchema(), iterator.getParserSchema());
        parser.close();
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(json);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        JsonLocation location = iterator.getCurrentLocation();
        parser.close();
        assertNotNull(location);
    }
}