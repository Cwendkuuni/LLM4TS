package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testMatchChar() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");

        char c1 = 'a';
        assertFalse(matcher.matches(c1));

        char c2 = '\0';
        assertFalse(matcher.matches(c2));

        char c3 = ' ';
        assertFalse(matcher.matches(c3));

        char c4 = '%';
        assertFalse(matcher.matches(c4));

        char c5 = 'A';
        assertFalse(matcher.matches(c5));
    }

    @Test
    public void testMatchCharWithValidCharacters() {
        BitSet chars = new BitSet();
        for (int i = 0; i < 256; i++) {
            chars.set(i);
        }

        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "description");

        char c1 = 'a';
        assertTrue(matcher.matches(c1));

        char c2 = '\0';
        assertFalse(matcher.matches(c2));

        char c3 = ' ';
        assertTrue(matcher.matches(c3));

        char c4 = '%';
        assertTrue(matcher.matches(c4));

        char c5 = 'A';
        assertTrue(matcher.matches(c5));
    }

    @Test
    public void testMatchCharWithFilter() {
        BitSet chars = new BitSet();
        chars.set(97);
        chars.set(98);
        chars.set(99);
        chars.set(100);
        chars.set(101);

        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "description");

        char c1 = 'a';
        assertTrue(matcher.matches(c1));

        char c2 = 'b';
        assertTrue(matcher.matches(c2));

        char c3 = 'c';
        assertTrue(matcher.matches(c3));

        char c4 = 'd';
        assertTrue(matcher.matches(c4));

        char c5 = 'e';
        assertTrue(matcher.matches(c5));

        char c6 = 'f';
        assertFalse(matcher.matches(c6));
    }
}