
package com.google.common.base;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_doesNotContainZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches_filterMatch() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_filterNoMatch() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_tableMatch() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_tableNoMatch() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_noZero() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
}
