package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(1923253287, SmallCharMatcher.smear(123456789));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[2], 1, true, "test");
        assertTrue(smallCharMatcher.checkFilter(0));
        assertFalse(smallCharMatcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "test");
        assertTrue(charMatcher.matches('1'));
        assertFalse(charMatcher.matches('2'));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{}, 0, false, "test");
        assertFalse(smallCharMatcher.matches('a'));
        smallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 1L, false, "test");
        assertTrue(smallCharMatcher.matches('a'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{}, 0, true, "test");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        assertTrue(table.get(0));
    }
}