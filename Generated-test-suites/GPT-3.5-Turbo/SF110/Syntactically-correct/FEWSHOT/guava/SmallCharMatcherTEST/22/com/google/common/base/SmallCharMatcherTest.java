package com.google.common.base;

import com.google.common.annotations.*;
import java.util.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Test cases for smear method
        assertEquals(-1511254741, SmallCharMatcher.smear(0));
        assertEquals(1008330483, SmallCharMatcher.smear(1));
        assertEquals(-826046001, SmallCharMatcher.smear(10));
    }

    @Test
    public void testCheckFilter() {
        // Test cases for checkFilter method
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(null, 0x2L, false, "");
        assertFalse(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(null, 0x3L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        // Test cases for chooseTableSize method
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(9));
        assertEquals(1024, SmallCharMatcher.chooseTableSize(500));
        assertEquals(2048, SmallCharMatcher.chooseTableSize(1001));
        assertEquals(2048, SmallCharMatcher.chooseTableSize(2048));
    }

    @Test
    public void testFrom() {
        // Test cases for from method
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(4);
        chars.set(7);
        CharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches((char)1));
        assertTrue(matcher.matches((char)4));
        assertTrue(matcher.matches((char)7));
        assertFalse(matcher.matches((char)2));
        assertFalse(matcher.matches((char)3));
        assertFalse(matcher.matches((char)5));
        assertFalse(matcher.matches((char)6));
        assertFalse(matcher.matches((char)8));
        assertFalse(matcher.matches((char)9));
    }

    @Test
    public void testMatches() {
        // Test cases for matches method
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(4);
        chars.set(7);
        CharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches((char)1));
        assertTrue(matcher.matches((char)4));
        assertTrue(matcher.matches((char)7));
        assertFalse(matcher.matches((char)2));
        assertFalse(matcher.matches((char)3));
        assertFalse(matcher.matches((char)5));
        assertFalse(matcher.matches((char)6));
        assertFalse(matcher.matches((char)8));
        assertFalse(matcher.matches((char)9));

        matcher = SmallCharMatcher.from(chars, "");
        assertFalse(matcher.matches('\0'));
        assertFalse(matcher.matches((char)1));
        assertFalse(matcher.matches((char)4));
        assertTrue(matcher.matches((char)7));
        assertFalse(matcher.matches((char)2));
        assertFalse(matcher.matches((char)3));
    }

    @Test
    public void testSetBits() {
        // Test cases for setBits method
        BitSet table = new BitSet();
        CharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, "");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertFalse(table.get(1));

        table.clear();
        matcher = new SmallCharMatcher(null, 0x2L, false, "");
        matcher.setBits(table);
        assertFalse(table.get(0));
        assertTrue(table.get(1));

        table.clear();
        matcher = new SmallCharMatcher(null, 0x3L, true, "");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
    }

}