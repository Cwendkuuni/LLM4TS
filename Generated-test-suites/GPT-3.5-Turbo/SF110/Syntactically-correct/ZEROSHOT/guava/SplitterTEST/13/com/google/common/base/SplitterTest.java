package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('*');
        
        Iterable<String> result = splitter.split("foo*bar*baz");
        List<String> resultList = splitter.splitToList("foo*bar*baz");
        
        assertEquals("foo", resultList.get(0));
        assertEquals("bar", resultList.get(1));
        assertEquals("baz", resultList.get(2));
        
        int count = 0;
        String[] expected = {"foo", "bar", "baz"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.digit());

        Iterable<String> result = splitter.split("a1b2c3");
        List<String> resultList = splitter.splitToList("a1b2c3");

        assertEquals("a", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));

        int count = 0;
        String[] expected = {"a", "b", "c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");

        Iterable<String> result = splitter.split("a,b,c");
        List<String> resultList = splitter.splitToList("a,b,c");

        assertEquals("a", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));

        int count = 0;
        String[] expected = {"a", "b", "c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));

        Iterable<String> result = splitter.split("hello world");
        List<String> resultList = splitter.splitToList("hello world");

        assertEquals("hello", resultList.get(0));
        assertEquals("world", resultList.get(1));

        int count = 0;
        String[] expected = {"hello", "world"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();

        Iterable<String> result = splitter.split("a,,b,c");
        List<String> resultList = splitter.splitToList("a,,b,c");

        assertEquals("a", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));

        int count = 0;
        String[] expected = {"a", "b", "c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);

        Iterable<String> result = splitter.split("a,b,c");
        List<String> resultList = splitter.splitToList("a,b,c");

        assertEquals("a", resultList.get(0));
        assertEquals("b,c", resultList.get(1));

        int count = 0;
        String[] expected = {"a", "b,c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();

        Iterable<String> result = splitter.split(" a , b , c ");
        List<String> resultList = splitter.splitToList(" a , b , c ");

        assertEquals("a", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));

        int count = 0;
        String[] expected = {"a", "b", "c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('a'));

        Iterable<String> result = splitter.split(" aa , ba , ca ");
        List<String> resultList = splitter.splitToList(" aa , ba , ca ");

        assertEquals("", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));

        int count = 0;
        String[] expected = {"", "b", "c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");

        Iterable<String> result = splitter.split("a,b,c");
        List<String> resultList = splitter.splitToList("a,b,c");

        assertEquals("a", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));

        int count = 0;
        String[] expected = {"a", "b", "c"};
        for (String s : result) {
            assertEquals(expected[count], s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");

        List<String> resultList = splitter.splitToList("a,b,c");

        assertEquals("a", resultList.get(0));
        assertEquals("b", resultList.get(1));
        assertEquals("c", resultList.get(2));
    }

    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter splitter = Splitter.on(",").withKeyValueSeparator("=");

        Map<String, String> resultMap = splitter.split("key1=value1,key2=value2,key3=value3");

        assertEquals("value1", resultMap.get("key1"));
        assertEquals("value2", resultMap.get("key2"));
        assertEquals("value3", resultMap.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter splitter = Splitter.on(",").withKeyValueSeparator('=');

        Map<String, String> resultMap = splitter.split("key1=value1,key2=value2,key3=value3");

        assertEquals("value1", resultMap.get("key1"));
        assertEquals("value2", resultMap.get("key2"));
        assertEquals("value3", resultMap.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter splitter = Splitter.on(",").withKeyValueSeparator(Splitter.on('='));

        Map<String, String> resultMap = splitter.split("key1=value1,key2=value2,key3=value3");

        assertEquals("value1", resultMap.get("key1"));
        assertEquals("value2", resultMap.get("key2"));
        assertEquals("value3", resultMap.get("key3"));
    }
}