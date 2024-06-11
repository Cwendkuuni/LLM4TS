package com.google.common.base;

import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, true, "test");
        
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilterFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, false, "test");
        
        assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_tableNotEmpty_match() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, true, "test");
        
        assertTrue(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_tableNotEmpty_noMatch() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, true, "test");
        
        assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_tableNotEmpty_startingIndexNoMatch() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, true, "test");
        
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, true, "test");
        
        BitSet result = new BitSet();
        result.set(0);
        
        BitSet table = new BitSet();
        matcher.setBits(table);
        
        assertEquals(result, table);
    }
    
    @Test
    public void testSetBits_tableNotEmpty() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, true, "test");
        
        BitSet result = new BitSet();
        result.set('a');
        result.set('b');
        result.set('c');
        
        BitSet table = new BitSet();
        matcher.setBits(table);
        
        assertEquals(result, table);
    }
}