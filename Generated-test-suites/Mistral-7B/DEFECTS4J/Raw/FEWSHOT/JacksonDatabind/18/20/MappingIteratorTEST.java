package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        JavaType type = null;
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        assertFalse(iterator.hasNext());
        iterator = new MappingIterator<>(type, null, null, null, false, "initialValue".getClass());
        iterator.next();
        assertFalse(iterator.hasNext());
        iterator = new MappingIterator<>(type, null, null, null, true, "initialValue".getClass());
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        iterator = new MappingIterator<>(type, parser, null, null, true, "initialValue".getClass());
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
        verify(parser).nextToken();
        verify(parser, times(2)).clearCurrentToken();
    }

    @Test
    public void testHasNextValue() throws IOException {
        JavaType type = null;
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());
        iterator = new MappingIterator<>(type, null, null, null, false, "initialValue".getClass());
        iterator.next();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
        iterator = new MappingIterator<>(type, null, null, null, true, "initialValue".getClass());
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        iterator = new MappingIterator<>(type, parser, null, null, true, "initialValue".getClass());
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
        verify(parser).clearCurrentToken();
        verify(parser, times(2)).nextToken();
    }

    @Test
    public void testNext() throws IOException {
        JavaType type = null;
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        assertNull(iterator.next());
        iterator = new MappingIterator<>(type, null, null, null, false, "initialValue".getClass());
        iterator.next();
        assertEquals("initialValue", iterator.next());
        assertNull(iterator.next());
        iterator = new MappingIterator<>(type, null, null, null, true, "initialValue".getClass());
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        iterator = new MappingIterator<>(type, parser, null, null, true, "initialValue".getClass());
        iterator.next();
        iterator.next();
        String value = iterator.next();
        assertEquals("initialValue", value);
        assertNull(iterator.next());
        verify(parser).nextToken();
        verify(parser, times(3)).clearCurrentToken();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        JavaType type = null;
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        JavaType type = null;
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        iterator.close();
        iterator = new MappingIterator<>(type, null, null, null, true, null);
        JsonParser parser = mock(JsonParser.class);
        iterator = new MappingIterator<>(type, parser, null, null, true, null);
        iterator.next();
        iterator.next();
        iterator.close();
        verify(parser).close();
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType type = null;
        List<String> resultList = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        iterator.next();
        iterator.next();
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("", resultList.get(0));
        assertEquals("", resultList.get(1));
        iterator = new MappingIterator<>(type, null, null, null, true, null);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("", resultList.get(0));
        assertEquals("", resultList.get(1));
        assertEquals("", resultList.get(2));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        JavaType type = null;
        Collection<String> results = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(type, null, null, null, false, null);
        iterator.next();
        iterator.next();
        iterator.readAll(results);
        assertEquals(2, results.size());
        assertEquals("", results.iterator().next());
        assertEquals("", results.iterator().next());
        iterator = new MappingIterator<>(type, null, null, null, true, null);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.readAll(results);
        assertEquals(3, results.size());
        assertEquals("", results.iterator().next());
        assertEquals("", results.iterator().next());
        assertEquals("", results.iterator().next());
    }
}