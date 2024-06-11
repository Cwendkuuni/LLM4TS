package com.google.common.base;

import org.junit.*;
import java.util.regex.*;
import com.google.common.annotations.*;
import java.util.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(':'));
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(":");
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile(":"));
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnPatternString() {
        Splitter splitter = Splitter.onPattern(":");
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.splitToList("abcde");
        List<String> expected = Arrays.asList("ab", "cd", "e");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        List<String> result = splitter.splitToList("a::b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b:c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(":").trimResults();
        List<String> result = splitter.splitToList(" a : b : c ");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(":").trimResults(CharMatcher.is(' '));
        List<String> result = splitter.splitToList(" a : b : c ");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter splitter = Splitter.on(":").withKeyValueSeparator("=");
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("a=1:b=2:c=3");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("c", "3");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter splitter = Splitter.on(":").withKeyValueSeparator("=");
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("a=1:b=2:c=3");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("c", "3");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter splitter = Splitter.on(":").withKeyValueSeparator("=");
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("a=1:b=2:c=3");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("c", "3");
        Assert.assertEquals(expected, result);
    }
}