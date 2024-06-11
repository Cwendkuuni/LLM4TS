package com.google.common.base;

import org.junit.Test;
import java.util.Map;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class SplitterTest {

    @Test
    public void testOn_Char() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("John,Doe,25");
        String[] expected = {"John", "Doe", "25"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testOn_Char_EmptyString() {
        Splitter splitter = Splitter.on('\0');
        Iterable<String> result = splitter.split("Hello\0World");
        String[] expected = {"Hello", "World"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.whitespace());
        Iterable<String> result = splitter.split("The quick brown fox");
        String[] expected = {"The", "quick", "brown", "fox"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testOn_String() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("apple;banana;orange");
        String[] expected = {"apple", "banana", "orange"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOn_EmptyString() {
        Splitter splitter = Splitter.on("");
        splitter.split("apple;banana;orange");
    }

    @Test
    public void testOn_Pattern() {
        Splitter splitter = Splitter.onPattern("\\d");
        Iterable<String> result = splitter.split("A5B15C20");
        String[] expected = {"A", "B", "C"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("abcdefghi");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.omitEmptyStrings().split("John,,Doe,,,25,");
        String[] expected = {"John", "Doe", "25"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("apple,banana,orange");
        String[] expected = {"apple", "banana,orange"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" apple , banana , orange ");
        String[] expected = {"apple", "banana", "orange"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testTrimResults_CharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split(" apple , banana , orange ");
        String[] expected = {"apple ", "banana ", "orange "};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(';');
        Iterable<String> result = splitter.split("apple;banana;orange");
        String[] expected = {"apple", "banana", "orange"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(';');
        List<String> result = splitter.splitToList("apple;banana;orange");
        String[] expected = {"apple", "banana", "orange"};
        int i = 0;
        for (String str : result) {
            assertEquals(expected[i++], str);
        }
    }

    @Test
    public void testSplitToList_EmptyString() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("");
        assertEquals(0, result.size());
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = Splitter.on(';').withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("name=John;age=25");
        assertEquals("John", result.get("name"));
        assertEquals("25", result.get("age"));
    }

    @Test
    public void testWithKeyValueSeparator_Char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("name:John,age:25");
        assertEquals("John", result.get("name"));
        assertEquals("25", result.get("age"));
    }

    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(';').withKeyValueSeparator(Splitter.on('='));
        Map<String, String> result = mapSplitter.split("name=John;age=25");
        assertEquals("John", result.get("name"));
        assertEquals("25", result.get("age"));
    }

    @Test
    public void testSplittingIterator_Char() {
        Splitter splitter = Splitter.on(';');
        String input = "apple;banana;orange";
        Splitter.SplittingIterator iterator = (Splitter.SplittingIterator) splitter.splittingIterator(input);
        assertEquals(0, iterator.offset);
        assertEquals(input.length(), iterator.limit);

        assertEquals(0, iterator.separatorStart(0));
        assertEquals(5, iterator.separatorEnd(4));
        assertEquals(6, iterator.separatorStart(6));
        assertEquals(12, iterator.separatorEnd(11));
        assertEquals(13, iterator.separatorStart(13));
        assertEquals(-1, iterator.separatorEnd(14));
    }

    @Test
    public void testSplittingIterator_String() {
        Splitter splitter = Splitter.onPattern("\\d");
        String input = "A5B15C20";
        Splitter.SplittingIterator iterator = (Splitter.SplittingIterator) splitter.splittingIterator(input);
        assertEquals(0, iterator.offset);
        assertEquals(input.length(), iterator.limit);

        assertEquals(1, iterator.separatorStart(0));
        assertEquals(2, iterator.separatorEnd(1));
        assertEquals(4, iterator.separatorStart(2));
        assertEquals(6, iterator.separatorEnd(5));
        assertEquals(7, iterator.separatorStart(7));
        assertEquals(-1, iterator.separatorEnd(8));
    }

    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(';').withKeyValueSeparator('=');

        String input = "name=John;age=25";
        Map<String, String> result = mapSplitter.split(input);
        assertEquals("John", result.get("name"));
        assertEquals("25", result.get("age"));
    }
}