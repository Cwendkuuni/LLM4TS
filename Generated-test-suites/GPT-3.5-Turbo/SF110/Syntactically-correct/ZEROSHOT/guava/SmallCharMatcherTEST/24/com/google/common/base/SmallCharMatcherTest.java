package com.google.common.base;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "containsZero");
        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_noContainsZero() {
        BitSet chars = new BitSet();
        chars.set(1);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "noContainsZero");
        assertFalse(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_containsChar() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "containsChar");
        assertFalse(matcher.matches('\0'));
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_containsMultipleChars() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "containsMultipleChars");
        assertFalse(matcher.matches('\0'));
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[10], 0L, true, "containsZero");
        matcher.setBits(table);
        assertTrue(table.get(0));
        for (int i = 1; i < table.length(); i++) {
            assertFalse(table.get(i));
        }
    }

    @Test
    public void testSetBits_noContainsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[10], 0L, false, "noContainsZero");
        matcher.setBits(table);
        assertFalse(table.get(0));
        for (int i = 1; i < table.length(); i++) {
            assertFalse(table.get(i));
        }
    }

}