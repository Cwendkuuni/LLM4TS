
package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.List;

public class SplitterTest {

    @Test
    public void testOn_charSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOn_CharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOn_stringSeparator() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("apple;banana;cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOn_patternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("apple banana cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("applebanana");
        assertEquals("app", result.iterator().next());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.omitEmptyStrings().split("apple,,banana,,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" apple ,banana, cherry ");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testTrimResults_withTrimmer() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('*'));
        Iterable<String> result = splitter.split("*apple*,*banana*,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("apple,banana,cherry");
        assertEquals("apple", result.get(0));
    }

    @Test
    public void testWithKeyValueSeparator_stringSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
    }

    @Test
    public void testWithKeyValueSeparator_charSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
    }

    @Test
    public void testWithKeyValueSeparator_SplitterSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(Splitter.on("="));
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
    }
}

