
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(97); // 'a'
        chars.set(98); // 'b'
        chars.set(99); // 'c'
        chars.set(100); // 'd'
        chars.set(101); // 'e'
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");

        assertFalse(matcher.matches('f'));
        assertFalse(matcher.matches('@'));
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertTrue(matcher.matches('d'));
        assertTrue(matcher.matches('e'));
        assertTrue(matcher.matches('A')); // check case-insensitivity
        assertTrue(matcher.matches('B'));
        assertTrue(matcher.matches('C'));
        assertTrue(matcher.matches('D'));
        assertTrue(matcher.matches('E'));

        chars.clear();
        matcher = SmallCharMatcher.from(chars, "Empty Matcher");

        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
    }
    
    @Test
    public void testSetBits() {
        BitSet expectedTable = new BitSet();
        expectedTable.set(97); // 'a'
        expectedTable.set(98); // 'b'
        expectedTable.set(99); // 'c'
        expectedTable.set(100); // 'd'
        expectedTable.set(101); // 'e'
        expectedTable.set(65); // 'A'
        expectedTable.set(66); // 'B'
        expectedTable.set(67); // 'C'
        expectedTable.set(68); // 'D'
        expectedTable.set(69); // 'E'

        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(expectedTable, "Test Matcher");
        matcher.setBits(table);

        assertEquals(expectedTable, table);

        table.clear();
        matcher = SmallCharMatcher.from(new BitSet(), "Empty Matcher");
        matcher.setBits(table);

        assertTrue(table.isEmpty());
    }
}
