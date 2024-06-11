
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnCharacterSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("1,2,3,4");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("1,2,3,4");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("|");
        Iterable<String> result = splitter.split("1|2|3|4");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("1 2 3 4");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("1234");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        	assertEquals(2, str.length());
        }
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("1,,2,,3,,4");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        Iterable<String> result = splitter.split("1,2,3,4");
        
        assertTrue(result instanceof Iterable);
        int count = 0;
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        	count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" 1 , 2 , 3 , 4 ");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.DIGIT);
        Iterable<String> result = splitter.split(" 1 , 2 , 3 , 4 ");
        
        assertTrue(result instanceof Iterable);
        for (String str : result) {
        	assertFalse(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("1,2,3,4");
        
        assertTrue(result instanceof List);
        for (String str : result) {
        	assertTrue(str.matches("[0-9]+"));
        }
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on('=').withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2,key3:value3");
        
        assertTrue(result instanceof Map);
        for (Map.Entry<String, String> entry : result.entrySet()) {
        	assertTrue(entry.getKey().startsWith("key"));
        	assertTrue(entry.getValue().startsWith("value"));
        }
    }
}
