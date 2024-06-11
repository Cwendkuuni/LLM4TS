package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.*;
import javax.annotation.*;
import com.google.common.annotations.*;
import java.util.*;

@GwtCompatible(emulated = true)
public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('a');
        Iterable<String> result = splitter.split("abcde");
        List<String> expected = Arrays.asList("bcde");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("a,b,c,d,e");
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(" ");
        Iterable<String> result = splitter.split("hello world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("hello   world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("abcdef");
        List<String> expected = Arrays.asList("abc", "def");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("a,,b,c,,d,e");
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(expected, result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        Iterable<String> result = splitter.split("a,b,c,d,e");
        List<String> expected = Arrays.asList("a", "b", "c,d,e");
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" a , b , c ");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on(',').withKeyValueSeparator('=');
        Map<String, String> result = splitter.split("key1=value1,key2=value2,key3=value3");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithKeyValueSeparator_duplicateKey() {
        Splitter splitter = Splitter.on(',').withKeyValueSeparator('=');
        splitter.split("key1=value1,key1=value2");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithKeyValueSeparator_invalidEntry() {
        Splitter splitter = Splitter.on(',').withKeyValueSeparator('=');
        splitter.split("key1");
    }
    
    @Test
    public void testSplittingIterator() {
        Splitter.SplittingIterator iterator = new Splitter.SplittingIterator(null, "");
        assertNull(iterator.computeNext());
    }
    
    @Test
    public void testSplittingIterator_separatorStart() {
        Splitter.SplittingIterator iterator = new Splitter.SplittingIterator(null, "");
        assertEquals(-1, iterator.separatorStart(0));
    }
    
    @Test
    public void testSplittingIterator_separatorEnd() {
        Splitter.SplittingIterator iterator = new Splitter.SplittingIterator(null, "");
        assertEquals(0, iterator.separatorEnd(0));
    }
    
    @Test
    public void testStrategy_iterator() {
        Splitter.Strategy strategy = new Splitter.Strategy() {
            @Override
            public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return new Splitter.SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int p0) {
                        return 0;
                    }

                    @Override
                    int separatorEnd(int p0) {
                        return 0;
                    }
                };
            }
        };
        Splitter.SplittingIterator iterator = strategy.iterator(null, "");
        assertNotNull(iterator);
    }
}