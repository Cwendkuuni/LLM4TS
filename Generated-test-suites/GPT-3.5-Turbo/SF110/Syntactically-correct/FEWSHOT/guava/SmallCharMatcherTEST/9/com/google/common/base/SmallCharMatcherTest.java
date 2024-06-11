
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class SmallCharMatcherTest {

    private SmallCharMatcher matcher;

    @Before
    public void setUp() {
        char[] table = new char[] {'a', 'b', 'c'};
        long filter = 7;
        boolean containsZero = true;
        String description = "test matcher";
        matcher = new SmallCharMatcher(table, filter, containsZero, description);
    }

    @Test
    public void testMatches_containsZero() {
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_trueCases() {
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
    }

    @Test
    public void testMatches_falseCases() {
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
        assertFalse(matcher.matches('f'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet expected = new BitSet();
        expected.set(0);
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetBits() {
        BitSet expected = new BitSet();
        expected.set('a');
        expected.set('b');
        expected.set('c');
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }
}
