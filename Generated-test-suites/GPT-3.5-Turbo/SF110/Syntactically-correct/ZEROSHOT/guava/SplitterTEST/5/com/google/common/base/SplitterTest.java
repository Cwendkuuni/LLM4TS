package com.google.common.base;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        String input = "apple,banana,orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf("!\","));
        String input = "apple!banana,orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        String input = "apple;banana;orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnEmptyStringSeparator() {
        Splitter.on("");
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "apple   banana   orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnPatternEmptyStringSeparator() {
        Splitter.onPattern("");
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "abcdefg";
        List<String> expected = new ArrayList<>();
        expected.add("ab");
        expected.add("cd");
        expected.add("ef");
        expected.add("g");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,,banana,,orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.omitEmptyStrings().splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        String input = "apple,banana,orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana,orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        String input = " apple , banana , orange ";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(", ")).trimResults(CharMatcher.anyOf("abc"));
        String input = " axpplaecb , cbabnaba , orange ";
        List<String> expected = new ArrayList<>();
        expected.add("ple");
        expected.add("n");
        expected.add("orng");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,banana,orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        Iterable<String> result = splitter.split(input);
        List<String> resultList = new ArrayList<>();
        for (String str : result) {
            resultList.add(str);
        }
        assertEquals(expected, resultList);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,banana,orange";
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("orange");

        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on(",").withKeyValueSeparator(":");
        String input = "key1:value1,key2:value2,key3:value3";
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(":");
        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("key1");
        expectedKeys.add("key2");
        expectedKeys.add("key3");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");

        Splitter.MapSplitter.Map<String, String> result = mapSplitter.split(input);
        assertEquals(expectedKeys.size(), result.size());
        for (int i = 0; i < expectedKeys.size(); i++) {
            String key = expectedKeys.get(i);
            String value = expectedValues.get(i);
            assertEquals(value, result.get(key));
        }
    }

}