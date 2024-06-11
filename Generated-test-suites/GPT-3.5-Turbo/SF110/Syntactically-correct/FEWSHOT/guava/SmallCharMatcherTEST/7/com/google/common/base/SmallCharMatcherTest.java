
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(-475751511, SmallCharMatcher.smear(1234567));
        assertEquals(-2078324830, SmallCharMatcher.smear(987654321));
        assertEquals(1431258396, SmallCharMatcher.smear(98765));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {}, 0L, false, "");
        assertFalse(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(new char[] {}, 1L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(new char[] {}, 2L, false, "");
        assertFalse(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));

        matcher = new SmallCharMatcher(new char[] {}, 3L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(7));
        assertEquals(16, SmallCharMatcher.chooseTableSize(8));
        assertEquals(16, SmallCharMatcher.chooseTableSize(9));
        assertEquals(32, SmallCharMatcher.chooseTableSize(16));
        assertEquals(32, SmallCharMatcher.chooseTableSize(17));
        assertEquals(64, SmallCharMatcher.chooseTableSize(32));
        assertEquals(64, SmallCharMatcher.chooseTableSize(33));
        assertEquals(128, SmallCharMatcher.chooseTableSize(64));
        assertEquals(128, SmallCharMatcher.chooseTableSize(65));
        assertEquals(256, SmallCharMatcher.chooseTableSize(128));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {}, 0L, false, "");
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('\0'));

        matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 3L, true, "");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
        assertTrue(matcher.matches('\0'));

        matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 1L, true, "");
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
        assertTrue(matcher.matches('\0'));

        matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 2L, true, "");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {}, 0L, false, "");
        matcher.setBits(table);
        assertEquals(0, table.size());

        table = new BitSet();
        matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 1L, true, "");
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertFalse(table.get('b'));
        assertFalse(table.get('c'));
        assertTrue(table.get('\0'));

        table = new BitSet();
        matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 2L, false, "");
        matcher.setBits(table);
        assertFalse(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('c'));
        assertFalse(table.get('\0'));

        table = new BitSet();
        matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 3L, true, "");
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('c'));
        assertTrue(table.get('\0'));
    }
}
