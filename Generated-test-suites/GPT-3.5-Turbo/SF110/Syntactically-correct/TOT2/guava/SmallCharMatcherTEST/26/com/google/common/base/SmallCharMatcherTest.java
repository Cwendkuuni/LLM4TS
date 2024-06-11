package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(746587614, SmallCharMatcher.smear(12345));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(6));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(65);
        chars.set(97);
        CharMatcher matcher = SmallCharMatcher.from(chars, "Test");
        assertTrue(matcher.matches('A'));
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(65);
        chars.set(97);
        CharMatcher matcher = SmallCharMatcher.from(chars, "Test");
        assertTrue(matcher.matches('A'));
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        BitSet expected = new BitSet();
        expected.set(0);
        expected.set(65);
        expected.set(97);
        BitSet chars = new BitSet();
        chars.set(65);
        chars.set(97);
        CharMatcher matcher = SmallCharMatcher.from(chars, "Test");
        matcher.setBits(table);
        assertEquals(expected, table);
    }

}