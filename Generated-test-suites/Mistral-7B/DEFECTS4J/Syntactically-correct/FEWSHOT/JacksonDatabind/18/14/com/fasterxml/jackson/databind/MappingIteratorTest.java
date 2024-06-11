package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertEquals(MappingIterator.EMPTY_ITERATOR, emptyIterator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNext() {
        JavaType type = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JsonDeserializer<String> deserializer = createMockDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNextValue());

        parser.expects(once()).method("nextToken").returns(new JsonToken(JsonToken.VALUE_STRING, "foo"));

        String next = iterator.next();
        assertEquals("foo", next);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNextValue());

        parser.expects(once()).method("nextToken").returns(JsonToken.START_ARRAY);

        iterator.next();
        assertFalse(iterator.hasNextValue());

        parser.expects(once()).method("close");
        iterator.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNextValue() throws IOException {
        JavaType type = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JsonDeserializer<String> deserializer = createMockDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        String next = iterator.nextValue();
        assertNull(next);

        parser.expects(once()).method("nextToken").returns(new JsonToken(JsonToken.VALUE_STRING, "foo"));

        next = iterator.nextValue();
        assertEquals("foo", next);

        parser.expects(once()).method("clearCurrentToken");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAll() throws IOException {
        JavaType type = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JsonDeserializer<String> deserializer = createMockDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        parser.expects(times(3)).method("nextToken");
        parser.expects(times(3)).method("clearCurrentToken");

        assertEquals(3, resultList.size());
        assertEquals("foo", resultList.get(0));
        assertEquals("bar", resultList.get(1));
        assertEquals("baz", resultList.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadAllCollection() throws IOException {
        JavaType type = new TypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JsonDeserializer<String> deserializer = createMockDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        Collection<String> results = new ArrayList<>();
        iterator.readAll(results);

        parser.expects(times(3)).method("nextToken");
        parser.expects(times(3)).method("clearCurrentToken");

        assertEquals(3, results.size());
        assertEquals("foo", results.iterator().next());
        assertEquals("bar", results.iterator().next());
        assertEquals("baz", results.iterator().next());
    }

    @Test
    public void testCloseParser() throws IOException {
        JavaType type = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JsonDeserializer<String> deserializer = createMockDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.close();

        parser.expects(once()).method("close");
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private JsonDeserializer<String> createMockDeserializer() {
        return mock(JsonDeserializer.class);
    }
}