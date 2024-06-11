package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        int hashCode = 123;
        int result = SmallCharMatcher.smear(hashCode);
        //assert statements
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0L, false, "");
        boolean result = matcher.checkFilter(97);
        //assert statements
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        //assert statements
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        String description = "";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        //assert statements
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0L, false, "");
        boolean result = matcher.matches('a');
        //assert statements
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0L, false, "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        //assert statements
    }
}