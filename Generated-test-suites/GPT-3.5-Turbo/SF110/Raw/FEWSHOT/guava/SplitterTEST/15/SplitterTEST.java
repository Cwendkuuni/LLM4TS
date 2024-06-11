package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertEquals(3, splitter.split("a,b,c").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a,b,c").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a,b,c").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        assertEquals(3, splitter.split("a,b,c").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a,b,c").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a,b,c").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        assertEquals(3, splitter.split("a;b;c").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a;b;c").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a;b;c").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[.;]");
        assertEquals(3, splitter.split("a;b.c").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a;b.c").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a;b.c").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertEquals(3, splitter.split("abc").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("abc").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("abc").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        assertEquals(2, splitter.split("a,,b,").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a,,b,").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a,,b,").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        assertEquals(2, splitter.split("a,b,c").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a,b,c").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a,b,c").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        assertEquals(3, splitter.split(" a ,b , c ").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split(" a ,b , c ").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split(" a ,b , c ").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",").trimResults();
        assertEquals(3, splitter.split("a,b,c").spliterator().getExactSizeIfKnown());
        assertTrue(splitter.split("a,b,c").spliterator().getExactSizeIfKnown() > 0);
        assertFalse(splitter.split("a,b,c").spliterator().hasCharacteristics(1));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(3, result.size());
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter splitter = Splitter.on(",").trimResults().withKeyValueSeparator(":");
        Map<String, String> result = splitter.split("a:1,b:2,c:3");
        assertEquals(3, result.size());
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter splitter = Splitter.on(",").trimResults().withKeyValueSeparator(':');
        Map<String, String> result = splitter.split("a:1,b:2,c:3");
        assertEquals(3, result.size());
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter outerSplitter = Splitter.on(",").trimResults();
        Splitter entrySplitter = Splitter.on(":").trimResults();
        Splitter.MapSplitter splitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        Map<String, String> result = splitter.split("a:1,b:2,c:3");
        assertEquals(3, result.size());
        assertFalse(result.isEmpty());
    }
}