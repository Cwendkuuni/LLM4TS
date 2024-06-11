package com.google.common.base;

import org.junit.Test;
import java.util.BitSet;
import java.util.Random;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        assertFalse(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches_containsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches_checkFilter() {
        BitSet chars = new BitSet();
        chars.set(1);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertFalse(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches_table() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(2);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
    }

    @Test
    public void testMatches_table_collision() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(5);
        chars.set(10);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
        assertTrue(matcher.matches('5'));
        assertFalse(matcher.matches('6'));
        assertFalse(matcher.matches('7'));
    }

    @Test
    public void testMatches_table_collision_wraparound() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(10);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
        assertFalse(matcher.matches('2'));
        assertFalse(matcher.matches('3'));
        assertFalse(matcher.matches('4'));
        assertTrue(matcher.matches('5'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        matcher.setBits(table);
        assertFalse(table.get(0));
        assertFalse(table.get('a'));
        assertFalse(table.get('b'));
        assertFalse(table.get('c'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertFalse(table.get('a'));
        assertFalse(table.get('b'));
        assertFalse(table.get('c'));
    }

    @Test
    public void testSetBits_table() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(2);
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
    }

}