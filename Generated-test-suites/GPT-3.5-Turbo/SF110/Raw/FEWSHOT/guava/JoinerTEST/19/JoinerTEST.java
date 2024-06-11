package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
        assertEquals(";", joiner.separator);
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        List<String> parts = Arrays.asList("foo", "bar", "baz");
        Joiner joiner = Joiner.on(",");
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, parts);
        assertEquals("foo,bar,baz", result.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        List<String> parts = Arrays.asList("foo", "bar", "baz");
        Joiner joiner = Joiner.on(",");
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, parts.iterator());
        assertEquals("foo,bar,baz", result.toString());
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        String[] parts = {"foo", "bar", "baz"};
        Joiner joiner = Joiner.on(",");
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, parts);
        assertEquals("foo,bar,baz", result.toString());
    }

    @Test
    public void testAppendToWithObjects() throws IOException {
        Object[] parts = {"foo", "bar", "baz"};
        Joiner joiner = Joiner.on(",");
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, parts);
        assertEquals("foo,bar,baz", result.toString());
    }

    @Test
    public void testJoinWithIterable() {
        List<String> parts = Arrays.asList("foo", "bar", "baz");
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(parts);
        assertEquals("foo,bar,baz", result);
    }

    @Test
    public void testJoinWithIterator() {
        List<String> parts = Arrays.asList("foo", "bar", "baz");
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(parts.iterator());
        assertEquals("foo,bar,baz", result);
    }

    @Test
    public void testJoinWithObjectArray() {
        String[] parts = {"foo", "bar", "baz"};
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(parts);
        assertEquals("foo,bar,baz", result);
    }

    @Test
    public void testJoinWithObjects() {
        Object[] parts = {"foo", "bar", "baz"};
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(parts);
        assertEquals("foo,bar,baz", result);
    }

    @Test
    public void testUseForNull() throws IOException {
        List<String> parts = Arrays.asList("foo", null, "baz");
        Joiner joiner = Joiner.on(",").useForNull("null");
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, parts);
        assertEquals("foo,null,baz", result.toString());
    }

    @Test
    public void testSkipNulls() throws IOException {
        List<String> parts = Arrays.asList("foo", null, "baz");
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, parts);
        assertEquals("foo,baz", result.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() {
        Joiner joiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
        assertEquals(",", mapJoiner.joiner.separator);
        assertEquals(":", mapJoiner.keyValueSeparator);
    }

    @Test
    public void testMapJoinerAppendToWithMap() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder result = new StringBuilder();
        mapJoiner.appendTo(result, map);
        assertEquals("one:1,two:2,three:3", result.toString());
    }

    @Test
    public void testMapJoinerAppendToWithEntries() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder result = new StringBuilder();
        mapJoiner.appendTo(result, map.entrySet());
        assertEquals("one:1,two:2,three:3", result.toString());
    }

    @Test
    public void testMapJoinerJoinWithMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        String result = mapJoiner.join(map);
        assertEquals("one:1,two:2,three:3", result);
    }

    @Test
    public void testMapJoinerJoinWithEntries() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        String result = mapJoiner.join(map.entrySet());
        assertEquals("one:1,two:2,three:3", result);
    }

    @Test
    public void testToStringWithCharSequence() {
        CharSequence sequence = "foo";
        Joiner joiner = Joiner.on(",");
        String result = joiner.toString(sequence);
        assertEquals("foo", result);
    }

    @Test
    public void testToStringWithNonCharSequence() {
        Object object = new Object();
        Joiner joiner = Joiner.on(",");
        String result = joiner.toString(object);
        assertEquals(object.toString(), result);
    }

    @Test
    public void testIterable() {
        List<String> parts = Arrays.asList("foo", "bar", "baz");
        Iterable<Object> iterable = Joiner.iterable("hello", 123, parts);
        assertNotNull(iterable);
        Iterator<Object> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("hello", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(123, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(parts, iterator.next());
        assertTrue(!iterator.hasNext());
    }
}