package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");

        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('!'));
        assertFalse(matcher.matches('9'));
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");
        BitSet table = new BitSet();

        matcher.setBits(table);

        assertFalse(table.get(0));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        chars.set(3);

        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "description");

        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('!'));
        assertTrue(matcher.matches('9'));
        assertFalse(matcher.matches('\0'));
    }
}