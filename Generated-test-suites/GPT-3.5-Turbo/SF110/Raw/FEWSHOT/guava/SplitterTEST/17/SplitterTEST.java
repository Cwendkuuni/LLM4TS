package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.*;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",."));
        Iterable<String> result = splitter.split("a,b.c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(" ");
        Iterable<String> result = splitter.split("hello world");
        List<String> expected = Arrays.asList("hello", "world");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Iterable<String> result = splitter.split("hello   world");
        List<String> expected = Arrays.asList("hello", "world");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcdef");
        List<String> expected = Arrays.asList("ab", "cd", "ef");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split(",a,,,b,c,");
        List<String> expected = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(3);
        Iterable<String> result = splitter.split("a,b,c,d");
        List<String> expected = Arrays.asList("a", "b", "c,d");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" a , b , c ");
        List<String> expected = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = result.iterator();
        for (String s : expected) {
            assertEquals(s, iterator.next());
        }
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("a=1,b=2,c=3");
        Map<String, String> expected = new HashMap<>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("c", "3");
        assertEquals(expected, result);
    }
}