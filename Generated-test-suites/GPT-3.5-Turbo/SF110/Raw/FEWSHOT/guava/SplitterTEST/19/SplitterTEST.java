
package com.google.common.base;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOn_char() {
        Splitter splitter = Splitter.on(',');

        List<String> result = splitter.splitToList("a,b,c,d,e");

        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(";,"));

        List<String> result = splitter.splitToList("a,b;c;d,e");

        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testOn_String() {
        Splitter splitter = Splitter.on(";");

        List<String> result = splitter.splitToList("a;b;c;d;e");

        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testOn_Pattern() {
        Splitter splitter = Splitter.onPattern("\\s+");

        List<String> result = splitter.splitToList("a b c d e");

        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);

        List<String> result = splitter.splitToList("abcdef");

        assertEquals(Arrays.asList("ab", "cd", "ef"), result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();

        List<String> result = splitter.splitToList("a,,,b,,,,c");

        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(3);

        List<String> result = splitter.splitToList("a:b:c:d:e");

        assertEquals(Arrays.asList("a", "b", "c:d:e"), result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();

        List<String> result = splitter.splitToList(" a , b , c ");

        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testTrimResults_CharMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));

        List<String> result = splitter.splitToList(" a , b , c ");

        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");

        Iterable<String> result = splitter.split("a,b,c,d,e");

        Iterator<String> iterator = result.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");

        List<String> result = splitter.splitToList("a,b,c,d,e");

        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator("=");

        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2:key3=value3");

        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparator_char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator('=');

        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2:key3=value3");

        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter keyValueSplitter = Splitter.on("=").trimResults();
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator(keyValueSplitter);

        Map<String, String> result = mapSplitter.split("key1 = value1 : key2 = value2 : key3 = value3");

        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

}
