package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        assertEquals(-1840776853, SmallCharMatcher.smear(1234567890));
        assertEquals(1543140976, SmallCharMatcher.smear(-1234567890));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1234567890L, true, "");
        
        assertTrue(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        assertEquals(16, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(8));
        assertEquals(32, SmallCharMatcher.chooseTableSize(9));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        
        assertTrue(matcher.containsZero);
        assertArrayEquals(new char[] { 1, 0 }, matcher.table);
        assertEquals(1L, matcher.filter);
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 2, 3, 4}, 1234567890L, true, "");
        
        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('@'));
        assertTrue(matcher.matches((char)1));
        assertTrue(matcher.matches((char)2));
        assertTrue(matcher.matches((char)3));
        assertTrue(matcher.matches((char)4));
        assertFalse(matcher.matches((char)5));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 3, 5, 7}, 1234567890L, false, "");
        
        matcher.setBits(table);
        
        assertTrue(table.get(1));
        assertFalse(table.get(0));
        assertTrue(table.get(3));
        assertFalse(table.get(2));
    }
}