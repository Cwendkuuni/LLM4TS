package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello-world"));
        assertEquals(expected, splitter.splitToList("-hello-world-"));
        assertEquals(expected, splitter.splitToList("-hello-world"));
        assertEquals(expected, splitter.splitToList("hello-world-"));
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-').or(CharMatcher.is('_')));
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello-world"));
        assertEquals(expected, splitter.splitToList("-hello_world-"));
        assertEquals(expected, splitter.splitToList("-hello-_world-"));
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(":");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello:world"));
        assertEquals(expected, splitter.splitToList(":hello:world:"));
        assertEquals(expected, splitter.splitToList(":hello:world"));
        assertEquals(expected, splitter.splitToList("hello:world:"));
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello    world"));
        assertEquals(expected, splitter.splitToList("hello    \tworld"));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> expected = Arrays.asList("hel", "lo", "wor", "ld");
        assertEquals(expected, splitter.splitToList("helloworld"));
        assertEquals(expected, splitter.splitToList("hel lo wor ld"));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello:::world"));
        assertEquals(expected, splitter.splitToList("hello:world"));
        assertEquals(expected, splitter.splitToList(":hello:world:"));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        List<String> expected = Arrays.asList("hello", "world,how are you");
        assertEquals(expected, splitter.splitToList("hello,world,how are you"));
        assertEquals(expected, splitter.splitToList("hello,world,how, are you"));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> expected = Arrays.asList("hello", "world", "how are you");
        assertEquals(expected, splitter.splitToList("hello , world , how are you"));
        assertEquals(expected, splitter.splitToList("  hello,   world ,   how   are   you   "));
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('_'));
        List<String> expected = Arrays.asList("hello", "world", "how are you");
        assertEquals(expected, splitter.splitToList("hello_,_world_,_how are you"));
        assertEquals(expected, splitter.splitToList("  hello_,   _world ,   how   are   you   "));
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello,world"));
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, splitter.splitToList("hello,world"));
    }

    @Test
    public void testSplitToMap() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator("=");
        Map<String, String> expected = Collections.singletonMap("key", "value");
        assertEquals(expected, mapSplitter.split("key=value"));
    }
}