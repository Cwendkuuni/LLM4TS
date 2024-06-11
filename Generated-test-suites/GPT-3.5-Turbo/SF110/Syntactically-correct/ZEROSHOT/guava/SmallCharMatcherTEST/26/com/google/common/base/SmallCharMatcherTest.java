package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int result = SmallCharMatcher.smear(10);
        assertEquals(1390904807, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 10, false, "");
        boolean result = matcher.checkFilter(5);
        assertFalse(result);
    }

    @Test
    public void testChooseTableSize() {
        int result = SmallCharMatcher.chooseTableSize(100);
        assertEquals(128, result);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(3);
        chars.set(7);
        chars.set(9);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertNotNull(matcher);
    }

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0, true, "");
        boolean result = matcher.matches('\0');
        assertTrue(result);
    }

    @Test
    public void testMatches_false() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 10, false, "");
        boolean result = matcher.matches('A');
        assertFalse(result);
    }

    @Test
    public void testMatches_true() {
        char[] table = new char[16];
        table[0] = 'A';
        table[1] = 'B';
        SmallCharMatcher matcher = new SmallCharMatcher(table, 10, false, "");
        boolean result = matcher.matches('B');
        assertTrue(result);
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0, true, "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits() {
        char[] table = new char[16];
        table[0] = 'A';
        table[1] = 'B';
        SmallCharMatcher matcher = new SmallCharMatcher(table, 10, false, "");
        BitSet result = new BitSet();
        matcher.setBits(result);
        assertTrue(result.get('A'));
        assertTrue(result.get('B'));
    }
}