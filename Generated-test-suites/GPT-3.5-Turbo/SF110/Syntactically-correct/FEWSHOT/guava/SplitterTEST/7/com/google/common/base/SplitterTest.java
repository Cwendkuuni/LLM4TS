package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        String input = "a,b,c,d";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c", "d"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(".,"));
        String input = "a.b,c.d";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c", "d"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        String input = "a;b;c;d";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c", "d"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s*;\\s*"));
        String input = "a  ;  b ;   c ; d";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c", "d"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "abcd";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"ab", "cd"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(';').omitEmptyStrings();
        String input = "a;;b;c;;d";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c", "d"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        String input = "a,b,c,d";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c,d"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        String input = " a , b , c ";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is('_'));
        String input = "_a_, _b, c_";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        int index = 0;
        for (String s : result) {
            assertEquals(expected[index], s);
            index++;
        }
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        String input = "a,b,c,d";
        List<String> result = splitter.splitToList(input);
        String[] expected = {"a", "b", "c", "d"};
        assertEquals(expected.length, result.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.get(i));
        }
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator('=');
        String input = "key1=value1:key2=value2:key3=value3";
        Map<String, String> result = mapSplitter.split(input);
        assertTrue(result.containsKey("key1"));
        assertEquals("value1", result.get("key1"));
        assertTrue(result.containsKey("key2"));
        assertEquals("value2", result.get("key2"));
        assertTrue(result.containsKey("key3"));
        assertEquals("value3", result.get("key3"));
    }
}