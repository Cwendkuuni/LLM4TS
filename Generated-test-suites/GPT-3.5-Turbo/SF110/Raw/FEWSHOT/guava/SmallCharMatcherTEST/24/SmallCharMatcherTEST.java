package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testSmear() {
        int result = SmallCharMatcher.smear(5);
        assertEquals(-1838852195, result);
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        assertFalse(matcher.checkFilter(5));
    }
    
    @Test
    public void testChooseTableSize() {
        int result = SmallCharMatcher.chooseTableSize(10);
        assertEquals(16, result);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(5);
        chars.set(10);
        
        SmallCharMatcher result = SmallCharMatcher.from(chars, "Test");
        
        assertTrue(result.matches((char)5));
        assertTrue(result.matches((char)10));
        assertFalse(result.matches((char)15));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        matcher.setBits(table);
        
        assertTrue(table.get(0));
    }
}