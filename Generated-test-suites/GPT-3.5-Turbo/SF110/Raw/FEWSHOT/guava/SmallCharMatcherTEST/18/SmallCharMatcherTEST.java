package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(1601257261, SmallCharMatcher.smear(1234567890));
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(-1919316219, SmallCharMatcher.smear(-1234567890));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1234567890L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
        assertTrue(matcher.checkFilter(31));
        assertTrue(matcher.checkFilter(32));
        assertFalse(matcher.checkFilter(33));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        assertEquals(16, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(6));
        assertEquals(16, SmallCharMatcher.chooseTableSize(7));
        assertEquals(16, SmallCharMatcher.chooseTableSize(8));
        assertEquals(32, SmallCharMatcher.chooseTableSize(9));
        assertEquals(32, SmallCharMatcher.chooseTableSize(10));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(2);
        CharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches((char) 0));
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 2));
        assertFalse(matcher.matches((char) 3));
    }

    @Test
    public void testMatches() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 1234567890L, true, "");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1234567890L, true, "");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertFalse(table.get(1));
        assertFalse(table.get(2));
        assertTrue(table.get(31));
        assertFalse(table.get(32));
        assertFalse(table.get(33));
    }
}