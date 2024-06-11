package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
    @Test
    public void testSmear() {
        assertEquals(1826877047, SmallCharMatcher.smear(123456789));
        assertEquals(-845230228, SmallCharMatcher.smear(-987654321));
        assertEquals(0, SmallCharMatcher.smear(0));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(new char[]{}, 1L, false, "");
        assertFalse(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        assertEquals(16, SmallCharMatcher.chooseTableSize(8));
        assertEquals(32, SmallCharMatcher.chooseTableSize(16));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        chars.set(5);
        chars.set(7);

        CharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertTrue(matcher.matches('e'));
        assertFalse(matcher.matches('f'));
        assertTrue(matcher.matches('g'));
    }

    @Test
    public void testMatches() {
        char[] table = new char[]{1, 3, 5, 7};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, true, "");

        assertFalse(matcher.matches((char) 0));
        assertTrue(matcher.matches((char) 1));
        assertFalse(matcher.matches((char) 2));
        assertTrue(matcher.matches((char) 3));
        assertFalse(matcher.matches((char) 4));
        assertTrue(matcher.matches((char) 5));
        assertFalse(matcher.matches((char) 6));
        assertTrue(matcher.matches((char) 7));
        assertFalse(matcher.matches((char) 8));
        assertFalse(matcher.matches((char) 9));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 3, 5, 7}, 0L, true, "");
        matcher.setBits(table);

        assertFalse(table.get(0));
        assertTrue(table.get(1));
        assertFalse(table.get(2));
        assertTrue(table.get(3));
        assertFalse(table.get(4));
        assertTrue(table.get(5));
        assertFalse(table.get(6));
        assertTrue(table.get(7));
        assertFalse(table.get(8));
        assertFalse(table.get(9));
    }
}