package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setUp() {
        splitter = Splitter.on(',');
    }

    @Test
    public void testOnCharSeparator() {
        Iterable<String> result = splitter.split("a,b,c");
        // Typical use case
        assertEquals("a", result.iterator().next());
    }

    @Test
    public void testOnCharSeparatorEdgeCases() {
        // Edge case: empty string
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());

        // Edge case: separator not found
        result = splitter.split("abc");
        assertEquals(1, Lists.newArrayList(result).size());
    }

    @Test
    public void testOnCharSeparatorException() {
        // Error scenario: null input
        assertThrows(NullPointerException.class, () -> {
            splitter.split(null);
        });
    }

    @Test
    public void testFixedLength() {
        Splitter fixedLengthSplitter = Splitter.fixedLength(2);
        Iterable<String> result = fixedLengthSplitter.split("123456");
        // Typical use case
        assertEquals("12", result.iterator().next());
    }

    @Test
    public void testFixedLengthEdgeCases() {
        // Edge case: empty string
        Splitter fixedLengthSplitter = Splitter.fixedLength(2);
        Iterable<String> result = fixedLengthSplitter.split("");
        assertFalse(result.iterator().hasNext());

        // Edge case: last chunk less than length
        fixedLengthSplitter = Splitter.fixedLength(2);
        result = fixedLengthSplitter.split("1");
        assertEquals("1", result.iterator().next());
    }

    @Test
    public void testFixedLengthException() {
        // Error scenario: negative length
        assertThrows(IllegalArgumentException.class, () -> {
            Splitter.fixedLength(-1);
        });
    }

    @Test
    public void testTrimResults() {
        Splitter trimmerSplitter = Splitter.on(',').trimResults();
        Iterable<String> result = trimmerSplitter.split(" a , b , c ");
        // Typical use case
        assertEquals("a", result.iterator().next());
    }

    @Test
    public void testTrimResultsException() {
        // Error scenario: null trimmer
        assertThrows(NullPointerException.class, () -> {
            Splitter.on(',').trimResults(null);
        });
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter omitEmptyStringsSplitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = omitEmptyStringsSplitter.split("a,,b,");
        // Typical use case
        assertEquals("a", result.iterator().next());
    }

    @Test
    public void testLimit() {
        Splitter limitSplitter = Splitter.on(',').limit(2);
        Iterable<String> result = limitSplitter.split("a,b,c,d");
        // Typical use case
        assertEquals(2, Lists.newArrayList(result).size());
    }

    @Test
    public void testLimitException() {
        // Error scenario: limit <= 0
        assertThrows(IllegalArgumentException.class, () -> {
            Splitter.on(',').limit(0);
        });
    }

    @Test
    public void testSplitToList() {
        List<String> result = splitter.splitToList("a, b, c");
        // Typical use case
        assertEquals(3, result.size());
    }

    @Test
    public void testSplitToListException() {
        // Error scenario: null input
        assertThrows(NullPointerException.class, () -> {
            splitter.splitToList(null);
        });
    }

    // More test cases can be added for other methods and scenarios

}