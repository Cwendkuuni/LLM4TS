package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        // Test with positive hash code
        int positiveHashCode = 12345;
        int smearedPositiveHashCode = SmallCharMatcher.smear(positiveHashCode);
        assertEquals(smearedPositiveHashCode, 614919618);
        
        // Test with negative hash code
        int negativeHashCode = -67890;
        int smearedNegativeHashCode = SmallCharMatcher.smear(negativeHashCode);
        assertEquals(smearedNegativeHashCode, -1234507686);
    }
    
    @Test
    public void testCheckFilter() {
        // Test with c = 0, filter = 1
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(null, 1L, false, null);
        boolean checkFilter1 = smallCharMatcher.checkFilter(0);
        assertTrue(checkFilter1);
        
        // Test with c = 1, filter = 2
        smallCharMatcher = new SmallCharMatcher(null, 2L, false, null);
        boolean checkFilter2 = smallCharMatcher.checkFilter(1);
        assertFalse(checkFilter2);
        
        // Test with c = 3, filter = 4
        smallCharMatcher = new SmallCharMatcher(null, 4L, false, null);
        boolean checkFilter3 = smallCharMatcher.checkFilter(3);
        assertFalse(checkFilter3);
    }
    
    @Test
    public void testChooseTableSize() {
        // Test with setSize = 0
        int setSize1 = 0;
        int tableSize1 = SmallCharMatcher.chooseTableSize(setSize1);
        assertEquals(tableSize1, 2);
        
        // Test with setSize = 1
        int setSize2 = 1;
        int tableSize2 = SmallCharMatcher.chooseTableSize(setSize2);
        assertEquals(tableSize2, 2);
        
        // Test with setSize = 16
        int setSize3 = 16;
        int tableSize3 = SmallCharMatcher.chooseTableSize(setSize3);
        assertEquals(tableSize3, 32);
        
        // Test with setSize = 100
        int setSize4 = 100;
        int tableSize4 = SmallCharMatcher.chooseTableSize(setSize4);
        assertEquals(tableSize4, 128);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        chars.set(3);
        SmallCharMatcher smallCharMatcher = (SmallCharMatcher) SmallCharMatcher.from(chars, "test");
        assertEquals(new String(smallCharMatcher.table), "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000");
        assertEquals(smallCharMatcher.containsZero, false);
        assertEquals(smallCharMatcher.filter, 14L);
        assertEquals(smallCharMatcher.getDescription(), "test");
    }
    
    @Test
    public void testMatches() {
        // Test when c = '\0' and containsZero = true
        SmallCharMatcher smallCharMatcher1 = new SmallCharMatcher(null, 0L, true, null);
        boolean matches1 = smallCharMatcher1.matches('\0');
        assertTrue(matches1);
        
        // Test when c = '\0' and containsZero = false
        SmallCharMatcher smallCharMatcher2 = new SmallCharMatcher(null, 0L, false, null);
        boolean matches2 = smallCharMatcher2.matches('\0');
        assertFalse(matches2);
        
        // Test when c is not in the filter
        SmallCharMatcher smallCharMatcher3 = new SmallCharMatcher(null, 15L, false, null);
        boolean matches3 = smallCharMatcher3.matches('a');
        assertFalse(matches3);
        
        // Test when c is in the filter
        SmallCharMatcher smallCharMatcher4 = new SmallCharMatcher(null, 7L, false, null);
        boolean matches4 = smallCharMatcher4.matches('a');
        assertTrue(matches4);
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a','b','\0'}, 0L, true, null);
        smallCharMatcher.setBits(table);
        assertEquals(table.get(0), true);
        assertEquals(table.get('a'), true);
        assertEquals(table.get('b'), true);
    }
    
}