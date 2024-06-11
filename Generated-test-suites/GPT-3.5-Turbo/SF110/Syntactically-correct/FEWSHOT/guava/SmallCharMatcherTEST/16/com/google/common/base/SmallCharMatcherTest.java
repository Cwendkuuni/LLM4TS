package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_checkFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty");
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches_tableLength() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty");
        assertEquals(2, matcher.matches('c'));
    }

    @Test
    public void testMatches_startingIndex() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty");
        assertEquals(2, matcher.matches('c'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Empty");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
    }

    @Test
    public void testSetBits_table() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Chars");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }

    @Test
    public void testSmear() {
        int hashCode = 10;
        int smear = SmallCharMatcher.smear(hashCode);
        assertTrue(smear > 0);
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertTrue(tableSize > 0);
    }
}