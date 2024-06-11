
package com.google.common.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(' ');
        Iterable<String> result = splitter.split("hello world");
        assertEquals("hello", result.iterator().next());
        assertEquals(2, Iterables.size(result));
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(" -,"));
        Iterable<String> result = splitter.split("hello-world,java programming");
        assertEquals("hello", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("hello,world,java");
        assertEquals("hello", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\.|,"));
        Iterable<String> result = splitter.split("hello,world.java");
        assertEquals("hello", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("hello world");
        assertEquals("hel", result.iterator().next());
        assertEquals(5, Iterables.size(result));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",")
                                .omitEmptyStrings();
        Iterable<String> result = splitter.split("hello,,world");
        assertEquals("hello", result.iterator().next());
        assertEquals(2, Iterables.size(result));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(" ")
                                .limit(2);
        Iterable<String> result = splitter.split("hello world java programming");
        assertEquals("hello", result.iterator().next());
        assertEquals(2, Iterables.size(result));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",")
                                .trimResults();
        Iterable<String> result = splitter.split(" hello , world ");
        assertEquals("hello", result.iterator().next());
        assertEquals(2, Iterables.size(result));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("hello,world,java");
        assertEquals("hello", result.get(0));
        assertEquals(3, result.size());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",")
                                                .withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
        assertEquals(2, result.size());
    }
}
