package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1L, 2L, 3L}), "Test Matcher");
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 2));
        assertTrue(matcher.matches((char) 3));
        assertFalse(matcher.matches((char) 4));
        assertFalse(matcher.matches((char) 0));
    }

    @Test
    public void testMatchesZeroChar() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1L, 2L, 3L, 0L}), "Test Matcher");
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 2));
        assertTrue(matcher.matches((char) 3));
        assertTrue(matcher.matches((char) 0));
        assertFalse(matcher.matches((char) 4));
    }

    @Test
    public void testMatchesWithEmptySet() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[0]), "Test Matcher");
        assertFalse(matcher.matches((char) 1));
        assertFalse(matcher.matches((char) 2));
        assertFalse(matcher.matches((char) 3));
        assertFalse(matcher.matches((char) 0));
        assertFalse(matcher.matches((char) 4));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1L, 2L, 3L}), "Test Matcher");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(1));
        assertTrue(table.get(2));
        assertTrue(table.get(3));
        assertFalse(table.get(0));
        assertFalse(table.get(4));
    }
}