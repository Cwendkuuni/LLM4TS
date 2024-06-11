package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZeroTrue() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test Matcher");
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_containsZeroFalse() {
        BitSet chars = new BitSet();
        chars.set(1, 5); // Setting 1, 2, 3, and 4 bits
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilterTrue() {
        BitSet chars = new BitSet();
        chars.set(1); // Setting 1 bit
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_checkFilterFalse() {
        BitSet chars = new BitSet();
        chars.set(1); // Setting 1 bit
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        assertFalse(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_tableEmpty() {
        BitSet chars = new BitSet();
        chars.set(1); // Setting 1 bit
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testSetBits_containsZeroTrue() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test Matcher");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_containsZeroFalse() {
        BitSet chars = new BitSet();
        chars.set(1); // Setting 1 bit
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
    
    @Test
    public void testSetBits_tableEmpty() {
        BitSet chars = new BitSet();
        chars.set(1); // Setting 1 bit
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(1));
    }
  
}