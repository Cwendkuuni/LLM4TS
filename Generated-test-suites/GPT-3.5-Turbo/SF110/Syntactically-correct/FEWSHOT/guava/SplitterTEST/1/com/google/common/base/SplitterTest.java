package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testOnCharWithEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("a,,b,c,");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("and");
        Iterable<String> result = splitter.split("one and two and three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one ", iterator.next());
        assertEquals(" two ", iterator.next());
        assertEquals(" three", iterator.next());
    }
    
    @Test
    public void testOnStringWithEmptyStrings() {
        Splitter splitter = Splitter.on("and").omitEmptyStrings();
        Iterable<String> result = splitter.split("one and two and three and");
        Iterator<String> iterator = result.iterator();
        assertEquals("one ", iterator.next());
        assertEquals(" two ", iterator.next());
        assertEquals(" three ", iterator.next());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[0-9]+");
        Iterable<String> result = splitter.split("a1b2c3");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testOnPatternWithEmptyStrings() {
        Splitter splitter = Splitter.onPattern("[0-9]+").omitEmptyStrings();
        Iterable<String> result = splitter.split("a1b2c3");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcdef");
        Iterator<String> iterator = result.iterator();
        assertEquals("ab", iterator.next());
        assertEquals("cd", iterator.next());
        assertEquals("ef", iterator.next());
    }
    
    @Test
    public void testFixedLengthWithEmptyStrings() {
        Splitter splitter = Splitter.fixedLength(2).omitEmptyStrings();
        Iterable<String> result = splitter.split("abcdef");
        Iterator<String> iterator = result.iterator();
        assertEquals("ab", iterator.next());
        assertEquals("cd", iterator.next());
        assertEquals("ef", iterator.next());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("a,,b,c,,");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("a,b,c");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b,c", iterator.next());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" a , b , c ");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testTrimResultsWithCustomMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is('_'));
        Iterable<String> result = splitter.split("_a_,_b_,_c_");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(';').withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("key1:value1;key2:value2");
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
}