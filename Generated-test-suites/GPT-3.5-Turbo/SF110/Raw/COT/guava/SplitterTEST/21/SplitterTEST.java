package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {
    @Test
    public void testOn_char() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testOn_string() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testOn_Pattern() {
        Splitter splitter = Splitter.onPattern(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcde");
        assertEquals("ab", result.iterator().next());
    }
  
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("a,,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split("  a  ,  b  ,  c  ");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
    }
  
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals("a", result.get(0));
    }
  
    @Test
    public void testWithKeyValueSeparator_string() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2");
        assertEquals("value1", result.get("key1"));
    }
  
    @Test
    public void testWithKeyValueSeparator_char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2");
        assertEquals("value1", result.get("key1"));
    }
}