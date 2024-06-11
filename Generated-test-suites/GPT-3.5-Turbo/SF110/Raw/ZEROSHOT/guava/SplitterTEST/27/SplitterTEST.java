package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterator<String> iterator = splitter.split("a,b,c").iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        Iterator<String> iterator = splitter.split("a,b;c").iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("#");
        Iterator<String> iterator = splitter.split("a#b#c").iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterator<String> iterator = splitter.split("Hello    World").iterator();
        assertEquals("Hello", iterator.next());
        assertEquals("World", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterator<String> iterator = splitter.split("1234").iterator();
        assertEquals("12", iterator.next());
        assertEquals("34", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterator<String> iterator = splitter.split("a,b,,c").iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterator<String> iterator = splitter.split("a,b,c").iterator();
        assertEquals("a", iterator.next());
        assertEquals("b,c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterator<String> iterator = splitter.split(" a , b , c ").iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> iterable = splitter.split("a,b,c");
        List<String> result = new ArrayList<>();
        for (String s : iterable) {
            result.add(s);
        }
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
}