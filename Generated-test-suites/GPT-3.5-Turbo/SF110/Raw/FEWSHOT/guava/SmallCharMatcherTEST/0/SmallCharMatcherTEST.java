package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(708553070, SmallCharMatcher.smear(100));
        assertEquals(-700307222, SmallCharMatcher.smear(-123));
        assertEquals(-800489912, SmallCharMatcher.smear(-456));
        assertEquals(178305662, SmallCharMatcher.smear(789));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x110L, true, "Matcher");
        
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
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
        chars.set(0);
        chars.set(2);
        chars.set(4);

        String description = "Test Matcher";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);

        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('1'));
        assertTrue(matcher.matches('2'));
        assertFalse(matcher.matches('3'));
        assertTrue(matcher.matches('4'));
    }

    @Test
    public void testMatches() {
        char[] table = {'a', 'c', 'e'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x101L, true, "Matcher");

        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertTrue(matcher.matches('e'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        char[] chars = {'a', 'b', 'c'};

        SmallCharMatcher matcher = new SmallCharMatcher(chars, 0x111L, true, "Matcher");
        matcher.setBits(table);

        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
    }
}