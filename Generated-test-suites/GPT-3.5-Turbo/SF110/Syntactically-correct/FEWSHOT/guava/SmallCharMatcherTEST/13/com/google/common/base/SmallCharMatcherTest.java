package com.google.common.base;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(1390113419, SmallCharMatcher.smear(-10));
        assertEquals(-1640531520, SmallCharMatcher.smear(0));
        assertEquals(-829939805, SmallCharMatcher.smear(10));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, null);
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(null, 0x2L, true, null);
        assertFalse(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        assertEquals(16, SmallCharMatcher.chooseTableSize(10));
        assertEquals(32, SmallCharMatcher.chooseTableSize(20));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(10);
        SmallCharMatcher matcher = (SmallCharMatcher) SmallCharMatcher.from(chars, null);

        assertTrue(matcher.matches((char) 0));
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 10));
        assertFalse(matcher.matches((char) 2));
        assertFalse(matcher.matches((char) 11));
        assertFalse(matcher.matches((char) 100));
    }

    @Test
    public void testMatches_ContainsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(10);
        SmallCharMatcher matcher = (SmallCharMatcher) SmallCharMatcher.from(chars, null);

        assertTrue(matcher.matches((char) 0));
        assertFalse(matcher.matches((char) 5));
    }

    @Test
    public void testMatches_ContainsOtherChars() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(10);
        SmallCharMatcher matcher = (SmallCharMatcher) SmallCharMatcher.from(chars, null);

        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 10));
        assertFalse(matcher.matches((char) 5));
        assertFalse(matcher.matches((char) 15));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(10);
        SmallCharMatcher matcher = (SmallCharMatcher) SmallCharMatcher.from(chars, null);
        matcher.setBits(table);

        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertTrue(table.get(10));
        assertFalse(table.get(2));
        assertFalse(table.get(11));
        assertFalse(table.get(100));
    }
}