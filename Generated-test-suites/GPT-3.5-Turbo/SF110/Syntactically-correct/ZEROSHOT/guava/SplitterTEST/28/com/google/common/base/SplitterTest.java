package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('.');
        Iterable<String> result = splitter.split("Hello.World");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('.'));
        Iterable<String> result = splitter.split("Hello.World");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("Hello,World");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s"));
        Iterable<String> result = splitter.split("Hello World");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("HelloWorld");
        String[] expected = {"He", "ll", "oW", "or", "ld"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,,World");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("Hello,World,Again");
        String[] expected = {"Hello", "World,Again"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split("  Hello  ,  World  ");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.whitespace());
        Iterable<String> result = splitter.split("  Hello  ,  World  ");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,World,Again");
        String[] expected = {"Hello", "World", "Again"};
        int i = 0;
        for (String s : result) {
            Assert.assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        List<String> result = splitter.splitToList("Hello,World,Again");
        String[] expected = {"Hello", "World", "Again"};
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(expected[i], result.get(i));
        }
    }

    @Test
    public void testWithKeyValueSeparatorOnChar() {
        Splitter splitter = Splitter.on(':').withKeyValueSeparator(',');
        MapSplitter.Map<String, String> result = splitter.split("key1,value1:key2,value2");
        String[] keys = {"key1", "key2"};
        String[] values = {"value1", "value2"};
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(keys[i], result.getKey(i));
            Assert.assertEquals(values[i], result.getValue(i));
        }
    }

    @Test
    public void testWithKeyValueSeparatorOnString() {
        Splitter splitter = Splitter.on(':').withKeyValueSeparator(",");
        MapSplitter.Map<String, String> result = splitter.split("key1,value1:key2,value2");
        String[] keys = {"key1", "key2"};
        String[] values = {"value1", "value2"};
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(keys[i], result.getKey(i));
            Assert.assertEquals(values[i], result.getValue(i));
        }
    }

    @Test
    public void testWithKeyValueSeparatorOnSplitter() {
        Splitter outerSplitter = Splitter.on(':');
        Splitter innerSplitter = Splitter.on(',').trimResults();
        Splitter.MapSplitter splitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        Splitter.MapSplitter.Map<String, String> result = splitter.split("key1,value1:key2, value2");
        String[] keys = {"key1", "key2"};
        String[] values = {"value1", "value2"};
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(keys[i], result.getKey(i));
            Assert.assertEquals(values[i], result.getValue(i));
        }
    }
}