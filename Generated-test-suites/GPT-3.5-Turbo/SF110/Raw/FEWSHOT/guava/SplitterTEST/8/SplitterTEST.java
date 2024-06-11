package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        assertNotNull(splitter);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        assertNotNull(splitter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnEmptyStringException() {
        Splitter splitter = Splitter.on("");
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        assertNotNull(splitter);
    }

    @Test(expected = NullPointerException.class)
    public void testOnPatternNullException() {
        Splitter splitter = Splitter.onPattern(null);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        assertNotNull(splitter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFixedLengthInvalidArguments() {
        Splitter splitter = Splitter.fixedLength(0);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        assertNotNull(splitter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitInvalidArguments() {
        Splitter splitter = Splitter.on(",").limit(0);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.WHITESPACE);
        assertNotNull(splitter);
    }
    
    @Test(expected = NullPointerException.class)
    public void testTrimResultsWithCharMatcherNullException() {
        Splitter splitter = Splitter.on(",").trimResults(null);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testSplitNullException() {
        Splitter splitter = Splitter.on(",");
        splitter.split(null);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testSplitToListNullException() {
        Splitter splitter = Splitter.on(",");
        splitter.splitToList(null);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on("=").withKeyValueSeparator(",");
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on('=').withKeyValueSeparator(',');
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter splitter = Splitter.on("-");
        Splitter.MapSplitter mapSplitter = Splitter.on("=").withKeyValueSeparator(splitter);
        assertNotNull(mapSplitter);
    }

    @Test
    public void testMapSplitterSplit() {
        Splitter splitter = Splitter.on(",");
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertNotNull(result);
        assertTrue(result.entrySet().size() > 0);
    }
}