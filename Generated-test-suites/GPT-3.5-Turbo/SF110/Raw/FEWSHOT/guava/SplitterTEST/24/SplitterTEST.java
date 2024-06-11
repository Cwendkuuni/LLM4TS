package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.LinkedHashMap;

public class SplitterTest {

    Splitter splitter;

    @Before
    public void setup() {
        splitter = Splitter.on(",");
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter omitEmptyStrSplitter = splitter.omitEmptyStrings();
        Iterable<String> result = omitEmptyStrSplitter.split("a,b,,c,d,,");
        assertTrue(result instanceof Iterable<?>);
        assertEquals("a", result.iterator().next());
        int count = 0;
        for (String str : result) {
            count++;
        }
        assertEquals(4, count);
    }

    @Test
    public void testLimit() {
        Splitter limitSplitter = splitter.limit(3);
        Iterable<String> result = limitSplitter.split("a,b,c,d,e");
        assertTrue(result instanceof Iterable<?>);
        assertEquals("a", result.iterator().next());
        int count = 0;
        for (String str : result) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testTrimResults() {
        Splitter trimSplitter = splitter.trimResults();
        Iterable<String> result = trimSplitter.split(" a ,   b   , c   ");
        assertTrue(result instanceof Iterable<?>);
        assertEquals("a", result.iterator().next());
        int count = 0;
        for (String str : result) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testSplit() {
        Iterable<String> result = splitter.split("a,b,c");
        assertTrue(result instanceof Iterable<?>);
        assertEquals("a", result.iterator().next());
        int count = 0;
        for (String str : result) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testSplitToList() {
        Splitter noEmptyStrSplitter = splitter.omitEmptyStrings();
        List<String> result = noEmptyStrSplitter.splitToList("a,b,c");
        assertTrue(result instanceof List<?>);
        assertEquals("a", result.get(0));
        int count = 0;
        for (String str : result) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = keyValueSplitter.split("a=1,b=2,c=3");
        assertTrue(result instanceof Map<?, ?>);
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
        assertEquals("3", result.get("c"));
    }
}