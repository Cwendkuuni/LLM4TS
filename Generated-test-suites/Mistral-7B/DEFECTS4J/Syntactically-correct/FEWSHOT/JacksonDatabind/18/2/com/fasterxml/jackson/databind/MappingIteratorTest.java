package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonParser parser = createMockParser();
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, false, null);
        assertTrue(iterator.hasNext());
        parser.nextToken();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        JsonParser parser = createMockParser();
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, false, null);
        assertTrue(iterator.hasNextValue());
        parser.nextToken();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class), parser, null, null, false, null);
        Object nextValue = iterator.nextValue();
        assertNotNull(nextValue);
        assertEquals(parser.readValueAsTree(parser.getValueAsString()), nextValue);
        parser.nextToken();
        nextValue = iterator.nextValue();
        assertNotNull(nextValue);
        assertEquals(parser.getValueAsString(), nextValue.toString());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        List<Object> resultList = new ArrayList<>();
        MappingIterator<Object> iterator = new MappingIterator<Object>(mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class), parser, null, null, false, null);
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        for (Object obj : resultList) {
            assertNotNull(obj);
        }
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        Collection<Object> results = new ArrayList<>();
        MappingIterator<Object> iterator = new MappingIterator<Object>(mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class), parser, null, null, false, null);
        iterator.readAll(results);
        assertEquals(2, results.size());
        for (Object obj : results) {
            assertNotNull(obj);
        }
    }

    @Test
    public void testCloseParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, true, null);
        parser.nextToken();
        iterator.nextValue();
        iterator.close();
        assertTrue(parser.isClosed());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, false, null);
        iterator.nextValue();
        iterator.remove();
    }

    private JsonParser createMockParser() {
        return new JsonParser() {
            @Override
            public void close() throws IOException {
                //mock implementation
            }

            @Override
            public JsonToken nextToken() throws IOException {
                return JsonToken.START_ARRAY;
            }

            @Override
            public JsonToken getCurrentToken() {
                return null;
            }

            @Override
            public void clearCurrentToken() {
                //mock implementation
            }

            @Override
            public Object readValueAsTree(String content) throws IOException {
                return null;
            }

            @Override
            public String getValueAsString() throws IOException {
                return "";
            }
        };
    }
}