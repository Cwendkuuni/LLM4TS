
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(',').splitToList("a,b,c"));
        assertEquals(Arrays.asList("abc"), Splitter.on(',').splitToList("abc"));
        assertEquals(Arrays.asList("", "a", "", "b", "", "c"), Splitter.on(',').splitToList(",a,,b,,c"));
    }

    @Test
    public void testOnString() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(" ").splitToList("a b c"));
        assertEquals(Arrays.asList("abc"), Splitter.on(" ").splitToList("abc"));
        assertEquals(Arrays.asList("", "a", " b ", "c"), Splitter.on(" ").splitToList(" a  b  c "));
    }

    @Test
    public void testOnPattern() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.onPattern("\\s+").splitToList("a b c"));
        assertEquals(Arrays.asList("abc"), Splitter.onPattern("\\s+").splitToList("abc"));
        assertEquals(Arrays.asList("", "a", " b ", "c"), Splitter.onPattern("\\s+").splitToList(" a  b  c "));
    }

    @Test
    public void testFixedLength() {
        assertEquals(Arrays.asList("abcd", "efgh", "ijkl"), Splitter.fixedLength(4).splitToList("abcdefghijkl"));
        assertEquals(Arrays.asList("abc", "def"), Splitter.fixedLength(3).splitToList("abcdef"));
        assertEquals(Arrays.asList("a"), Splitter.fixedLength(1).splitToList("a"));
    }

    @Test
    public void testOmitEmptyStrings() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(',').omitEmptyStrings().splitToList("a,b,c"));
        assertEquals(Arrays.asList("abc"), Splitter.on(',').omitEmptyStrings().splitToList("abc"));
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(',').omitEmptyStrings().splitToList(",a,,b,,c"));
        assertEquals(Arrays.asList(" "), Splitter.on(',').omitEmptyStrings().splitToList(" , "));
    }

    @Test
    public void testLimit() {
        assertEquals(Arrays.asList("a", "b,c,d,e,f"), Splitter.on(',').limit(2).splitToList("a,b,c,d,e,f"));
        assertEquals(Arrays.asList("a,b", "c,d,e,f"), Splitter.on(',').limit(1).splitToList("a,b,c,d,e,f"));
    }

    @Test
    public void testTrimResults() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(',').trimResults().splitToList("a,b,c"));
        assertEquals(Arrays.asList("a ", " b", "c "), Splitter.on(',').trimResults().splitToList("a , b, c "));
    }
    
    @Test
    public void testSplit() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(',').split("a,b,c"));
        assertEquals(Arrays.asList("abc"), Splitter.on(',').split("abc"));
        assertEquals(Arrays.asList("", "a", "", "b", "", "c"), Splitter.on(',').split(",a,,b,,c"));
    }
    
    @Test
    public void testSplitToList() {
        assertEquals(Arrays.asList("a", "b", "c"), Splitter.on(',').splitToList("a,b,c"));
        assertEquals(Arrays.asList("abc"), Splitter.on(',').splitToList("abc"));
        assertEquals(Arrays.asList("", "a", "", "b", "", "c"), Splitter.on(',').splitToList(",a,,b,,c"));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");
        assertEquals(expectedMap, Splitter.on(',').withKeyValueSeparator('=').split("key1=value1,key2=value2"));
        assertEquals(Collections.emptyMap(), Splitter.on(',').withKeyValueSeparator('=').split(""));
    }
}
