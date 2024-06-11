package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(-862048943, SmallCharMatcher.smear(0));
        assertEquals(-862048943, SmallCharMatcher.smear(1));
        assertEquals(-898125199, SmallCharMatcher.smear(2));
        assertEquals(865245599, SmallCharMatcher.smear(3));
        // add more test cases here
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1L, true, null);
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        // add more test cases here
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        // add more test cases here
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(2);
        CharMatcher matcher = SmallCharMatcher.from(chars, null);
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        // add more test cases here
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, true, null);
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        // add more test cases here
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, true, null);
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        // add more test cases here
    }
}