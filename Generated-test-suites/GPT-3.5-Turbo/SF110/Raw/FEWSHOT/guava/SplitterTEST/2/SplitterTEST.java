package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOn_char() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("foo,bar,baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("foo,bar,baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testOn_String() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("foo;bar;baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testOn_Pattern() {
        Splitter splitter = Splitter.onPattern("[\\,\\;]");
        Iterable<String> result = splitter.split("foo,bar;baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("foobarbaz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("foo,,bar,,baz");
        assertTrue(result.toString().contains("foo"));
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("foo,bar,baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar,baz"));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split("foo , bar , baz ");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testTrimResults_CharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split("foo , bar , baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        Iterable<String> result = splitter.split("foo,bar,baz");
        assertEquals("foo", result.iterator().next());
        assertTrue(result.toString().contains("bar"));
        assertTrue(result.toString().contains("baz"));
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        List<String> result = splitter.splitToList("foo,bar,baz");
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").trimResults().omitEmptyStrings().withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testWithKeyValueSeparator_char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").trimResults().omitEmptyStrings().withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter outerSplitter = Splitter.on(",").trimResults().omitEmptyStrings();
        Splitter entrySplitter = Splitter.on("=").trimResults();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
}